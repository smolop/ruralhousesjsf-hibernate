package services.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.classic.Session;

import com.mysql.jdbc.ResultSet;

import domain.Offer;
import domain.RuralHouse;
import exceptions.OverlappingOfferExists;
import setting.HibernateUtil;

public class HibernateDataAccess {

	private Session session;

	public HibernateDataAccess() {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		//session.beginTransaction();
	}

	private Connection jdbcConnect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection("jdbc:mysql://localhost/rural_house", "root", "admin");
		}catch (Exception errno) {
			errno.printStackTrace();
		}
		return null;
	}

	public void initializeDB(){

		try {
			session.getTransaction().begin();
			RuralHouse rh1=new RuralHouse();
			rh1.setDescription("Ezkioko etxea");
			rh1.setCity("Ezkio");
			RuralHouse rh2=new RuralHouse();
			rh2.setDescription("Etxetxikia");
			rh2.setCity("Iruna");
			RuralHouse rh3=new RuralHouse();
			rh3.setDescription("Udaletxea");
			rh3.setCity("Bilbo");
			RuralHouse rh4=new RuralHouse();
			rh4.setDescription("Gaztetxea");
			rh4.setCity("Renteria");

//			Date firstDay = new Date(2017,12,18);
//			Date lastDay = new Date(2018, 1, 5);
//
//			Offer of1 = new Offer();	
//			of1.setFirstDay(firstDay);
//			of1.setLastDay(lastDay);
//			of1.setPrice(550);
//			of1.setRuralHouse(rh1);
//			Offer of2 = new Offer();
//			of2.setFirstDay(firstDay);
//			of2.setLastDay(lastDay);
//			of2.setPrice(750);
//			of2.setRuralHouse(rh2);
//			Offer of3 = new Offer();
//			of3.setFirstDay(firstDay);
//			of3.setLastDay(lastDay);
//			of3.setPrice(850);
//			of3.setRuralHouse(rh3);		

			session.save(rh1);
			session.save(rh2);
			session.save(rh3);
			session.save(rh4);

			session.beginTransaction().commit();
			System.out.printf("rural house added: \n%s \n%s \n%s \n%s ", rh1, rh2, rh3, rh4);

		}catch(Exception e){
			e.printStackTrace();
		}

	}

	public Set<RuralHouse> getAllRuralHouses(){
		session.beginTransaction();
		List<RuralHouse> ruralHousesList = session.createQuery("FROM RuralHouse").list();
		Set<RuralHouse> ruralHouses = new HashSet<RuralHouse>(ruralHousesList);
		return ruralHouses;
	}

	public Set<Offer> getOffers(RuralHouse rh, Date firstDay,  Date lastDay){
		session.getTransaction();
		List<Offer> offersList = session.createQuery("FROM Offer").list();
		Set<Offer> offers = new HashSet<Offer>(offersList);
		return offers;
	}

	public RuralHouse createRuralHouse(String description, String city) {
		try {
			session.beginTransaction();
			RuralHouse rh = new RuralHouse();
			rh.setDescription(description);
			rh.setCity(city);
			session.save(rh);
			session.beginTransaction().commit();
			return rh;
		}catch (Exception e) {
			System.out.println("RuralHouse not created: "+e.toString());
			return null;	
		}	
	}

	public Offer createOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay, float price) {
		try {
			session.beginTransaction();
			Offer of = new Offer();
			of.setRuralHouse(ruralHouse);
			of.setFirstDay(firstDay);
			of.setLastDay(lastDay);
			of.setPrice(price);
			session.save(of);
			session.getTransaction().commit();
			return of;
		}catch (Exception e) {
			System.out.println("Offer not created: "+e.toString());
			return null;	
		}	
	}

	public boolean existsOverlappingOffer(RuralHouse rh,Date firstDay, Date lastDay) throws  OverlappingOfferExists{
		try{
			RuralHouse rhObj = (RuralHouse) session.get(RuralHouse.class, rh.getHouseNumber());
			if (rhObj.overlapsWith(firstDay,lastDay)!=null) return true;
		} catch (Exception e){
			System.out.println("Error: "+e.toString());
			return true;
		}
		return false;
	}

	public void close() {
		session.close();
	}

}
