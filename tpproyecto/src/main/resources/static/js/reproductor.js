
document.addEventListener("DOMContentLoaded", () => {
    const audio = document.getElementById("audio");
    const nombreActual = document.getElementById("cancion-actual");
    const botonReproducir = document.getElementById("reproducir");
    const botonAnterior = document.getElementById("anterior");
    const botonSiguiente = document.getElementById("siguiente");
    const barra = document.getElementById("barra-progreso");
    const tiempoActual = document.getElementById("tiempo-actual");
    const duracionTotal = document.getElementById("duracion-total");

    const botonesCanciones = [
        ...document.querySelectorAll(".seleccionar-cancion")
    ];

    let indiceActual = -1;

    function formatoTiempo(segundos) {
        if (!Number.isFinite(segundos)) {
            return "0:00";
        }

        const minutos = Math.floor(segundos / 60);
        const resto = Math.floor(segundos % 60);

        return `${minutos}:${String(resto).padStart(2, "0")}`;
    }

    function seleccionarCancion(indice) {
        if (indice < 0 || indice >= botonesCanciones.length) {
            return;
        }

        indiceActual = indice;

        const boton = botonesCanciones[indice];
        const nombre = boton.dataset.nombre || "Canción desconocida";
        const artista = boton.dataset.artista || "";
        const ruta = boton.dataset.audio;

        if (!ruta) {
            nombreActual.textContent = "La canción no tiene una ruta de audio.";
            return;
        }

        audio.src = ruta;
        audio.load();

        nombreActual.textContent = artista
            ? `${nombre} — ${artista}`
            : nombre;

        barra.value = 0;
        tiempoActual.textContent = "0:00";
        duracionTotal.textContent = "0:00";

        audio.play().then(() => {
            botonReproducir.textContent = "⏸ Pausar";
        }).catch(() => {
            botonReproducir.textContent = "▶ Reproducir";
        });
    }

    botonReproducir.addEventListener("click", () => {
        if (indiceActual === -1) {
            if (botonesCanciones.length > 0) {
                seleccionarCancion(0);
            }
            return;
        }

        if (audio.paused) {
            audio.play().then(() => {
                botonReproducir.textContent = "⏸ Pausar";
            }).catch(() => {
                botonReproducir.textContent = "▶ Reproducir";
            });
        } else {
            audio.pause();
            botonReproducir.textContent = "▶ Reproducir";
        }
    });

    let ultimoClicAnterior = 0;
    let temporizadorAnterior;

    botonAnterior.addEventListener("click", () => {
        const ahora = Date.now();

        if (ahora - ultimoClicAnterior < 300) {
            clearTimeout(temporizadorAnterior);
            ultimoClicAnterior = 0;

            if (indiceActual > 0) {
                seleccionarCancion(indiceActual - 1);
            }

            return;
        }

        ultimoClicAnterior = ahora;

        temporizadorAnterior = setTimeout(() => {
            if (indiceActual === -1) {
                ultimoClicAnterior = 0;
                return;
            }

            audio.currentTime = 0;

            ultimoClicAnterior = 0;
        }, 300);
    });

    botonSiguiente.addEventListener("click", () => {
        if (indiceActual < botonesCanciones.length - 1) {
            seleccionarCancion(indiceActual + 1);
        }
    });


    audio.addEventListener("loadedmetadata", () => {
        barra.max = audio.duration || 0;
        duracionTotal.textContent = formatoTiempo(audio.duration);
    });

    audio.addEventListener("timeupdate", () => {
        barra.value = audio.currentTime;
        tiempoActual.textContent = formatoTiempo(audio.currentTime);
    });

    barra.addEventListener("input", () => {
        audio.currentTime = Number(barra.value);
    });

    audio.addEventListener("ended", () => {
        if (indiceActual < botonesCanciones.length - 1) {
            seleccionarCancion(indiceActual + 1);
        } else {
            botonReproducir.textContent = "▶ Reproducir";
        }
    });

    audio.addEventListener("pause", () => {
        botonReproducir.textContent = "▶ Reproducir";
    });

    audio.addEventListener("play", () => {
        botonReproducir.textContent = "⏸ Pausar";
    });

    botonesCanciones.forEach((boton, indice) => {
        boton.addEventListener("click", () => {
            seleccionarCancion(indice);
        });
    });
});
