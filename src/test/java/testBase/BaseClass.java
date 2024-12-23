package testBase;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.openqa.selenium.remote.DesiredCapabilities; //SeleniumGrid
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.Platform; //SeleniumGrid
import org.apache.logging.log4j.LogManager; //log4j
import org.apache.logging.log4j.Logger; //log4j
import java.net.URL;

public class BaseClass {

public static WebDriver driver; //we do static as we create obj of base class in extent report so same driver refer in obj
public Logger logger;
public Properties p;

	@BeforeClass (groups = { "Sanity", "Regression", "Master" })
	@Parameters({"os","browser"})
	public void setup(String os, String br) throws IOException 
	{
		//loading the properties file
		FileReader file=new FileReader(".//src//test//resources//config.properties");
		p=new Properties();
		p.load(file);
		
		//Loading log4j
		logger=LogManager.getLogger(this.getClass()); //log4J
		
		if(p.getProperty("execution_env").equalsIgnoreCase("remote")) // To run in Selenium Grid server--> Start server file by Cmd in system
		{
			DesiredCapabilities capabilities=new DesiredCapabilities();
		    URL gridUrl = new URL("http://localhost:4444/wd/hub");

			//os
			if(os.equalsIgnoreCase("windows"))
			{
				capabilities.setPlatform(Platform.WIN11);
			}
			else if (os.equalsIgnoreCase("mac"))
			{
				capabilities.setPlatform(Platform.MAC);
			}
			else
			{
				System.out.println("No matching os");
				return;
			}
			
			//browser
			switch(br.toLowerCase())
			{
			case "chrome": capabilities.setBrowserName("chrome"); break;
			case "edge": capabilities.setBrowserName("MicrosoftEdge"); break;
			default: System.out.println("No matching browser"); return;
			}
			
			driver=new RemoteWebDriver(gridUrl,capabilities); // Define the URL for the Selenium Grid hub
		}
		
				
		if(p.getProperty("execution_env").equalsIgnoreCase("local")) // run in Local system
		{

			switch(br.toLowerCase())
			{
			case "chrome" : driver=new ChromeDriver(); break;
			case "edge" : driver=new EdgeDriver(); break;
			case "firefox": driver=new FirefoxDriver(); break;
			default : System.out.println("Invalid browser name.."); return;
			}
		}

		
		
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(120));
		driver.get(p.getProperty("appURL")); // get url from config.properties file
		driver.manage().window().maximize();
	}

	@AfterClass (groups= {"Sanity","Regression","Master"})
	public void tearDown() // teardown method is used to clean up after a test has finished.
	{
		driver.quit();
	}

	
/*	public String randomeString() { 
	  String generatedString=RandomStringUtils.randomAlphabetic(5); 
	  return generatedString; }
	  
	public String randomeNumber() { 
	  String generatedString=RandomStringUtils.randomNumeric(10); 
	  return generatedString; }
	  
	public String randomAlphaNumeric() { 
	  String str=RandomStringUtils.randomAlphabetic(3); 
	  String num=RandomStringUtils.randomNumeric(3);
	  return (str+"@"+num); }  */
	 

	// Use SecureRandom predefine class
	private static final SecureRandom secureRandom = new SecureRandom();

	// Method to generate a random numerical string (digits only)
	public String randomeNumber(int length) {
	  StringBuilder numericString = new StringBuilder(length);
	    for (int i = 0; i < length; i++) {
	    	//Generate a random number between 0 and 9	
			  numericString.append(secureRandom.nextInt(10)); 
		}
		return numericString.toString();
    }
	// Method to generate a random alphabetical string (letters only)
	public String randomString(int length) {
	  StringBuilder alphabeticString = new StringBuilder(length);
	   for (int i = 0; i < length; i++) {
		  //Generate a random number between 0 and 51, then map it to a letter (A-Z or a-z)
			int randomChar = secureRandom.nextInt(52); // 52 letters (26 uppercase + 26 lowercase)
			char charToAppend;

			if (randomChar < 26) {
				charToAppend = (char) ('A' + randomChar); // Uppercase letters (A-Z)
			} else {
				charToAppend = (char) ('a' + randomChar - 26); // Lowercase letters (a-z)
			}

			alphabeticString.append(charToAppend);
		}
		return alphabeticString.toString();
	}

	// Method to generate a random alphanumeric string (letters and digits)
	public String randomAlphaNumeric(int length) {
		StringBuilder alphanumericString = new StringBuilder(length);
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

		for (int i = 0; i < length; i++) {
			// Generate a random index to pick a character from the alphanumeric set
			int randomIndex = secureRandom.nextInt(characters.length());
			alphanumericString.append(characters.charAt(randomIndex));
		}
		return alphanumericString.toString();
	}
	
	
	public String captureScreen(String tname) throws IOException {

		String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
				
		TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
		File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
		String targetFilePath=System.getProperty("user.dir")+"\\screenshots\\" + tname + "_" + timeStamp + ".png";
		
		//FileUtils.copyFile(sourceFile, new File(targetFilePath)); //Also write in 1 line of below 2 line
		File targetFile=new File(targetFilePath);
		sourceFile.renameTo(targetFile);

		return targetFilePath;
	}

}
