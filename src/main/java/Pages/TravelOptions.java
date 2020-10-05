package Pages;

import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

public class TravelOptions extends BasePage{
	
	@FindBy(id="direction") private WebElement direction;
	@FindBy(id="position") private WebElement position;
	@FindBy(id="carriageType") private WebElement coachType;
	@FindBy(xpath="//button[@data-test='cjs-button-continue']") private WebElement Continue;
	@FindBy(xpath="//button[@data-test='login-form-submit']") private WebElement continueAsGuest;
	@FindBy(xpath="//span[contains(text(),'Select a delivery option')]") private WebElement DeliveryOptionHeading;
	@FindBy(xpath="//button[@data-test='login-form-submit']") private WebElement SignIn;
	@FindBy(xpath="//span[contains(text(),'This is where we will be sending your tickets')]") private WebElement message;
	@FindBy(xpath="//input[@name='isGuest' and @value='true']/../span") private WebElement isGuest;
	@FindBy(xpath="//div[contains(@class,'guest-checkout')]") private WebElement asGuest;
	@FindBy(xpath="//input[@type='email']") private WebElement email;
	@FindBy(xpath="//div[@data-test='basket-summary-booking-fee-amount']") private WebElement bookingFee;
	
	By totalPrice=By.xpath("//span[@data-test='cjs-price']/span");
	
	static String expectedPrice;	
	static String  emailid;	
	
	/*function to select the travel options and submit the details,
	 * on moving to the next page selects the 'continue as guest' option 	 
	 * and provides an email address and submits the form
	 */	
	public void selectTravelOptions()
	{
		//selecting the travel options direction, position and coach types
		String options;
		logger.info("Choosing the Travel Options randomly");
		options=populateDropDown(direction);
		logger.info("Selected Direction : "+options);
		options=populateDropDown(position);
		logger.info("Selected Position : "+options);
		options=populateDropDown(coachType);
		logger.info("Selected Coach Type : "+options);
		
		/*calculating the total cost by adding booking fee retrieved from travel options page to
		 * the 'ticketPrice' in SelectTiming class and verifies if it is same as the TotalPrice 
		 * displayed on the travel options page 
		 */
		expectedPrice=String.valueOf(Float.valueOf(SelectTiming.ticketPrice)+Float.valueOf(bookingFee.getText().substring(1)));
		Assert.assertTrue("Total Price is not as expected. Expected : "+expectedPrice+", but Found : "+driver.findElement(totalPrice).getText(), 
				driver.findElement(totalPrice).getText().contains(expectedPrice));
		logger.info("Validation : Total ticket Price is as expected : "+expectedPrice);
		//submitting the travel option selections and navigates to sing in page
		logger.info("Submitting Travel Options");
		Continue.click();
		//checking out as guest and verifying that the Ticket Delivery Options page is loaded successfully
		checkOutAsGuest();
		Assert.assertTrue("Could not navigate to Ticket Delivery Options page", waitForVisibility(DeliveryOptionHeading,10,"Could not navigate to Ticket Delivery Options page")!=null);
		logger.info("Ticket Delivery Options page launched");
	}
	
	/*function to select 'Checkout as guest' option for 2 different layouts displayed (one with green button and the 
	 * other with blue button). Enters a Randomly generated email to make sure that previously registered email is not 
	 * used, the email is saved to  and submits the form
	 */		
	private void checkOutAsGuest()
	{
		logger.info("Checking out as Guest");
		waitForVisibility(SignIn,15,"");
		if(waitForVisibility(isGuest,3,"")!=null)
			isGuest.click();
		else
			moveToElement(asGuest).click();

		waitForVisibility(message,5,"");
		emailid = generateRandomEmail();
		logger.info("Entering email : "+emailid);
		email.sendKeys(emailid);
		moveToElement(continueAsGuest).click();
	}
	
	/*function to randomly generate an email : first 8 characters are randomly generated followed by a randomly generated 
	 * number which is less than 1000 followed by domain name @test.com
	 */	
	private String generateRandomEmail()
	{
		String email = "user";
		String characterCollection="abcdefghijklmnopqrstuvwxyz";
		for(int i=0;i<8;i++)	
			email=email.concat(String.valueOf(characterCollection.charAt(new Random().nextInt(26))));
		
		email=email.concat(String.valueOf(new Random().nextInt(1000))).concat("@test.com");
		return email;
	}
	
	//function to populate the drop down passed via argument with an option which is randomly selected
	private String populateDropDown(WebElement element)
	{	
		Select dropDown=new Select(element);
		List<WebElement> options=dropDown.getOptions();
		String choice=options.get(new Random().nextInt(options.size())).getText();
		dropDown.selectByVisibleText(choice);
		return choice;
	}
}
