package org.github.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RestauranteDTO {

    String proprietario;

    String cnpj;

    String nome;

    LocalizacaoDTO localizacao;

}
