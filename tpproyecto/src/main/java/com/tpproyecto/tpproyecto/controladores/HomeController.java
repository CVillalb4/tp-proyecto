package com.tpproyecto.tpproyecto.controladores;

import com.tpproyecto.tpproyecto.clases.Cancion;
import com.tpproyecto.tpproyecto.servicios.CancionService;

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

    public HomeController(CancionService cancionService) {
        this.cancionService = cancionService;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("canciones",cancionService.listarCanciones());

        model.addAttribute("nuevaCancion", new Cancion());

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

}