package services.repository;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.classic.Session;

import com.objectdb.o.SLV.U;

import domain.Offer;
import domain.RuralHouse;
import domain.User;
import exceptions.OverlappingOfferExists;
import setting.HibernateUtil;

public class HibernateDataAccess {

	private Session session;

	public HibernateDataAccess() {}

	public void initializeDB(){
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
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

			session.save(rh1);
			session.save(rh2);
			session.save(rh3);
			session.save(rh4);


			User u = new User();
			u.setUsername("Admin");
			u.setPassword("Qwerty");

			session.save(u);

			session.getTransaction().commit();
			System.out.printf("rural house added: \n%s \n%s \n%s \n%s ", rh1, rh2, rh3, rh4);
		}catch(Exception e){
			System.out.println(e.getMessage());
			session.getTransaction().rollback();
		}
	}

	public Set<RuralHouse> getAllRuralHouses(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		List<RuralHouse> ruralHousesList = session.createQuery("FROM RuralHouse").list();
		Set<RuralHouse> ruralHouses = new HashSet<RuralHouse>(ruralHousesList);
		session.getTransaction().commit();
		return ruralHouses;
	}

	public Set<Offer> getOffers() {
		System.out.println("TRACE 0 : Before create session. ");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		System.out.println("TRACE 1 : After session created ");
		List<Offer> offersList = session.createQuery("FROM Offer WHERE booked=0").list();
		System.out.println(" OFFERS ::: "+offersList);
		Set<Offer> offers = new HashSet<Offer>(offersList);
		System.out.println(" OFFERS (R) ::: "+offers);
		session.getTransaction().commit();
		return offers;
	}

	public Set<Offer> getOffers(RuralHouse rh, Date firstDay,  Date lastDay){
		System.out.println("TRACE 0 : Before create session. ");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		System.out.println("TRACE 1 : After session created ");
		System.out.printf("firstday=%s, lastDay=%s, rh=%d", String.valueOf(firstDay), String.valueOf(lastDay), rh.getHouseNumber());
		List<Offer> offersList = session.createQuery("FROM Offer WHERE ruralhouse='"+rh.getHouseNumber()+"' AND booked=0").list();
		System.out.println(" OFFERS ::: "+offersList);
		Set<Offer> offers = new HashSet<Offer>();
		for (Iterator iterator = offersList.iterator(); iterator.hasNext();) {
			Offer offer = (Offer) iterator.next();
			if(offer.getFirstDay().compareTo(firstDay) >= 0 && offer.getLastDay().compareTo(lastDay) <= 0 )
				offers.add(offer);
		}
		System.out.println(" OFFERS (R) ::: "+offers);
		session.getTransaction().commit();
		return offers;
	}

	public RuralHouse createRuralHouse(String description, String city) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			RuralHouse rh = new RuralHouse();
			rh.setDescription(description);
			rh.setCity(city);
			session.save(rh);
			session.getTransaction().commit();
			return rh;
		}catch (Exception e) {
			System.out.println("RuralHouse not created: "+e.toString());
			session.getTransaction().rollback();
			return null;	
		}	
	}

	public Offer createOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay, float price) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Offer of = new Offer();
			of.setRuralHouse(ruralHouse);
			if (of.getRuralHouse() == null)
				return null;
			of.setFirstDay(firstDay);
			of.setLastDay(lastDay);
			of.setPrice(price);
			of.setHousenumber(ruralHouse.getHouseNumber());
			session.save(of);
			session.getTransaction().commit();
			System.out.println("RuralHouse created: "+ruralHouse.toString());
			return of;
		}catch (Exception e) {
			System.out.println("Offer not created: "+e.toString());
			session.getTransaction().rollback();
			return null;	
		}	
	}

	public Offer getOffer(Long offerNumber) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Offer ofObj = (Offer) session.get(Offer.class, offerNumber);
		if(!ofObj.getOfferNumber().equals(offerNumber))
			return null;
		session.getTransaction().commit();
		return ofObj;
	}

	public void bookOffer(Offer offer, String username) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Offer ofObj = (Offer) session.get(Offer.class, offer.getOfferNumber());
		if(!ofObj.isBooked()) {
			User u = (User) session.get(User.class, username);
			ofObj.setBookedBy(u);
			ofObj.setBooked(true);
		}
		session.getTransaction().commit();
	}

	public boolean cancelBookingOffer(Offer offer, String username) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Offer ofObj = (Offer) session.get(Offer.class, offer.getOfferNumber());
		if(ofObj.isBooked()) {
			ofObj.setBookedBy(null);
			ofObj.setBooked(false);
		}
		session.getTransaction().commit();
		if((ofObj.getBookedBy() == null) && (ofObj.getBookedBy() == null))
			return true;
		return false;
	}

	public boolean existsOverlappingOffer(RuralHouse rh, Date firstDay, Date lastDay) throws OverlappingOfferExists{
		Session session = null;
		try{
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			RuralHouse rhObj = (RuralHouse) session.get(RuralHouse.class, rh.getHouseNumber());
			System.out.println("RuralHouse ov = "+rhObj);
			session.getTransaction().commit();
			System.out.println("OverLappinfOffer ? = "+(rhObj.overlapsWith(firstDay,lastDay)));
			if (rhObj.overlapsWith(firstDay,lastDay)!=null) return true;
		} catch (Exception e){
			System.out.println("Error: "+e.toString());
			session.getTransaction().rollback();
			return true;
		}
		return false;
	}

	public void close() {
		session.close();
	}

	public User createUser(String username, String password) {
		Session session = null;
		try{
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			User u = new User();
			u.setUsername(username);
			u.setPassword(password);
			session.save(u);
			session.getTransaction().commit();
			System.out.println("RuralHouse created: "+u.toString());
			return u;
		} catch (Exception e){
			System.out.println("Error: "+e.toString());
			session.getTransaction().rollback();
			return null;
		}
	}

	public boolean userExists(String username) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		User u = (User) session.get(User.class, username);
		session.getTransaction().commit();
		if(u == null)
			return false;
		return true;
	}

	public boolean isValidUser(String username, String password) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		User u = (User) session.get(User.class, username);
		session.getTransaction().commit();
		if(u == null)
			return false;
		if(u.getPassword().equals(password))
			return true;
		return false;
	}

	public boolean offerExists(Long offerNumber) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Offer o =  (Offer) session.get(Offer.class, offerNumber);
		session.getTransaction().commit();
		if(o == null)
			return false;
		return true;
	}

	public Set<Offer> getUserBookedOffers(String username) {
		System.out.println("TRACE 0 : Before create session. ");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		System.out.println("TRACE 1 : After session created ");
		List<Offer> offersList = session.createQuery("FROM Offer WHERE bookedBy='"+username+"' AND booked=1").list();
		System.out.println(" OFFERS ::: "+offersList);
		Set<Offer> offers = new HashSet<Offer>(offersList);
		System.out.println(" OFFERS (R) ::: "+offers);
		session.getTransaction().commit();
		return offers;
	}



}
