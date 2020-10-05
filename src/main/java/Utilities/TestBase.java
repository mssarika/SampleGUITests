package Utilities;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import Pages.BasePage;

public class TestBase{

private static TestSetup setup=new TestSetup();	
protected static Logger logger=null;
protected static WebDriver driver=null;
public static String testname;
	
	@BeforeClass
	public static void setUpLogger()
	{
		//initialising logger
		setup.initialiseLogger();
		logger=Logger.getLogger(TestSetup.class);
	}
	
	@Before
	public void initialiseTest()
	{		
		logger.info("Initialising the test");
		//loading properties file
		setup.loadPropertiesFile();
		//getting the URL under test from Properties file
		String url=setup.fetchProperty("testURL");
		//choosing a browser (Chrome/Firefox) randomly
		setup.initialiseDriver();
		driver=TestSetup.getDriver();
		logger.info("Test initialisation complete");
		logger.info("########################################################################################################");
		//loading the URL under test
		BasePage bpage=PageFactory.initElements(driver, BasePage.class);
		bpage.launchURL(url);
	}
	
	@After
	public void testTearDown()
	{	
		logger.info("########################################################################################################");
		logger.info("Test tearing down");	
		//taking a final screenshot in case the test terminates due to Assertion error and closing the browser
		new BasePage().takeScreenshot("FinalScreen");
		setup.closeDriver();		
		logger.info("Test tear down complete");
	}
}
