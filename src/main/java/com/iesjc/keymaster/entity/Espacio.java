package com.iesjc.keymaster.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "espacio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Espacio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_espacio")
    private Integer idEspacio;

    @Column(name = "codigo", nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(name = "descripcion", length = 150)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoEspacio tipo;

    // Relación Bidireccional: Un espacio tiene muchas llaves.
    // 'mappedBy = "espacio"' le dice a Hibernate que la clase Llave es la "dueña" de la relación.
    @OneToMany(mappedBy = "espacio", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Llave> llaves;
}