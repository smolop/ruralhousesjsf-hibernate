package experiments;

import services.repository.HibernateDataAccess;

public class Main {
	
	public static void main(String[] args) {
		//FacadeImplementationWS facadeImplementationWS = new FacadeImplementationWS();
		HibernateDataAccess hdbMng = new HibernateDataAccess();
		hdbMng.initializeDB();
//		Set<RuralHouse> ruralHouses = new HashSet<>(facadeImplementationWS.getAllRuralHouses());
//		for (RuralHouse rh : ruralHouses) {
//			System.out.println(rh.offers.size());
//		}
	}

}
