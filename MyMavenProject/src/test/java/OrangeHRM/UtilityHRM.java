package OrangeHRM;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;


public class UtilityHRM {
	
	WebDriver driver;
	public UtilityHRM (WebDriver driver) {
		this.driver= driver;
	}
	
	public void LaunchURL(String website) {
		// Open browser and launch website
		System.out.println("Launching url.........");
		driver.get(website);
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
	
	public WebElement FindUserName() {
		// Find WebElement for entering username
		  WebElement username= driver.findElement(By.cssSelector("input[placeholder=Username]"));
		  return username;
	}
	
	public WebElement FindPassword() {
		// Find WebElement for entering password 
		 WebElement password= driver.findElement(By.cssSelector("input[placeholder=Password]"));
		 return password;
	}
	
	public WebElement FindLoginButton() {
		// Find login button WebElement
		WebElement button= driver.findElement(By.cssSelector("button[type=submit]"));
		return button;
	}
	
	public void InputTextField(WebElement objhndl, String input) {
		 objhndl.sendKeys(input);
		
	}
	
	public void ClickElement(WebElement ele) {
		  ele.click();
	}
	
	public WebDriver RefreshPage() {
		driver.navigate().refresh();
		return driver;
		
	}
	
	public String FindPresentURL() {
		String cururl= driver.getCurrentUrl();
		return cururl;
	}
	
	public void UserLogout() {
		 driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[1]/header/div[1]/div[3]/ul/li/span/p")).click();
		 driver.findElement(By.linkText("Logout")).click();
	}
	
	public ExtentReports CustomReportSetup(ExtentSparkReporter htmlreport, ExtentReports report,String reportpath) {
		// Customised report creation setup
		//**********************************
		// Creating an object for report
		htmlreport = new ExtentSparkReporter(reportpath);
		report = new ExtentReports();
		report.attachReporter(htmlreport);
		
		//setting environment for extentreport
		report.setSystemInfo("Machine Name", "Dell");
		report.setSystemInfo("Tester Name", "Manju");
		report.setSystemInfo("OS", "Windows 11"); 
		report.setSystemInfo("Browser", "Google chrome");
		
		//Configuration of the report
		htmlreport.config().setDocumentTitle("OHRM_Login_Report");
		htmlreport.config().setReportName("OHRM_TC_Results");
		htmlreport.config().setTheme(Theme.STANDARD);
		htmlreport.config().setTimeStampFormat("EEEE dd, MMM YYYY HH:mm:ss");
		return report;
	}
	
	public List<WebElement> FindControlsByXPATH (String xpathval) {
	List<WebElement> controllist= driver.findElements(By.xpath(xpathval));
	return controllist;
	}
	
	public WebElement FindControlByXPATH (String xpathval) {
	WebElement control = driver.findElement(By.xpath(xpathval));                
	return control;
	}
	
	public void ClickByLinkText(String user) {
		//Find element and click
	  driver.findElement(By.linkText(user)).click();
	}
	
	public WebElement FindElementBycSSselector(String csstext) {
	WebElement control= driver.findElement(By.cssSelector(csstext));
	return control;
	}
	
}

