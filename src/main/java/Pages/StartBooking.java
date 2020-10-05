package Pages;

import java.text.SimpleDateFormat;
import java.util.Calendar;


import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StartBooking extends BasePage{
	
	@FindBy(id="from.search") private WebElement origin;
	@FindBy(id="to.search") private WebElement destination;
	@FindBy(id="single") private WebElement oneWay;
	@FindBy(id="return") private WebElement returnTrip;
	@FindBy(id="openReturn") private WebElement openreturnTrip;
	@FindBy(id="page.journeySearchForm.outbound.title") private WebElement outdate;
	@FindBy(id="page.journeySearchForm.inbound.title") private WebElement returndate;
	@FindBy(xpath="//fieldset[@data-test='outbound-datepicker']//select[@name='hours']") private WebElement out_hour;
	@FindBy(xpath="//fieldset[@data-test='outbound-datepicker']//select[@name='minutes']") private WebElement out_minutes;	
	@FindBy(xpath="//fieldset[@data-test='inbound-datepicker']//select[@name='hours']") private WebElement return_hour;
	@FindBy(xpath="//fieldset[@data-test='inbound-datepicker']//select[@name='minutes']") private WebElement return_minutes;	
	@FindBy(xpath="//button[@data-test='submit-journey-search-button']") private WebElement getTickets;	
	@FindBy(xpath="//button[@data-test='cjs-button-continue']") private WebElement Continue;
	@FindBy(xpath="//a[contains(@id,'page.journeySearchForm.outbound.title')]") private WebElement dateDropdown;
	
	WebDriverWait wait=new WebDriverWait(driver,10);
	
	Actions act=new Actions(driver);
	
	static String departure;
	static String arrival;
	static String journeyDate;
	static String journeyReturnDate;
	static String journeyType;
	
	//function to populate all the search criteria
	public void enterSearchCriteria(String station1, String station2, String type)
	{		
		departure=station1;
		arrival=station2;
		journeyType=type;
		//Entering the search criteria Origin, Destination, Journey type, Date and Time
		logger.info("Entering the search criteria");
		enterStationName("from",station1);
		enterStationName("to",station2);
		selectTypeOfJourney(type);
		enterDateAndTime();
		//Submitting the details and verifying if Train timings are retrieved
		logger.info("Submitting the search details");
		moveToElement(getTickets).click();
		Assert.assertTrue("No train timings retrieved", waitForVisibility(Continue,10,"Search was not successful")!=null);
		logger.info("Train timings successfully retrieved");
	}
	
	//function to Enter the origin and destination
	private void enterStationName(String fromORto,String station)
	{
		if(fromORto.equals("from"))
		{
			logger.info("Entering 'From' field: "+station);
			origin.sendKeys(station);
			origin.sendKeys(Keys.ENTER);
		}
		else if(fromORto.equals("to"))
		{
			logger.info("Entering 'To' field: "+station);
			destination.sendKeys(station);
			destination.sendKeys(Keys.ENTER);
		}
	}
	
	//Function to select the journey types
	private void selectTypeOfJourney(String type)
	{
		logger.info("Selecting Journey Type: "+type);
		if(type.equals("SINGLE"))	
			oneWay.click();
		else if(type.equals("RETURN"))
			returnTrip.click();
		else if(type.equals("OPENRETURN"))
			openreturnTrip.click();		
	}
	
	/*this function finds a future date, 10 days ahead of the current date and 
	 * selects the date from the dropdown menu and selects time 10:30 for the OUT journey 
	 *and 11:30 for the INWARD journey if the journey type is RETURN
	 */
	private void enterDateAndTime()
	{
		outdate.click();
		journeyDate=getFutureDate(10);
		logger.info("Selecting Date : "+journeyDate);
		String date=journeyDate.split("-")[0];
		String month=journeyDate.split("-")[1];
		
		WebElement chosenDate=driver.findElement(By.id("page.journeySearchForm.outbound.title"+month+"-"+date));		
		clickElement(moveToElement(chosenDate));
		logger.info("Selecting Time : 10:30");
		setTime("10","30",out_hour,out_minutes);
		//entering the return details in case of Return journey type
		if(journeyType.equals("RETURN"))
		{
			returndate.click();
			journeyReturnDate=getFutureDate(11);
			logger.info("Selecting Date : "+journeyReturnDate);
			date=journeyReturnDate.split("-")[0];
			month=journeyReturnDate.split("-")[1];
			chosenDate=driver.findElement(By.id("page.journeySearchForm.inbound.title"+month+"-"+date));		
			clickElement(moveToElement(chosenDate));
			logger.info("Selecting Time : 11:30");
			setTime("11","30",return_hour,return_minutes);
		}
	}		

	//function to select the hour and minutes fields from the drop down menu
	private void setTime(String hour_value, String min_value,WebElement hour, WebElement minutes)
	{
		Select hourDropDown=new Select(wait.until(ExpectedConditions.elementToBeClickable(hour)));
		hourDropDown.selectByVisibleText(hour_value);
		Select minDropDown=new Select(wait.until(ExpectedConditions.elementToBeClickable(minutes)));
		minDropDown.selectByVisibleText(min_value);
	}
	
	//function to find a future date which is ahead of current date by parameter value passed
	private String getFutureDate(int add)
	{
		String futureDate;
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, add);		
		SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yy");
		futureDate=dateFormat.format(cal.getTime());
		
		return(futureDate);
	}
}
