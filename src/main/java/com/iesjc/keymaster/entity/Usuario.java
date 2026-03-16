package com.iesjc.keymaster.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    // La longitud 255 es vital para almacenar el Hash de BCrypt
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private TipoUsuario rol;

    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true; // Soft Delete para no perder el rastro de conserjes antiguos
}