package com.tpproyecto.tpproyecto.servicios;


import com.tpproyecto.tpproyecto.clases.Cancion;
import com.tpproyecto.tpproyecto.repositorios.CancionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CancionService {

    private final CancionRepository cancionRepository;

    public CancionService(CancionRepository cancionRepository) {
        this.cancionRepository = cancionRepository;
    }

    public List<Cancion> listarCanciones() {
        return cancionRepository.findAll();
    }

    public Cancion guardarCancion(Cancion cancion) {
        return cancionRepository.save(cancion);
    }

    public List<Cancion> buscarPorNombre(String nombre) {
        return cancionRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
