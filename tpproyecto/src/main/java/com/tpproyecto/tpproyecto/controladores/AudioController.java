package com.tpproyecto.tpproyecto.controladores;


import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class AudioController {

    private final Path carpetaMusica =
            Path.of("music").toAbsolutePath().normalize();

    @GetMapping("/audio/{nombreArchivo}")
    public ResponseEntity<Resource> obtenerAudio(
            @PathVariable String nombreArchivo) {

        if (!nombreArchivo.matches("[a-fA-F0-9-]+\\.mp3")) {
            return ResponseEntity.badRequest().build();
        }

        Path archivo = carpetaMusica.resolve(nombreArchivo).normalize();

        if (!archivo.startsWith(carpetaMusica)
                || !Files.isRegularFile(archivo)) {
            return ResponseEntity.notFound().build();
        }

        Resource recurso = new FileSystemResource(archivo);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + nombreArchivo + "\""
                )
                .body(recurso);
    }
}

