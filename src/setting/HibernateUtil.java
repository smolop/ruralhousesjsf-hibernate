package setting;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			return new Configuration().configure().buildSessionFactory();
			// Crea una instancia de SessionFactory con los datos de configuraci�n
			// de hibernate.cfg.xml, que debe colocarse en el subdirectorio �src�
			// Si se creara la instancia de SessionFactory de la siguiente manera:
			// new Configuration().configure(new File("hibernate.cfg.xml")).buildSessionFactory();
			// entonces, habr�a que colocar el fichero en el directorio ra�z del proyecto
			// Y no tendr�a por qu� llamarse "hibernate.cfg.xml" sino el nombre que se quiera 
		}catch (Throwable ex) {
				System.err.println("Fallo creando el SessionFactory." + ex);
				throw new ExceptionInInitializerError(ex);
			}
		}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}