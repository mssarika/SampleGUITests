package Pages;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SelectTiming extends BasePage{
	
	@FindBy(xpath="//button[@data-test='cjs-button-continue']") private WebElement Continue;
	@FindBy(xpath="//h1[@data-test='travel-options-page-title']") private WebElement TravelOptionsHeading;
	By rates=By.xpath("//input[@type='radio']");
	By OUTrates=By.xpath("//div[@data-test='train-results-container-OUTWARD']//input[@type='radio']");
	By INWARDrates=By.xpath("//div[@data-test='train-results-container-INWARD']//input[@type='radio']");
	By RETURNrates=By.xpath("//div[@data-test='return-tickets']//input[@type='radio']");
	By totalPrice=By.xpath("//span[@data-test='cjs-price']/span");
	By selectedTrainCost=By.xpath("./../../div[@data-test='alternative-price']//span[contains(text(),'.')]");
	By selectedReturnTrainCost=By.xpath("./../../../../../../../../..//span[contains(text(),'.')]");
	static String ticketPrice;
	float cost=0;
	
	//function to select a train, submit the selection and continue to travel options page 
	public void selectATrain()
	{
		logger.info("Randomly selecting one of the available trains");
		/*finds all the listed timings and selects one out of them randomly,
		 *  depending on the journey type selects outward train and inward train
		 *  Also calculate the total cost from the selected trains and check if that is 
		 *  same as the cost displayed in the basket  
		 */
		List<WebElement> listOfTrains = null;
		if(StartBooking.journeyType.equals("SINGLE"))
			listOfTrains=driver.findElements(OUTrates);
		if(StartBooking.journeyType.equals("RETURN"))
		{	
			listOfTrains=driver.findElements(OUTrates);
			WebElement trainOfChoice=listOfTrains.get(new Random().nextInt(listOfTrains.size()));
			moveToElement(trainOfChoice).click();
			cost=Float.parseFloat(trainOfChoice.findElement(selectedTrainCost).getText().substring(1));
			listOfTrains=driver.findElements(INWARDrates);
		}
		else if(StartBooking.journeyType.equals("OPENRETURN"))
			listOfTrains=driver.findElements(RETURNrates);
		
		WebElement trainOfChoice=listOfTrains.get(new Random().nextInt(listOfTrains.size()));
		moveToElement(trainOfChoice).click();
		if(StartBooking.journeyType.equals("OPENRETURN"))
			cost=cost+Float.parseFloat(trainOfChoice.findElement(selectedReturnTrainCost).getText().substring(1));
		else
			cost=cost+Float.parseFloat(trainOfChoice.findElement(selectedTrainCost).getText().substring(1));
		
		String expectedValue=new DecimalFormat("0.00").format(cost);
		
		/*once the train is selected and the cost before adding the booking fee is displayed,		 
		 *  the value is captured in 'ticketPrice' variable to verify the cost in following pages.
		 */
		ticketPrice=driver.findElement(totalPrice).getText().substring(1);
		
		Assert.assertTrue("Total cost displayed in basket does not correspond to the trains selected, Expected : "+expectedValue+",but Found : "+ticketPrice, 
				ticketPrice.equals(expectedValue));
		logger.info("Validation : Total cost displayed in basket corresponds to the trains selected");
		
		//Continue to the next page and verifies if the page is loaded 
		logger.info("Submiting the selection");
		try{Continue.click();}
		catch(ElementClickInterceptedException e)
		{	clickElement(moveToElement(Continue));		}
	
		Assert.assertTrue("Could not navigate to Travel Options page", waitForVisibility(TravelOptionsHeading,10,"Could not navigate to Travel Options page")!=null);
		logger.info("Travel Options page launched");
	}

}
