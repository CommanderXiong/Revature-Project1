package com.revature.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.dto.MessageDTO;
import com.revature.exceptions.BadParameterException;
import com.revature.exceptions.DatabaseException;
import com.revature.exceptions.NotAuthorizedException;
import com.revature.exceptions.NotFoundException;

import io.javalin.Javalin;
import io.javalin.http.ExceptionHandler;

public class ExceptionController implements Controller {
	private Logger logger = LoggerFactory.getLogger(ExceptionController.class);

	// Handlers
	private ExceptionHandler<BadParameterException> badParameterExceptionHandler= (e, ctx) -> {
		logger.warn("User provided a bad parameter.\n" + e.getMessage());
		ctx.status(400);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<NotFoundException> notFoundExceptionHandler = (e, ctx) -> {
		logger.warn("Could not find a record in the database.\n" + e.getMessage());
		ctx.status(404);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<DatabaseException> dataBaseExceptionHandler = (e, ctx) -> {
		logger.error("Error while trying to connect to database.\n" + e.getMessage());
		ctx.status(503);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<NotAuthorizedException> notAuthorizedExceptionHandler = (e, ctx) -> {
		logger.warn("User not authorized to access.\n" +  e.getMessage());
		ctx.status(401);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	@Override
	public void mapEndpoints(Javalin app) {
		app.exception(BadParameterException.class, badParameterExceptionHandler);
		app.exception(NotFoundException.class, notFoundExceptionHandler);
		app.exception(DatabaseException.class, dataBaseExceptionHandler);
		app.exception(NotAuthorizedException.class, notAuthorizedExceptionHandler);
	}
}
