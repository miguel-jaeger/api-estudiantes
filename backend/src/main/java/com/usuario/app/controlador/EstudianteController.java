package com.usuario.app.controlador;

import com.usuario.app.modelo.Estudiante;
import com.usuario.app.repositorio.EstudianteRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteRepository estudianteRepository;

     public EstudianteController(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    @PostMapping
    public ResponseEntity<Estudiante> crearEstudiante(@RequestBody Estudiante nuevoEstudiante) {
        Estudiante estudianteGuardado = estudianteRepository.save(nuevoEstudiante);
        return new ResponseEntity<>(estudianteGuardado, HttpStatus.CREATED);
    }

    // --- Nuevo método para listar todos los estudiantes ---
    @GetMapping
    public ResponseEntity<List<Estudiante>> listarEstudiantes() {
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        return new ResponseEntity<>(estudiantes, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEstudiante(@PathVariable Long id) {
        // Verifica si el estudiante existe antes de intentar eliminarlo
        if (estudianteRepository.existsById(id)) {
            estudianteRepository.deleteById(id);
            // Retorna una respuesta 204 No Content para indicar una eliminación exitosa sin cuerpo de respuesta
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            // Retorna una respuesta 404 Not Found si el estudiante no existe
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
