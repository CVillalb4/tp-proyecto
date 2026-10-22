package com.tpproyecto.tpproyecto.servicios;

import com.tpproyecto.tpproyecto.clases.Cancion;
import com.tpproyecto.tpproyecto.clases.Favorito;
import com.tpproyecto.tpproyecto.repositorios.CancionRepository;
import com.tpproyecto.tpproyecto.repositorios.FavoritoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final CancionRepository cancionRepository;

    public FavoritoService(
            FavoritoRepository favoritoRepository,
            CancionRepository cancionRepository) {
        this.favoritoRepository = favoritoRepository;
        this.cancionRepository = cancionRepository;
    }

    @Transactional
    public boolean alternarFavorito(Long cancionId) {
        Favorito favoritoExistente =
                favoritoRepository.findByCancionId(cancionId)
                        .orElse(null);

        if (favoritoExistente != null) {
            favoritoRepository.delete(favoritoExistente);
            return false;
        }

        Cancion cancion = cancionRepository.findById(cancionId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe la canción con ID " + cancionId));

        Favorito favorito = new Favorito();
        favorito.setCancion(cancion);
        favoritoRepository.save(favorito);

        return true;
    }

    public boolean esFavorito(Long cancionId) {
        return favoritoRepository.existsByCancionId(cancionId);
    }
}
