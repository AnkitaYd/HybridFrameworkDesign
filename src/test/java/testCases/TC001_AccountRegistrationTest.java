package testCases;
import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;
import testBase.BaseClass;

public class TC001_AccountRegistrationTest extends BaseClass {

	@Test (groups= {"Regression","Master"}) //Step8 groups added
	public void verify_account_registration()
	{
		logger.info("*****Starting TC001_AccountRegistrationTest******");
		
	try {	
		HomePage hp=new HomePage(driver);
		hp.clickMyAccount();
		logger.info("Clicked on MyAccount Link..");
		logger.debug("This is a debug log message");

		
		hp.clickRegister();
		logger.info("Clicked on Register Link..");

		AccountRegistrationPage regpage=new AccountRegistrationPage(driver);
		
		logger.info("Providing Customer details...");
		//Use RandomStringUtils
	/*	regpage.setFirstName(randomString().toUpperCase());
		regpage.setLastName(randomString().toUpperCase());
		regpage.setEmail(randomString()+"@gmail.com");// randomly generated the email
		regpage.setTelephone(randomeNumber());
		
	    String password=randomAlphaNumeric(); //store in password var. to match both set/confirm password
		regpage.setPassword(password);
		regpage.setConfirmPassword(password); */
		
		//Use SecureRandom predefine class
		regpage.setFirstName(randomString(5).toUpperCase());
		regpage.setLastName(randomString(5).toUpperCase());
		regpage.setEmail(randomString(5)+"@gmail.com");// randomly generated the email
		regpage.setTelephone(randomeNumber(10));
		
	    String password=randomAlphaNumeric(10); 
		regpage.setPassword(password);
		regpage.setConfirmPassword(password);
		
		
		regpage.setPrivacyPolicy();
		regpage.clickContinue();
		
		logger.info("Validating expected message...");
		String confmsg = regpage.getConfirmationMsg();
		Assert.assertEquals(confmsg, "Your Account Has Been Created!", "Confirmation message mismatch");

		logger.info("Test passed");
		} 
	
		catch (Exception e)
		{
			Assert.fail();
		} 
	
		logger.info("***** Finished TC001_AccountRegistrationTest *****");
		
	}	
}
