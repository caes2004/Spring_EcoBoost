let darkmode = localStorage.getItem('darkmode'); // Obtiene el estado de darkmode del localStorage
const toggle = document.getElementById('darkmode-toggle'); // Checkbox de modo oscuro

const enableDarkmode = () => {
    document.body.classList.add('darkmode');
    localStorage.setItem('darkmode', 'active');
    toggle.checked = true; // Sincronizar checkbox con el switch

};

const disableDarkmode = () => {
    document.body.classList.remove('darkmode');
    localStorage.setItem('darkmode', 'inactive');
    toggle.checked = false;// Sincronizar checkbox con el switch
};

// 🌟 Asegurar que el estado persista correctamente al recargar la página
if (darkmode === "active") {
    enableDarkmode();
} else {
    disableDarkmode(); // Si no está activo, forzamos el modo claro
}

// Escuchar cambios en el checkboxxx
toggle.addEventListener("change", () => {
    if (toggle.checked) {
        enableDarkmode();
    } else {
        disableDarkmode();
    }
});
