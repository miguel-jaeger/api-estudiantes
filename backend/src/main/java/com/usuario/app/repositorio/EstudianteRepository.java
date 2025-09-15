package com.usuario.app.repositorio;
 
import com.usuario.app.modelo.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
}

