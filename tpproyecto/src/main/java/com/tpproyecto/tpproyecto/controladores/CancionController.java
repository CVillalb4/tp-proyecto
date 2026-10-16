package com.tpproyecto.tpproyecto.controladores;

import com.tpproyecto.tpproyecto.clases.Cancion;
import com.tpproyecto.tpproyecto.servicios.CancionService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@ResponseBody
public class CancionController {

    private final CancionService cancionService;

    public CancionController(CancionService cancionService) {
        this.cancionService = cancionService;
    }

    @GetMapping("/buscar")
    public String buscarCanciones(@RequestParam(name = "query", required = false) String query, Model model) {
        if (query != null) {
            List<Cancion> resultados = cancionService.buscarPorNombre(query);
            model.addAttribute("resultados", resultados);
            model.addAttribute("query", query);
        }
        return "buscar";
    }

    @GetMapping("/api/canciones/buscar")
    @ResponseBody
    public List<Cancion> buscarApi(@RequestParam(name = "query", required = false) String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }
        return cancionService.buscarPorNombre(query.trim());
    }

}
