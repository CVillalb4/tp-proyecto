package com.tpproyecto.tpproyecto.repositorios;

import com.tpproyecto.tpproyecto.clases.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoritoRepository
        extends JpaRepository<Favorito, Long> {

    Optional<Favorito> findByCancionId(Long cancionId);

    boolean existsByCancionId(Long cancionId);
}