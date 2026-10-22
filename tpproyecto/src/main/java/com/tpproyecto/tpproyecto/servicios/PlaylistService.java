package com.tpproyecto.tpproyecto.servicios;

import com.tpproyecto.tpproyecto.clases.Cancion;
import com.tpproyecto.tpproyecto.clases.Playlist;
import com.tpproyecto.tpproyecto.repositorios.CancionRepository;
import com.tpproyecto.tpproyecto.repositorios.PlaylistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final CancionRepository cancionRepository;

    public PlaylistService(PlaylistRepository playlistRepository, CancionRepository cancionRepository) {
        this.playlistRepository = playlistRepository;
        this.cancionRepository = cancionRepository;
    }

    @Transactional
    public boolean agregarCancionAPlaylist(Long playlistId, Long cancionId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist no encontrada con ID: " + playlistId));

        Cancion cancion = cancionRepository.findById(cancionId)
                .orElseThrow(() -> new RuntimeException("Canción no encontrada con ID: " + cancionId));

        // Evitar duplicados si la canción ya está en la playlist
        if (!playlist.getCanciones().contains(cancion)) {
            playlist.getCanciones().add(cancion);
            playlistRepository.save(playlist);
            return true;
        }

        return false; // La canción ya pertenecía a la lista
    }

    @Transactional
    public boolean agregarABiblioteca(Long cancionId) {
        // Busca la primera playlist o crea una si no existe ninguna
        Playlist playlist = playlistRepository.findAll().stream().findFirst()
                .orElseGet(() -> {
                    Playlist nueva = new Playlist();
                    nueva.setNombre("Mis Favoritos");
                    return playlistRepository.save(nueva);
                });

        return agregarCancionAPlaylist(playlist.getId(), cancionId);
    }

    public List<Cancion> obtenerCancionesDePlaylist(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist no encontrada"));
        return playlist.getCanciones();
    }

    @Transactional
public Playlist crearPlaylist(String nombre) {
    Playlist playlist = new Playlist();
    playlist.setNombre(nombre);
    
    // Si tu entidad maneja una lista de canciones, asegurate de inicializarla
    if (playlist.getCanciones() == null) {
        playlist.setCanciones(new ArrayList<>());
    }

    return playlistRepository.save(playlist);
}

public List<Playlist> obtenerTodas() {
    List<Playlist> lista = playlistRepository.findAll();
    return lista != null ? lista : new ArrayList<>();
}

public List<Cancion> obtenerCancionesPorPlaylist(Long playlistId) {
    Playlist playlist = playlistRepository.findById(playlistId).orElse(null);
    if (playlist != null) {
        return playlist.getCanciones();
    }
    return new ArrayList<>(); // O retornar una lista vacía
}
    
}
