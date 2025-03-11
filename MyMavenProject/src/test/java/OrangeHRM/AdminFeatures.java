package OrangeHRM;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

public class AdminFeatures {

	WebDriver driver;
	UtilityHRM uhrm;
	String url= "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";
	String uname= "Admin";
	String pword= "admin123";
	String tcName;
	String tcDesc;
	int statusFlag;
	//Initaialising extended report
	ExtentSparkReporter htmlreport; 
	ExtentReports report; 
	ExtentTest test; 
	String extentreportpath = System.getProperty("user.dir") + "\\Reports\\OHRM_Admin_Report.html"; 
	

	@Test (priority = 1)
	public void LeftMenuRead() throws InterruptedException {
		//Finding the options on left menu
		System.out.println("***** Scenario to count and list leftmenu options ******");
		tcName= "TC1";
		tcDesc= "Scenario to count and list leftmenu options";
		List <WebElement> controls = uhrm.FindControlsByXPATH("//*[@id=\"app\"]/div[1]/div[1]/aside/nav/div[2]/ul/li/a");
		//int num= controls.size();
		System.out.println("No:of options in menu is " + controls.size());
		Assert.assertEquals(controls.size(),12);
		statusFlag= 1;
		System.out.println("Left Menu options are:"); 
		for (WebElement i : controls) {
			System.out.println(i.getText()); 
		}

		//click on Admin option
		uhrm.ClickByLinkText(uname);
		Thread.sleep(2000);
		System.out.println("Executed @Test1");
	}

	@Test (priority = 2)
	public void SearchByName() throws InterruptedException {
		System.out.println("***** Scenario to count users- Searchby name ******");
		tcName= "TS2";
		tcDesc= "Scenario to count users- Searchby name";
		// Find username and type "Admin"
		WebElement control= uhrm.FindControlByXPATH ("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div[1]/div[2]/form/div[1]/div/div[1]/div/div[2]/input");
		uhrm.InputTextField(control,uname);

		Thread.sleep(2000);

		// Find Search button and search
		WebElement search= uhrm.FindElementBycSSselector("button[type=submit]");
		uhrm.ClickElement(search);
		List <WebElement> controls = uhrm.FindControlsByXPATH("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div[2]/div[3]/div/div[2]/div/div/div[2]/div");
		System.out.println("Total no:of records found: " + controls.size());

		//Reading usernames listed
		System.out.println("UserNames listed are: ");
		for(int i=0 ; i<controls.size(); i++) {
			System.out.println(controls.get(i).getText());
			Assert.assertTrue(controls.get(i).getText().contains(uname), "Records otherthan Admin listed");
			statusFlag= 1;
		}

		// Reading total records display
		WebElement recordnum = uhrm.FindControlByXPATH("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div[2]/div[2]/div/span");
		System.out.println("SearchBy name " + recordnum.getText());
		//System.out.println("Executed @Test2");
	}

	@Test (priority = 3)
	public void SearchByRole() throws InterruptedException {
		
		System.out.println("***** Scenario to count users- Searchby role ******");
		tcName= "TS3";
		tcDesc= "Scenario to count users- Searchby role";
		// Find userrole by selecting "Admin"
		WebElement control = uhrm.FindControlByXPATH("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div[1]/div[2]/form/div[1]/div/div[2]/div/div[2]/div/div/div[2]/i");
		uhrm.ClickElement(control);
		Thread.sleep(2000);
		WebElement option = uhrm.FindControlByXPATH("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div[1]/div[2]/form/div[1]/div/div[2]/div/div[2]/div/div[2]/div[2]");
		uhrm.ClickElement(option);

		Thread.sleep(2000);	 

		// Find Search button and search
		WebElement search= uhrm.FindElementBycSSselector("button[type=submit]");
		uhrm.ClickElement(search);
		Thread.sleep(3000);
		
		// Reading total records display
		WebElement recordnum = uhrm.FindControlByXPATH("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div[2]/div[2]/div/span");
		System.out.println("SearchBy type " + recordnum.getText());

		statusFlag= 1;
		//System.out.println("Executed @Test3");
	}
	
	@Test (priority = 4)
	public void SearchByStatus() throws InterruptedException {
		System.out.println("***** Scenario to count users- Searchby status ******");
		tcName= "TS4";
		tcDesc= "Scenario to count users- Searchby status";
		// Find userrole by selecting "Admin"
		WebElement control = uhrm.FindControlByXPATH("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div[1]/div[2]/form/div[1]/div/div[4]/div/div[2]/div/div/div[2]/i");
		uhrm.ClickElement(control);
		Thread.sleep(2000);
		//find expath for enabled and pass
		WebElement option = uhrm.FindControlByXPATH("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div[1]/div[2]/form/div[1]/div/div[4]/div/div[2]/div/div[2]/div[2]/span");
		uhrm.ClickElement(option);

		Thread.sleep(2000);	 

		// Find Search button and search
		WebElement search= uhrm.FindElementBycSSselector("button[type=submit]");
		uhrm.ClickElement(search);
		Thread.sleep(3000);

		// Reading total records display
		WebElement recordnum = uhrm.FindControlByXPATH("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div[2]/div[2]/div/span");
		System.out.println("SearchBy type " + recordnum.getText());
		
		//Assert			
		statusFlag= 1;
		//System.out.println("Executed @Test4");
	}
		

	@AfterMethod
	public void afterMethod() throws IOException {
		System.out.println("");
		
		//Taking screeshot
		String ssFileName= tcName+"AdminFeatures-screenshot.jpeg";
		uhrm.PageScreenShot(ssFileName,"HRMAdminscreenshots");
		
		test = report.createTest(tcName + " " + tcDesc);
		if(statusFlag== 1) {
			test.log(Status.PASS, MarkupHelper.createLabel(tcName + " : " + "Login pass for " + tcDesc, ExtentColor.GREEN));
			statusFlag= 0;
		} else {
			test.log(Status.FAIL, MarkupHelper.createLabel(tcName + " : " + "Login pass for " + tcDesc, ExtentColor.RED));	
		}
		// Page refresh
		uhrm.RefreshPage();
	}

	@BeforeTest
	public void beforeTest() throws InterruptedException {
		
		//creating driver
		driver= new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		//Creating object of class UtilityHRM
		//**********************************
		uhrm= new UtilityHRM(driver);

		//Launch website 
		uhrm.LaunchURL(url);


		// Login with username and password
		WebElement username= uhrm.FindUserName();
		uhrm.InputTextField(username, uname);
		WebElement password= uhrm.FindPassword();
		uhrm.InputTextField(password, pword);
		WebElement login= uhrm.FindLoginButton();
		uhrm.ClickElement(login);
		Thread.sleep(2000);

		//Confirm the login is successful
		String url= uhrm.FindPresentURL();
		Assert.assertTrue(url.contains("dashboard"),"Login to OrangeHRM failed");
		System.out.println("Login is successful");
		
		// Customised report creation setup
		//**********************************
		// Creating an object for report
		report= uhrm.CustomReportSetup(htmlreport,report,extentreportpath);

	}

	@AfterTest
	public void afterTest() {
		report.flush();
		//Logout from the site
		uhrm.UserLogout();
		
		//closing driver
		driver.quit();
	}  

}
