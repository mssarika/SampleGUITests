package Pages;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Payment extends BasePage{
	
	@FindBy(id="card-number") private WebElement cardDetails;
	@FindBy(xpath="//form[@data-test='confirmation-emails-form']//ul//span") private WebElement confirmationemail;
	@FindBy(xpath="//button[@data-test='payWithCard']") private WebElement payWithCard;
	//@FindBy(xpath="//div[@data-test='arrivalStation']") private WebElement arrivalStation;
	@FindBy(xpath="//div[@data-test='trip-card']//span/span") private WebElement tripType;
	@FindBy(xpath="//div[@class='paypal-button-label-container']") private WebElement PayPal;
	@FindBy(xpath="//h3[@data-test='basket-total']/span/span/span") private WebElement amountToPay;
	By startdateDetails=By.xpath("//span[@data-test='OUT-departure-time-trip-card']/span/span/span");
	By returndateDetails=By.xpath("//span[@data-test='RTN-departure-time-trip-card']/span/span/span");
	By paymentFrame=By.id("IFramePaymentForm");
	By departure=By.xpath("//span[@data-test='departureStation']");
	By arrival=By.xpath("//div[@data-test='arrivalStation']");
	private Boolean testPass=true;
	
	//function to validate all the displayed details in the Ticket and select the payment option 
	public void validateJourneyDetails()
	{
		logger.info("Verifying the Ticket details");
		//verifying that the email is same as the registered email
		if(!confirmationemail.getText().equals(TravelOptions.emailid))
		{
			logError("email for sending confirmation is not same as the registered email, Expected : "+TravelOptions.emailid+", but Found: "+confirmationemail.getText());
			testPass=false;
		}
		else
			logger.info("Validation : email for sending confirmation is same as the registered email");
		
		//verifying the journey type
		if(!StartBooking.journeyType.contains(tripType.getText()))
		{
			logError("Journey Type is not as expected, Expected: "+StartBooking.journeyType+", but Found: "+ tripType.getText());
			testPass=false;
		}
		else
			logger.info("Validation : Journey Type is as expected");
		
		//verifying the Total amount
		if(!amountToPay.getText().contains(TravelOptions.expectedPrice))
		{
			logError("Total amount to pay is not as expected, Expected: "+TravelOptions.expectedPrice+", but Found: "+ amountToPay.getText());	
			testPass=false;
		}
		else
			logger.info("Validation : Total amount to pay is as expected");
		
		//verifying the stations
		String departureStation="";
		try{ departureStation=driver.findElement(departure).getText();}
		catch(StaleElementReferenceException exc)
		{	
			waitForVisibility(driver.findElement(departure), 10,"Could not retrieve Departure station details");
			departureStation=driver.findElement(departure).getText();
		}
		
		if(!departureStation.equals(StartBooking.departure))
		{
			logError("Departure station is not as expected, Expected : "+StartBooking.departure+",but Found : "+departureStation);
			testPass=false;
		}
		else
			logger.info("Validation : Departure station is as expected");		
		
		String arrivalStation="";
		try{ arrivalStation=driver.findElement(arrival).getText();}
		catch(StaleElementReferenceException exc)
		{
			waitForVisibility(driver.findElement(arrival), 10,"Could not retrieve Arrival station details");
			arrivalStation=driver.findElement(arrival).getText();
		}
		if(!arrivalStation.equals(StartBooking.arrival))
		{
			logError("Arrival station is not as expected, Expected : "+StartBooking.arrival+",but Found : "+arrivalStation);
			testPass=false;
		}
		else
			logger.info("Validation : Arrival station is as expected");
		
		//verifying the journey dates 
		if(verifyTravelDates(startdateDetails,StartBooking.journeyDate))
			logger.info("Validation : Outbound Journey date is as expected");
		else
			logger.error("Outbound Journey date is not as expected");
		
		if(StartBooking.journeyType.equals("RETURN"))
		{
			if(verifyTravelDates(returndateDetails,StartBooking.journeyReturnDate))
				logger.info("Validation : Return Journey date is as expected");
			else
				logger.error("Return Journey date is not as expected");
		}
		
		Assert.assertTrue("Ticket Details are not as expected", testPass);
		
		//selecting the payment option, once the card details form is displayed the test terminates
		logger.info("Selecting 'Pay with Card' option");
		payWithCard.click();
		new WebDriverWait(driver, 10).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(paymentFrame));
		Assert.assertTrue("Did not ask for card details", waitForVisibility(cardDetails,10,"Did not ask for card details")!=null);
		logger.info("Time to enter the card details...Test terminates here....");
	}
	
	//function to retrieve the dates displayed on ticket and compare with the dates entered in the search criteria 
	private boolean verifyTravelDates(By dateDetails, String journeyDate)
	{
		List<WebElement> dateValues=driver.findElements(dateDetails);
		LocalDate expectedDate=LocalDate.parse(journeyDate, DateTimeFormatter.ofPattern("dd-MM-yy"));		
		String foundDate=dateValues.get(1).getText()+"-"+dateValues.get(2).getText()+"-"+dateValues.get(3).getText();		
		LocalDate ticketDate=LocalDate.parse(foundDate, DateTimeFormatter.ofPattern("dd-MMM-yyyy"));	
		if(!expectedDate.equals(ticketDate))
		{
			logError("Journey Day is not as expected, Expected: "+expectedDate+", but Found: "+ticketDate);
			testPass=false;
			return false;
		}
		else
			return true;
	}

}
