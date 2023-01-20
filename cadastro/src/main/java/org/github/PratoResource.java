package org.github;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/pratos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PratoResource {

    @GET
    public List<Prato> buscar() { return Prato.listAll(); }

    @POST
    @Transactional
    public Response adicionar(Prato prato, @Context UriInfo uriInfo) {
        prato.persist();
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(Long.toString(prato.id));
        return Response.created(uriBuilder.build()).entity(prato).build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    public Response atualizar(@PathParam("id") Long id, Prato dto) {
        Optional<Prato> pratoOp = Prato.findByIdOptional(id);
        if (pratoOp.isEmpty()) throw new NotFoundException();
        Prato prato = pratoOp.get();

        prato.nome = dto.nome;
        prato.descricao = dto.descricao;
        prato.preco = dto.preco;
        prato.restaurante = dto.restaurante;
        prato.persist();
        return Response.ok(prato).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public Response deletar(@PathParam("id") Long id) {
        Optional<Prato> pratoOptional = Prato.findByIdOptional(id);
        pratoOptional.ifPresentOrElse(Prato::delete, () -> { throw new NotFoundException(); });
        return Response.ok().build();
    }
}
