package FunctionalTests;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.openqa.selenium.support.PageFactory;

import Pages.DeliveryOption;
import Pages.Payment;
import Pages.SelectTiming;
import Pages.StartBooking;
import Pages.TravelOptions;
import Utilities.TestBase;

public class GetTicketsTests extends TestBase{
	
	@Rule
	public final TestName test = new TestName();
	
	//Testing functionality Getting Tickets for 'OneWay' trip
	@Test
	public void book_OneWay_Ticket()
	{
		logger.info("Test Functionality --> Getting Tickets for 'OneWay' trip");
		testname=test.getMethodName();
		
		//Entering the search criteria for One way journey type
		StartBooking search=PageFactory.initElements(driver,StartBooking.class);
		search.enterSearchCriteria("London Waterloo", "Aber", "SINGLE");
		
		//Selecting a train
		SelectTiming selectTime=PageFactory.initElements(driver,SelectTiming.class);
		selectTime.selectATrain();
		
		//choosing travel options
		TravelOptions travelOptions=PageFactory.initElements(driver,TravelOptions.class);
		travelOptions.selectTravelOptions();
		
		//selecting a delivery option for ticket and checking out as guest
		DeliveryOption deliveryOptions=PageFactory.initElements(driver,DeliveryOption.class);
		deliveryOptions.selectDeliveryOption();
		
		//validating the ticket details in Payment page. Test terminates as card details are asked
		Payment payment=PageFactory.initElements(driver,Payment.class);
		payment.validateJourneyDetails();		
	}
	
	//Testing functionality Getting Tickets for 'Return' trip
	@Test
	public void book_Return_Ticket()
	{
		logger.info("Test Functionality --> Getting Tickets for 'Return' trip");
		testname=test.getMethodName();
		
		//Entering the search criteria for Return journey type
		StartBooking search=PageFactory.initElements(driver,StartBooking.class);
		search.enterSearchCriteria("London Waterloo", "Aber", "RETURN");
		
		//Selecting a train
		SelectTiming selectTime=PageFactory.initElements(driver,SelectTiming.class);
		selectTime.selectATrain();
		
		//choosing travel options
		TravelOptions travelOptions=PageFactory.initElements(driver,TravelOptions.class);
		travelOptions.selectTravelOptions();
		
		//selecting a delivery option for ticket and checking out as guest
		DeliveryOption deliveryOptions=PageFactory.initElements(driver,DeliveryOption.class);
		deliveryOptions.selectDeliveryOption();
		
		//validating the ticket details in Payment page. Test terminates as card details are asked
		Payment payment=PageFactory.initElements(driver,Payment.class);
		payment.validateJourneyDetails();		
	}
	
	//Testing functionality Getting Tickets for 'OpenReturn' trip
	@Test
	public void book_OpenReturn_Ticket()
	{
		logger.info("Test Functionality --> Getting Tickets for 'Open Return' trip");
		testname=test.getMethodName();
		
		//Entering the search criteria for Open Return journey type
		StartBooking search=PageFactory.initElements(driver,StartBooking.class);
		search.enterSearchCriteria("London Waterloo", "Aber", "OPENRETURN");
		
		//Selecting a train
		SelectTiming selectTime=PageFactory.initElements(driver,SelectTiming.class);
		selectTime.selectATrain();
		
		//choosing travel options
		TravelOptions travelOptions=PageFactory.initElements(driver,TravelOptions.class);
		travelOptions.selectTravelOptions();
		
		//selecting a delivery option for ticket and checking out as guest
		DeliveryOption deliveryOptions=PageFactory.initElements(driver,DeliveryOption.class);
		deliveryOptions.selectDeliveryOption();
		
		//validating the ticket details in Payment page. Test terminates as card details are asked
		Payment payment=PageFactory.initElements(driver,Payment.class);
		payment.validateJourneyDetails();		
	}
}