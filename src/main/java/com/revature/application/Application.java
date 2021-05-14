package com.revature.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.controller.Controller;
import com.revature.controller.ExceptionController;
import com.revature.controller.TicketController;
import com.revature.controller.UserController;

import io.javalin.Javalin;

public class Application {
	private static Javalin app;
	private static Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {

		app = Javalin.create((config) -> {
			config.addStaticFiles("static");
			config.enableCorsForAllOrigins();
		});
		// Log every request to the database
		app.before(ctx -> {
			String URI = ctx.req.getRequestURI();
			String httpMethod = ctx.req.getMethod();
			logger.info(httpMethod + " request to endpoint " + URI + " received");
		});

		mapControllers(new TicketController(), new UserController(), new ExceptionController());

		app.start(7001);

	}

	public static void mapControllers(Controller... controllers) {
		for (Controller c : controllers) {
			c.mapEndpoints(app);
		}
	}
}
