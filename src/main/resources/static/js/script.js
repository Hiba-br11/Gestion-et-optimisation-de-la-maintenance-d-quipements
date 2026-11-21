
"use strict";

const API_LOGIN = "/api/auth/login";

const PAGES_PAR_ROLE = {
    RESPONSABLE: "responsable/tableau-de-bord.html",
    TECHNICIEN: "technicien/interventions.html",
    DEMANDEUR: "demandeur/mes-demandes.html",
};

const $ = (id) => document.getElementById(id);
const root = document.documentElement;

// ---- Thème clair / sombre ----
function appliquerTheme(theme) {
    root.dataset.theme = theme;

    $("themeToggle").setAttribute(
        "aria-label",
        theme === "dark" ? "Passer en mode clair" : "Passer en mode sombre"
    );
}

const themeEnregistre = localStorage.getItem("theme");

appliquerTheme(
    themeEnregistre ||
    (window.matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light")
);

$("themeToggle").addEventListener("click", () => {
    const suivant = root.dataset.theme === "dark" ? "light" : "dark";
    appliquerTheme(suivant);
    localStorage.setItem("theme", suivant);
});

// ---- Menu mobile ----
const menuToggle = $("menuToggle");
const mainNav = $("mainNav");

menuToggle.addEventListener("click", () => {
    const ouvert = mainNav.classList.toggle("is-open");
    menuToggle.setAttribute("aria-expanded", String(ouvert));
});

mainNav.querySelectorAll("a").forEach((lien) => {
    lien.addEventListener("click", () => {
        mainNav.classList.remove("is-open");
        menuToggle.setAttribute("aria-expanded", "false");
    });
});

// ---- Afficher / masquer le mot de passe ----
$("passwordToggle").addEventListener("click", (e) => {
    const champ = $("password");
    const visible = champ.type === "text";

    champ.type = visible ? "password" : "text";
    e.currentTarget.textContent = visible ? "◉" : "Masquer";
    e.currentTarget.setAttribute(
        "aria-label",
        visible ? "Afficher le mot de passe" : "Masquer le mot de passe"
    );
});

// ---- Se souvenir de moi ----
const emailMemorise = localStorage.getItem("email");

if (emailMemorise) {
    $("email").value = emailMemorise;
    $("remember").checked = true;
}

// ---- Messages et validation ----
function afficherAlerte(message) {
    const alerte = $("formMessage");
    alerte.textContent = message;
    alerte.hidden = !message;
}

function valider() {
    const email = $("email").value.trim();
    const mdp = $("password").value;

    if (!email) {
        afficherAlerte("Saisissez votre adresse e-mail.");
        $("email").setAttribute("aria-invalid", "true");
        return false;
    }

    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
        afficherAlerte("Cette adresse e-mail n’est pas valide.");
        $("email").setAttribute("aria-invalid", "true");
        return false;
    }

    if (!mdp) {
        afficherAlerte("Saisissez votre mot de passe.");
        $("password").setAttribute("aria-invalid", "true");
        return false;
    }

    if (mdp.length < 6) {
        afficherAlerte("Le mot de passe doit contenir au moins 6 caractères.");
        $("password").setAttribute("aria-invalid", "true");
        return false;
    }

    $("email").setAttribute("aria-invalid", "false");
    $("password").setAttribute("aria-invalid", "false");
    afficherAlerte("");

    return true;
}

["email", "password"].forEach((id) => {
    $(id).addEventListener("input", () => {
        $("formMessage").textContent = "";
        $("formMessage").hidden = true;
        $(id).setAttribute("aria-invalid", "false");
    });
});

// ---- Envoi du formulaire ----
$("loginForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    if (!valider()) return;

    if ($("remember").checked) {
        localStorage.setItem("email", $("email").value.trim());
    } else {
        localStorage.removeItem("email");
    }

    const bouton = $("loginForm").querySelector('button[type="submit"]');

    bouton.disabled = true;
    bouton.textContent = "Connexion en cours…";

    try {
        const reponse = await fetch(API_LOGIN, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            credentials: "same-origin",
            body: JSON.stringify({
                email: $("email").value.trim(),
                motDePasse: $("password").value
            })
        });

        if (reponse.status === 401) {
            afficherAlerte("Adresse e-mail ou mot de passe incorrect.");
            return;
        }

        if (!reponse.ok) {
            throw new Error("HTTP " + reponse.status);
        }

        const { role } = await reponse.json();
        const page = PAGES_PAR_ROLE[role];

        if (!page) {
            throw new Error("Rôle inconnu : " + role);
        }

        window.location.href = page;

    } catch (err) {
        console.error(err);
        afficherAlerte(
            "Connexion impossible pour le moment. Vérifiez que le serveur est démarré."
        );
    } finally {
        bouton.disabled = false;
        bouton.textContent = "➜ Se connecter";
    }
});

// ---- Accès invité ----
$("guestButton").addEventListener("click", () => {
    window.location.href = "invite.html";
});
