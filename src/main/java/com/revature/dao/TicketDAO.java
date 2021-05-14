package com.revature.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.dto.InputTicket;
import com.revature.dto.ShorthandTicket;
import com.revature.exceptions.DatabaseException;
import com.revature.exceptions.NotAuthorizedException;
import com.revature.exceptions.NotFoundException;
import com.revature.model.Status;
import com.revature.model.Ticket;
import com.revature.model.Type;
import com.revature.model.User;
import com.revature.util.SessionUtility;

public class TicketDAO {
	private static Logger logger = LoggerFactory.getLogger(TicketDAO.class);
	private SessionFactory sessionFactory = SessionUtility.getSessionFactory();
	private UserDAO userDao = new UserDAO();

	// Behaviors
	public List<ShorthandTicket> getAll(User user) throws DatabaseException {
		logger.info("TicketDAO.getAll() executed");

		Session session = sessionFactory.openSession();
		try {
			List<Ticket> resultset = new ArrayList<>();

			if (user.getUserRole().getId() == 1) {
				Query<Ticket> query = session.createQuery("FROM Ticket t where t.author = :user", Ticket.class);
				query.setParameter("user", user);
				resultset = query.getResultList();
			} else {
				resultset = session.createQuery("FROM Ticket t", Ticket.class).getResultList();
			}

			ArrayList<ShorthandTicket> result = new ArrayList<>();
			for (Ticket t : resultset) {
				result.add(new ShorthandTicket(t));
			}
			session.close();
			return result;
		} catch (HibernateException e) {
			throw new DatabaseException("Something happened while trying to connect to the database." + e.getMessage());
		}
	}

	public List<ShorthandTicket> getAllByStatus(int inputStatusId, User user)
			throws DatabaseException, NotFoundException {
		logger.info("TicketDAO.getAllByStatus() executed");

		Session session = sessionFactory.openSession();
		try {
			Status status = this.getStatus(inputStatusId);
			List<Ticket> resultset = new ArrayList<>();

			if (user.getUserRole().getId() == 1) {
				Query<Ticket> query = session.createQuery("FROM Ticket t where t.author = :user AND t.status = :status",
						Ticket.class);
				query.setParameter("user", user);
				query.setParameter("status", status);
				resultset = query.getResultList();
			} else {
				Query<Ticket> query = session.createQuery("FROM Ticket t where t.status = :status", Ticket.class);
				query.setParameter("status", status);
				resultset = query.getResultList();
			}

			ArrayList<ShorthandTicket> result = new ArrayList<>();
			for (Ticket t : resultset) {
				result.add(new ShorthandTicket(t));
			}
			session.close();
			return result;
		} catch (HibernateException e) {
			throw new DatabaseException("Something happened while trying to connect to the database." + e.getMessage());
		}
	}

	public Ticket getTicketById(int inputId, User user)
			throws DatabaseException, NotFoundException, NotAuthorizedException {
		logger.info("TicketDAO.getById() executed");

		Session session = sessionFactory.openSession();
		try {
			Query<Ticket> query = session.createQuery("FROM Ticket where id = :id", Ticket.class);
			query.setParameter("id", inputId);
			Ticket result = query.getSingleResult();

			if (user.getUserRole().getId() == 1 && result.getAuthor().getId() != user.getId()) {
				throw new NotAuthorizedException("User is not authorized to veiw this ticket.");
			}

			session.close();
			return result;
		} catch (HibernateException e) {
			throw new DatabaseException("Something happened while trying to connect to the database." + e.getMessage());
		} catch (NoResultException e) {
			throw new NotFoundException("Ticket with id: " + inputId + " not found.");
		}
	}

	private Type getType(int inputId) throws DatabaseException, NotFoundException {
		Session session = sessionFactory.openSession();
		try {
			Query<Type> query = session.createQuery("FROM Type where id = :id", Type.class);
			query.setParameter("id", inputId);
			Type type = query.getSingleResult();
			session.close();
			return type;
		} catch (HibernateException e) {
			throw new DatabaseException("Something happened while trying to connect to the database." + e.getMessage());
		} catch (NoResultException e) {
			throw new NotFoundException("Type with id: " + inputId + " not found.");
		}
	}

	private Status getStatus(int inputId) throws DatabaseException, NotFoundException {
		Session session = sessionFactory.openSession();
		try {
			Query<Status> query = session.createQuery("FROM Status where id = :id", Status.class);
			query.setParameter("id", inputId);
			Status status = query.getSingleResult();
			session.close();
			return status;
		} catch (HibernateException e) {
			throw new DatabaseException("Something happened while trying to connect to the database." + e.getMessage());
		} catch (NoResultException e) {
			throw new NotFoundException("Status with id: " + inputId + " not found.");
		}
	}

	public Ticket addTicket(InputTicket inputTicket) throws DatabaseException, NotFoundException {
		logger.info("TicketDAO.addTicket() executed");

		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			User author = userDao.getUserById(inputTicket.getAuthorId());
			Type type = this.getType(inputTicket.getTypeId());
			Status status = this.getStatus(1);

			Ticket result = new Ticket();
			result.setAmount(inputTicket.getAmount());
			result.setAuthor(author);
			result.setResolver(null);
			result.setStatus(status);
			result.setType(type);
			result.setDescription(inputTicket.getDescription());
			result.setIssued(new Date(System.currentTimeMillis()));
			result.setResolved(null);
			//result.setImage(inputTicket.getImage());

			session.persist(result);
			session.getTransaction().commit();
			session.close();
			return result;
		} catch (HibernateException e) {
			session.clear();
			throw new DatabaseException("Something happened while trying to connect to the database." + e.getMessage());
		}
	}

	public Ticket updateTicket(int inputId, InputTicket inputTicket, User user)
			throws DatabaseException, NotFoundException, NotAuthorizedException {
		logger.info("TicketDAO.updateTicket() executed");

		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			Ticket result = this.getTicketById(inputId, user);

			if (result.getStatus().getId() != 1) {
				throw new NotAuthorizedException("This ticket is already resolved.");
			}

			User author = userDao.getUserById(inputTicket.getAuthorId());
			if (author == null) {
				throw new NotFoundException("User id: " + " for author not found.");
			}
			if (user.getUserRole().getId() == 1 && author.getId() != user.getId()) {
				throw new NotAuthorizedException("User is not authorized to veiw this ticket.");
			}
			result.setAuthor(author);

			Status status = this.getStatus(inputTicket.getStatusId());
			if (status.getStatus() != "Pending") {
				User resolver = userDao.getUserById(inputTicket.getResolverId());
				if (resolver == null || resolver.getUserRole().getId() == 1) {
					throw new NotFoundException("User id: " + " is not a resolver.");
				}
				if(resolver.getId() == author.getId()) {
					throw new NotAuthorizedException("User not authorized to resolve tickets they issued themselves.");
				}
				result.setResolver(resolver);
				result.setResolved(new Date(System.currentTimeMillis()));
			}
			result.setStatus(status);

			result.setAmount(inputTicket.getAmount());
			Type type = this.getType(inputTicket.getTypeId());
			result.setType(type);
			result.setDescription(inputTicket.getDescription());
			//result.setImage(inputTicket.getImage());

			session.update(result);
			session.getTransaction().commit();
			session.close();
			return result;
		} catch (HibernateException e) {
			session.clear();
			throw new DatabaseException("Something happened while trying to connect to the database." + e.getMessage());
		} catch (NoResultException e) {
			session.clear();
			throw new NotFoundException("Ticket with id: " + inputId + " not found.");
		}
	}

	public void deleteTicket(int inputId, User user)
			throws DatabaseException, NotFoundException, NotAuthorizedException {
		logger.info("TicketDAO.deleteTicket() executed");

		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			Ticket result = this.getTicketById(inputId, user);
			session.delete(result);
			session.getTransaction().commit();
			session.close();
		} catch (HibernateException e) {
			session.clear();
			throw new DatabaseException("Something happened while trying to connect to the database." + e.getMessage());
		} catch (NoResultException e) {
			session.clear();
			throw new NotFoundException("Ticket with id: " + inputId + " not found.");
		}
	}
}
