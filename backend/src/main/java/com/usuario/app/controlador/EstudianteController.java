package com.usuario.app.controlador;

import com.usuario.app.modelo.Estudiante;
import com.usuario.app.repositorio.EstudianteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException; // Importar para manejar duplicados

@RestController
@RequestMapping("/api/estudiantes")
// ¡IMPORTANTE! Asegúrate de que PUT esté incluido en allowedMethods
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
// Si usas una configuración global de CORS (WebConfig.java), asegúrate de que también incluya PUT en allowedMethods.
public class EstudianteController {

    @Autowired
    private EstudianteRepository estudianteRepository;

    public EstudianteController(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    @PostMapping
    public ResponseEntity<?> crearEstudiante(@RequestBody Estudiante nuevoEstudiante) {
        try {
            Estudiante estudianteGuardado = estudianteRepository.save(nuevoEstudiante);
            return new ResponseEntity<>(estudianteGuardado, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("El correo electrónico ya se encuentra registrado.", HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al crear el estudiante: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Estudiante>> listarEstudiantes() {
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        return new ResponseEntity<>(estudiantes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estudiante> obtenerEstudiantePorId(@PathVariable Long id) {
        Optional<Estudiante> estudiante = estudianteRepository.findById(id);
        if (estudiante.isPresent()) {
            return new ResponseEntity<>(estudiante.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --- ¡ESTE ES EL MÉTODO CLAVE QUE DEBE ESTAR CORRECTO! ---
    @PutMapping("/{id}") // <--- Asegúrate de esta anotación y PathVariable
    public ResponseEntity<?> actualizarEstudiante(@PathVariable Long id, @RequestBody Estudiante estudianteActualizado) {
        Optional<Estudiante> estudianteExistente = estudianteRepository.findById(id);

        if (estudianteExistente.isPresent()) {
            Estudiante estudiante = estudianteExistente.get();
            estudiante.setNombre(estudianteActualizado.getNombre());
            estudiante.setApellido(estudianteActualizado.getApellido());
            estudiante.setFechaNacimiento(estudianteActualizado.getFechaNacimiento());
            estudiante.setCorreo(estudianteActualizado.getCorreo()); // El correo también puede ser actualizado
            
            try {
                Estudiante estudianteGuardado = estudianteRepository.save(estudiante);
                return new ResponseEntity<>(estudianteGuardado, HttpStatus.OK);
            } catch (DataIntegrityViolationException e) {
                return new ResponseEntity<>("El correo electrónico ya se encuentra registrado para otro estudiante.", HttpStatus.CONFLICT);
            } catch (Exception e) {
                return new ResponseEntity<>("Error interno al actualizar el estudiante: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("Estudiante no encontrado para actualizar.", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEstudiante(@PathVariable Long id) {
        if (estudianteRepository.existsById(id)) {
            estudianteRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}