package com.tpproyecto.tpproyecto.controladores;

import com.tpproyecto.tpproyecto.servicios.PlaylistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.Map;


@Controller
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping("/biblioteca")
    public String mostrarBiblioteca(Model model) {
        model.addAttribute(
                "playlists",
                playlistService.listarPlaylists()
        );

        return "biblioteca";
    }

    @GetMapping("/biblioteca/playlist/{id}")
    public String mostrarPlaylist(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute(
                    "playlist",
                    playlistService.obtenerPlaylist(id));

            return "playlist";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage());

            return "redirect:/biblioteca";
        }
    }


    
    @PostMapping("/biblioteca/playlist/{playlistId}/quitar-cancion")
    public String quitarCancion(
            @PathVariable Long playlistId,
            @RequestParam Long cancionId,
            RedirectAttributes redirectAttributes) {

        try {
            playlistService.quitarCancion(playlistId, cancionId);

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "Canción quitada de la playlist.");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage());
        }

        return "redirect:/biblioteca/playlist/" + playlistId;
    }



    @PostMapping("/biblioteca/crear")
    public String crearPlaylist(
            @RequestParam("nombre") String nombre,
            RedirectAttributes redirectAttributes) {
        try {
            playlistService.crearPlaylist(nombre);
            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "Playlist creada correctamente."
            );
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/biblioteca";
    }

    @PostMapping("/biblioteca/eliminar/{id}")
    public String eliminarPlaylist(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        try {
            playlistService.eliminarPlaylist(id);
            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "Playlist eliminada correctamente."
            );
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/biblioteca";
    }


    @PostMapping("/biblioteca/agregar-cancion")
    public String agregarCancion(
            @RequestParam("playlistId") Long playlistId,
            @RequestParam("cancionId") Long cancionId,
            RedirectAttributes redirectAttributes) {
        try {
            playlistService.agregarCancion(playlistId, cancionId);
            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "Canción agregada a la playlist.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage());
        }

        return "redirect:/";
    }

    @PostMapping("/api/playlists/agregar-cancion")
    @ResponseBody
    public ResponseEntity<String> agregarCancionApi(
            @RequestParam("playlistId") Long playlistId,
            @RequestParam("cancionId") Long cancionId) {
        try {
            playlistService.agregarCancion(playlistId, cancionId);
            return ResponseEntity.ok("Canción agregada a la playlist.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/api/playlists")
    @ResponseBody
    public List<Map<String, Object>> listarPlaylistsApi() {
        return playlistService.listarPlaylists()
                .stream()
                .map(playlist -> Map.<String, Object>of(
                        "id", playlist.getId(),
                        "nombre", playlist.getNombre()))
                .toList();
    }

}
