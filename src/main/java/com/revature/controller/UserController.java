package com.revature.controller;

import com.revature.dto.InputUser;
import com.revature.dto.LoginDTO;
import com.revature.dto.MessageDTO;
import com.revature.exceptions.NotAuthorizedException;
import com.revature.model.User;
import com.revature.service.UserService;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class UserController implements Controller {
	private UserService userService = new UserService();

	public UserController() {
		super();
	}

	// Handlers
	private Handler loginHandler = (ctx) -> {
		LoginDTO loginDTO = ctx.bodyAsClass(LoginDTO.class);
		User user = userService.login(loginDTO);
		ctx.sessionAttribute("currentlyLoggedInUser", user);
		ctx.json(user);
		ctx.status(200);
	};

	private Handler currentUserHandler = (ctx) -> {
		User user = (User) ctx.sessionAttribute("currentlyLoggedInUser");
		if (user == null) {
			throw new NotAuthorizedException("User not logged in.");
		} else {
			ctx.json(user);
		}
		ctx.status(200);
	};

	private Handler logoutHandler = (ctx) -> {
		MessageDTO message = new MessageDTO();
		message.setMessage("You are now logged out.");
		ctx.json(message);
		ctx.status(204);
		ctx.req.getSession().invalidate();
	};

	private Handler getById = ctx -> {
		String stringUserId = ctx.pathParam("id");
		User result = userService.getById(stringUserId);
		ctx.json(result);
		ctx.status(200);
	};

	private Handler add = ctx -> {
		InputUser user = (InputUser)ctx.bodyAsClass(InputUser.class);
		User result = userService.add(user);
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
		} else {

			String id = ctx.pathParam("id");
			InputUser inputUser = ctx.bodyAsClass(InputUser.class);
			User result = userService.update(id, inputUser, user);
			ctx.json(result);
			ctx.status(200);
		}
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
		userService.delete(id);
		ctx.status(204);
	};

	@Override
	public void mapEndpoints(Javalin app) {
		app.post("/login", loginHandler);
		app.get("/current_user", currentUserHandler);
		app.post("/logout", logoutHandler);
		app.get("/users/:id", getById);
		app.post("/users", add);
		app.put("/users/:id", update);
		app.delete("/users/:id", delete);
	}

}
