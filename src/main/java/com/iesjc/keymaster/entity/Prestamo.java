package com.iesjc.keymaster.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "prestamo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prestamo")
    private Integer idPrestamo;

    @Column(name = "fecha_salida", nullable = false, updatable = false)
    private LocalDateTime fechaSalida;

    @Column(name = "fecha_entrada")
    private LocalDateTime fechaEntrada;

    @Column(name = "fecha_limite")
    private LocalTime fechaLimite;

    // Relación Muchos a Uno: Muchos préstamos se hacen sobre una misma llave (historial)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_llave", nullable = false, foreignKey = @ForeignKey(name = "fk_prestamo_llave"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Llave llave;

    // Relación Muchos a Uno: Un profesor puede tener muchos préstamos en su historial
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_profesor", nullable = false, foreignKey = @ForeignKey(name = "fk_prestamo_profesor"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Profesor profesor;

    // Relación Muchos a Uno: El conserje que entregó la llave
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_salida", nullable = false, foreignKey = @ForeignKey(name = "fk_prestamo_us_salida"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario usuarioSalida;

    // Relación Muchos a Uno: El conserje que recibió la llave (Nullable, porque al principio está fuera)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_entrada", foreignKey = @ForeignKey(name = "fk_prestamo_us_entrada"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario usuarioEntrada;

    // Métod0 de ciclo de vida de JPA
    @PrePersist
    protected void onCreate() {
        if (this.fechaSalida == null) {
            this.fechaSalida = LocalDateTime.now();
        }
    }
}