package Pages;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Utilities.TestBase;
import Utilities.TestSetup;

public class BasePage {
	
	static int count=1;
	protected Logger logger=Logger.getLogger(TestSetup.class);
	protected WebDriver driver=TestSetup.getDriver();
	
	@FindBy(id="app") private WebElement getTicketForm;

	//function to launch the URL under test
	public void launchURL(String url)
	{
		logger.info("Launching the URL under test : "+url);
		driver.get(url);		
		Assert.assertTrue("Could not launch the test URL",waitForVisibility(getTicketForm,60,"Website did not launch")!=null);
		logger.info("Trainline site launched successfully");
	}
	
	//function to scroll the page to view an element  
	protected WebElement moveToElement(WebElement element)
	{
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		return element;
	}
	
	//function to click an element using JavaScript
	protected void clickElement(WebElement element)
	{
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", element);
	}	
	
	/*
	 * function to wait for the specified amount of time till an element is visible, 
	 * if so returns the element else returns null
	 * after logging an error if a message is passed to the function 
	 */	
	protected WebElement waitForVisibility(WebElement elem, int seconds, String msg)
	{
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		try 
		{		
			new WebDriverWait(driver,seconds).until(ExpectedConditions.visibilityOf(elem));		
		}
		catch(Exception e)
		{
			if(!msg.isEmpty())
				logError(msg);
			elem=null;		
		}
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		return elem;
	}
	
	/*
	 * function to wait for the specified amount of time till an element is clickable, 
	 * if so returns the element else returns null
	 * after logging an error if a message is passed to the function 
	 */
	protected WebElement waitToEnable(WebElement elem, int seconds, String msg)
	{
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		try 
		{		
			new WebDriverWait(driver,seconds).until(ExpectedConditions.elementToBeClickable(elem));		
		}
		catch(Exception e)
		{
			if(!msg.isEmpty())
				logError(msg);
			elem=null;		
		}
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		return elem;
	}
	
	//function to log the error with the message passed and takes a screenshot of the error
	protected void logError(String msg)
	{
		logger.error(msg);
		takeScreenshot("Error");
	}	
	
	/* function to take screenshot and copy the image to src/test/resources/Screenshots folder
	 * image name is specified as 'Error + count.png' ex:Error1, Error2 etc so that 
	 * if multiple images are taken during a test run the count would indicate which error logged 
	 * is the screenshot related to. While test tear down another screenshot is taken which would  
	 * be a supporting evidence for Assertion errors. The name of the screenshot starts with the 
	 * name of the test
	 */
	public void takeScreenshot(String name)
	{
		name=TestBase.testname+"_"+name;

		if(name.equals("Error"))
			name="Error"+(count++);
		
		File SrcFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		File resourcesDirectory = new File("src/test/resources/Screenshots");
        File DestFile=new File(resourcesDirectory.getAbsolutePath()+"/"+name+".png");
 
		try {
			Files.move(SrcFile.toPath(), DestFile.toPath(), REPLACE_EXISTING);
			logger.info("Screenshot succesfully transferred to: src/test/resources/Screenshots folder");

		} catch (IOException io) {
			logger.info("Problem transferring screenshots to proper directory");
		}
	}
}
