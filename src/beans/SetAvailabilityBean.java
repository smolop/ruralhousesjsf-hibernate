package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import domain.Offer;
import domain.RuralHouse;
import exceptions.BadDates;
import exceptions.OverlappingOfferExists;
public class SetAvailabilityBean {
	
	private List<RuralHouse> ruralHouses;
	private RuralHouse ruralHouse;
	private Date firstDay;
	private Date lastDay;
	private float price = 0f;
	
	public SetAvailabilityBean() {}
	
	public Date getFirstDay() {
		return firstDay;
	}
	public void setFirstDay(Date firstDay) {
		this.firstDay = firstDay;
	}
	public Date getLastDay() {
		return lastDay;
	}
	public void setLastDay(Date lastDay) {
		this.lastDay = lastDay;
	}
	public List<RuralHouse> getRuralHouses() {
		this.ruralHouses = Facade.getInstance().getAllRuralHouses();
		return new ArrayList<RuralHouse>(this.ruralHouses);
	}
	public void setRuralHouses(List<RuralHouse> ruralHouses) {
		this.ruralHouses = ruralHouses;
	}
	public RuralHouse getRuralHouse() {
		return ruralHouse;
	}
	public void setRuralHouse(RuralHouse ruralHouse) {
		this.ruralHouse = ruralHouse;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public void setAvailability() {
		if(!Facade.getUserLogged().equals("Admin")) {
			showNotificationError("Error", "You don't have permission");
			return;
		}
		if (ruralHouse == null) {
			showNotificationError("Error", "Rural house is null");
			return;
		} else if (price < 0) {
			showNotificationError("Error", "Price can't be negative");
			return;
		} else if (firstDay == null) {
			showNotificationError("Error", "Enter a valid first day");
			return;
		} else if (lastDay == null) {
			showNotificationError("Error", "Enter a valid last day");
			return;
		}
		
		try {
			Offer offer = Facade.getInstance().createOffer(ruralHouse, firstDay, lastDay, price);
			if (offer == null) {
				throw new OverlappingOfferExists();
			}
			showNotification("Success", "Your offer has been successfully seted");
		} catch (OverlappingOfferExists e) {
			showNotificationError("Error", "Overlapping offer exists");
			e.printStackTrace();
		} catch (BadDates e) {
			showNotificationError("Error", "Bad dates");
			e.printStackTrace();
		} catch (Exception e) {
			showNotificationError("Error", "Invalid offer");
		}
	}

	private void showNotification(String title, String content) {
		FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, title, content));
	}
	
	private void showNotificationError(String title, String content) {
		FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, title, content));
	}
	
	public String main() {
		return "main";
	}

}
