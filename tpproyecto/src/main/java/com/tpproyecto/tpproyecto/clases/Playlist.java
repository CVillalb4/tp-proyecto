package com.tpproyecto.tpproyecto.clases;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "playlists")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @ManyToMany
    @JoinTable(
        name = "playlist_canciones",
        joinColumns = @JoinColumn(name = "playlist_id"),
        inverseJoinColumns = @JoinColumn(name = "cancion_id"),
        uniqueConstraints = @UniqueConstraint(
            columnNames = {"playlist_id", "cancion_id"}
        )
    )
    private List<Cancion> canciones = new ArrayList<>();

    public Playlist() {
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Cancion> getCanciones() {
        return canciones;
    }

    public void agregarCancion(Cancion cancion) {
        if (!canciones.contains(cancion)) {
            canciones.add(cancion);
        }
    }

    public void quitarCancion(Cancion cancion) {
        canciones.remove(cancion);
    }
}
