"use strict";

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

// ---- Fonctionnalités clés (onglets) ----
const FONCTIONNALITES = [
    {
        id: "demandes",
        onglet: "Demandes",
        titre: "Un problème ou une panne ? Signalez-le facilement",
        texte: "Lorsqu’une panne survient, le demandeur peut créer une demande en décrivant le problème rencontré, en précisant son niveau d’urgence et en suivant son évolution jusqu’à sa résolution.",
        points: [
            "Quatre niveaux de priorité : critique, haute, normale et basse",
            "Durée estimée comprise entre 1 et 8 heures ; au-delà, la demande est refusée",
            "Suivi des différents statuts : ouverte, assignée, en cours ou terminée"
        ],
        image: "demandes.jpg"
    },
    {
        id: "planification",
        onglet: "Planification",
        titre: "Une organisation optimisée des interventions",
        texte: "Le responsable définit les journées de travail et l’application propose une répartition des demandes en tenant compte des priorités, des compétences des techniciens et de leur disponibilité.",
        points: [
            "Les demandes les plus urgentes et les plus anciennes sont traitées en priorité",
            "Le responsable peut ajuster la planification avant de la valider",
            "Une comparaison permet d’évaluer les résultats de la planification proposée"
        ],
        image: "planification.jpg"
    },
    {
        id: "planning",
        onglet: "Planning",
        titre: "Un suivi clair de l’activité des techniciens",
        texte: "Le planning offre une vue quotidienne des interventions de chaque technicien, permettant de consulter rapidement sa charge de travail et ses disponibilités.",
        points: [
            "Affichage de la charge de travail en heures, avec une limite de 8 heures par jour",
            "Visualisation des jours d’indisponibilité directement sur le planning",
            "Signalement des situations où les règles de planification ne sont pas respectées"
        ],
        image: "planning.jpg"
    },
    {
        id: "alertes",
        onglet: "Alertes",
        titre: "Une communication au bon moment",
        texte: "Les notifications permettent d’informer automatiquement chaque intervenant des événements importants liés aux demandes et aux interventions.",
        points: [
            "Le responsable reçoit un e-mail lorsqu’une nouvelle demande est créée",
            "Le technicien est averti lorsqu’une intervention lui est attribuée",
            "Le demandeur est informé lorsque sa panne est résolue"
        ],
        image: "alertes.jpg"
    },
    {
        id: "tableaux-de-bord",
        onglet: "Tableaux de bord",
        titre: "Une vision globale grâce aux indicateurs",
        texte: "Le responsable dispose de graphiques et d’indicateurs synthétiques pour suivre l’activité, analyser les performances et faciliter la prise de décision.",
        points: [
            "Répartition des demandes selon leur statut, leur catégorie et leur priorité",
            "Suivi du délai moyen de résolution et du respect des délais",
            "Visualisation de la charge de travail des techniciens et des demandes en attente"
        ],
        image: "tableaux_de_bord.jpg"
    }
];

const tabsBox = $("featureTabs");
const keyPanel = $("keyPanel");
const boutonsOnglets = [];

function afficherFonctionnalite(index, animer = true) {
    const f = FONCTIONNALITES[index];

    boutonsOnglets.forEach((bouton, i) => {
        const actif = i === index;
        bouton.setAttribute("aria-selected", String(actif));
        bouton.tabIndex = actif ? 0 : -1;
    });

    $("keyKicker").textContent = f.onglet.toUpperCase();
    $("keyTitle").textContent = f.titre;
    $("keyText").textContent = f.texte;
    $("keyPhoto").style.setProperty("--img", "url('/images/" + f.image + "')");
    keyPanel.setAttribute("aria-labelledby", "tab-" + f.id);

    const liste = $("keyList");
    liste.textContent = "";
    f.points.forEach((point) => {
        const li = document.createElement("li");
        li.textContent = point;
        liste.appendChild(li);
    });

    if (animer) {
        keyPanel.classList.remove("is-changing");
        void keyPanel.offsetWidth;
        keyPanel.classList.add("is-changing");
    }
}

FONCTIONNALITES.forEach((f, i) => {
    const bouton = document.createElement("button");
    bouton.type = "button";
    bouton.className = "tab";
    bouton.id = "tab-" + f.id;
    bouton.setAttribute("role", "tab");
    bouton.setAttribute("aria-controls", "keyPanel");
    bouton.textContent = f.onglet;

    bouton.addEventListener("click", () => afficherFonctionnalite(i));

    bouton.addEventListener("keydown", (e) => {
        let cible = null;

        if (e.key === "ArrowRight") cible = (i + 1) % FONCTIONNALITES.length;
        if (e.key === "ArrowLeft") cible = (i - 1 + FONCTIONNALITES.length) % FONCTIONNALITES.length;

        if (cible !== null) {
            e.preventDefault();
            afficherFonctionnalite(cible);
            boutonsOnglets[cible].focus();
        }
    });

    boutonsOnglets.push(bouton);
    tabsBox.appendChild(bouton);
});

// Par défaut : la première fonctionnalité est affichée
afficherFonctionnalite(0, false);