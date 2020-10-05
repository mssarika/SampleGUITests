package Utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


public class TestSetup {
	
	private static WebDriver driver=null;
	private static Properties configProp=null;	
	private static Logger logger=null;
	private static ConsoleAppender console=new ConsoleAppender(new PatternLayout("%d{HH:mm:ss} %-25F [%-5p] : %m%n"));
	
	//initialising logger
	void initialiseLogger()
	{
		if(logger==null)
		{
			console.setThreshold(Level.DEBUG);
			logger=Logger.getLogger(TestSetup.class);
			logger.addAppender(console);
		}	
	}	
	
	//Initialising the driver
	void initialiseDriver() 
	{
		if(driver==null)
		{
			logger.info("Setting up driver");
		 
			//randomly select browser for test
			String browser;
			
			if(new Random().nextInt(2)==0)
				browser="Chrome";
			else
				browser="Firefox";

			logger.info("Randomly selected browser : "+browser);
				
			if(browser.equals("Chrome"))
			{
				System.setProperty("webdriver.chrome.driver","src\\main\\resources\\Drivers\\chromedriver.exe");
				setDriver(new ChromeDriver());
			}
			else if(browser.equals("Firefox"))
			{
				System.setProperty("webdriver.gecko.driver","src\\main\\resources\\Drivers\\geckodriver.exe");
				setDriver(new FirefoxDriver());
			}		
		}

		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	
	}
	
	public static WebDriver getDriver() {
			return driver;
	}

	private static void setDriver(WebDriver driver) {
			TestSetup.driver = driver;
	}
	
	//function to close the browser
	void closeDriver()
	{		
		if(getDriver()!=null)
		{ 	
			getDriver().quit();
			setDriver(null);
		}
	}
	
	//function to fetch a value from properties file
	String fetchProperty(String property)
	{
		if(configProp==null)
			loadPropertiesFile();
		
		return(configProp.getProperty(property));
	}
	
	//function to load the properties file
	void loadPropertiesFile()
	{
		logger.info("Fetching the properties files");
		
		configProp=new Properties(); 
		try{	configProp.load(loadFile("Config.properties"));		} 
		catch (IOException e){	Assert.assertTrue("Cannot initialise the Config properties file, Check if the file is corrupted", false);	}			
	}

	//function to load a file
	private FileInputStream loadFile(String file)
	{
		logger.info("Fetching File : "+System.getProperty("user.dir")+"\\src\\main\\resources\\"+file);
		try 
		{
			return(new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\resources\\"+file));
		} catch (FileNotFoundException e) {				
			Assert.assertTrue(file+" file is missing", false);
			return null;
		}
	}
}
