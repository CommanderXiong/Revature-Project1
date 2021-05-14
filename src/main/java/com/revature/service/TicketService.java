package com.revature.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.dao.TicketDAO;
import com.revature.dao.UserDAO;
import com.revature.dto.InputTicket;
import com.revature.dto.ShorthandTicket;
import com.revature.exceptions.BadParameterException;
import com.revature.exceptions.DatabaseException;
import com.revature.exceptions.NotAuthorizedException;
import com.revature.exceptions.NotFoundException;
import com.revature.model.Ticket;
import com.revature.model.User;

public class TicketService {
	private TicketDAO ticketDao;
	private UserDAO userDao;
	private static Logger logger = LoggerFactory.getLogger(TicketService.class);

	// Constructors, first for default, other for mock testing
	public TicketService() {
		ticketDao = new TicketDAO();
	}

	public TicketService(TicketDAO ticketDao) {
		this.ticketDao = ticketDao;
	}

	// Behaviors
	public List<ShorthandTicket> getAll(User user) throws DatabaseException {
		logger.info("TicketService.getAll() executed.");

		return ticketDao.getAll(user);
	}

	public List<ShorthandTicket> getAll(String statusId, User user)
			throws DatabaseException, BadParameterException, NotFoundException {
		logger.info("TicketService.getAll(StatusId) executed.");

		try {
			int status = Integer.parseInt(statusId);
			return ticketDao.getAllByStatus(status, user);
		} catch (NumberFormatException e) {
			throw new BadParameterException("Status id must be an integer. User provided " + statusId);
		}
	}

	public Ticket getById(String stringTicketId, User user)
			throws DatabaseException, NotFoundException, BadParameterException, NotAuthorizedException {
		logger.info("TicketService.getById() executed.");

		try {
			int ticketId = Integer.parseInt(stringTicketId.trim());
			if (ticketId < 0) {
				throw new BadParameterException(
						"Ticket id must be positive. " + "User entered Ticket Id: " + stringTicketId);
			}
			return ticketDao.getTicketById(ticketId, user);
		} catch (NumberFormatException e) {
			throw new BadParameterException("Ticket id must be an integer. User provided " + stringTicketId);
		} catch (NullPointerException e) {
			throw new BadParameterException("Ticket id cannot be null.");
		}
	}

	public Ticket add(InputTicket inputTicket) throws DatabaseException, NotFoundException, BadParameterException {
		logger.info("TicketService.add() executed.");

		try {
			if (inputTicket.getAmount() < 0) {
				throw new BadParameterException("Amount must be positive.");
			}
			if (inputTicket.getAuthorId() < 0) {
				throw new BadParameterException("Author Id must be positive.");
			}
			if (inputTicket.getTypeId() < 1 || inputTicket.getTypeId() > 4) {
				throw new BadParameterException("Type Id must be 1 through 4.");
			}
//			if(inputTicket.getImage() == null) { // FIXME Research how to get and pass an image from the front end
//				throw new BadParameterException("Must have a file or image of a receipt.");
//			}
			return ticketDao.addTicket(inputTicket);
		} catch (NullPointerException e) {
			throw new BadParameterException("Please make sure all required fields are filled in.");
		}
	}

	public Ticket update(String stringTicketId, InputTicket inputTicket, User user)
			throws NotFoundException, DatabaseException, BadParameterException, NotAuthorizedException {
		logger.info("TicketService.update() executed.");

		try {
			int ticketId = Integer.parseInt(stringTicketId.trim());
			if (inputTicket.getAmount() < 0) {
				throw new BadParameterException("Amount must be positive.");
			}
			if (inputTicket.getAuthorId() < 0) {
				throw new BadParameterException("Author Id must be valid.");
			}
			if (inputTicket.getTypeId() < 0 || inputTicket.getTypeId() > 4) {
				throw new BadParameterException("Type Id must be 1 through 4.");
			}
			if (inputTicket.getStatusId() < 1 || inputTicket.getStatusId() > 3) {
				throw new BadParameterException("Status Id must be 1(Pending), 2(Rejected), or 3(Accepted).");
			}
//			if(inputTicket.getImage() == null) { //FIXME again... research image saving
//				throw new BadParameterException("Must have a file or image of a receipt.");
//			}
			return ticketDao.updateTicket(ticketId, inputTicket, user);
		} catch (NumberFormatException e) {
			throw new BadParameterException("Ticket id must be an integer. User provided: " + stringTicketId);
		} catch (NullPointerException e) {
			throw new BadParameterException("Please make sure all the required fields are filled in.");
		}
	}

	public void delete(String stringTicketId, User user)
			throws NotFoundException, DatabaseException, BadParameterException, NotAuthorizedException {
		logger.info("TicketService.delete() executed.");

		try {
			int ticketId = Integer.parseInt(stringTicketId.trim());
			if (ticketId < 0) {
				throw new BadParameterException(
						"Ticket id must be valid. " + "User entered Ticket Id: " + stringTicketId);
			}
			ticketDao.deleteTicket(ticketId, user);
		} catch (NumberFormatException e) {
			throw new BadParameterException("Ticket id must be an integer. User provided " + stringTicketId);
		}
	}
}
