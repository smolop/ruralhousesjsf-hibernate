package beans;

public class MainBean {
	
	
	public MainBean() {System.out.println("Creada instancia");}

	
	public String queryAvailability() {
		return "query-availability";
	}
	
	public String setAvailability() {
		return "set-availability";
	}

}
