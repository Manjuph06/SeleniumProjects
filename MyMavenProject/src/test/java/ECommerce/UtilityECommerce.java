package ECommerce;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math3.exception.util.ExceptionContext;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class UtilityECommerce {

	WebDriver driver;
	WebDriverWait wait;
	public UtilityECommerce() {
		driver= new ChromeDriver();
	}

	public UtilityECommerce(WebDriver driver) {
		this.driver= driver;
	}

	public void LaunchURL(String website) {
		// Open browser and launch website
		System.out.println("Launching EComm website.........");
		driver.get(website);
	} 


	public String SignInUser(String act,String uid,String email,String pid,String epwd,String buttonxpath,String closexpath) throws InterruptedException {

		String altmsg="NA";
		//Register/Sign up user
		driver.findElement(By.id(act)).click();
		driver.findElement(By.id(uid)).sendKeys(email);
		driver.findElement(By.id(pid)).sendKeys(epwd);
		driver.findElement(By.cssSelector(buttonxpath)).click();
		Thread.sleep(3000);

		if(act.contains("signin2"))  {
			//Handling alert after it pops up
			Alert alert = driver.switchTo().alert();
			//Get alert text
			altmsg = alert.getText();
			System.out.println("Alert text is " + altmsg);
			//click alertmessage ok
			alert.accept();
			//click close window
			driver.findElement(By.xpath(closexpath)).click();            

		}

		return altmsg;

	}

	// find product
	public WebElement findProduct(int itemno) {
		WebElement product= driver.findElement(By.xpath("//*[@id=\"tbodyid\"]/div["+itemno+"]"));
		return product;
	}

	// get product details
	public void getProdDetails(WebElement product) {
		System.out.println(product.getText());
	}

	// click product
	public void clickProduct(WebElement product) {
		product.click();
		System.out.println("Product clicked");
	}

	// Add product to cart
	public String addToCart(String xpathval) throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath(xpathval)).click();
		Thread.sleep(2000);
		Alert alt= driver.switchTo().alert();
		String msg= alt.getText();
		alt.accept();
		return msg;
	}

	public int viewCart() throws InterruptedException {
		driver.findElement(By.linkText("Cart")).click();
		Thread.sleep(2000);

		//list of products to be deleted and its links
		List <WebElement> deletelink= driver.findElements(By.linkText("Delete")); 
		return deletelink.size();
	}

	public void deleteFromCart(int records) throws InterruptedException {
		
		for(int i=0 ; i< records ; i++) {
		WebElement deletelink= driver.findElement(By.linkText("Delete"));
			try {
				deletelink.click();
				Thread.sleep(2000);
				System.out.println("Product getting deleted");
			} catch (Exception e) {
				System.out.println("Something went wrong.");
				e.printStackTrace();
			}
		}
	}

	public void proceedToCheckOut() {
		driver.findElement(By.xpath("//*[@id=\"page-wrapper\"]/div/div[2]/button")).click();
		System.out.println("Proceeded to check out");
	}

	public void findControlByID(String controlid,String value) {
		driver.findElement(By.id(controlid)).sendKeys(value);

	}

	public String confirmPurchase() throws InterruptedException {
		driver.findElement(By.xpath("//*[@id=\"orderModal\"]/div/div/div[3]/button[2]")).click();
		String msg= driver.findElement(By.xpath("/html/body/div[10]/h2")).getText();
		return msg;
	}

	public void PageScreenShot(String ssFileName,String foldername) throws IOException {
		String fpath= System.getProperty("user.dir")+"\\"+foldername+"\\"+ssFileName;
		System.out.println("filepath of screenshot is " + fpath);
		TakesScreenshot tshot= ((TakesScreenshot) driver);
		File srcfile = tshot.getScreenshotAs(OutputType.FILE);
		FileHandler filehndr= new FileHandler();
		try {
			filehndr.copy(srcfile, new File(fpath)); 
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Screenshot taken: "+ fpath);
	}


	public ExtentReports CustomReportSetup(String Browser,String OsName, ExtentSparkReporter htmlreport, ExtentReports report,String reportpath) {
		// Customised report creation setup
		//**********************************
		// Creating an object for report
		htmlreport = new ExtentSparkReporter(reportpath);
		report = new ExtentReports();
		report.attachReporter(htmlreport);

		//setting environment for extentreport
		report.setSystemInfo("Machine Name", "Dell");
		report.setSystemInfo("Tester Name", "Manju");
		report.setSystemInfo("OS", OsName); 
		report.setSystemInfo("Browser", Browser);

		//Configuration of the report
		htmlreport.config().setDocumentTitle("DBlaze_Login_Report");
		htmlreport.config().setReportName("DBlaze_TC_Results");
		htmlreport.config().setTheme(Theme.STANDARD);
		htmlreport.config().setTimeStampFormat("EEEE dd, MMM YYYY HH:mm:ss");
		return report;
	}
}
