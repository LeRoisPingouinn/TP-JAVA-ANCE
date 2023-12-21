package fr.unilasalle.flight.api.resources;

import fr.unilasalle.flight.api.beans.passenger;
import fr.unilasalle.flight.api.repositories.passengerRepository;
import fr.unilasalle.flight.api.beans.Avion;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Path("/passenger") //Commande possible sur planes
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class passengerResource extends GenericResource {//Fait appel au repositories pour récup dans la bdd et ensuite construit la réponse
    @Inject
    private passengerRepository repository;

    @Inject
    Validator validator;

    @GET
    @Path("/{id}")
    public Response getpassenger(@PathParam("id") Long id){
        var passenger = repository
                .findByIdOptional(id).orElse(null);
        return getOr404(passenger);
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updatePassenger(@PathParam("id") Long id, passenger updatedPassenger) {

        var violations = validator.validate(updatedPassenger);
        if (!violations.isEmpty()) {
            return Response.status(400)
                    .entity(new ErrorWrapper(violations))
                    .build();
        }
        var existingPassenger = repository.findByIdOptional(id).orElse(null);


        if (existingPassenger == null) {
            return Response.status(404).entity(new ErrorWrapper("Passenger not found")).build();
        }

        // Update the existing passenger with the new data
        existingPassenger.setSurname(updatedPassenger.getSurname());
        existingPassenger.setFirstname(updatedPassenger.getFirstname());
        existingPassenger.setEmail_address(updatedPassenger.getEmail_address());

        try {
            repository.persistAndFlush(existingPassenger);
            return Response.status(200).build();
        } catch (PersistenceException e) {
            return Response.serverError()
                    .entity(new ErrorWrapper(e.getMessage()))
                    .build();
        }
    }

}