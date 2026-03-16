package com.iesjc.keymaster.entity;

import jakarta.persistence.*;
//import jakarta.validation.constraints.Email;
import lombok.*;

@Entity
@Table(name = "profesor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profesor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profesor")
    private Integer idProfesor;

    @Column(name = "dni", nullable = false, unique = true, length = 20)
    private String dni;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    // Validado a nivel de Jakarta y mapeado a MySQL
//    @Email(message = "El formato del correo electrónico no es válido")
//    @Column(name = "email", length = 150, unique = true)
//    private String email;

    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true; // Implementación del Soft Delete

    // Relación Muchos a Uno: Muchos profesores pertenecen a un departamento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_departamento", foreignKey = @ForeignKey(name = "fk_prof_dep"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Departamento departamento;
}