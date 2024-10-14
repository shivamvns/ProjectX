package endToendAutomation;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;//log4j
import org.apache.logging.log4j.Logger; 
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class OrangeHRM {

	
	public Logger logger;
    WebDriver driver;
	public Properties p; 
	@BeforeClass
	@Parameters({"os","browser"})
	void setup(String os,String br) throws InterruptedException, IOException, URISyntaxException
	
	{
		
		//Loading config.properties file
				FileReader file=new FileReader("./src//test//resources//config.properties");
				p=new Properties();
				p.load(file);
		
		logger=LogManager.getLogger(this.getClass());//Log4j
		
		logger.info("***** Starting TC001_AccountRegistrationTest  ****");
		
		
		if(p.getProperty("execution_env").equalsIgnoreCase("remote"))
		{
			DesiredCapabilities capabilities=new DesiredCapabilities();
			
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
			
			URL url= new URI("http://localhost:4444/").toURL();
			driver=new RemoteWebDriver(url,capabilities);
		}
		
				
		if(p.getProperty("execution_env").equalsIgnoreCase("local"))
		{

			switch(br.toLowerCase())
			{
			case "chrome" : driver=new ChromeDriver(); break;
			case "edge" : driver=new EdgeDriver(); break;
			case "firefox": driver=new FirefoxDriver(); break;
			default : System.out.println("Invalid browser name.."); return;
			}
		}
				
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		logger.info("Launching the website");
		driver.get(p.getProperty("appURL1"));
		driver.manage().window().maximize();
		Thread.sleep(3000);
		logger.info("Launching the website");
		
	}
	
	
	
	@Test(priority=1)
	void TC_01_testLogo()
	{
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		logger.info("Logo Assertion starts");
		boolean status=driver.findElement(By.xpath("//img[@alt='company-branding']")).isDisplayed();
		Assert.assertEquals(status, true);
		logger.info("Logo Assertion finished");
	}
	
	
	@Test(priority=2)
	void TC_02_testAppUrl()
	{
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		logger.info("URL Assertion starts");
		Assert.assertEquals(driver.getCurrentUrl(),"https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
		logger.info("URL Assertion finished");
	}
	
	
	
	@Test(priority=3)
	void TC_03_testHomePageTitle()
	{
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		logger.info("HomePage Title Assertion starts");
		Assert.assertEquals(driver.getTitle(),"OrangeHRM");
		logger.info("HomePage Title Assertion finished");
	}
	
	
	@AfterClass
	void tearDown()
	{
		
		driver.quit();
		logger.info("Quitting the browser");
		logger.info("*****  Completed Test Cases  ****");
	}
}
