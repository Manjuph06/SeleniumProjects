package OrangeHRM;

import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.AfterTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class HRMLogin {

	UtilityHRM uhrm;

	File file;
	FileInputStream fis;
	XSSFWorkbook wb;
	XSSFSheet sheet;
	XSSFRow row;
	XSSFCell cell;	
	WebDriver driver;

	//Initaialising extended report
	ExtentSparkReporter htmlreport; 
	ExtentReports report; 
	ExtentTest test; 
	String extentreportpath = System.getProperty("user.dir") + "\\Reports\\OHRM_Login_Report.html"; 
	String tcName;
	String tcDesc;
	String expectedStatus;
	
	@DataProvider (name= "TestData")
	public String[][] loginData() { 
		int totalrows = sheet.getPhysicalNumberOfRows(); 
		String[][] tData = new String[totalrows - 1][5]; 
		System.out.println("Inside DATA PROVIDER");
		for (int i = 0; i < totalrows - 1; i++) { 
			row = sheet.getRow(i + 1); 
			for (int j = 0; j < 5; j++) { 
				cell = row.getCell(j); 
				tData[i][j] = cell.getStringCellValue(); 

				System.out.println("Values " + i + j + tData[i][j]);
			} 
		} 
		return tData;
	}

	@Test (dataProvider= "TestData" )
	public void userLogin(String tcname, String desc, String uname, String pword, String status ) throws InterruptedException, IOException {
		System.out.println("I am inside @Test");
		System.out.println("values: " + tcname + desc + uname + pword + status);
		tcName= tcname;
		tcDesc= desc;
		expectedStatus= status;

		// Login with username and password
		WebElement username= uhrm.FindUserName();
		uhrm.InputTextField(username, uname);
		WebElement password= uhrm.FindPassword();
		uhrm.InputTextField(password, pword);
		WebElement login= uhrm.FindLoginButton();
		uhrm.ClickElement(login);
		Thread.sleep(2000);

		System.out.println("Executed @Test");
	}

	@BeforeMethod(alwaysRun= true)
	public void beforeMethod() throws InterruptedException {
		driver.navigate().refresh();
		// Finding the url before test
		String urls= uhrm.FindPresentURL();
		System.out.println("Url before test "+ urls);
		Thread.sleep(2000);
		System.out.println("Executed @BeforeMethod");	
	}

	@AfterMethod(alwaysRun= true)
	public void afterMethod() throws IOException {

		System.out.println("Inside afterMethod, got: " + expectedStatus + tcName + tcDesc);

		String ssFileName= tcName+"HRMLogin-screenshot.jpeg";
		uhrm.PageScreenShot(ssFileName,"HRMLoginscreenshots");                

		// Finding the url after test
		String urls= uhrm.FindPresentURL();
		System.out.println("Current url "+ urls);

		if(urls.contains(expectedStatus)) {
			//Assertion to confirm login with valid cridentials
			Assert.assertTrue(urls.contains("dashboard"), "Assert Failed: TestCase FAIL");
			System.out.println("Valid cridentials, Login success");		 
			System.out.println("TestCase PASS");
			test = report.createTest(tcName + " " + tcDesc);
			test.log(Status.PASS, MarkupHelper.createLabel(tcName + " : " + "Login pass for " + tcDesc, ExtentColor.GREEN));
			// logout from url
			uhrm.UserLogout();

		} else {

			if(urls.contains("dashboard")) {
				//Assertion to confirm if logged in with invalid cridentials
				Assert.assertTrue(urls.contains("dashboard"), "Assert Failed: TestCase PASS");
				System.out.println("Invalid cridentials, but Login pass");
				System.out.println("TestCase Fail");
				test = report.createTest(tcName + " " + tcDesc);
				test.log(Status.FAIL, MarkupHelper.createLabel(tcName + " : " + "Login fail for " + tcDesc, ExtentColor.RED));
				// logout from url
				uhrm.UserLogout();
			} else {
				//Assertion to confirm login fails with invalid cridentials
				Assert.assertFalse(urls.contains("dashboard"), "Assert Failed: TestCase FAIL");
				System.out.println("Invalid cridentials, Login fail");
				System.out.println("TestCase PASS"); 
				test = report.createTest(tcName + " " + tcDesc);
				test.log(Status.PASS, MarkupHelper.createLabel(tcName + " : " + "Login fail for " + tcDesc, ExtentColor.RED));
			}
		}



	}


	@BeforeTest(alwaysRun= true)
	public void beforeTest() throws IOException {

		//Creating a driver
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5000));
		
		//Creating object of class UtilityHRM
		//**********************************
		uhrm= new UtilityHRM(driver);
		
		// Launching URL
		//***************
		// Open browser and launch website
		uhrm.LaunchURL("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");


		// Excel File reading setup
		//**************************
		String fileName= System.getProperty("user.dir")+"\\src\\test\\java\\OrangeHRM\\Excel_OHRM.xlsx";
		System.out.println("File path is " + fileName);
		file= new File(fileName);
		fis= new FileInputStream(file);
		wb= new XSSFWorkbook(fis);
		sheet= wb.getSheet("SheetOHRM");
		System.out.println("Executed @BeforeTest");	

		// Customised report creation setup
		//**********************************
		// Creating an object for report
		report= uhrm.CustomReportSetup(htmlreport,report,extentreportpath);

	}

	@AfterTest(alwaysRun= true)
	public void afterTest() throws IOException  {
		fis.close();
		wb.close();
		report.flush();
		//Logout from the site
		uhrm.UserLogout();
		
		//close driver
		driver.quit();
	}


}
