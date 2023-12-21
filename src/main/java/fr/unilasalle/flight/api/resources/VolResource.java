package fr.unilasalle.flight.api.resources;

import fr.unilasalle.flight.api.beans.Vol;
import fr.unilasalle.flight.api.beans.reservations;
import fr.unilasalle.flight.api.repositories.VolRepository;
import fr.unilasalle.flight.api.repositories.reservationsRepository;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Path("/flights") //Commande possible sur planes
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class VolResource extends GenericResource {
    @Inject
    private VolRepository repository;

    @Inject
    private reservationsRepository repository_reservation;

    @Inject
    Validator validator;

    @GET
    @Path("/{id}")
    public Response getVol(@PathParam("id") Long id){   //Récuperation des données d'un avion
        var Vol = repository
                .findByIdOptional(id).orElse(null);
        return getOr404(Vol);
    }

    @GET
    public Response getVol(@QueryParam("destination") String destination){//Récuperer les données des avions par destionations
        List<Vol> list;
        if(StringUtils.isBlank(destination)){
            list = repository.listAll();
        }else{
            list = repository.findbyDest(destination);
        }
        return getOr404(list);
    }

    @POST
    @Transactional
    public Response createVol(Vol fly){
        var violations = validator.validate(fly);
        if(!violations.isEmpty()){
            return Response.status(400)
                    .entity(new ErrorWrapper(violations))
                    .build();

        }
        try{
            repository.persistAndFlush(fly);
            return Response.status(201).build();
        } catch (PersistenceException e) {
            return Response.serverError()
                    .entity(new ErrorWrapper(e.getMessage())).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteVol(@PathParam("id") Long id) {
        var existingVol = repository.findByIdOptional(id).orElse(null);

        if (existingVol == null) {
            return Response.status(404).entity(new ErrorWrapper("Vol not found")).build();
        }

        try {
            // Supprimer les réservations liées au vol
            repository_reservation.delete("flight_id", existingVol);

            // Supprimer le vol
            repository.delete(existingVol);

            return Response.status(204).build();  // 204 indicates successful deletion
        } catch (Exception e) {
            return Response.serverError()
                    .entity(new ErrorWrapper(e.getMessage()))
                    .build();
        }
    }

}
