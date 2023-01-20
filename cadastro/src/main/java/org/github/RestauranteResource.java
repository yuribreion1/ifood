package org.github;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestauranteResource {

    @GET
    public List<Restaurante> buscar () {
        return Restaurante.listAll();
    }

    @POST
    @Transactional
    public Response adicionar(Restaurante restaurante, @Context UriInfo uriInfo) {
        restaurante.persist();
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(Long.toString(restaurante.id));
        return Response.created(uriBuilder.build()).entity(restaurante).build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    public Response atualizar(@PathParam("id") Long id, Restaurante dto) {
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);
        if (restauranteOp.isEmpty()) throw new NotFoundException();
        Restaurante restaurante = restauranteOp.get();

        restaurante.nome = dto.nome;
        restaurante.persist();
        return Response.ok(restaurante).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public Response deletar(@PathParam("id") Long id) {
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);

        restauranteOp.ifPresentOrElse(Restaurante::delete, () -> {
            throw new NotFoundException();
        });
        return Response.ok().build();
    }
    
}
