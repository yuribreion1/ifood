package org.github.dto;

import org.github.Restaurante;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface RestauranteMapper {

    Restaurante toRestaurante(RestauranteDTO restauranteDTO);
}
