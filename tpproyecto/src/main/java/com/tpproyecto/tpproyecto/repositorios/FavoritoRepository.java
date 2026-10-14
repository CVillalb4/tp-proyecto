package com.tpproyecto.tpproyecto.repositorios;

import com.tpproyecto.tpproyecto.clases.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritoRepository
        extends JpaRepository<Favorito, Long> {
}
