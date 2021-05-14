package com.revature.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.NoResultException;
import javax.xml.bind.DatatypeConverter;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.dto.InputUser;
import com.revature.dto.LoginDTO;
import com.revature.exceptions.DatabaseException;
import com.revature.exceptions.NotAuthorizedException;
import com.revature.exceptions.NotFoundException;
import com.revature.model.User;
import com.revature.model.UserRole;
import com.revature.util.SessionUtility;

public class UserDAO {
	private static Logger logger = LoggerFactory.getLogger(UserDAO.class);
	private static SessionFactory sessionFactory = SessionUtility.getSessionFactory();
			
	public User getUserByCredentials(LoginDTO loginDTO) throws DatabaseException, NotFoundException, NoSuchAlgorithmException {
		logger.info("UserDAO.getUserByCredentials() executed");

		Session session = sessionFactory.openSession();
		try {
			
			Query<User> query = session.createQuery(
					"FROM User u where u.username=:inputUsername AND u.password=:inputPassword", User.class);
			query.setParameter("inputUsername", loginDTO.getUsername());
			query.setParameter("inputPassword", hashPassword(loginDTO.getPassword()));
			User result = query.getSingleResult();
			return result;
		} catch (HibernateException e) {
			throw new DatabaseException("Something happened while trying to connect to the database." + e.getMessage());
		} catch (NoResultException e) {
			throw new NotFoundException("Username and/or password not found.");
		}
	}

	public User getUserById(int inputId) throws DatabaseException {
		logger.info("UserDAO.getById() executed");

		Session session = sessionFactory.openSession();
		try {
			Query<User> query = session.createQuery("FROM User u where u.id = :id", User.class);
			query.setParameter("id", inputId);
			User result = query.uniqueResult();
			session.close();
			return result;
		} catch (HibernateException e) {
			session.clear();
			throw new DatabaseException("Something happened while trying to connect to the database." + e.getMessage());
		}
	}

	private UserRole getUserRole(int inputId) throws DatabaseException, NotFoundException {
		
		Session session = sessionFactory.openSession();
		try {
			Query<UserRole> query = session.createQuery("FROM UserRole where id = :id", UserRole.class);
			query.setParameter("id", inputId);
			UserRole result = query.getSingleResult();
			session.close();
			return result;
		} catch (HibernateException e) {
			session.clear();
			throw new DatabaseException("Something happened while trying to connect to the database." + e.getMessage());
		} catch (NoResultException e) {
			session.clear();
			throw new NotFoundException("User role id: " + " not found.");
		}
	}
	private String hashPassword(String password) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
	    byte[] digest = md.digest();
	    String passwordHashed = DatatypeConverter.printHexBinary(digest).toUpperCase();
		return passwordHashed;
	}
	
	public void addUser(User user) throws NoSuchAlgorithmException {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		user.setPassword(this.hashPassword(user.getPassword()));
		session.persist(user);
		session.getTransaction().commit();
		session.close();
	}

	public User addUser(InputUser inputUser) throws DatabaseException, NotFoundException {
		logger.info("TicketRepository.addUser() executed");
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			UserRole userRole = this.getUserRole(1);

			User result = new User();
			result.setFirstName(inputUser.getFirstName());
			result.setLastName(inputUser.getLastName());
			result.setUsername(inputUser.getUsername());
			
			result.setPassword(hashPassword(inputUser.getPassword()));
			
			result.setEmail(inputUser.getEmail());
			result.setUserRole(userRole);

			session.persist(result);
			session.getTransaction().commit();
			session.close();
			return result;
		} catch (HibernateException e) {
			session.clear();
			throw new DatabaseException("Something happened while trying to connect to the database." + e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			session.clear();
			throw new DatabaseException("Something happened while trying to connect to the database." + e.getMessage());
		}
	}

	public User updateUser(int userId, InputUser inputUser, User user) throws DatabaseException, NotFoundException, NotAuthorizedException {
		logger.info("TicketRepository.updateUser() executed");
		
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			UserRole userRole = this.getUserRole(inputUser.getUserRoleId());
			if(userRole.getId() == 2 && user.getUserRole().getId() != 2) {
				throw new NotAuthorizedException("User not authorized to promote another user.");
			}

			User result = this.getUserById(userId);
			result.setFirstName(inputUser.getFirstName());
			result.setLastName(inputUser.getLastName());
			result.setUsername(inputUser.getUsername());
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(inputUser.getPassword().getBytes());
		    byte[] digest = md.digest();
		    String passwordHashed = DatatypeConverter.printHexBinary(digest).toUpperCase();
			result.setPassword(passwordHashed);
			result.setEmail(inputUser.getEmail());
			result.setUserRole(userRole);

			session.update(result);
			session.getTransaction().commit();
			session.close();
			session.close();
			return result;
		} catch (HibernateException e) {
			session.clear();
			throw new DatabaseException("Something happened while trying to connect to the database." + e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			session.clear();
			throw new DatabaseException("Something happened while trying to connect to the database." + e.getMessage());
		}
	}

	public void deleteUser(int userId) throws DatabaseException, NotFoundException {
		logger.info("TicketRepository.deleteUser() executed");
		
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			User result = this.getUserById(userId);
			session.delete(result);
			session.getTransaction().commit();
			session.close();
		} catch (HibernateException e) {
			session.clear();
			throw new DatabaseException("Something happened while trying to connect to the database." + e.getMessage());
		}
	}
}
