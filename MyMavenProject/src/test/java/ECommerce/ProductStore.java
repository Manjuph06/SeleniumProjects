package ECommerce;

import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import ECommerce.UtilityECommerce;

import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.apache.commons.collections4.bag.SynchronizedSortedBag;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

public class ProductStore {

	WebDriver driver;
	UtilityECommerce ucomm;	
	WebElement prod;
	String tcName;
	String tcDesc;
	String alertmsg;
	int statusFlag=0;
	
	//Initaialising extended report
	ExtentSparkReporter htmlreport; 
	ExtentReports report; 
	ExtentTest test; 
	String extentreportpath = System.getProperty("user.dir") + "\\Reports\\ECommerce_demoblaze_Report.html"; 
	
  
  @Test (priority=1)
  public void SignUp() throws InterruptedException {
	  tcName= "TC1";
	  tcDesc= "SignUp";
	//Sign up or Login user
	String alertmessage= ucomm.SignInUser("signin2","sign-username","manju2@gmail.com","sign-password", "manju1","button[type=\"button\"][onclick=\"register()\"]","//*[@id=\"signInModal\"]/div/div/div[3]/button[1]"); 
	
	if(alertmessage.contains("user already exist") || alertmessage.contains("Sign up successful") ) {	
		System.out.println("SignUp Testcase Passed");
		statusFlag= 1;
	} else {
		System.out.println("SignUp Testcase Failed");
	}
  }
  
  @Test (priority=2)
  public void LogIn() throws InterruptedException {
	tcName= "TC2";
	tcDesc= "LogIn";
	//Sign up or Login user
	String alertmessage= ucomm.SignInUser("login2","loginusername","manjus@gmail.com","loginpassword", "manju1", "button[type=\"button\"][onclick=\"logIn()\"]", "NA");
	
	if(alertmessage.contains("User does not exist") || alertmessage.contains("Wrong password")) {	
		System.out.println("LogIn Testcase Failed");
	} 
	
	WebElement loginstatus= driver.findElement(By.id("nameofuser"));
	if(loginstatus.getText().contains("Welcome")) {
		System.out.println("LogIn Testcase Passed");
		statusFlag= 1;
	}
	
  }
   
  
  @Test (priority=3)
  public void ViewFeaturedPromotions() throws IOException, InterruptedException {
	  tcName= "TC3";
	  tcDesc= "ViewFeaturedProducts";

	  List <WebElement> prod= driver.findElements(By.cssSelector("[alt$=\"slide\"]"));
	  System.out.println("Featured products listed are: ");
	  for(WebElement p : prod) {
		  System.out.println(p.getAttribute("src"));

	  }
	  statusFlag= 1;

	  //Taking screeshot
	  String ssFileName= tcName+"DemoBlaze-screenshot.jpeg";
	  ucomm.PageScreenShot(ssFileName,"DemoBlazescreenshots");
	  Thread.sleep(2000);
  }
  
  @DataProvider
  public String[][] prodcategories() {
    return new String[][] {
      new String[] { "Phones"},
      new String[] { "Laptops"},
      new String[] { "Monitors"}
    };
  }
  
  @Test (priority= 4,dataProvider = "prodcategories")
  public void ProductBrowsing(String category) throws InterruptedException, IOException {
	  tcName= "TC4";
	  tcDesc= "ProductBrowsing-"+category;
	  driver.findElement(By.linkText(category)).click();
	  Thread.sleep(2000);
	  List <WebElement> products= driver.findElements(By.xpath("//*[@id=\"tbodyid\"]/div"));  
	  System.out.println("Products under category "+ category + " :");
	  List<WebElement> rows= driver.findElements(By.xpath("//*[@id=\"tbodyid\"]/div/div"));
	  
	  int flag=0;
	  System.out.println("Following products have $790 ");
	  for(WebElement itemdetail : rows) {
		    
		  //Filtering products cost $790 only
		  if( itemdetail.getText().contains("$790")) {
			  System.out.println(itemdetail.getText());
			  flag= 1;
		  } 
	
	  }
	  
	  if(flag==0) {
		  System.out.println("No products listed");
	  }
	  
	  System.out.println("Testcase ProductBrowing for "+ category + " Passed");
	  statusFlag= 1;
	  
	//Taking screeshot
	  String ssFileName= tcName+"DemoBlaze-screenshot.jpeg";
	  ucomm.PageScreenShot(ssFileName,"DemoBlazescreenshots");
	  Thread.sleep(2000);
	  
	  System.out.println();
	  System.out.println();
	  Thread.sleep(4000);
	  driver.navigate().refresh();
  }
  
  
  @Test (priority= 5)
  public void ViewProductDetails() throws InterruptedException, IOException {
	  tcName= "TC5";
	  tcDesc= "ViewProductDetails";
	  //Thread.sleep(2000);
	  prod= ucomm.findProduct(2);
	  Thread.sleep(2000);
	  
	  System.out.println("Product details are:");
	  ucomm.getProdDetails(prod);
	  
	  ucomm.clickProduct(prod);
	  Thread.sleep(3000);
	
	  alertmsg= ucomm.addToCart("//*[@id=\"tbodyid\"]/div[2]/div/a") ;
	  System.out.println("Alert msg: " + alertmsg);
	 
	  List <WebElement> dellinks= ucomm.viewCart();
	  statusFlag= 1;
	  
	  //Taking screeshot
	  String ssFileName= tcName+"DemoBlaze-screenshot.jpeg";
	  ucomm.PageScreenShot(ssFileName,"DemoBlazescreenshots");
	  Thread.sleep(2000);
	  		
	  //clearing the cart
	  ucomm.deleteFromCart(dellinks);
	  
	  //Returning to Home page
	  for(int i=0 ; i<4 ; i++) {
		  driver.navigate().back();
		  Thread.sleep(1000);
	  }
  }  
	  
  @Test (priority= 6)
  public void proceedToCheckOut() throws InterruptedException, IOException {
	  tcName= "TC6";
	  tcDesc= "proceedToCheckOut";
	  //Adding 3 items to cart 
	  prod= ucomm.findProduct(3);
	  Thread.sleep(2000);
	  ucomm.clickProduct(prod);
	  Thread.sleep(3000);
	  alertmsg= ucomm.addToCart("//*[@id=\"tbodyid\"]/div[2]/div/a") ;
	  System.out.println("Alert msg: " + alertmsg);
	  
	  //Returning to Home page
	  for(int i=0 ; i<2 ; i++) {
		  driver.navigate().back();
		  Thread.sleep(1000);
	  }

	  prod= ucomm.findProduct(2);
	  Thread.sleep(2000);
	  ucomm.clickProduct(prod);
	  Thread.sleep(3000);
	  alertmsg= ucomm.addToCart("//*[@id=\"tbodyid\"]/div[2]/div/a") ;
	  System.out.println("Alert msg: " + alertmsg);
	  
	  //Returning to Home page
	  for(int i=0 ; i<2 ; i++) {
		  driver.navigate().back();
		  Thread.sleep(1000);
	  }

	  prod= ucomm.findProduct(3);
	  Thread.sleep(2000);
	  ucomm.clickProduct(prod);
	  Thread.sleep(3000);
	  alertmsg= ucomm.addToCart("//*[@id=\"tbodyid\"]/div[2]/div/a") ;
	  System.out.println("Alert msg: " + alertmsg);
	  
	  //Returning to Home page
	  for(int i=0 ; i<2 ; i++) {
		  driver.navigate().back();
		  Thread.sleep(1000);
	  }

	  List <WebElement> dellinks= ucomm.viewCart();

	  // deleting 1 item from cart
	  //System.out.println("Size before " +dellinks);
	  //dellinks.get(0).click();
	  //dellinks.remove(0);
	  //System.out.println("Size after " +dellinks);
	  Thread.sleep(1000);

	  // proceeding to checkout
	  ucomm.proceedToCheckOut();
	  statusFlag= 1;
	  Thread.sleep(2000);
	  
	//Taking screeshot
	  String ssFileName= tcName+"DemoBlaze-screenshot.jpeg";
	  ucomm.PageScreenShot(ssFileName,"DemoBlazescreenshots");
	  Thread.sleep(2000);
 	

	  //escape form to fill
	  driver.navigate().refresh();
	  Thread.sleep(2000);
	
	  //clearing the cart
	  //ucomm.deleteFromCart(dellinks);

	  //Returning to Home page
	  for(int i=0 ; i<1 ; i++) {
		  driver.navigate().back();
		  Thread.sleep(1000);
	  }

  }  
	  
 
  @Test (priority= 7)
  public void getOrderConfirmation() throws InterruptedException, IOException {
	  tcName= "TC7";
	  tcDesc= "getOrderConfirmation";
	  //Adding 2 items to cart 
	  Thread.sleep(2000);
	  prod= ucomm.findProduct(1);
	  Thread.sleep(2000);
	  ucomm.clickProduct(prod);
	  Thread.sleep(3000);
	  alertmsg= ucomm.addToCart("//*[@id=\"tbodyid\"]/div[2]/div/a") ;
	  System.out.println("Alert msg: " + alertmsg);
	  //Returning to Home page
	  for(int i=0 ; i<2 ; i++) {
		  driver.navigate().back();
		  Thread.sleep(1000);
	  }   

	  prod= ucomm.findProduct(3);
	  Thread.sleep(2000);
	  ucomm.clickProduct(prod);
	  Thread.sleep(3000);
	  alertmsg= ucomm.addToCart("//*[@id=\"tbodyid\"]/div[2]/div/a") ;
	  System.out.println("Alert msg: " + alertmsg);
	  
	  //Returning to Home page
	  for(int i=0 ; i<2 ; i++) {
		  driver.navigate().back();
		  Thread.sleep(1000);
	  }

	  List <WebElement> dellinks= ucomm.viewCart();

	  // proceeding to checkout
	  ucomm.proceedToCheckOut();
	  Thread.sleep(2000);

	  ucomm.findControlByID("name","Manju");
	  ucomm.findControlByID("country","India");
	  ucomm.findControlByID("city","Bangalore");
	  ucomm.findControlByID("card","12345678");
	  ucomm.findControlByID("month","March");
	  ucomm.findControlByID("year","2025");

	  String resp= ucomm.confirmPurchase(); 
	  System.out.println("Respose is " + resp);
	  statusFlag= 1;
	  	  
	  //Taking screeshot
	  String ssFileName= tcName+"DemoBlaze-screenshot.jpeg";
	  ucomm.PageScreenShot(ssFileName,"DemoBlazescreenshots");
	  Thread.sleep(2000);
	  
	  // Accept the Order details by clicking ok button
	  driver.findElement(By.xpath("/html/body/div[10]/div[7]/div/button")).click();
	  Thread.sleep(2000);

	  //escape form to fill
	  driver.navigate().refresh();
	  Thread.sleep(1000);

	  //clearing the cart
	  System.out.println("no: of items to delete now " + dellinks.size());
	  ucomm.deleteFromCart(dellinks);
	  Thread.sleep(2000);

	  //Returning to Home page
	  for(int i=0 ; i<2 ; i++) {
		  driver.navigate().back();
		  Thread.sleep(1000);
	  }
  }
  
  @AfterMethod
  public void afterMethod() throws IOException, InterruptedException {

	  test = report.createTest(tcName + " " + tcDesc);
	  if(statusFlag== 1) {
		  test.log(Status.PASS, MarkupHelper.createLabel(tcName + " : " + "Testcase pass for " + tcDesc, ExtentColor.GREEN));
	  } else {
		  test.log(Status.FAIL, MarkupHelper.createLabel(tcName + " : " + "Testcase pass for " + tcDesc, ExtentColor.RED));	
	  }
  }

@BeforeTest
    
@Parameters({"browser","os"})
public void setup(String browser,String opsystem) {
	System.out.println("Browser: " + browser);
	System.out.println("OS Name: " + opsystem);

	if (browser.equalsIgnoreCase("chrome")) {
		// WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	} else if (browser.equalsIgnoreCase("firefox")) {
		//WebDriverManager.firefoxdriver().setup();
		driver = new FirefoxDriver();
	} else if (browser.equalsIgnoreCase("edge")) {
		//WebDriverManager.edgedriver().setup();
		driver = new EdgeDriver();
	}

	//driver = new ChromeDriver();
	driver.manage().window().maximize();
	driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10000));

	//Creating object of class UtilityECommerce
	//**********************************
	ucomm= new UtilityECommerce(driver);

	//Launch website 
	ucomm.LaunchURL("https://www.demoblaze.com/");

	// Customised report creation setup
	//**********************************
	// Creating an object for report
	report= ucomm.CustomReportSetup(browser,opsystem,htmlreport,report,extentreportpath);
  }

  @AfterTest
  public void afterTest() {
	  report.flush();
	  driver.quit();
  }

}
