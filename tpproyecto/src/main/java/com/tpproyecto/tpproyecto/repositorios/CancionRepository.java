package com.tpproyecto.tpproyecto.repositorios;

import com.tpproyecto.tpproyecto.clases.Cancion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CancionRepository
        extends JpaRepository<Cancion, Long> {

    List<Cancion> findByNombreContainingIgnoreCase(String nombre);
}