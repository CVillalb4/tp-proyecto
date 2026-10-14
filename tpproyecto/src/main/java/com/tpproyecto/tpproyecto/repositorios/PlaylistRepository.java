package com.tpproyecto.tpproyecto.repositorios;

import com.tpproyecto.tpproyecto.clases.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository
        extends JpaRepository<Playlist, Long> {
} 