package com.usuario.app.modelo;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "estudiantes")
@Data 


public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEstudiante")
     private Long idEstudiante; 
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
 
    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;
 
    @Column(name = "fechaNacimiento")
    private LocalDate fechaNacimiento;
 
    @Column(name = "correo", unique = true, nullable = false)
    private String correo;


}
