package org.example.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.entities.UsuarioModel.Denunciante;
import org.example.repositories.DenunciantesRepository;
import org.example.services.DenuncianteService;


import java.util.List;

@Path("denunciantes")
public class DenunciantesResource {

    public DenunciantesRepository denunciantesRepository;
    public DenuncianteService denuncianteService;

    public DenunciantesResource(){
        denunciantesRepository = new DenunciantesRepository();
        denuncianteService = new DenuncianteService();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Denunciante> readAll(
            @QueryParam("orderby") String orderBy,
            @QueryParam("direction") String direction,
            @QueryParam("limit") int limit,
            @QueryParam("offset") int offset){
       return denunciantesRepository.readAll(orderBy,direction, limit, offset);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response read(@PathParam("id") int id){
        var denunciante = denunciantesRepository.read(id);
        return denunciante.isPresent() ?
                Response.ok(denunciante.get()).build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Denunciante denunciante){
        try{
            denuncianteService.create(denunciante);
            return Response.status(Response.Status.CREATED).build();
        }
        catch(IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Denunciante produto){
        try{
            denuncianteService.update(id, produto);
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        catch(IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id){
        try{
            denuncianteService.delete(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        catch(IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
