package org.github;

import java.util.Date;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurante")
public class Restaurante extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String proprietario;

    String cnpj;

    String nome;

    @OneToOne(cascade = CascadeType.ALL)
    Localizacao localizacao;

    @CreationTimestamp
    Date dataCriacao;

    @UpdateTimestamp
    Date dataAtualizacao;

    
}
