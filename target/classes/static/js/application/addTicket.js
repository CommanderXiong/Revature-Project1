"use strict";
document.querySelector('#submitTicket').addEventListener('click', addTicket);


function addTicket() {
	let amount = document.querySelector('#ticketAmount').value;
	let type = document.querySelector('#ticketType').value;
	let description = document.querySelector('#ticketDescription').value;

	let ticketDto = {
		amount: amount,
		authorId: null,
		resolverId: null,
		statusId: null,
		typeId: type,
		description: description
	}
	fetch('http://localhost:7001/tickets', {
		method: 'POST',
		credentials: 'include',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(ticketDto)
	}).then((response) => {
		if (response.status === 201) {
			let bodyElement = document.querySelector('body');

			let pElement = document.createElement('p');
			pElement.innerHTML = 'Ticket Added!';

			bodyElement.appendChild(pElement);
		}
	})
}