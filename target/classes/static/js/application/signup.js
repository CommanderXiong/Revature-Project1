"use strict";
document.querySelector('#addNewUser').addEventListener('click', addUser);

function addUser() {
	let fName = document.querySelector('#firstname').value;
	let lName = document.querySelector('#lastname').value;
	let email = document.querySelector('#email').value;
	let username = document.querySelector('#username').value;
	let password = document.querySelector('#password').value;
	let password2 = document.querySelector('#passwordCheck').value;
	let userRole = 1;
	if(password != password2){
		displayInvalidInputs();
	}
	
	let inputUser = {
		firstName: fName,
		lastName: lName,
		username: username,
		password: password,
		email: email,
		userRoleId: userRole
	}
	
	let request = new XMLHttpRequest();
	request.addEventListener('readystatechange', receive);
	request.open('POST', 'http://localhost:7001/users');
	request.withCredentials = true;
	request.send(JSON.stringify(inputUser));
	
	function receive() {
		if (request.readyState === 4) {
			if (request.status === 200) {
				let data = JSON.parse(request.responseText);
				
				// data manipulation here
				
			} else if (request.status === 401) {
				window.location.replace("index.html");
				alert("Please log in before accessing this page.")
			}
		}
	}
}

function displayInvalidInputs() {
	let bodyElement = document.querySelector('body');
	let check = document.querySelector('#signUpWarning');

	if (check == null) {
		let pElement = document.createElement('p');
		pElement.style.color = 'red';
		pElement.innerHTML = 'Passwords do not match!';
		pElement.className = "signUpWarning";
		bodyElement.appendChild(pElement);
	}
}