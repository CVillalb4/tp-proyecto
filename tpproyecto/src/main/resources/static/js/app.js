// ==========================================
// ESTADO GLOBAL DE LA APLICACIÓN
// ==========================================
let searchTimeoutId = null;
let delaySubmenuId = null;
let cancionSeleccionadaId = null;

// ==========================================
// 1. INICIALIZACIÓN (Event Listeners Globale)
// ==========================================
document.addEventListener('DOMContentLoaded', () => {
    cargarPlaylistsSidebar();

    // Vinculación explícita de eventos para el menú contextual
    const menuBtn = document.getElementById('menuAddToPlaylist');
    const submenu = document.getElementById('playlistSubmenu');

    if (menuBtn) {
        menuBtn.addEventListener('mouseenter', () => {
            console.log("-> Evento mouseenter detectado en 'Agregar a la playlist'");
            mostrarSubmenuPlaylists();
        });

        menuBtn.addEventListener('mouseleave', () => {
            ocultarSubmenuPlaylistsConDelay();
        });
    }

    if (submenu) {
        submenu.addEventListener('mouseenter', () => {
            cancelarOcultarSubmenu();
        });

        submenu.addEventListener('mouseleave', () => {
            ocultarSubmenuPlaylistsConDelay();
        });
    }

    // Cerrar menús al hacer clic fuera
    document.addEventListener('click', (e) => {
        const searchForm = document.getElementById('searchForm');
        const dropdown = document.getElementById('resultsDropdown');
        const contextMenu = document.getElementById('contextMenu');

        if (searchForm && !searchForm.contains(e.target) && dropdown) {
            dropdown.style.display = 'none';
        }
        if (contextMenu && !contextMenu.contains(e.target)) {
            ocultarContextMenu();
        }
    });
});

// ==========================================
// 2. BÚSQUEDA EN TIEMPO REAL (Topbar)
// ==========================================
function buscarEnTiempoReal() {
    const searchInput = document.getElementById('searchInput');
    const dropdown = document.getElementById('resultsDropdown');
    const clearBtn = document.getElementById('clearBtn');

    if (!searchInput || !dropdown) return;

    const query = searchInput.value.trim();

    if (clearBtn) {
        clearBtn.style.display = query.length > 0 ? 'block' : 'none';
    }

    if (query.length === 0) {
        dropdown.style.display = 'none';
        dropdown.innerHTML = '';
        ocultarContextMenu();
        return;
    }

    clearTimeout(searchTimeoutId);
    searchTimeoutId = setTimeout(() => {
        fetch('/api/canciones/buscar?query=' + encodeURIComponent(query))
            .then(res => res.json())
            .then(canciones => {
                dropdown.innerHTML = '';

                if (canciones.length === 0) {
                    dropdown.innerHTML = `
                        <div style="padding: 16px; color: #B3B3B3; font-size: 0.85rem; line-height: 1.4;">
                            <p style="margin: 0; font-weight: bold; color: #fff;">
                                No se encontraron resultados para "${query}"
                            </p>
                            <p style="margin: 6px 0 0 0; font-size: 0.75rem;">
                                Por favor, revisá la ortografía o intentá otra búsqueda.
                            </p>
                        </div>`;
                } else {
                    canciones.forEach(cancion => {
                        const item = document.createElement('div');
                        item.style.cssText = 'display: flex; align-items: center; padding: 10px 14px; cursor: pointer; border-bottom: 1px solid #242424; transition: background 0.2s; user-select: none;';
                        item.onmouseover = () => item.style.backgroundColor = '#282828';
                        item.onmouseout = () => item.style.backgroundColor = 'transparent';

                        item.innerHTML = `
                            <div style="width: 32px; height: 32px; background-color: #282828; border-radius: 4px; display: flex; align-items: center; justify-content: center; margin-right: 12px; pointer-events: none;">
                                <svg width="16" height="16" viewBox="0 0 24 24" fill="#B3B3B3">
                                    <path d="M12 3v10.55c-.59-.34-1.27-.55-2-.55-2.21 0-4 1.79-4 4s1.79 4 4 4 4-1.79 4-4V7h4V3h-6z"/>
                                </svg>
                            </div>
                            <div style="display: flex; flex-direction: column; pointer-events: none;">
                                <span style="color: #fff; font-size: 0.9rem; font-weight: 500;">${cancion.nombre}</span>
                                <span style="color: #B3B3B3; font-size: 0.75rem;">${cancion.artista} ${cancion.album ? '• ' + cancion.album : ''}</span>
                            </div>
                        `;

                        // EVENTO CLIC DERECHO
                        item.addEventListener('contextmenu', (e) => {
                            e.preventDefault();
                            e.stopPropagation();
                            cancionSeleccionadaId = cancion.id;
                            mostrarContextMenu(e.clientX, e.clientY);
                        });

                        dropdown.appendChild(item);
                    });
                }
                dropdown.style.display = 'block';
            })
            .catch(err => console.error("Error en la búsqueda:", err));
    }, 200);
}

function limpiarBuscador() {
    const searchInput = document.getElementById('searchInput');
    const dropdown = document.getElementById('resultsDropdown');
    const clearBtn = document.getElementById('clearBtn');

    if (searchInput) searchInput.value = '';
    if (dropdown) dropdown.style.display = 'none';
    if (clearBtn) clearBtn.style.display = 'none';
    ocultarContextMenu();
}

// ==========================================
// 3. MENÚ CONTEXTUAL Y SUBMENÚ DE PLAYLISTS
// ==========================================
function mostrarContextMenu(x, y) {
    const contextMenu = document.getElementById('contextMenu');
    if (!contextMenu) return;

    contextMenu.style.left = `${x}px`;
    contextMenu.style.top = `${y}px`;
    contextMenu.style.display = 'block';

    const submenu = document.getElementById('playlistSubmenu');
    if (submenu) submenu.style.display = 'none';
}

function ocultarContextMenu() {
    const contextMenu = document.getElementById('contextMenu');
    if (contextMenu) contextMenu.style.display = 'none';
    cancionSeleccionadaId = null;
}



function mostrarSubmenuPlaylists() {
    cancelarOcultarSubmenu();
    console.log("-> Pasaste el mouse sobre 'Agregar a la playlist'"); // Para comprobar en F12

    const submenu = document.getElementById('playlistSubmenu');
    if (!submenu) {
        console.error("No se encontró el elemento #playlistSubmenu en el DOM");
        return;
    }

    fetch('/api/playlists/listar')
        .then(res => res.json())
        .then(playlists => {
            console.log("Playlists obtenidas para el submenú:", playlists);
            submenu.innerHTML = '';

            if (!Array.isArray(playlists) || playlists.length === 0) {
                submenu.innerHTML = `<div style="padding: 10px 14px; color: #B3B3B3; font-size: 0.8rem; white-space: nowrap;">Sin playlists creadas</div>`;
            } else {
                playlists.forEach(pl => {
                    const option = document.createElement('div');
                    option.style.cssText = 'padding: 10px 14px; color: #fff; font-size: 0.85rem; cursor: pointer; white-space: nowrap; transition: background 0.2s;';
                    option.innerText = pl.nombre;

                    option.onmouseover = () => option.style.backgroundColor = '#3E3E3E';
                    option.onmouseout = () => option.style.backgroundColor = 'transparent';

                    option.onclick = (e) => {
                        e.stopPropagation();
                        e.preventDefault();
                        agregarCancionAPlaylistEspecifica(pl.id, cancionSeleccionadaId);
                    };

                    submenu.appendChild(option);
                });
            }
            // Forzar visualización
            submenu.style.display = 'block';
        })
        .catch(err => console.error("Error al cargar playlists:", err));
}

function ocultarSubmenuPlaylistsConDelay() {
    delaySubmenuId = setTimeout(() => {
        const submenu = document.getElementById('playlistSubmenu');
        if (submenu) submenu.style.display = 'none';
    }, 300); // 300ms de tolerancia para que dé tiempo a mover el mouse sin que se cierre
}

function cancelarOcultarSubmenu() {
    if (delaySubmenuId) {
        clearTimeout(delaySubmenuId);
        delaySubmenuId = null;
    }
}

// ==========================================
// 4. GESTIÓN DE PLAYLISTS (API Backend)
// ==========================================
function crearNuevaPlaylist() {
    const nombre = prompt("Ingresá el nombre de la nueva playlist:");
    if (!nombre || nombre.trim() === "") return;

    fetch('/api/playlists/crear', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: 'nombre=' + encodeURIComponent(nombre.trim())
    })
    .then(async response => {
        if (response.ok) return response.json();
        const errorText = await response.text();
        throw new Error(errorText || 'Error ' + response.status);
    })
    .then(playlist => {
        // Renderizar inmediatamente en el sidebar sin recargar la página
        agregarPlaylistAlSidebarDOM(playlist);
    })
    .catch(error => {
        console.error("Error al crear la playlist:", error);
        alert('No se pudo crear la playlist: ' + error.message);
    });
}

function cargarPlaylistsSidebar() {
    fetch('/api/playlists/listar')
        .then(res => res.json())
        .then(playlists => {
            console.log("Respuesta recibida de /api/playlists/listar:", playlists);

            // Verificamos si realmente es un Array antes de hacer el forEach
            if (!Array.isArray(playlists)) {
                console.error("El backend no devolvió un Array. Devolvió:", typeof playlists, playlists);
                return;
            }

            const contenedor = document.getElementById('userPlaylists');
            if (!contenedor) return;

            contenedor.innerHTML = '<div style="color: #B3B3B3; font-size: 0.85rem; padding: 4px 8px;">Tus playlists:</div>';

            playlists.forEach(pl => {
                agregarPlaylistAlSidebarDOM(pl);
            });
        })
        .catch(err => console.error("Error cargando playlists del sidebar:", err));
}

function agregarPlaylistAlSidebarDOM(playlist) {
    const contenedor = document.getElementById('userPlaylists');
    if (!contenedor) return;

    const item = document.createElement('div');
    item.style.cssText = 'padding: 8px 12px; color: #B3B3B3; font-size: 0.9rem; cursor: pointer; border-radius: 4px; transition: background 0.2s, color 0.2s;';
    item.innerText = playlist.nombre;

    item.onmouseover = () => { item.style.backgroundColor = '#1A1A1A'; item.style.color = '#fff'; };
    item.onmouseout = () => { item.style.backgroundColor = 'transparent'; item.style.color = '#B3B3B3'; };

    contenedor.appendChild(item);
}

function agregarCancionAPlaylistEspecifica(playlistId, cancionId) {
    if (!cancionId) return;

    fetch(`/api/playlists/agregar?cancionId=${cancionId}&playlistId=${playlistId}`, {
        method: 'POST'
    })
    .then(async res => {
        if (res.ok) {
            alert('🎵 ¡Canción agregada a la playlist!');
        } else {
            const txt = await res.text();
            alert('Aviso: ' + txt);
        }
        ocultarContextMenu();
    })
    .catch(err => {
        console.error(err);
        alert('Error al agregar la canción.');
    });
}