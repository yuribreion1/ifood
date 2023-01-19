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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    public Response adicionar(Restaurante restaurante) {
        restaurante.persist();
        return Response.ok(restaurante).build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    public void atualizar(@PathParam("id") Long id, Restaurante dto) {
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);
        if (restauranteOp.isEmpty()) throw new NotFoundException();
        Restaurante restaurante = restauranteOp.get();

        restaurante.nome = dto.nome;
        restaurante.persist();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public void deletar(@PathParam("id") Long id) {
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);

        restauranteOp.ifPresentOrElse(Restaurante::delete, () -> {
            throw new NotFoundException();
        });
    }
    
}
