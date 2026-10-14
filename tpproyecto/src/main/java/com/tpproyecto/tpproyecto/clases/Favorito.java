package com.tpproyecto.tpproyecto.clases;

import jakarta.persistence.*;

@Entity
@Table(
    name = "favoritos",
    uniqueConstraints = @UniqueConstraint(
        columnNames = "cancion_id"
    )
)
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(
        name = "cancion_id",
        nullable = false,
        unique = true
    )
    private Cancion cancion;

    public Favorito() {
    }

    public Long getId() {
        return id;
    }

    public Cancion getCancion() {
        return cancion;
    }

    public void setCancion(Cancion cancion) {
        this.cancion = cancion;
    }
}