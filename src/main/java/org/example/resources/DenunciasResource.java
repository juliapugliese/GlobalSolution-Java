package org.example.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.entities.ServicoModel.Denuncia;
import org.example.entities.UsuarioModel.Denunciante;
import org.example.repositories.DenunciasRepository;
import org.example.services.DenunciaService;


import java.util.List;

@Path("denuncias")
public class DenunciasResource {

    public DenunciasRepository denunciasRepository;
    public DenunciaService denunciaService;

    public DenunciasResource(){
        denunciasRepository = new DenunciasRepository();
        denunciaService = new DenunciaService();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Denuncia> readAll(
            @QueryParam("orderby") String orderBy,
            @QueryParam("direction") String direction,
            @QueryParam("limit") int limit,
            @QueryParam("offset") int offset){
       return denunciasRepository.readAll(orderBy,direction, limit, offset);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response read(@PathParam("id") int id){
        var denuncia = denunciasRepository.read(id);
        return denuncia.isPresent() ?
                Response.ok(denuncia.get()).build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Denuncia denuncia){
        try{
            denunciaService.update(id, denuncia);
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
            denunciaService.delete(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        catch(IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
