"use strict";

const table = document.querySelector('#ticketTable');

window.onload = function() {
	renderCurrentUser();
	renderTicketTable();
}

document.querySelector('#statusSorting').addEventListener('change', renderTicketTable);
document.querySelector('#logout').addEventListener('click', logout)

function renderCurrentUser() {

	let request = new XMLHttpRequest();
	request.addEventListener('readystatechange', receive);
	request.open('GET', 'http://localhost:7001/current_user');
	request.withCredentials = true;
	request.send();

	function receive() {
		if (request.readyState === 4) {
			if (request.status === 200) {
				let data = JSON.parse(request.responseText);
				let firstName = data.firstName;
				let lastName = data.lastName;
				let userInfoElement = document.querySelector('#user');
				userInfoElement.innerHTML = `${firstName} ${lastName}`;
			} else if (request.status === 401) {
				window.location.replace("index.html");
				alert("Please log in before accessing this page.")
			}
		}
	}
}

function renderTicketTable() {
	let status = document.getElementById("statusSorting");
	let optionValue = status.value;
	let url = new URL('http://localhost:7001/tickets');
	if (optionValue > 0) {
		let param = { "status": optionValue };
		url.search = new URLSearchParams(param);
	}

	let request = new XMLHttpRequest();
	request.addEventListener('readystatechange', receive);
	request.open('GET', url);
	request.withCredentials = true;
	request.send();

	function receive() {
		if (request.readyState === 4) {
			if (request.status === 200) {
				let oldTableBody = document.querySelector('#tableBody')
				let newTableBody = document.createElement('tbody');
				newTableBody.id = "tableBody";

				let dataList = JSON.parse(request.responseText);
				dataList.forEach(function(ticket) {
					let tableRow = document.createElement('tr');
					tableRow.className = 'ticketTableRow';

					let ticketId = document.createElement('td');
					let link = document.createElement('a');
					link.innerHTML = ticket.id;
					link.href = "/viewTicket.html";
					ticketId.append(link);

					let ticketAmt = document.createElement('td');
					ticketAmt.innerText = "$" + ticket.amount;

					let ticketAuthor = document.createElement('td');
					ticketAuthor.innerText = ticket.authorId;

					let ticketResolver = document.createElement('td');
					if (ticket.resolverId == 0) {
						ticketResolver.innerHTML = "N/A";
					} else {
						ticketResolver.innerText = ticket.resolverId;
					}
					let ticketStatus = document.createElement('td');
					ticketStatus.innerText = ticket.status;

					let ticketType = document.createElement('td');
					ticketType.innerText = ticket.type;

					let ticketIssued = document.createElement('td');
					let issuedDate = new Date(ticket.issued);
					ticketIssued.innerText = issuedDate.toLocaleDateString("en-US");

					let ticketResolved = document.createElement('td');
					if (ticket.resolved == null) {
						ticketResolved.innerText = "N/A";
					} else {
						let resolvedDate = new Date(ticket.resolved);
						ticketResolved.innerText = resolvedDate.toLocaleDateString("en-US");
					}

					tableRow.append(ticketId, ticketAmt, ticketAuthor, ticketResolver, ticketStatus, ticketType, ticketIssued, ticketResolved);
					newTableBody.append(tableRow);
				});
				oldTableBody.parentNode.replaceChild(newTableBody, oldTableBody);
			} else if (request.status === 401) {
				alert("Please log in before accessing this page.")
				window.location.replace("index.html");
			}
		}
	}
}

function logout() {
	let request = new XMLHttpRequest();
	request.addEventListener('readystatechange', releaseSession);
	request.open('POST', 'http://localhost:7001/logout');
	request.withCredentials = true;
	request.send();
	
	function releaseSession() {
		window.location.replace("/index.html")
	}
	
}