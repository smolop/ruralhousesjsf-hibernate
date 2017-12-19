package beans;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import domain.User;

public class RegisterBean {

	private String username;
	private String password;


	public RegisterBean () {}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	public String logUp() {
		try {
			User u = Facade.getInstance().createUser(username, password);
			if(u == null) {
				showNotificationError("Error", "User couldn't be registered or already was registered");
				return null;
			}
			showNotification("Notify", "User have been successfully registered");
			Facade.setUserLogged(username);
			Thread.sleep(3000);
			return "main";
		}catch(Exception e) {
			showNotificationError("Error/Fatal", "User already was registered or couldn't be registered by a unknow problem");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public String logIn() {
		try {
			boolean b = Facade.getInstance().isValidUser(username, password);
			if(!b) {
				showNotificationError("Error", "User couldn't be login, username is incorrect or password doesn't match");
				return null;
			}
			showNotification("Notify", "Welcome, successfully loged");
			Facade.setUserLogged(username);
			Thread.sleep(3000);
			return "main";
		}catch(Exception e) {
			showNotificationError("Error/Fatal", "User couldn't access cuz a unknow problem");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}

	}

	private void showNotification(String title, String content) {
		FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, title, content));
	}

	private void showNotificationError(String title, String content) {
		FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, title, content));
	}
	
}
