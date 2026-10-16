package com.tpproyecto.tpproyecto.controladores;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.tpproyecto.tpproyecto.clases.Playlist;
import com.tpproyecto.tpproyecto.servicios.PlaylistService;

@RestController
@RequestMapping("/api/playlists")

public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @PostMapping("/agregar")
    public ResponseEntity<String> agregarCancionAPlaylist(
            @RequestParam Long cancionId,
            @RequestParam(required = false) Long playlistId) {

        boolean agregada;
        if (playlistId != null) {
            agregada = playlistService.agregarCancionAPlaylist(playlistId, cancionId);
        } else {
            // Si desde el JS del clic derecho no enviás playlistId, la agrega a la lista predeterminada
            agregada = playlistService.agregarABiblioteca(cancionId);
        }

        if (agregada) {
            return ResponseEntity.ok("Canción agregada a la playlist con éxito.");
        } else {
            return ResponseEntity.badRequest().body("La canción ya está en la playlist.");
        }
    }

    @PostMapping("/crear")
public ResponseEntity<?> crearPlaylist(@RequestParam(name = "nombre") String nombre) {
    if (nombre == null || nombre.trim().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre no puede estar vacío");
    }
    
    Playlist nueva = playlistService.crearPlaylist(nombre.trim());
    return ResponseEntity.ok(nueva);
}

@GetMapping("/listar")
public ResponseEntity<List<Playlist>> listarPlaylists() {
    return ResponseEntity.ok(playlistService.obtenerTodas());
}
}