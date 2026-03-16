package com.iesjc.keymaster.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "departamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_departamento")
    private Integer idDepartamento;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    // Relación Bidireccional: Un departamento agrupa a muchos profesores
    @OneToMany(mappedBy = "departamento", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Profesor> profesores;
}