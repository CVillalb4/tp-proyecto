
package com.tpproyecto.tpproyecto.servicios;

import com.tpproyecto.tpproyecto.clases.Cancion;
import com.tpproyecto.tpproyecto.clases.Playlist;
import com.tpproyecto.tpproyecto.repositorios.CancionRepository;
import com.tpproyecto.tpproyecto.repositorios.PlaylistRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final CancionRepository cancionRepository;

    public PlaylistService(
            PlaylistRepository playlistRepository,
            CancionRepository cancionRepository) {
        this.playlistRepository = playlistRepository;
        this.cancionRepository = cancionRepository;
    }

    @Transactional(readOnly = true)
    public List<Playlist> listarPlaylists() {
        List<Playlist> playlists = playlistRepository.findAll();

        // Cargar las canciones mientras la sesión de Hibernate está abierta.
        for (Playlist playlist : playlists) {
            playlist.getCanciones().size();
        }

        return playlists;
    }

    @Transactional(readOnly = true)
    public Playlist obtenerPlaylist(Long id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe la playlist con ID " + id));

        // Carga las canciones mientras la sesión de Hibernate está abierta.
        playlist.getCanciones().size();

        return playlist;
    }

    @Transactional
    public Playlist crearPlaylist(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la playlist no puede estar vacío.");
        }

        Playlist playlist = new Playlist();
        playlist.setNombre(nombre.trim());

        return playlistRepository.save(playlist);
    }

    @Transactional
    public void eliminarPlaylist(Long id) {
        Playlist playlist = obtenerPlaylist(id);
        playlistRepository.delete(playlist);
    }

    @Transactional
    public void agregarCancion(Long playlistId, Long cancionId) {
        Playlist playlist = obtenerPlaylist(playlistId);

        Cancion cancion = cancionRepository.findById(cancionId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe la canción con ID " + cancionId));

        if (playlist.getCanciones().stream()
                .anyMatch(c -> c.getId().equals(cancionId))) {
            return;
        }

        playlist.agregarCancion(cancion);
        playlistRepository.save(playlist);
    }

    @Transactional
    public void quitarCancion(Long playlistId, Long cancionId) {
        Playlist playlist = obtenerPlaylist(playlistId);

        playlist.getCanciones().removeIf(
                cancion -> cancion.getId().equals(cancionId));

        playlistRepository.save(playlist);
    }
}
