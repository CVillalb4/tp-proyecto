package com.tpproyecto.tpproyecto;

import com.tpproyecto.tpproyecto.clases.Cancion;
import com.tpproyecto.tpproyecto.repositorios.CancionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CargarDatosIniciales implements CommandLineRunner {

private final CancionRepository cancionRepository;

    public CargarDatosIniciales(CancionRepository cancionRepository) {
        this.cancionRepository = cancionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Solo inserta si la base de datos está vacía
        if (cancionRepository.count() == 0) {
            cancionRepository.save(new Cancion("Bohemian Rhapsody", "Queen", "A Night at the Opera", null, 354));
            cancionRepository.save(new Cancion("De Música Ligera", "Soda Stereo", "Canción Animal", null, 212));
            cancionRepository.save(new Cancion("Hotel California", "Eagles", "Hotel California", null, 390));
            cancionRepository.save(new Cancion("Thrift Shop", "Macklemore & Ryan Lewis", "The Heist", null, 235));
            cancionRepository.save(new Cancion("Billie Jean", "Michael Jackson", "Thriller", null, 294));
        }
    }
}