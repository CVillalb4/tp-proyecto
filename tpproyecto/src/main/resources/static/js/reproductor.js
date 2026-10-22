
document.addEventListener("DOMContentLoaded", () => {
    const audio = document.getElementById("audio");

    const botonVolumen = document.getElementById('boton-volumen');
    const iconoVolumen = document.getElementById('icono-volumen');
    const barraVolumen = document.getElementById('barra-volumen');

    let ultimoVolumen = 0.7;

    audio.volume = 0.7;
    barraVolumen.value = 70;

    const nombreActual = document.getElementById("cancion-actual");
    const botonReproducir = document.getElementById("reproducir");
    const botonAnterior = document.getElementById("anterior");
    const botonSiguiente = document.getElementById("siguiente");
    const barra = document.getElementById("barra-progreso");
    const tiempoActual = document.getElementById("tiempo-actual");
    const duracionTotal = document.getElementById("duracion-total");
    const iconoReproducir = document.getElementById("icono-reproducir");

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
            actualizarBotonReproducir();
        }).catch(() => {
            actualizarBotonReproducir();
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
                actualizarBotonReproducir();
            }).catch(() => {
                actualizarBotonReproducir();
            });
        } else {
            audio.pause();
            actualizarBotonReproducir();
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
            actualizarBotonReproducir();
        }
    });

    audio.addEventListener("pause", () => {
        actualizarBotonReproducir();
    });

    audio.addEventListener("play", () => {
        actualizarBotonReproducir();
    });

    botonesCanciones.forEach((boton, indice) => {
        boton.addEventListener("click", () => {
            seleccionarCancion(indice);
        });
    });


    function actualizarBotonReproducir() {
        if (audio.paused) {
            iconoReproducir.innerHTML =
                '<path d="M8 5v14l11-7z" fill="currentColor"/>';
            botonReproducir.setAttribute("aria-label", "Reproducir");
            botonReproducir.title = "Reproducir";
        } else {
            iconoReproducir.innerHTML =
                '<path d="M6 5h4v14H6zM14 5h4v14h-4z" fill="currentColor"/>';
            botonReproducir.setAttribute("aria-label", "Pausar");
            botonReproducir.title = "Pausar";
        }
    }

    function actualizarIconoVolumen() {
        if (audio.muted || audio.volume === 0) {
            iconoVolumen.innerHTML = `
            <polygon points="11 5 6 9 2 9 2 15 6 15 11 19 11 5"></polygon>
            <line x1="23" y1="9" x2="17" y2="15"></line>
            <line x1="17" y1="9" x2="23" y2="15"></line>
        `;
            botonVolumen.title = 'Activar sonido';
            botonVolumen.setAttribute('aria-label', 'Activar sonido');
        } else if (audio.volume < 0.5) {
            iconoVolumen.innerHTML = `
            <polygon points="11 5 6 9 2 9 2 15 6 15 11 19 11 5"></polygon>
            <path d="M15.5 8.5a5 5 0 0 1 0 7"></path>
        `;
            botonVolumen.title = 'Silenciar';
            botonVolumen.setAttribute('aria-label', 'Silenciar');
        } else {
            iconoVolumen.innerHTML = `
            <polygon points="11 5 6 9 2 9 2 15 6 15 11 19 11 5"></polygon>
            <path d="M15.5 8.5a5 5 0 0 1 0 7"></path>
            <path d="M19 5a10 10 0 0 1 0 14"></path>
        `;
            botonVolumen.title = 'Silenciar';
            botonVolumen.setAttribute('aria-label', 'Silenciar');
        }
    }

    // Cambiar el volumen arrastrando o haciendo clic en la barra.
    barraVolumen.addEventListener('input', () => {
        const volumen = Number(barraVolumen.value) / 100;

        audio.volume = volumen;
        audio.muted = volumen === 0;

        if (volumen > 0) {
            ultimoVolumen = volumen;
        }

        actualizarIconoVolumen();
    });

    // Silenciar o recuperar el volumen con el icono.
    botonVolumen.addEventListener('click', () => {
        if (audio.muted || audio.volume === 0) {
            audio.muted = false;
            audio.volume = ultimoVolumen > 0 ? ultimoVolumen : 0.7;
            barraVolumen.value = audio.volume * 100;
        } else {
            ultimoVolumen = audio.volume;
            audio.muted = true;
        }

        actualizarIconoVolumen();
    });

    // Mantener sincronizados el icono y la barra.
    audio.addEventListener('volumechange', () => {
        if (!audio.muted) {
            barraVolumen.value = audio.volume * 100;
        }

        actualizarIconoVolumen();
    });

    actualizarIconoVolumen();

});
