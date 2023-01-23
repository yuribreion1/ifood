package org.github;

import io.quarkus.logging.Log;
import io.quarkus.panache.common.Sort;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.github.dto.RestauranteDTO;
import org.github.dto.RestauranteMapper;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
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

    @Inject
    RestauranteMapper restauranteMapper;

    @GET
    @Tag(name = "Restaurante")
    public List<Restaurante> buscar () {
        Log.info("CADASTRO - Listando restaurantes");
        return Restaurante.listAll(Sort.by("id"));
    }

    @POST
    @Transactional
    @Tag(name = "Restaurante")
    public Response adicionar(RestauranteDTO restauranteDTO, @Context UriInfo uriInfo) {
        Restaurante restaurante = restauranteMapper.toRestaurante(restauranteDTO);
        restaurante.persist();
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(Long.toString(restaurante.id));
        Log.info("CADASTRO - Adicionando restaurante: " + restaurante.toString());
        return Response.created(uriBuilder.build()).entity(restaurante).build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    @Tag(name = "Restaurante")
    public Response atualizar(@PathParam("id") Long id, RestauranteDTO restauranteDTO) {
        Optional<Restaurante> restauranteOp = verificarSeRestauranteExisteNoBanco(id);
        Restaurante restaurante = restauranteOp.get();

        restaurante.setNome(restauranteDTO.getNome());
        restaurante.setCnpj(restauranteDTO.getCnpj());
        restaurante.setProprietario(restauranteDTO.getProprietario());

        restaurante.persist();
        return Response.ok(restaurante).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    @Tag(name = "Restaurante")
    public Response deletar(@PathParam("id") Long id) {
        Optional<Restaurante> restauranteOp = verificarSeRestauranteExisteNoBanco(id);

        restauranteOp.ifPresentOrElse(Restaurante::delete, () -> {
            throw new NotFoundException();
        });
        return Response.ok().build();
    }

    @GET
    @Path("{id}/pratos")
    @Tag(name = "Prato")
    public List<Restaurante> listarPratos(@PathParam("id") Long id) {
        Optional<Restaurante> restauranteOptional = verificarSeRestauranteExisteNoBanco(id);
        return Prato.list("restaurante", restauranteOptional.get());
    }

    @POST
    @Transactional
    @Path("{id}/pratos")
    @Tag(name = "Prato")
    public Response adicionarPrato(@PathParam("id") Long id, Prato dto) {
        Optional<Restaurante> restauranteOptional = verificarSeRestauranteExisteNoBanco(id);
        Prato.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .preco(dto.getPreco())
                .restaurante(restauranteOptional.get())
                .build()
                .persist();
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Transactional
    @Path("{id}/pratos/{idPrato}")
    @Tag(name = "Prato")
    public Response atualizarPrato(@PathParam("id") Long id,@PathParam("idPrato") Long idPrato, Prato dto) {
        verificarSeRestauranteExisteNoBanco(id);
        Optional<Prato> pratoOptional = verificarSePratoExisteNoBanco(idPrato);

        Prato prato = pratoOptional.get();

        prato.setNome(dto.getNome());
        prato.setDescricao(dto.getDescricao());
        prato.setPreco(dto.getPreco());

        prato.persist();
        return Response.ok(prato).build();
    }

    @DELETE
    @Transactional
    @Path("{id}/pratos/{idPrato}")
    @Tag(name = "Prato")
    public Response apagarPrato(@PathParam("id") Long id, @PathParam("idPrato") Long idPrato) {
        verificarSeRestauranteExisteNoBanco(id);

        Optional<Prato> pratoOptional = verificarSePratoExisteNoBanco(idPrato);
        pratoOptional.ifPresentOrElse(Prato::delete, () -> {
            throw new NotFoundException("Prato não encontrado");
        });
        return Response.ok(pratoOptional.get()).build();
    }

    private static Optional<Restaurante> verificarSeRestauranteExisteNoBanco(Long id) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(id);
        if (restauranteOptional.isEmpty()) throw new NotFoundException("Restaurante não existe");
        return restauranteOptional;
    }

    private static Optional<Prato> verificarSePratoExisteNoBanco(Long id) {
        Optional<Prato> pratoOptional = Prato.findByIdOptional(id);
        if (pratoOptional.isEmpty()) throw new NotFoundException("Prato não existe");
        return pratoOptional;
    }
}
