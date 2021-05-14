package com.revature.service;

import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.dao.UserDAO;
import com.revature.dto.InputUser;
import com.revature.dto.LoginDTO;
import com.revature.exceptions.BadParameterException;
import com.revature.exceptions.DatabaseException;
import com.revature.exceptions.NotAuthorizedException;
import com.revature.exceptions.NotFoundException;
import com.revature.model.User;

public class UserService {
	private UserDAO userDao;
	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	// Constructors, first for default, other for mock testing
	public UserService() {
		userDao = new UserDAO();
	}

	public UserService(UserDAO userDao) {
		this.userDao = userDao;
	}

	// Behaviors
	public User login(LoginDTO loginDTO) throws BadParameterException, NotFoundException, DatabaseException, NoSuchAlgorithmException {
		logger.info("UserService.login() executed.");
		
		if (!loginDTO.getPassword().trim().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
			throw new BadParameterException(
					"Password must be at least 8 characters with at least one letter and one number.");
		}
		User user = userDao.getUserByCredentials(loginDTO);
		return user;
	}

	public User getById(String stringUserId) throws DatabaseException, NotFoundException, BadParameterException {
		logger.info("UserService.getById() executed.");
		
		try {
			int userId = Integer.parseInt(stringUserId.trim());
			if (userId < 0) {
				throw new BadParameterException("User id must be positive. " + "User entered User Id: " + stringUserId);
			}
			return userDao.getUserById(userId);
		} catch (NumberFormatException e) {
			throw new BadParameterException("User id must be an integer. User provided " + stringUserId);
		} catch (NullPointerException e) {
			throw new BadParameterException("User id cannot be null.");
		}
	}

	public User add(InputUser inputUser) throws DatabaseException, NotFoundException, BadParameterException {
		logger.info("UserService.add() executed.");
		// Check if firstName and lastName are only letters
		try {
			if (!inputUser.getFirstName().trim().matches("^[a-zA-Z']+$")
					&& !inputUser.getLastName().trim().matches("^[a-zA-Z']+$")) {
				throw new BadParameterException("User name must contain only letters. User entered: "
						+ inputUser.getFirstName() + " " + inputUser.getLastName());
			}
			if (inputUser.getEmail().indexOf('@') == -1) {
				throw new BadParameterException("A valid email must be entered.");
			}
			if (!inputUser.getPassword().trim().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
				throw new BadParameterException(
						"Password must be at least 8 characters with at least one letter and one number.");
			}
			if (inputUser.getUserRoleId() < 1 || inputUser.getUserRoleId() > 2) {
				throw new BadParameterException("New user role id must be a 1(Employee) or 2(Finance manager).");
			}
			if (inputUser.getUsername() == null) {
				throw new BadParameterException("Please make sure all required fields are filled in.");
			}
			return userDao.addUser(inputUser);
		} catch (NullPointerException e) {
			throw new BadParameterException("Please make sure all required fields are filled in.");
		}
	}

	public User update(String stringUserId, InputUser inputUser, User user)
			throws NotFoundException, DatabaseException, BadParameterException, NotAuthorizedException {
		logger.info("UserService.update() executed.");
		
		try {
			int userId = Integer.parseInt(stringUserId.trim());
			if (!inputUser.getFirstName().trim().matches("^[a-zA-Z']+$")
					&& !inputUser.getLastName().trim().matches("^[a-zA-Z']+$")) {
				throw new BadParameterException("User name must contain only letters. User entered: "
						+ inputUser.getFirstName() + " " + inputUser.getLastName());
			}
			if (inputUser.getEmail().indexOf('@') == -1) {
				throw new BadParameterException("A valid email must be entered.");
			}
			if (!inputUser.getPassword().trim().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
				throw new BadParameterException(
						"Password must be at least 8 characters with at least one letter and one number.");
			}
			if (inputUser.getUserRoleId() < 1 || inputUser.getUserRoleId() > 2) {
				throw new BadParameterException("New user role id must be a 1(Employee) or 2(Finance manager).");
			}
			if (inputUser.getUsername() == null) {
				throw new BadParameterException("Please make sure all required fields are filled in.");
			}
			return userDao.updateUser(userId, inputUser, user);
		} catch (NumberFormatException e) {
			throw new BadParameterException("User id must be an integer. User provided: " + stringUserId);
		} catch (NullPointerException e) {
			throw new BadParameterException("Please make sure all required fields are filled in.");
		}
	}

	public void delete(String stringUserId) throws NotFoundException, DatabaseException, BadParameterException {
		logger.info("UserService.delete() executed.");
		
		try {
			int userId = Integer.parseInt(stringUserId.trim());
			if (userId < 0) {
				throw new BadParameterException("User id must be positive. " + "User entered User Id: " + stringUserId);
			}
		} catch (NumberFormatException e) {
			throw new BadParameterException("User id must be an integer. User provided " + stringUserId);
		}
	}
}
