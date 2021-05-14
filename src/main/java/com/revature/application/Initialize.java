package com.revature.application;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

import org.hibernate.Session;

import com.revature.dao.UserDAO;
import com.revature.model.Status;
import com.revature.model.Ticket;
import com.revature.model.Type;
import com.revature.model.User;
import com.revature.model.UserRole;
import com.revature.util.SessionUtility;

public class Initialize {
	public static void main(String[] args) throws NoSuchAlgorithmException {
		Status pending = new Status("Pending");
		Status rejected = new Status("Rejected");
		Status accepted = new Status("Accepted");
		Type lodging = new Type("Lodging");
		Type travel = new Type("Travel");
		Type food = new Type("Food");
		Type other = new Type("Other");
		UserRole user = new UserRole("User");
		UserRole manager = new UserRole("Finance Manager");
		Session session = SessionUtility.getSessionFactory().openSession();
		session.beginTransaction();
		session.persist(pending);
		session.persist(rejected);
		session.persist(accepted);
		session.persist(lodging);
		session.persist(travel);
		session.persist(food);
		session.persist(other);
		session.persist(user);
		session.persist(manager);
		session.getTransaction().commit();
		session.close();
		
		User john = new User("John", "Doe", "username1", "password1", "johnDoe@gmail.com", user);
		User jane = new User("Jane", "Dean", "username2", "password2", "janeDean@gmail.com", user);
		User jim = new User("Jim", "Dave", "username3", "password3", "jimDave@gmail.com", manager);
		User jenny = new User("Jenny", "Dawson", "username4", "password4", "jennyDawson@gmail.com", manager);
		Ticket ticket1 = new Ticket(50.00, john, null, pending, food, "Business meeting lunch", new Timestamp(System.currentTimeMillis()), null, null);
		Ticket ticket2 = new Ticket(200.00, jane, jim, accepted, lodging, "Two weeks hotel fee", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), null);
		Ticket ticket3 = new Ticket(25.00, jim, null, pending, travel, "Taxi fare", new Timestamp(System.currentTimeMillis()), null, null);
		Ticket ticket4 = new Ticket(35.00, jim, jenny, accepted, other, "Cleaning services", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), null);
		Ticket ticket5 = new Ticket(45.00, jenny, jim, rejected, food, "After work celebration", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), null);
		Ticket ticket6 = new Ticket(70.00, jane, jenny, accepted, travel, "Train ticket", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), null);
		Ticket ticket7 = new Ticket(53.00, jenny, null, pending, lodging, "Hotel", new Timestamp(System.currentTimeMillis()), null, null);
		Ticket ticket8 = new Ticket(61.00, jane, null, pending, other, "Office Supplies", new Timestamp(System.currentTimeMillis()), null, null);
		Ticket ticket9 = new Ticket(72.00, john, jim, accepted, food, "Business dinner", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), null);
		Ticket ticket10 = new Ticket(31.00, jim, null, pending, food, "Meal during overtime", new Timestamp(System.currentTimeMillis()), null, null);
		Ticket ticket11 = new Ticket(14.00, john, jenny, accepted, other, "Services", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), null);
		
		UserDAO userDao = new UserDAO();
		userDao.addUser(john);
		userDao.addUser(jane);
		userDao.addUser(jim);
		userDao.addUser(jenny);
		
		
		Session session2 = SessionUtility.getSessionFactory().openSession();
		session2.beginTransaction();
		session2.persist(ticket1);
		session2.persist(ticket2);
		session2.persist(ticket3);
		session2.persist(ticket4);
		session2.persist(ticket5);
		session2.persist(ticket6);
		session2.persist(ticket7);
		session2.persist(ticket8);
		session2.persist(ticket9);
		session2.persist(ticket10);
		session2.persist(ticket11);
		session2.getTransaction().commit();
		session2.close();
	}
}
