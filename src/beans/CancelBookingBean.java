package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import domain.Offer;
import domain.RuralHouse;

public class CancelBookingBean {
	
	private List<RuralHouse> ruralHouses;
	private List<Offer> bookedOffers;
	private Offer offer;
	private Long offerNumber;
	private RuralHouse ruralHouse;
	private Date firstDay;
	private Date lastDay;
	private float price = 0f;
	private String username;

	public CancelBookingBean() {}

	public List<RuralHouse> getRuralHouses() {
		return ruralHouses;
	}
	
	public Offer getOffer() {
		return offer;
	}
	
	public List<Offer> getUserBookedOffers() {
		 this.bookedOffers = Facade.getInstance().getUserBookedOffers(username);
		 return this.bookedOffers;
	}
	
	public Long getOfferNumber() {
		return offerNumber;
	}

	public void setOfferNumber(Long offerNumber) {
		this.offerNumber = offerNumber;
	}
	
	public RuralHouse getRuralHouse() {
		return ruralHouse;
	}

	public Date getFirstDay() {
		return firstDay;
	}

	public Date getLastDay() {
		return lastDay;
	}

	public float getPrice() {
		return price;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void cancelBooking() {
		if(offerNumber <= 0) {
			showNotificationError("Error", "Unavailable offer");
			return;
		}
		boolean ub = Facade.getInstance().userExists(username);
		if(!ub){
			showNotificationError("Error", "This user doesn't exists");
			return;
		}
		if(!Facade.getUserLogged().equals(username) && !Facade.getUserLogged().equals("Admin")) {
			showNotificationError("Error", "Incorrect user, this user don-t belong to you.");
			return;
		}
		boolean ob = Facade.getInstance().offerExists(offerNumber);
		if(!ob){
			showNotificationError("Error", "This offer doesn't exists");
			return;
		}
		offer = Facade.getInstance().getOffer(offerNumber);
		if(offer == null) {
			showNotificationError("Error", "This offer doesn't exists");
			return;
		}
		if(!offer.isBooked()) {
			showNotificationError("Error", "This offer wasn't booked");
			return;
		}
		if(!offer.getBookedBy().getUsername().equals(username)) {
			showNotificationError("Error", "This offer wasn't booked by you");
			return;
		}
		this.ruralHouse = offer.getRuralHouse();
		this.firstDay = offer.getFirstDay();
		this.lastDay = offer.getLastDay();
		this.price = offer.getPrice();
		try {
		boolean b = Facade.getInstance().cancelBookingOffer(offer, username);
		showNotification("Notify", "SUMMARY OFFER TAKEN: [Offer Number: "+offerNumber+"] [Rural House: "+ruralHouse+"] [Price: "+price+"] [First day: "+firstDay+"] [Last day: "+lastDay+"]");
		}catch(Exception e) {
			showNotificationError("Error", "Problem unkown");
			e.printStackTrace();
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
