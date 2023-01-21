package org.github;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.*;

@Entity
@Table(name = "prato")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Prato extends PanacheEntityBase { 
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String nome;

    String descricao;

    @ManyToOne
    Restaurante restaurante;

    BigDecimal preco;
}
