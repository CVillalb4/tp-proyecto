package com.tpproyecto.tpproyecto.controladores;

import com.tpproyecto.tpproyecto.clases.Cancion;
import com.tpproyecto.tpproyecto.servicios.CancionService;

import com.tpproyecto.tpproyecto.servicios.FavoritoService;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Controller
public class HomeController {

    private final CancionService cancionService;
    private final FavoritoService favoritoService;

    public HomeController(CancionService cancionService, FavoritoService favoritoService) {
        this.cancionService = cancionService;
        this.favoritoService = favoritoService;
    }

   @GetMapping("/")
    public String inicio(Model model) {
        var canciones = cancionService.listarCanciones();

        Set<Long> favoritosIds = canciones.stream()
                .filter(cancion -> favoritoService.esFavorito(cancion.getId()))
                .map(Cancion::getId)
                .collect(Collectors.toSet());

        model.addAttribute("canciones", canciones);
        model.addAttribute("nuevaCancion", new Cancion());
        model.addAttribute("favoritosIds", favoritosIds);

        model.addAttribute(
                "favoritos",
                canciones.stream()
                        .filter(cancion -> favoritosIds.contains(cancion.getId()))
                        .toList()
        );

        return "index";
    }

    @PostMapping("/canciones")
    public String guardarCancion(
            @ModelAttribute("nuevaCancion") Cancion cancion,
            @RequestParam("archivo") MultipartFile archivo,
            RedirectAttributes redirectAttributes) {

        try {
            cancionService.guardarCancionConAudio(cancion, archivo);

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "¡Canción y archivo MP3 guardados correctamente!");

        } catch (IllegalArgumentException | IOException e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "No se pudo guardar la canción: " + e.getMessage());
        }

        return "redirect:/";
    }

    @PostMapping("/favoritos")
    public String alternarFavorito(
            @RequestParam("cancionId") Long cancionId,
            RedirectAttributes redirectAttributes) {

        try {
            boolean esFavorito =
                    favoritoService.alternarFavorito(cancionId);

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    esFavorito
                            ? "Canción agregada a favoritos."
                            : "Canción quitada de favoritos."
            );

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "No se pudo actualizar el favorito: " + e.getMessage()
            );
        }

        return "redirect:/";
    }

}