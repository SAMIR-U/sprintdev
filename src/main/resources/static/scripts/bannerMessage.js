document.addEventListener("DOMContentLoaded", () => {
    const banner = document.querySelector(".banner");
    if (banner) {
        setTimeout(() => {
            banner.style.transition = "opacity 0.3s ease";
            banner.style.opacity = "0";
            setTimeout(() => banner.remove(), 300);
        }, 4000); // se oculta después de 4 segundos
    }
});