package beans;

public class Facade {

	private static FacadeImplementationWS instance = new FacadeImplementationWS();
	private static String userLogged;
	
	private Facade() {}

	public static String getUserLogged() {
		return userLogged;
	}
	
	public static void setUserLogged(String username) {
		userLogged = username;
	}
	
	public static FacadeImplementationWS getInstance() {
		return instance;
	}
}
