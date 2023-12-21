package fr.unilasalle.flight.api.resources;

import fr.unilasalle.flight.api.repositories.AvionsRepository;
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

@Path("/planes") //Commande possible sur planes
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class AvionResource extends GenericResource {
    @Inject
    private AvionsRepository repository; //On se refere a AvionsRepository

    @Inject
    Validator validator;

    @GET
    public Response getPlanes(@QueryParam("operator") String operator){//Récupérer tt les avions ou une lsite par fabricant
        List<Avion> list;
        if(StringUtils.isBlank(operator)){
            list = repository.listAll();
        }else{
            list = repository.findByOperator(operator); //Fonction du fichier AvionsRepository
        }
        return getOr404(list);
    }

    @GET
    @Path("/{id}")
    public Response getPlane(@PathParam("id") Long id){
        var avion = repository
                .findByIdOptional(id).orElse(null);
        return getOr404(avion);
    }
    @POST
    @Transactional
    public Response createPlane(Avion plane){
        var violations = validator.validate(plane);
        if(!violations.isEmpty()){
            return Response.status(400)
                    .entity(new ErrorWrapper(violations))
                    .build();

        }
        try{
            repository.persistAndFlush(plane);
            return Response.status(201).build();
        } catch (PersistenceException e) {
            return Response.serverError()
                    .entity(new ErrorWrapper(e.getMessage())).build();
        }

    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteavion(@PathParam("id") Long id) {
        var existingavion = repository.findByIdOptional(id).orElse(null);
        if (existingavion == null) {
            return Response.status(404).entity(new ErrorWrapper("Passenger not found")).build();
        }

        try {
            repository.delete(existingavion);

            return Response.status(204).build();  // 204 indicates successful deletion
        } catch (Exception e) {
            return Response.serverError()
                    .entity(new ErrorWrapper(e.getMessage()))
                    .build();
        }
    }
}
