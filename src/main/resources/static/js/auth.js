"use strict";

const API_LOGIN = "/api/auth/login";
const API_REGISTER = "/api/auth/register";

const PAGES_PAR_ROLE = {
    RESPONSABLE: "/responsable/tableau-de-bord.html",
    TECHNICIEN: "/technicien/interventions.html",
    DEMANDEUR: "/demandeur/mes-demandes.html",
};

const $ = (id) => document.getElementById(id);
const root = document.documentElement;

const REGEX_EMAIL = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

// ---- Thème clair / sombre (même réglage que la page d'accueil) ----
function appliquerTheme(theme) {
    root.dataset.theme = theme;

    $("themeToggle").setAttribute(
        "aria-label",
        theme === "dark" ? "Passer en mode clair" : "Passer en mode sombre"
    );
}

appliquerTheme(
    localStorage.getItem("theme") ||
    (window.matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light")
);

$("themeToggle").addEventListener("click", () => {
    const suivant = root.dataset.theme === "dark" ? "light" : "dark";
    appliquerTheme(suivant);
    localStorage.setItem("theme", suivant);
});

// ---- Afficher / masquer le mot de passe ----
document.querySelectorAll("[data-toggle-password]").forEach((bouton) => {
    bouton.addEventListener("click", () => {
        const champ = $(bouton.dataset.togglePassword);
        const visible = champ.type === "text";

        champ.type = visible ? "password" : "text";
        bouton.textContent = visible ? "Afficher" : "Masquer";
        bouton.setAttribute(
            "aria-label",
            visible ? "Afficher le mot de passe" : "Masquer le mot de passe"
        );
    });
});

// ---- Messages ----
function afficherMessage(texte, succes = false) {
    const zone = $("formMessage");
    zone.textContent = texte;
    zone.hidden = !texte;
    zone.classList.toggle("is-success", succes);
}

function signaler(idChamp, texte) {
    afficherMessage(texte);
    $(idChamp).setAttribute("aria-invalid", "true");
    $(idChamp).focus();
    return false;
}

document.querySelectorAll("input").forEach((champ) => {
    champ.addEventListener("input", () => {
        champ.setAttribute("aria-invalid", "false");
        afficherMessage("");
    });
});

function verrouiller(bouton, texte) {
    bouton.dataset.libelle = bouton.textContent;
    bouton.disabled = true;
    bouton.textContent = texte;
}

function deverrouiller(bouton) {
    bouton.disabled = false;
    bouton.textContent = bouton.dataset.libelle;
}

// ============================================================
// PAGE CONNEXION
// ============================================================
const loginForm = $("loginForm");

if (loginForm) {

    // Se souvenir de moi (seule l'adresse e-mail est mémorisée)
    const emailMemorise = localStorage.getItem("email");

    if (emailMemorise) {
        $("email").value = emailMemorise;
        $("remember").checked = true;
    }

    function validerConnexion() {
        const email = $("email").value.trim();
        const mdp = $("password").value;

        if (!email) return signaler("email", "Saisissez votre adresse e-mail.");
        if (!REGEX_EMAIL.test(email)) return signaler("email", "Cette adresse e-mail n’est pas valide.");
        if (!mdp) return signaler("password", "Saisissez votre mot de passe.");
        if (mdp.length < 6) return signaler("password", "Le mot de passe doit contenir au moins 6 caractères.");

        return true;
    }

    loginForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        if (!validerConnexion()) return;

        if ($("remember").checked) {
            localStorage.setItem("email", $("email").value.trim());
        } else {
            localStorage.removeItem("email");
        }

        const bouton = loginForm.querySelector('button[type="submit"]');
        verrouiller(bouton, "Connexion en cours…");

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
                afficherMessage("Adresse e-mail ou mot de passe incorrect.");
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
            afficherMessage("Connexion impossible pour le moment. Vérifiez que le serveur est démarré.");
        } finally {
            deverrouiller(bouton);
        }
    });

    // Accès invité
    $("guestButton").addEventListener("click", () => {
        window.location.href = "/invite.html";
    });
}

// ============================================================
// PAGE INSCRIPTION
// ============================================================
const registerForm = $("registerForm");

if (registerForm) {

    function validerInscription() {
        const nom = $("nom").value.trim();
        const prenom = $("prenom").value.trim();
        const email = $("email").value.trim();
        const mdp = $("password").value;
        const confirmation = $("confirmPassword").value;

        if (!nom) return signaler("nom", "Saisissez votre nom.");
        if (!prenom) return signaler("prenom", "Saisissez votre prénom.");
        if (!email) return signaler("email", "Saisissez votre adresse e-mail.");
        if (!REGEX_EMAIL.test(email)) return signaler("email", "Cette adresse e-mail n’est pas valide.");
        if (!mdp) return signaler("password", "Choisissez un mot de passe.");
        if (mdp.length < 6) return signaler("password", "Le mot de passe doit contenir au moins 6 caractères.");
        if (mdp !== confirmation) return signaler("confirmPassword", "Les deux mots de passe ne sont pas identiques.");

        return true;
    }

    registerForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        if (!validerInscription()) return;

        const bouton = registerForm.querySelector('button[type="submit"]');
        verrouiller(bouton, "Création du compte…");

        let compteCree = false;

        try {
            const reponse = await fetch(API_REGISTER, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                credentials: "same-origin",
                body: JSON.stringify({
                    nom: $("nom").value.trim(),
                    prenom: $("prenom").value.trim(),
                    email: $("email").value.trim(),
                    motDePasse: $("password").value,
                    role: "DEMANDEUR"
                })
            });

            if (reponse.status === 409) {
                signaler("email", "Un compte existe déjà avec cette adresse e-mail.");
                return;
            }

            if (reponse.status === 400) {
                afficherMessage("Certaines informations sont invalides. Vérifiez le formulaire.");
                return;
            }

            if (!reponse.ok) {
                throw new Error("HTTP " + reponse.status);
            }

            compteCree = true;
            afficherMessage("Compte créé. Redirection vers la connexion…", true);

            setTimeout(() => {
                window.location.href = "/connexion";
            }, 1500);

        } catch (err) {
            console.error(err);
            afficherMessage("Inscription impossible pour le moment. Vérifiez que le serveur est démarré.");
        } finally {
            if (!compteCree) deverrouiller(bouton);
        }
    });
}
