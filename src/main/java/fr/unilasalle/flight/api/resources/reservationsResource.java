package fr.unilasalle.flight.api.resources;

import fr.unilasalle.flight.api.beans.Vol;
import fr.unilasalle.flight.api.beans.passenger;
import fr.unilasalle.flight.api.beans.reservations;
import fr.unilasalle.flight.api.repositories.passengerRepository;
import fr.unilasalle.flight.api.repositories.reservationsRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import fr.unilasalle.flight.api.repositories.VolRepository;


import java.util.List;

@Path("/reserve")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class reservationsResource extends GenericResource {
    @Inject
    private reservationsRepository repository;
    @Inject
    private passengerRepository repository_passenger;

    @Inject
    private VolRepository VolRepository;


    @Inject
    Validator validator;

    @GET
    public Response getreservation(@QueryParam("flight_id") long flight_id){ //Récuperation de la liste de reservation sur un vol
        List<reservations> list_reservation;
        list_reservation = repository.findByflight_idParameter(flight_id);
        return getOr404(list_reservation);
    }

    @GET
    public Response getAllReservations() {
        List<reservations> reservationList = reservations.listAll();
        return getOr404(reservationList);
    }




    @POST
    @Transactional
    @Path("/createWithPassenger")
    public Response createReservationWithPassenger(reservations newReservation) {
        var violations = validator.validate(newReservation);
        if (!violations.isEmpty()) {
            return Response.status(400)
                    .entity(new ErrorWrapper(violations))
                    .build();
        }

        try {
            // Récupérez le vol à partir de la base de données
            Vol flight = null;
            if (newReservation.getFlight_id() != null) {
                flight = VolRepository.findById(newReservation.getFlight_id().getId());
            }

            if (flight == null) {
                return Response.status(400)
                        .entity(new ErrorWrapper("Invalid flight ID"))
                        .build();
            }

            // Vérifiez la capacité du vol
            int currentCapacity = (int) repository.count("flight_id", flight);
            if (currentCapacity >= flight.getPlane_id().getCapacity()) {
                return Response.status(400)
                        .entity(new ErrorWrapper("Flight is already fully booked"))
                        .build();
            }

            // Vérifiez si le passager existe déjà dans la base de données
            passenger existingPassenger = repository_passenger.findByEmail(newReservation.getPassenger_id().getEmail_address());

            // Si le passager n'existe pas, créez un nouveau passager
            if (existingPassenger == null) {
                passenger newPassenger = new passenger();
                newPassenger.setSurname(newReservation.getPassenger_id().getSurname());
                newPassenger.setFirstname(newReservation.getPassenger_id().getFirstname());
                newPassenger.setEmail_address(newReservation.getPassenger_id().getEmail_address());

                // Enregistrez le nouveau passager dans la base de données
                repository_passenger.persistAndFlush(newPassenger);

                // Associez le nouveau passager à la réservation
                newReservation.setPassenger_id(newPassenger);
            } else {
                // Utilisez le passager existant
                newReservation.setPassenger_id(existingPassenger);
            }

            // Associez le vol à la réservation
            newReservation.setFlight_id(flight);

            // Enregistrez la réservation dans la base de données
            repository.persistAndFlush(newReservation);

            return Response.status(201).build();
        } catch (PersistenceException e) {
            return Response.serverError()
                    .entity(new ErrorWrapper(e.getMessage()))
                    .build();
        }
    }



    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deletereservation(@PathParam("id") Long id) {
        var existingreservation = repository.findByIdOptional(id).orElse(null);
        List<passenger> list_passenger = null;

        if (existingreservation == null) {
            return Response.status(404).entity(new ErrorWrapper("Reservation not found")).build();
        }

        // Fetch the list of passengers associated with the reservation
        list_passenger = repository_passenger.findByReservationParameter(existingreservation.getId());

        try {
            // Delete the reservation
            repository.delete(existingreservation);

            // Delete associated passengers
            for (int i = 0; i < list_passenger.size(); i++) {
                passenger passenger = list_passenger.get(i);
                repository_passenger.delete(passenger);
            }

            return Response.status(204).build();  // 204 indicates successful deletion

        } catch (Exception e) {
            return Response.serverError()
                    .entity(new ErrorWrapper(e.getMessage()))
                    .build();
        }
    }

}
