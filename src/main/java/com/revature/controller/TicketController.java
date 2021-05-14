package com.revature.controller;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dto.InputTicket;
import com.revature.dto.MessageDTO;
import com.revature.dto.ShorthandTicket;
import com.revature.exceptions.NotAuthorizedException;
import com.revature.model.Ticket;
import com.revature.model.User;
import com.revature.service.TicketService;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class TicketController implements Controller {

	private TicketService ticketService;

	public TicketController() {
		this.ticketService = new TicketService();
	}

	// Handlers
	private Handler getAll = ctx -> {
		User user = (User) ctx.sessionAttribute("currentlyLoggedInUser");
		if (user == null) {
			throw new NotAuthorizedException("User not logged in.");
		} else {
			String statusId = ctx.queryParam("status", String.class).getOrNull();
			List<ShorthandTicket> result;
			if (statusId != null) {
				result = ticketService.getAll(statusId, user);
			} else {
				result = ticketService.getAll(user);
			}
			ctx.json(result);
			ctx.status(200);
		}
	};

	private Handler getById = ctx -> {
		User user = (User) ctx.sessionAttribute("currentlyLoggedInUser");
		if (user == null) {
			MessageDTO messageDTO = new MessageDTO();
			messageDTO.setMessage("User is not currently logged in!");
			ctx.json(messageDTO);
			ctx.status(400);
		}

		String id = ctx.pathParam("id");
		Ticket result = ticketService.getById(id, user);
		ctx.json(result);
		ctx.status(200);
	};

	private Handler add = ctx -> {
		InputTicket ticket = ctx.bodyAsClass(InputTicket.class);
		User user = (User) ctx.sessionAttribute("currentlyLoggedInUser");
		ticket.setAuthorId(user.getId());
		Ticket result = ticketService.add(ticket);
		ctx.json(result);
		ctx.status(201);
	};

	private Handler update = ctx -> {
		User user = (User) ctx.sessionAttribute("currentlyLoggedInUser");
		if (user == null) {
			MessageDTO messageDTO = new MessageDTO();
			messageDTO.setMessage("User is not currently logged in!");
			ctx.json(messageDTO);
			ctx.status(400);
		}

		String id = ctx.pathParam("id");
		InputTicket ticket = ctx.bodyAsClass(InputTicket.class);
		Ticket result = ticketService.update(id, ticket, user);
		ctx.json(result);
		ctx.status(200);
	};

	private Handler delete = ctx -> {
		User user = (User) ctx.sessionAttribute("currentlyLoggedInUser");
		if (user == null) {
			MessageDTO messageDTO = new MessageDTO();
			messageDTO.setMessage("User is not currently logged in!");
			ctx.json(messageDTO);
			ctx.status(400);
		}

		String id = ctx.pathParam("id");
		ticketService.delete(id, user);
		ctx.status(204);
	};

	@Override
	public void mapEndpoints(Javalin app) {
		app.get("/tickets", getAll);
		app.get("/tickets/:id", getById);
		app.post("/tickets", add);
		app.put("/tickets/:id", update);
		app.delete("/tickets/:id", delete);
	}

}
