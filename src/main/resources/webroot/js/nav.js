/*jshint globalstrict: true*/
/* jshint esnext: true */
/* jshint browser:true */
"use strict";

document.addEventListener("DOMContentLoaded", init);

function init() {
    if (debug) {
        console.log("init");
    }
    registerNavEventListeners();
}

function registerNavEventListeners() {
    let aboutNav = document.querySelector("#js-nav-about");
    let registerNav = document.querySelector("#js-nav-register");
    let loginNav = document.querySelector("#js-nav-login");
    aboutNav.addEventListener("click", modalAbout);
    registerNav.addEventListener("click", modalRegister);
    loginNav.addEventListener("click", modalLogin);
}

function modalAbout() {
    let aboutModal = document.createElement('div');
    aboutModal.classList.add("modal");
    aboutModal.classList.add("modal-about");
    aboutModal.innerHTML = "<h1 class='modal-header'>About us</h1>";
    mui.overlay('on', aboutModal);
}

function modalRegister() {
    let registerModal = document.createElement('div');
    registerModal.classList.add("modal");
    registerModal.classList.add("modal-register");
    let resString = "<h1 class='modal-header'>Register</h1>";
    resString += "<form class='mui-form'>";
    resString += "<div class='mui-textfield mui-textfield--float-label'>";
    resString += "<input type='text'>";
    resString += "<label>Username</label>";
    resString += "</div>";
    resString += "<div class='mui-textfield mui-textfield--float-label'>";
    resString += "<input type='password'>";
    resString += "<label>Password</label>";
    resString += "</div>";
    resString += "<div class='mui-textfield mui-textfield--float-label'>";
    resString += "<input type='password'>";
    resString += "<label>Confirm password</label>";
    resString += "</div>";
    resString += "<button type='submit' class='mui-btn mui-btn--raised'>Register</button>";
    resString += "</form>";
    registerModal.innerHTML = resString;
    mui.overlay('on', registerModal);
}

function modalLogin() {
    let loginModal = document.createElement('div');
    loginModal.classList.add("modal");
    loginModal.classList.add("modal-login");
    let resString = "<h1 class='modal-header'>Login</h1>";
    resString += "<form class='mui-form'>";
    resString += "<div class='mui-textfield mui-textfield--float-label'>";
    resString += "<input type='text'>";
    resString += "<label>Username</label>";
    resString += "</div>";
    resString += "<div class='mui-textfield mui-textfield--float-label'>";
    resString += "<input type='password'>";
    resString += "<label>Password</label>";
    resString += "</div>";
    resString += "<button type='submit' class='mui-btn mui-btn--raised'>Login</button>";
    resString += "</form>";
    loginModal.innerHTML = resString;
    mui.overlay('on', loginModal);
}
