package com.tpproyecto.tpproyecto.clases;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "canciones")
public class Cancion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String artista;

    @Column(length = 150)
    private String album;

    @Column(length = 500)
    private String rutaArchivo;

    @Column(nullable = false)
    private int duracionSegundos;

    public Cancion(String nombre, String artista, String album, String rutaArchivo, int duracionSegundos) {
        this.nombre = nombre;
        this.artista = artista;
        this.album = album;
        this.rutaArchivo = rutaArchivo;
        this.duracionSegundos = duracionSegundos;
    }

}
