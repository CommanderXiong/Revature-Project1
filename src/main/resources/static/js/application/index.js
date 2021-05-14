"use strict";
document.querySelector('#login').addEventListener('click', login);

function login() {
	let un = document.querySelector('#username').value;
	let pw = document.querySelector('#password').value;

	let data = {
		username: un,
		password: pw
	};

	let request = new XMLHttpRequest();
	request.addEventListener('readystatechange', receive);
	request.open('POST', 'http://localhost:7001/login');
	request.withCredentials = true;
	request.send(JSON.stringify(data));

	function receive() {
		if (request.readyState === 4) {
			if (request.status === 200) {
				window.location.href = '/landing.html';
			} else if (request.status === 400){
				displayInvalidLogin();
			}
		}
	}
}

function displayInvalidLogin() {
	let bodyElement = document.querySelector('body');
	let check = document.querySelector('#loginWarning');

	if (check == null) {
		let pElement = document.createElement('p');
		pElement.style.color = 'red';
		pElement.innerHTML = 'Invalid login!';
		pElement.className = "loginWarning";
		bodyElement.appendChild(pElement);
	}
}