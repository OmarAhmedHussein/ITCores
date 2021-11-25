package utils;

import java.io.File;
import java.io.IOException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.apache.commons.io.FileUtils;

public class Selenium_Base {
	static WebDriver driver;
	static WebDriverWait wait;


	public static void Selenium_config(String URL) {

		System.setProperty("webdriver.chrome.driver",
				System.getProperty("user.dir") +"/src/main/resources/chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		driver = new ChromeDriver(options);

		driver.navigate().to(URL);

		wait = new WebDriverWait(driver, 20);
		driver.manage().window().maximize();
	}


	public static WebDriver getChromeDriver() {
		return driver;
	}

	public static WebDriverWait getWebDriverWait() {
		return wait;
	}

	public static void takeScreenShot(String testName) throws IOException {		
			
		String fileWithPath = new File(System.getProperty("user.dir")).getAbsolutePath() +"/src/test/resources/Screenshots/";

		//Convert web driver object to TakeScreenshot
		File SrcFile = ((TakesScreenshot)Selenium_Base.driver).getScreenshotAs(OutputType.FILE);
		
		//Move image file to new destination
		File DestFile=new File((String)fileWithPath + testName + ".png");

		//Copy file at destination
		FileUtils.copyFile(SrcFile, DestFile);
		
		//to appear in index.html / testng report
        Reporter.log("<a href='"+ DestFile.getAbsolutePath() + "'> <img src='"+ DestFile.getAbsolutePath() + "' height='100' width='100'/> </a>");

	}

	public static void close() {
		driver.quit();
	}





}