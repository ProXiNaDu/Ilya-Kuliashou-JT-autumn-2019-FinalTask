'use strict';

const loader = document.getElementById('loader');
const errorMsg = document.getElementById('errorMsg');
const errorText = document.getElementById('errorText');
const closeMsgBtn = document.getElementById('closeMsgBtn');
const editTourForm = document.getElementById('editTourForm');
const closeEditTourFormBtn = document.getElementById('closeEditTourFormBtn');
const addTourBtn = document.getElementById('addTourBtn');
const logOutBtn = document.getElementById('logOutBtn');
const tourTypes = document.getElementById('tourTypes');
const closeAddDiscountFormBtn = document.getElementById('closeAddDiscountFormBtn');
const addDiscountForm = document.getElementById('addDiscountForm');
const searchForm = document.getElementById('searchForm');
const foundItems = document.getElementById('foundItems');


let currentUserDiscountEl;
let currentTourEl;
let tourEl;
let userEl;

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
    temp.lastChild.classList.add('editTourBtn');
    temp.lastChild.innerHTML = 'Edit Tour';
    temp.appendChild(document.createElement('button'));
    temp.lastChild.classList.add('deleteTourBtn');
    temp.lastChild.innerHTML = 'Delete Tour';
}

function createUserElement() {
    userEl = document.createElement('li');
    userEl.classList.add('user');
    userEl.appendChild(document.createElement('span'));
    userEl.lastChild.classList.add('userName');
    userEl.appendChild(document.createElement('span'));
    userEl.lastChild.classList.add('userEmail');
    userEl.appendChild(document.createElement('span'));
    userEl.lastChild.classList.add('userMoney');
    userEl.appendChild(document.createElement('span'));
    userEl.lastChild.classList.add('userDiscount');
    userEl.appendChild(document.createElement('button'));
    userEl.lastChild.classList.add('addDiscountBtn');
    userEl.lastChild.innerHTML = '+';
}

function init() {
    addTourBtn.addEventListener('click', () => {
        sendRequest('../AgentHandler', JSON.stringify({
            action: 'GET_TOUR_TYPES'
        }), (res) => {
            tourTypes.innerHTML = '';
            for (let type of res.types) {
                let typeOption = document.createElement('option');
                typeOption.value = type;
                tourTypes.appendChild(typeOption);
            }
        }, showResErrorMsg);
        
        editTourForm.tourID.value = -1;
        editTourForm.classList.remove('hidden');
    });
    closeMsgBtn.addEventListener('click', () => {
        errorMsg.classList.add('hidden');
    });
    closeEditTourFormBtn.addEventListener('click', () => {
        editTourForm.classList.add('hidden');
        currentTourEl = null;
    });
    closeAddDiscountFormBtn.addEventListener('click', () => {
        addDiscountForm.classList.add('hidden');
        currentUserDiscountEl = null;
    });
    
    logOutBtn.addEventListener('click', () => {
        sendRequest('../AgentHandler', JSON.stringify({
            action: 'LOGOUT'
        }), (res) => {
            document.location = res.url;
        });
    });

    createTourElement();
    createUserElement();
}

searchForm.onsubmit = function() {
    let action = searchForm.isUser.checked ? 'SEARCH_USER' : 'SEARCH_TOUR';

    sendRequest('../AgentHandler', JSON.stringify({
        action: action,
        keyword: searchForm.keyword.value
    }), (res) => {
        foundItems.innerHTML = '';
        if (searchForm.isUser.checked) {
            displayUsers(res);
        } else {
            displayTours(res);
        }
    }, showResErrorMsg);

    return false;
};

editTourForm.onsubmit = function() {
    if (editTourForm.tourPrice.value < 0) {
        editTourForm.tourPrice.parentNode.classList.add('invalid');
        return false;
    } else {
        editTourForm.tourPrice.parentNode.classList.remove('invalid');
    }

    let action = editTourForm.tourID.value === '-1' ?
        'ADD_TOUR' : 'EDIT_TOUR';

    let req = {
        action: action,
        id: editTourForm.tourID.value,
        name: editTourForm.tourName.value,
        city: editTourForm.tourCity.value,
        type: editTourForm.tourType.value,
        price: editTourForm.tourPrice.value,
        date: editTourForm.tourDate.value,
        description: editTourForm.tourDesc.value,
        isLastMinute: editTourForm.isLastMinute.checked
    };
    sendRequest('../AgentHandler', JSON.stringify(req), (res) => {
        editTourForm.classList.add('hidden');
        if (currentTourEl) {
            currentTourEl.getElementsByClassName('tourName')[0].innerHTML = req.name ? req.name : '';
            currentTourEl.getElementsByClassName('tourCity')[0].innerHTML = req.city ? req.city : '';
            currentTourEl.getElementsByClassName('tourType')[0].innerHTML = req.type ? req.type : '';
            currentTourEl.getElementsByClassName('tourPrice')[0].innerHTML = req.price ? req.price : '';
            currentTourEl.getElementsByClassName('сheckInDate')[0].innerHTML = req.date ? req.date : '';
            currentTourEl.getElementsByClassName('tourDescription')[0].innerHTML = req.description ? req.description : '';
            
            if (req.isLastMinute) {
                currentTourEl.getElementsByClassName('tourPrice')[0].classList.add('hot');
            } else {
                currentTourEl.getElementsByClassName('tourPrice')[0].classList.remove('hot');
            }
        }
    }, showResErrorMsg);

    return false;
};

addDiscountForm.onsubmit = function() {
    if (addDiscountForm.newDiscount.value < 0 || addDiscountForm.newDiscount.value > 100) {
        addDiscountForm.newDiscount.parentNode.classList.add('invalid');
        return false;
    } else {
        addDiscountForm.newDiscount.parentNode.classList.remove('invalid');
    }

    sendRequest('../AgentHandler', JSON.stringify({
        action: 'CHANGE_DISCOUNT',
        id: addDiscountForm.userID.value,
        discount: addDiscountForm.newDiscount.value
    }), () => {
        addDiscountForm.classList.add('hidden');
        currentUserDiscountEl.innerHTML = addDiscountForm.newDiscount.value + '%';
    }, showResErrorMsg);

    return false;
};

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

function createTour(tour) {
    let dispTour = tourEl.cloneNode(true);

    dispTour.getElementsByClassName('tourName')[0].innerHTML = tour.name ? tour.name : '';
    dispTour.getElementsByClassName('tourCity')[0].innerHTML = tour.city ? tour.city : '';
    dispTour.getElementsByClassName('tourType')[0].innerHTML = tour.type ? tour.type : '';
    dispTour.getElementsByClassName('tourPrice')[0].innerHTML = tour.price ? tour.price : '';
    dispTour.getElementsByClassName('сheckInDate')[0].innerHTML = tour.date ? tour.date : '';
    dispTour.getElementsByClassName('tourDescription')[0].innerHTML = tour.description ? tour.description : '';
    
    if (tour.isLastMinute) {
        dispTour.getElementsByClassName('tourPrice')[0].classList.add('hot');
    }
    
    dispTour.firstChild.addEventListener('click', openTourDesc);
    dispTour.getElementsByClassName('editTourBtn')[0].addEventListener('click', () => {
        sendRequest('../AgentHandler', JSON.stringify({
            action: 'GET_TOUR_TYPES'
        }), (res) => {
            tourTypes.innerHTML = '';
            for (let type of res.types) {
                let typeOption = document.createElement('option');
                typeOption.value = type;
                tourTypes.appendChild(typeOption);
            }
        }, showResErrorMsg);
        
        currentTourEl = dispTour;
        editTourForm.tourID.value = tour.id;
        editTourForm.tourName.value = tour.name ? tour.name : '';
        editTourForm.tourCity.value = tour.city ? tour.city : '';
        editTourForm.tourType.value = tour.type ? tour.type : '';
        editTourForm.tourPrice.value = tour.price ? tour.price : '';
        editTourForm.tourDate.value = tour.date ? tour.date : '';
        editTourForm.tourDesc.value = tour.description ? tour.description : '';
        editTourForm.isLastMinute.checked = tour.isLastMinute;
        editTourForm.classList.remove('hidden');
    });

    dispTour.getElementsByClassName('deleteTourBtn')[0].addEventListener('click', () => {
        sendRequest('../AgentHandler', JSON.stringify({
            action: 'DELETE_TOUR',
            id: tour.id
        }), () => {
            dispTour.remove();
        }, showResErrorMsg);
    });

    return dispTour;
}

function createUser(user) {
    let dispUser = userEl.cloneNode(true);

    dispUser.getElementsByClassName('userName')[0].innerHTML = user.name ? user.name : '';
    dispUser.getElementsByClassName('userEmail')[0].innerHTML = user.email ? user.email : '';
    dispUser.getElementsByClassName('userMoney')[0].innerHTML = (user.money ? user.money : '0') + '$';
    dispUser.getElementsByClassName('userDiscount')[0].innerHTML = (user.discount ? user.discount : '0') + '%';
    
    dispUser.getElementsByClassName('addDiscountBtn')[0].addEventListener('click', () => {
        currentUserDiscountEl = dispUser.getElementsByClassName('userDiscount')[0];
        addDiscountForm.userID.value = user.id;
        addDiscountForm.classList.remove('hidden');
    });

    return dispUser;
}

function displayTours(res) {
    foundItems.innerHTML = '';
    for (let i = 0; i < res.tours.length; i++) {
        let tour = createTour(res.tours[i]);
        tour.style.animationDelay = i / 10 + 's';
        foundItems.appendChild(tour);
    }
}

function displayUsers(res) {
    foundItems.innerHTML = '';
    for (let i = 0; i < res.users.length; i++) {
        let user = createUser(res.users[i]);
        user.style.animationDelay = i / 10 + 's';
        foundItems.appendChild(user);
    }
}

function openTourDesc() {
    this.parentNode.classList.toggle('open');
}

function showResErrorMsg(res) {
    errorText.innerHTML = res.msg;
    errorMsg.classList.remove('hidden');
}

init();