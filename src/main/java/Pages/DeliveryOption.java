package Pages;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DeliveryOption extends BasePage{
	
	@FindBy(xpath="//button[@data-test='payWithCard']") private WebElement payWithCard;
	@FindBy(xpath="//input[@data-test='kioskRadioButton']") private WebElement collect;
	@FindBy(xpath="//button[@data-test='cjs-button-continue']") private WebElement Continue;
	@FindBy(xpath="//input[@type='email']") private WebElement email;	
		
	//function to select the 'collect' option for delivering the ticket and navigates to Payment page
	public void selectDeliveryOption()
	{
		logger.info("Selecting 'Collect' option for Ticket");
		collect.click();
		if(!Continue.isEnabled())
			waitToEnable(Continue,20,"Continue button not clickable");
		//verifying that email displayed for ticket collection is same as the registered email
		Assert.assertTrue("email for ticket collection is not same as the registered email",email.getAttribute("value").equals(TravelOptions.emailid));
		logger.info("Validation : email for ticket collection is same as the registered email");
		
		Continue.click();
		
		Assert.assertTrue("Could not navigate to Payment page", waitForVisibility(payWithCard,10,"Could not navigate to Payments page")!=null);
		logger.info("Payment page launched");
		
	}
}
