package beans;

import java.util.Date;
import java.util.Set;
import java.util.Vector;

import domain.Offer;
import domain.RuralHouse;
import exceptions.BadDates;
import exceptions.OverlappingOfferExists;
import services.repository.HibernateDataAccess;;

public class FacadeImplementationWS implements ApplicationFacadeInterfaceWS {

	@Override
	public Offer createOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay, float price)
			throws OverlappingOfferExists, BadDates {
		System.out.println(">> FacadeImplementationWS: createOffer=> ruralHouse= "+ruralHouse+" firstDay= "+firstDay+" lastDay="+lastDay+" price="+price);
	
		HibernateDataAccess hdbMng = new HibernateDataAccess();
		Offer o = null;
		if (firstDay.compareTo(lastDay)>=0) {throw new BadDates();}
		boolean b = hdbMng.existsOverlappingOffer(ruralHouse,firstDay,lastDay); 
		if (!b) o = hdbMng.createOffer(ruralHouse,firstDay,lastDay,price);		
		//hdbMng.close();
		System.out.println("<< FacadeImplementationWS: createOffer=> O= "+o);
		return o;
	}

	@Override
	public Vector<RuralHouse> getAllRuralHouses() {
		System.out.println(">> FacadeImplementationWS: getAllRuralHouses");
		HibernateDataAccess hdbMng = new HibernateDataAccess();
		Set<RuralHouse>  ruralHouses = hdbMng.getAllRuralHouses();
		Vector<RuralHouse> rhs = new Vector<RuralHouse>(ruralHouses);
		System.out.println("<< FacadeImplementationWS:: getAllRuralHouses");
		return rhs;
	}

	@Override
	public Vector<Offer> getOffers(RuralHouse rh, Date firstDay,  Date lastDay) {
		System.out.printf(">> FacadeImplementationWS: offers  FirstDay: %s, LastDay: %s, RuralHouse: %s ", firstDay.toString(), lastDay.toString(), rh.toString());
		HibernateDataAccess hdbMng = new HibernateDataAccess();
		System.out.println("TRACE 0 : After Set<Offer> , Before getOffers");
		Set<Offer> offers = hdbMng.getOffers(rh,firstDay,lastDay);
		System.out.println("TRACE 1 : After Set<Offer> , After getOffers ");
		System.out.println("TRACE 2 : Offers :" + offers);
		Vector<Offer> offrs = new  Vector<Offer>(offers);
		System.out.printf("<< FacadeImplementationWS: offers  FirstDay: %s, LastDay: %s, RuralHouse: %s ", firstDay.toString(), lastDay.toString(), rh.toString());
		return offrs;
	}

	@Override
	public void initializeBD() {
		HibernateDataAccess hdbMng = new HibernateDataAccess();
		hdbMng.initializeDB();
		hdbMng.close();
	}

}
