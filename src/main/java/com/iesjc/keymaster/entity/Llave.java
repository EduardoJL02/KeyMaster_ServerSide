package com.iesjc.keymaster.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "llave")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Llave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_llave")
    private Integer idLlave;

    @Column(name = "codigo_interno", nullable = false, unique = true, length = 50)
    private String codigoInterno;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoLlave estado;

    // Relación Muchos a Uno: Muchas llaves pueden pertenecer a un mismo espacio (si hay copias)
    // OJO: Esto requerirá que creemos la entidad Espacio a continuación.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_espacio", nullable = false, foreignKey = @ForeignKey(name = "fk_llave_espacio"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Espacio espacio;
}