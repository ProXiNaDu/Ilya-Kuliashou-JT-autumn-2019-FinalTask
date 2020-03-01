'use strict';

const menu = document.getElementById('menu');
const errorMsg = document.getElementById('errorMsg');
const errorText = document.getElementById('errorText');
const signInForm = document.getElementById('signInForm');
const signUpForm = document.getElementById('signUpForm');
const openSignInFormBtn = document.getElementById('openSignInFormBtn');
const openSignUpFormBtn = document.getElementById('openSignUpFormBtn');
const switchToSignInFormBtn = document.getElementById('switchToSignInFormBtn');
const switchToSignUpFormBtn = document.getElementById('switchToSignUpFormBtn');
const logOutBtn = document.getElementById('logOutBtn');
const closeMsgBtn = document.getElementById('closeMsgBtn');
const closeSignInFormBtn = document.getElementById('closeSignInFormBtn');
const closeSignUpFormBtn = document.getElementById('closeSignUpFormBtn');
const foundTours = document.getElementById('foundTours');
const userDisplayName = document.getElementById('userDisplayName');
const userBalance = document.getElementById('userBalance');
const userBalanceBtn = document.getElementById('userBalanceBtn');
const userToursBtn = document.getElementById('userToursBtn');
const addMoneyForm = document.getElementById('addMoneyForm');
const closeAddMoneyFormBtn = document.getElementById('closeAddMoneyFormBtn');
const loader = document.getElementById('loader');
const searchTourForm = document.getElementById('searchTourForm');

let user;
let tourEl;

function createTourElement() {
    let temp;

    tourEl = document.createElement('li');
    tourEl.classList.add('tour');
    tourEl.appendChild(document.createElement('div'));
    tourEl.lastChild.appendChild(document.createElement('div'));

    temp = tourEl.lastChild.lastChild;
    temp.appendChild(document.createElement('span'));
    temp.lastChild.classList.add('tourName');
    temp.appendChild(document.createElement('span'));
    temp.lastChild.classList.add('tourCity');
    temp.appendChild(document.createElement('span'));
    temp.lastChild.classList.add('tourType');

    tourEl.lastChild.appendChild(document.createElement('span'));
    tourEl.lastChild.lastChild.classList.add('tourPrice');

    tourEl.appendChild(document.createElement('div'));
    temp = tourEl.lastChild;
    temp.appendChild(document.createElement('span'));
    temp.lastChild.classList.add('сheckInDate');
    temp.appendChild(document.createElement('p'));
    temp.lastChild.classList.add('tourDescription');
    temp.appendChild(document.createElement('button'));
    temp.lastChild.classList.add('bookTourBtn');
    temp.lastChild.innerHTML = 'Book a Tour';
    temp.appendChild(document.createElement('span'));
    temp.lastChild.classList.add('userDiscount');
}

function init() {
    openSignInFormBtn.addEventListener('click', () => {
        signInForm.classList.remove('hidden');
    });
    openSignUpFormBtn.addEventListener('click', () => {
        signUpForm.classList.remove('hidden');
    });
    userBalanceBtn.addEventListener('click', () => {
        addMoneyForm.classList.remove('hidden');
    });

    switchToSignInFormBtn.addEventListener('click', () => {
        signInForm.classList.remove('hidden');
        signUpForm.classList.add('hidden');
    });
    switchToSignUpFormBtn.addEventListener('click', () => {
        signUpForm.classList.remove('hidden');
        signInForm.classList.add('hidden');
    });

    closeMsgBtn.addEventListener('click', () => {
        errorMsg.classList.add('hidden');
    });
    closeSignInFormBtn.addEventListener('click', () => {
        signInForm.classList.add('hidden');
    });
    closeSignUpFormBtn.addEventListener('click', () => {
        signUpForm.classList.add('hidden');
    });
    closeAddMoneyFormBtn.addEventListener('click', () => {
        addMoneyForm.classList.add('hidden');
    });

    userToursBtn.addEventListener('click', () => {
        sendRequest('CustomerHandler', JSON.stringify({
            action: 'GET_CUSTOMER_TOURS'
        }), displayTours);
    });
    logOutBtn.addEventListener('click', () => {
        sendRequest('CustomerHandler', JSON.stringify({
            action: 'LOGOUT'
        }), (res) => {
            updateUser({name: '', balance: 0});
            menu.classList.remove('authorized');
        });
    });

    createTourElement();

    let email = getCookie('email');
    let pass = getCookie('pass');
    if (email && pass) {
        sendRequest('CustomerHandler', JSON.stringify({
            action: 'LOGIN',
            email: email,
            password: pass
        }), loginUser);
    } else {
        updateUser({});
    }

    sendRequest('CustomerHandler', JSON.stringify({
        action: 'GET_ALL_TOURS'
    }), displayTours);
}

function updateUser(newUser) {
    user = newUser;

    if (!user.name) {
        user.name = '';
    }
    if (!user.balance) {
        user.balance = 0;
    }
    if (!user.discount) {
        user.discount = 0;
    }

    userDisplayName.innerHTML = user.name;
    userBalance.innerHTML = user.balance;
}

function sendRequest(url, data, resolve, reject) {
    let xhr = new XMLHttpRequest();

    if (!xhr) {
        console.log('Unable to create XMLHTTP instance.');
        return null;
    }

    xhr.open('POST', url, true);
    xhr.responseType = 'json';
    xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
    xhr.send(data);

    loader.classList.remove('hidden');

    if (!xhr) {
        loader.classList.add('hidden');
        return false;
    }

    xhr.onload = function() {
        loader.classList.add('hidden');

        if (xhr.status === 200) {
            let res = xhr.response;
            if (res.success && resolve) {
                resolve(res);
            } else if (reject) {
                reject(res);
            }
        } else {
            errorText.innerHTML = 'Error ' + xhr.status;
            errorMsg.classList.remove('hidden');
        }
    };
}

function validateSignUpForm() {
    let isValid = true; 

    let usernameRE = /^[A-ZА-Я][A-ZА-Я0-9]{2,15}$/i;
    let passwordRE = /^(?=.*[A-Z].*)(?=.*[0-9].*)(?=.*[a-z].*).{8,}$/;
    let emailRE = /^[A-Z0-9._%+-]+@[A-Z0-9-]+.+.[A-Z]{2,4}$/i;

    let username = signUpForm.signUpUsername.value;
    let email = signUpForm.signUpEmail.value;
    let password = signUpForm.signUpPassword.value;
    let confirmPassword = signUpForm.confirmPassword.value;

    if (usernameRE.test(username)) {
        signUpForm.signUpUsername.parentNode.classList.remove('invalid');
    } else {
        signUpForm.signUpUsername.parentNode.classList.add('invalid');
        isValid = false;
    }

    if (emailRE.test(email)) {
        signUpForm.signUpEmail.parentNode.classList.remove('invalid');
    } else {
        signUpForm.signUpEmail.parentNode.classList.add('invalid');
        isValid = false;
    }

    if (passwordRE.test(password)) {
        signUpForm.signUpPassword.parentNode.classList.remove('invalid');
    } else {
        signUpForm.signUpPassword.parentNode.classList.add('invalid');
        isValid = false;
    }

    if (password === confirmPassword) {
        signUpForm.confirmPassword.parentNode.classList.remove('invalid');
    } else {
        signUpForm.confirmPassword.parentNode.classList.add('invalid');
        isValid = false;
    }

    return isValid;
}

signUpForm.onsubmit = function() {
    if (!validateSignUpForm()) {
        return false;
    }

    signUpForm.classList.add('hidden');

    sendRequest('CustomerHandler', JSON.stringify({
        action: 'REGISTER',
        username: signUpForm.signUpUsername.value,
        password: signUpForm.signUpPassword.value,
        email: signUpForm.signUpEmail.value,
        isTourAgent: signUpForm.isTravelAgent.checked
    }), loginUser, showResErrorMsg);

    return false;
};

signInForm.onsubmit = function() {
    signInForm.classList.add('hidden');

    sendRequest('CustomerHandler', JSON.stringify({
        action: 'LOGIN',
        email: signInForm.userEmail.value,
        password: signInForm.password.value
    }), loginUser, showResErrorMsg);

    return false;
};

searchTourForm.onsubmit = function() {
    sendRequest('CustomerHandler', JSON.stringify({
        action: 'SEARCH_TOUR',
        keyword: searchTourForm.keyword.value
    }), displayTours, showResErrorMsg);

    return false;
};

addMoneyForm.onsubmit = function() {
    if (addMoneyForm.amountToReceived.value < 0) {
        addMoneyForm.amountToReceived.parentNode.classList.add('invalid');
        return false;
    } else {
        addMoneyForm.amountToReceived.parentNode.classList.remove('invalid');
    }

    sendRequest('CustomerHandler', JSON.stringify({
        action: 'ADD_MONEY',
        amount: addMoneyForm.amountToReceived.value
    }), (res) => {
        addMoneyForm.classList.add('hidden');
        updateUser(res.user);
    }, showResErrorMsg);

    return false;
};

function loginUser(res) {
    if (res.url) {
        document.location = res.url;
    }
    updateUser(res.user);
    menu.classList.add('authorized');
}

function openTourDesc() {
    this.parentNode.classList.toggle('open');
}

function createTour(tour) {
    let dispTour = tourEl.cloneNode(true);

    dispTour.getElementsByClassName('tourName')[0].innerHTML = tour.name ? tour.name : '';
    dispTour.getElementsByClassName('tourCity')[0].innerHTML = tour.city ? tour.city : '';
    dispTour.getElementsByClassName('tourType')[0].innerHTML = tour.type ? tour.type : '';
    dispTour.getElementsByClassName('tourPrice')[0].innerHTML = tour.price ? tour.price : '';
    dispTour.getElementsByClassName('сheckInDate')[0].innerHTML = tour.date ? tour.date : '';
    dispTour.getElementsByClassName('tourDescription')[0].innerHTML = tour.description ? tour.description : '';
    dispTour.getElementsByClassName('userDiscount')[0].innerHTML = user && user.discount ? user.discount + '%' : '';

    if (tour.isLastMinute) {
        dispTour.getElementsByClassName('tourPrice')[0].classList.add('hot');
    }
    
    dispTour.firstChild.addEventListener('click', openTourDesc);
    dispTour.getElementsByClassName('bookTourBtn')[0].addEventListener('click', () => {
        sendRequest('CustomerHandler', JSON.stringify({
            action: 'BOOK_TOUR',
            id: tour.id
        }), (res) => {
            updateUser(res.user);
        }, showResErrorMsg);
    });

    return dispTour;
}

function displayTours(res) {
    foundTours.innerHTML = '';
    for (let i = 0; i < res.tours.length; i++) {
        let tour = createTour(res.tours[i]);
        tour.style.animationDelay = i / 10 + 's';
        foundTours.appendChild(tour);
    }
}

function getCookie(name) {
    let matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}

function showResErrorMsg(res) {
    errorText.innerHTML = res.msg;
    errorMsg.classList.remove('hidden');
}

init();