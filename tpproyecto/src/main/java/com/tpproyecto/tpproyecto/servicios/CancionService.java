package com.tpproyecto.tpproyecto.servicios;


import com.tpproyecto.tpproyecto.clases.Cancion;
import com.tpproyecto.tpproyecto.repositorios.CancionRepository;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.UUID;

import java.util.List;

@Service
public class CancionService {

    private final CancionRepository cancionRepository;

    public CancionService(CancionRepository cancionRepository) {
        this.cancionRepository = cancionRepository;
    }

    public List<Cancion> listarCanciones() {
        return cancionRepository.findAll();
    }

    public Cancion guardarCancion(Cancion cancion) {
        return cancionRepository.save(cancion);
    }

    public List<Cancion> buscarPorNombre(String nombre) {
        return cancionRepository.findByNombreContainingIgnoreCase(nombre);
    }

    private final Path carpetaMusica =
        Path.of("music").toAbsolutePath().normalize();


    public Cancion guardarCancionConAudio(
            Cancion cancion,
            MultipartFile archivo) throws IOException {

        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException(
                    "Tenés que seleccionar un archivo MP3.");
        }

        if (archivo.getSize() > 50L * 1024 * 1024) {
            throw new IllegalArgumentException(
                    "El archivo no puede superar los 50 MB.");
        }

        String nombreOriginal = archivo.getOriginalFilename();

        if (nombreOriginal == null
                || !nombreOriginal.toLowerCase(Locale.ROOT)
                        .endsWith(".mp3")) {
            throw new IllegalArgumentException(
                    "Por ahora solo se permiten archivos .mp3.");
        }

        Files.createDirectories(carpetaMusica);

        String nombreArchivo = UUID.randomUUID() + ".mp3";
        Path destino = carpetaMusica.resolve(nombreArchivo);

        try (InputStream entrada = archivo.getInputStream()) {
            Files.copy(
                    entrada,
                    destino,
                    StandardCopyOption.REPLACE_EXISTING);
        }

        cancion.setRutaArchivo("/audio/" + nombreArchivo);

        try {
            return cancionRepository.save(cancion);
        } catch (RuntimeException e) {
            Files.deleteIfExists(destino);
            throw e;
        }
    }
}
