package beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;

import org.primefaces.context.RequestContext;

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

	public void setNumberOfNights(int numberOfNights) {
		this.numberOfNights = numberOfNights;
	}
	
	private void showNotification(String title, String content) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, title, content);
		RequestContext.getCurrentInstance().showMessageInDialog(message);
	}

	public String main() {
		return "main";
	}
	
	public void queryAvailability() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(firstDay);
		calendar.add(Calendar.DATE, numberOfNights);
		if (ruralHouse == null) {
			showNotification("Error", "rh null");
			return;
		}
		this.offers = Facade.getInstance().getOffers(ruralHouse, firstDay, calendar.getTime());
		showNotification("Traza", "numero de ofertas: " + offers.size());
	}

}