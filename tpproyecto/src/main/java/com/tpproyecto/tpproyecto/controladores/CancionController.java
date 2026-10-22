
package com.tpproyecto.tpproyecto.controladores;

import com.tpproyecto.tpproyecto.clases.Cancion;
import com.tpproyecto.tpproyecto.servicios.CancionService;
import com.tpproyecto.tpproyecto.servicios.PlaylistService;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CancionController {

    private final CancionService cancionService;
    private final PlaylistService playlistService;

    public CancionController(
            CancionService cancionService,
            PlaylistService playlistService) {
        this.cancionService = cancionService;
        this.playlistService = playlistService;
    }

    @GetMapping("/buscar")
    public String buscarCanciones(
            @RequestParam(name = "query", required = false) String query,
            Model model) {

        //model.addAttribute("playlists", playlistService.listarPlaylists());

        if (query != null) {
            List<Cancion> resultados =
                    cancionService.buscarPorNombre(query);

            model.addAttribute("resultados", resultados);
            model.addAttribute("query", query);
        }

        return "buscar";
    }

    @GetMapping("/api/canciones/buscar")
    @ResponseBody
    public List<Cancion> buscarApi(
            @RequestParam(name = "query", required = false) String query) {

        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }

        return cancionService.buscarPorNombre(query.trim());
    }
}
