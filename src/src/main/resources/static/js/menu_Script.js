/*jshint esversion: 6 */

// Deklaracja funkcji ustawiającej szerokość rozwijanego menu opcji logowania względem przycisku rozwijającego
function setStaticOptionWidth() {
    var static_option_button = document.getElementById("static_option_button");
    var static_option_menu = document.getElementById("static_option_menu");
    static_option_menu.style.width = static_option_button.getBoundingClientRect().width + "px";
}

// Deklaracja obiektu przechowującego przyciski rozwijającego menu chowane dla paska nawigacyjnego poruszającego sie za kursorem
var menuObject = {
    offer: new Array("offer", document.getElementById("offer_button"), document.getElementById("offer_menu")),
    animal: new Array("animal", document.getElementById("animal_button"), document.getElementById("animal_menu")),
    staff: new Array("staff", document.getElementById("staff_button"), document.getElementById("staff_menu")),
    management: new Array("management", document.getElementById("management_button"), document.getElementById("management_menu")),
    all: new Array("all", document.getElementById("all_button"), document.getElementById("all_menu"))
};

// Deklaracja funkcji ustawiającej górny margines sekcji main względem wysokości dynamicznego paska menu
function setMainMarginTop() {
    var dynamic_nav = document.getElementById("dynamic_nav"); // Pobiera referencje do elementu o id dynamic_nav
    var main = document.getElementById("main");
    main.style.marginTop = dynamic_nav.getBoundingClientRect().height + "px";
}

// Deklaracja funkcji chowającej lub rozwijającej menu chowne paska nawigacyjnego
function expandMenu(option) {
    var static_option_button = document.getElementById("static_option_button");
    var static_option_menu = document.getElementById("static_option_menu");
    static_option_button.classList.remove("show_log");
    static_option_button.classList.add("hide_log");
    static_option_menu.classList.add("hide");

    Object.keys(menuObject).forEach(key => {
        if(menuObject[key][1] != null && menuObject[key][2] != null && typeof menuObject[key][1] !== undefined && typeof menuObject[key][2] !== undefined)
            if(key != option)
            {
                menuObject[key][1].classList.remove("show_menu");
                menuObject[key][2].classList.remove("show");
                menuObject[key][1].classList.add("hide_menu");
                menuObject[key][2].classList.add("hide");
            }
            else
            {
                if(menuObject[key][1].classList.contains("hide_menu"))
                {
                    menuObject[key][1].classList.remove("hide_menu");
                    menuObject[key][2].classList.remove("hide");
                    menuObject[key][1].classList.add("show_menu");
                    menuObject[key][2].classList.add("show");
                }
                else
                {
                    menuObject[key][1].classList.remove("show_menu");
                    menuObject[key][2].classList.remove("show");
                    menuObject[key][1].classList.add("hide_menu");
                    menuObject[key][2].classList.add("hide");
                }
            }
        });
    setMainMarginTop();
}

// Deklaracja funkcji zamykającej menu chowne paska nawigacyjnego w przypadku zmiany rozmiaru
function autoHideMenu() {
    Object.keys(menuObject).forEach(key => {
        if(menuObject[key][1] != null && menuObject[key][2] != null && typeof menuObject[key][1] !== undefined && typeof menuObject[key][2] !== undefined)
            if(window.getComputedStyle(menuObject[key][1], null).display == "none")
            {
                menuObject[key][1].classList.remove("show_menu");
                menuObject[key][2].classList.remove("show");
                menuObject[key][1].classList.add("hide_menu");
                menuObject[key][2].classList.add("hide");
                closeCategoryMenu();
            }
        });
}

function autoHideAllMenu() {
    allMenuButton = document.getElementById("all_button");
    allMenu = document.getElementById("all_menu");
    block = 0
    for (let i = 0; i < allMenu.children.length; i++)
        if(window.getComputedStyle(allMenu.children[i], null).display == "block")
            block++;
    if(block == 1)
        allMenuButton.classList.add("hide");
    else
        allMenuButton.classList.remove("hide");
}

var xButtons = document.getElementsByClassName("x_icon");

// Deklaracja funkcji zamykającej menu chowane paska nawigacyjnego
function closeMenu()
{
    Object.keys(menuObject).forEach(key => {
        if(menuObject[key][1] != null && menuObject[key][2] != null && typeof menuObject[key][1] !== undefined && typeof menuObject[key][2] !== undefined)
        {
            menuObject[key][1].classList.remove("show_menu");
            menuObject[key][2].classList.remove("show");
            menuObject[key][1].classList.add("hide_menu");
            menuObject[key][2].classList.add("hide");
        }
    });
    setMainMarginTop();
}

// Deklaracja funkcji zmieniającej margines górny dla paska nawigacyjnego zmieniającego położenie
function scrollMenu() {
    var dynamic_menu = document.getElementById("dynamic_menu"); // Pobiera referencje do elementu o id dynamic_nav
    var static_menu = document.getElementById("static_menu");
    if (window.pageYOffset <= static_menu.getBoundingClientRect().height)
    {
        dynamic_menu.style.top = static_menu.getBoundingClientRect().height - window.pageYOffset + "px";
    }
    else
    {
        dynamic_menu.style.top  = "0";
    }
}

var static_option_button = document.getElementById("static_option_button");
var static_option_menu = document.getElementById("static_option_menu");

// Deklaracja funkcji zamykającej lub otwierającej menu logowania
function logMenu() {
    closeMenu();
    static_option_button.classList.toggle("hide_log");
    static_option_button.classList.toggle("show_log");
    static_option_menu.classList.toggle("hide");
    setMainMarginTop();
}

var category_button = document.getElementsByClassName("category_button");
// Deklaracja funkcji zamykającej lub otwierającej menu kategorii
function categoryMenu() {
    this.classList.toggle("hide_category");
    this.classList.toggle("show_category");
    setMainMarginTop();
}

// Deklaracja funkcji zamykającej menu kategorii
function closeCategoryMenu() {
    var category_button = document.getElementsByClassName("category_button");
    Object.keys(category_button).forEach(key => {
        if(category_button[key] != null)
        {
        category_button[key].classList.remove("show_category");
        category_button[key].classList.add("hide_category");
        }
    });
}

// Deklaracja funkcji ustawiającej informacje o autorach programu i aktualną date
function setCopyright() {
    var copyright = document.getElementById("copyright");
    var date = new Date().getFullYear();
    copyright.innerHTML = "&#169;" + date + " Michał Dybaś, Krystian Dybaś";
}

window.addEventListener('load', function() {
    setCopyright();
    setStaticOptionWidth();
    autoHideAllMenu();
    setMainMarginTop();
    scrollMenu();
});

Object.keys(menuObject).forEach(key => {
    if(menuObject[key][1] != null && menuObject[key][2] != null && typeof menuObject[key][1] !== undefined && typeof menuObject[key][2] !== undefined)
        menuObject[key][1].addEventListener("click", function() {
        closeCategoryMenu();
        expandMenu(menuObject[key][0]);
        });
});

Object.keys(xButtons).forEach(key => {
    if(xButtons[key] != null)
        xButtons[key].addEventListener("click", function() {
        closeCategoryMenu();
        closeMenu();
        });
});

Object.keys(category_button).forEach(key => {
    if(category_button[key] != null)
        category_button[key].addEventListener("click", categoryMenu);
});

static_option_button.addEventListener("click", function() {
    closeCategoryMenu();
    logMenu();
});

window.addEventListener("resize", function() {
    autoHideMenu();
    autoHideAllMenu();
    setMainMarginTop();
});

window.addEventListener("scroll", function() {
    scrollMenu();
});