package services.repository;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.classic.Session;

import domain.Offer;
import domain.RuralHouse;
import exceptions.OverlappingOfferExists;
import setting.HibernateUtil;

public class HibernateDataAccess {

	private Session session;

	public HibernateDataAccess() {}

	public void initializeDB(){
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

			session.beginTransaction().commit();
			System.out.printf("rural house added: \n%s \n%s \n%s \n%s ", rh1, rh2, rh3, rh4);

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public Set<RuralHouse> getAllRuralHouses(){
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		List<RuralHouse> ruralHousesList = session.createQuery("FROM RuralHouse").list();
		Set<RuralHouse> ruralHouses = new HashSet<RuralHouse>(ruralHousesList);
		session.beginTransaction().commit();
		return ruralHouses;
	}

	public Set<Offer> getOffers(RuralHouse rh, Date firstDay,  Date lastDay){
		System.out.println("TRACE 0 : Before create session. ");
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		System.out.println("TRACE 1 : After session created ");
		System.out.printf("firstday=%s, lastDay=%s, rh=%d", String.valueOf(firstDay), String.valueOf(lastDay), rh.getHouseNumber());
		//List<Offer> offersList = session.createQuery("FROM Offer WHERE firstDay='"+firstDay+"' AND lastDAy='"+lastDay+"'").list();
		List<Offer> offersList = session.createQuery("FROM Offer").list();
		System.out.println(offersList);
		Set<Offer> offers = new HashSet<Offer>(offersList);
		session.beginTransaction().commit();
		return offers;
	}

	public RuralHouse createRuralHouse(String description, String city) {
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
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
			session.beginTransaction().commit();
			System.out.println("RuralHouse created: "+ruralHouse.toString());
			return of;
		}catch (Exception e) {
			System.out.println("Offer not created: "+e.toString());
			return null;	
		}	
	}

	public boolean existsOverlappingOffer(RuralHouse rh, Date firstDay, Date lastDay) throws OverlappingOfferExists{
		try{
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			RuralHouse rhObj = (RuralHouse) session.get(RuralHouse.class, rh.getHouseNumber());
			System.out.println("RuralHouse ov = "+rhObj);
			session.beginTransaction().commit();
			System.out.println("OverLappinfOffer ? = "+(rhObj.overlapsWith(firstDay,lastDay)));
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
