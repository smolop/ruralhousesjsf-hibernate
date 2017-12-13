package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import antlr.collections.impl.Vector;
import domain.Offer;
import domain.RuralHouse;
import exceptions.BadDates;
import exceptions.OverlappingOfferExists;
import services.repository.HibernateDataAccess;;

public class FacadeImplementationWS implements ApplicationFacadeInterfaceWS {

	@Override
	public Offer createOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay, float price)
			throws OverlappingOfferExists, BadDates {
		HibernateDataAccess hdbMng = new HibernateDataAccess();
		
		if(firstDay.compareTo(lastDay)>=0){
			hdbMng.close();
		}
		
		Offer o = null;
		if (firstDay.compareTo(lastDay)>=0) { hdbMng.close(); throw new BadDates();}
		
		boolean b = hdbMng.existsOverlappingOffer(ruralHouse,firstDay,lastDay); 
		if (!b) o=hdbMng.createOffer(ruralHouse,firstDay,lastDay,price);		

		hdbMng.close();
		System.out.println("<< FacadeImplementationWS: createOffer=> O= "+o);
		return o;
	}

	@Override
	public List<RuralHouse> getAllRuralHouses() {
		System.out.println(">> FacadeImplementationWS: getAllRuralHouses");

		HibernateDataAccess hdbMng = new HibernateDataAccess();

		Set<RuralHouse>  ruralHouses = hdbMng.getAllRuralHouses();
		hdbMng.close();
		
		System.out.println("<< FacadeImplementationWS:: getAllRuralHouses");
		return  new ArrayList<RuralHouse>(ruralHouses);
	}

	@Override
	public List<Offer> getOffers(RuralHouse rh, Date firstDay,  Date lastDay) {
		HibernateDataAccess hdbMng =new HibernateDataAccess();
		Set<Offer> offers= new HashSet<Offer>();
		  offers = hdbMng.getOffers(rh,firstDay,lastDay);
		  hdbMng.close();
		return new ArrayList<Offer>(offers);
	}

	@Override
	public void initializeBD() {
		HibernateDataAccess hdbMng = new HibernateDataAccess();
		hdbMng.initializeDB();
		hdbMng.close();
	}
	
}
