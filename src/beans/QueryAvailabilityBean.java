package beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import domain.Offer;
import domain.RuralHouse;

public class QueryAvailabilityBean {

	private List<RuralHouse> ruralHouses;
	private RuralHouse ruralHouse;
	private Date firstDay;
	private List<Offer> offers;
	private int numberOfNights;
		
	public QueryAvailabilityBean() {}

	public Date getFirstDay() {
		return this.firstDay;
	}

	public void setFirstDay(Date firstDay) {
		this.firstDay = firstDay;
	}

	public List<RuralHouse> getRuralHouses() {
		this.ruralHouses = Facade.getInstance().getAllRuralHouses();
		return new ArrayList<RuralHouse>(this.ruralHouses);
	}

	public void setRuralHouses(List<RuralHouse> ruralHouses) {
		this.ruralHouses = ruralHouses;
	}

	public RuralHouse getRuralHouse() {
		return this.ruralHouse;
	}

	public void setRuralHouse(RuralHouse ruralHouse) {
		this.ruralHouse = ruralHouse;
	}

	public List<Offer> getOffers() {
		return offers;
	}

	public void setOffers(List<Offer> offers) {
		this.offers = offers;
	}
	
	public int getNumberOfNights() {
		return numberOfNights;
	}
	
	public String nights() {
		showNotification("Notify", "Number of nights : "+String.valueOf(numberOfNights));
		return "nights";
	}

	public void setNumberOfNights(int numberOfNights) {
		this.numberOfNights = numberOfNights;
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
	
	public void queryAvailability() {
		Calendar calendar = Calendar.getInstance();
		if(firstDay.equals(null))
		showNotificationError("Error", "First day is null");
		calendar.setTime(firstDay);
		if(!(numberOfNights>0)) {
		showNotificationError("Error", "Number of night must be upper to 0");
		return;
		}
		calendar.add(Calendar.DATE, numberOfNights);
		if (ruralHouse == null) {
			showNotificationError("Error", "rh null");
			return;
		}
		this.offers = Facade.getInstance().getOffers(ruralHouse, firstDay, calendar.getTime());
		showNotification("Notify", "numero de ofertas: " + offers.size());
	}

}
