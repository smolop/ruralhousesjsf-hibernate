package beans;

public class Facade {

	private static FacadeImplementationWS instance = new FacadeImplementationWS();

	private Facade() {}

	public static FacadeImplementationWS getInstance() {
		return instance;
	}
}
