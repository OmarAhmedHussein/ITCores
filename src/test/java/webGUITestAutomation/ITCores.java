package webGUITestAutomation;

import org.openqa.selenium.JavascriptExecutor;

import static utils.Selenium_Base.getWebDriverWait;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static utils.Selenium_Base.getChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.Selenium_Base;



public class ITCores {

	@Test(priority=1)
	public static void verifyThatTheResultsMatchTheSearchCriteria() {

		String destination = "Rome, Italy";
		int guests = 3;
		String checkInDate = getDateAfterWeeks(1,true);
		String checkOutDate = getDateAfterWeeks(2,false);

		By location = By.xpath("//*[@id=\"bigsearch-query-location-input\"]");
		By clickOnCheckIn = By.xpath("//*[@id=\"search-tabpanel\"]/div/div[3]/div[1]/div/div/div[2]");
		By chooseCheckInDate = By.xpath("//*[@aria-label=\""+checkInDate+"\"]");
		By chooseCheckOutDate = By.xpath("//*[@aria-label=\""+checkOutDate+"\"]");
		By clickOnGuests = By.xpath("//*[@id=\"search-tabpanel\"]/div/div[5]/div[1]/div/div[2]");
		By selectAdults = By.xpath("//*[@id=\"stepper-adults\"]/button[2]");
		By selectChildren = By.xpath("//*[@id=\"stepper-children\"]/button[2]");
		By clickOnSearch = By.xpath("//*[@id=\"search-tabpanel\"]/div/div[5]/div[4]");

		By locationInFiltersArea = By.xpath("/html/body/div[5]/div/div/div[1]/div/div/div/div/div/div[1]/div/nav/div/div/div/header/div/div[2]/div/div/div/div[1]/div/button[1]/div");
		By dateInFiltersArea = By.xpath("/html/body/div[5]/div/div/div[1]/div/div/div/div/div/div[1]/div/nav/div/div/div/header/div/div[2]/div/div/div/div[1]/div/button[2]/div");
		By numberOfGuestsInFiltersArea = By.xpath("/html/body/div[5]/div/div/div[1]/div/div/div/div/div/div[1]/div/nav/div/div/div/header/div/div[2]/div/div/div/div[1]/div/button[3]/div[1]");

		By resultsHeader = By.xpath("//*[@id=\"site-content\"]/div[1]/div/div/div/div/div/section/h1/div");

		By firstProperty = By.xpath("//*[@class=\"_8ssblpx\"]/div/div/div[2]/div/div/div/div[2]/div[2]/div[3]/span[1]");

		Selenium_Base.Selenium_config("https://www.airbnb.com");		//Open www.airbnb.com.
		actionOnElementUsingXpath(location,false,1,destination);		//Select Rome, Italy as a location.
		actionOnElementUsingXpath(clickOnCheckIn,true,1);		//Click on Check-In
		actionOnElementUsingXpath(chooseCheckInDate,true,1);		//Pick a Check-In date one week after the current date.
		actionOnElementUsingXpath(chooseCheckOutDate,true,1);		//Pick a Check-Out date one week after the Check-In date.
		//Select the number of guests as 2 adults and 1 child.
		actionOnElementUsingXpath(clickOnGuests,true,1);
		actionOnElementUsingXpath(selectAdults,true,2);
		actionOnElementUsingXpath(selectChildren,true,1);

		actionOnElementUsingXpath(clickOnSearch,true,1);		//Search for properties.
		//Verify that the applied filters are correct (in the filters area and search results header).
		Assert.assertTrue(destination.contains(getTextFromElementByXpath(locationInFiltersArea)));

		//TODO//the header disappeared one day ago, so I commented the assertions of results headers

//		Assert.assertTrue(getTextFromElementByXpath(resultsHeader).contains(getTextFromElementByXpath(dateInFiltersArea).replace("–","-")));
//		Assert.assertTrue(getTextFromElementByXpath(resultsHeader).contains(getTextFromElementByXpath(numberOfGuestsInFiltersArea)));

		//Verify that the properties displayed on the first page can accommodate at least the selected number of guests.
		int resultedFirstProperty = Integer.parseInt(getTextFromElementByXpath(firstProperty).replaceAll("[^0-9]", ""));
//		System.out.println(resultedFirstProperty);
		Assert.assertTrue(resultedFirstProperty >= guests);
		for (int i=2;i<=20;i++){
			By propertiesDisplayed = By.xpath(String.format("//*[@class=\"_8ssblpx\"][%s]/div/div/div[2]/div/div/div/div[1]/div[2]/div[3]/span[1]",i));

			int resultedProperties = Integer.parseInt(getTextFromElementByXpath(propertiesDisplayed).replaceAll("[^0-9]", ""));
//			System.out.println(resultedProperties);
			Assert.assertTrue(resultedProperties >= guests);
		}
	}

	@Test(priority=2)
	public static void verifyThatTheResultsAndDetailsPageMatchTheExtraFilters() {

		By filters = By.xpath("//*[@aria-label=\"Filters\"]");
		By numberOfBedrooms = By.xpath("//*[@class=\"_13z7md5\"][2]/div/div[2]/button[2]");
		By pool = By.xpath("//*[@name=\"Pool\"]");
		By showStays = By.xpath("//*[@data-plugin-in-point-id=\"COMPOSITE_FILTER_FOOTER_MORE_FILTERS_WIDE\"]/div/footer/a");
		By firstPropertyDetails = By.xpath("//*[@id=\"site-content\"]/div[4]/div/div/div/div/div/div/div[2]/div/div/div/div/div/div/div[1]/div/div/div[2]/div/div/div/div[2]/div[2]/div[3]/span[3]");
		By firstProperty = By.xpath("//*[@itemprop=\"itemListElement\"][1]");
		int bedrooms = 5;

		actionOnElementUsingXpath(filters,true,1);
		actionOnElementUsingXpath(numberOfBedrooms,true,5);

		JavascriptExecutor js = (JavascriptExecutor) getChromeDriver();
		WebElement Pool = getChromeDriver().findElement(pool);
		js.executeScript("arguments[0].scrollIntoView();", Pool);
		Pool.click();

		WebElement ShowStays = getChromeDriver().findElement(showStays);
		ShowStays.click();

		//Verify that the properties displayed on the first page have at least the number of selected bedrooms.
		int resultedFirstProperty = Integer.parseInt(getTextFromElementByXpath(firstPropertyDetails).replaceAll("[^0-9]", ""));
//		System.out.println(resultedFirstProperty);
		Assert.assertTrue(resultedFirstProperty >= bedrooms);
		for (int i=2;i<=20;i++){
			By propertiesDisplayed = By.xpath(String.format("//*[@class=\"_8ssblpx\"][%s]/div/div/div[2]/div/div/div/div[1]/div[2]/div[3]/span[3]",i));
			int resultedProperties = Integer.parseInt(getTextFromElementByXpath(propertiesDisplayed).replaceAll("[^0-9]", ""));
//			System.out.println(resultedProperties);
			Assert.assertTrue(resultedProperties >= bedrooms);
		}

		actionOnElementUsingXpath(firstProperty,true,1);

		//handle the new tab
		ArrayList<String> tabs2 = new ArrayList<String> (getChromeDriver().getWindowHandles());
		getChromeDriver().switchTo().window(tabs2.get(1));

		//wait until the page load
		getChromeDriver().manage().timeouts().implicitlyWait(15,TimeUnit.SECONDS) ;

		//Create List of Amenties
		ArrayList<String> Ament = new ArrayList<String>();
		int index = 0;
		for (int i=1; i<=10; i++) {
			By amenties = By.xpath(String.format("//*[@class=\"_19xnuo97\"][%s]/div/div[1]", i));
			WebElement Amenties = getChromeDriver().findElement(amenties);
			js.executeScript("arguments[0].scrollIntoView();", Amenties);
			//add all results in lowercase, not to get error in assertion
			String AMENTIES = Amenties.getText().toLowerCase();
			if(AMENTIES.contains("pool")){
				index = i-1; //"-1" because the loop starts with i=1, and the list "where we put the index" starts with 0
			}
			Ament.add(AMENTIES);
		}
		//when asserting on the list, it doesn`t catch "outdoor pool" as a "pool",
		// so we catch any type of pool in the above condition and then assert on it with the proper index of the list
		Assert.assertTrue(Ament.get(index).contains("pool"));

		getChromeDriver().close();
		getChromeDriver().switchTo().window(tabs2.get(0));
		getChromeDriver().close();
	}





	public static String getDateAfterWeeks(long weeks, boolean start){
		//Choose Sunday, December 5, 2021 as your start date. It's available.
		//Choose FRIDAY, NOVEMBER 19, 2021 as your start date. It's available.
		String output = "";
		LocalDate currentDate = LocalDate.now().plusWeeks(weeks);
		String day = currentDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.US);
		String month = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.US);
		int dayOfMonth = currentDate.getDayOfMonth();
		int year = currentDate.getYear();
		if(start) {
			 output = String.format("Choose %s, %s %s, %s as your start date. It's available.",
					day, month, dayOfMonth, year);
		}
		else{
			output = String.format("Choose %s, %s %s, %s as your end date. It's available.",
					day, month, dayOfMonth, year);
		}

		//System.out.println(output);
		return output;
	}

	public static void actionOnElementUsingXpath(By element, boolean click, int clicks, String... sendKeys){
		getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated(element));
		getWebDriverWait().until(ExpectedConditions.elementToBeClickable(element));
		WebElement Element = getChromeDriver().findElement(element);
		if(click) {
			for (int i = 0; i<clicks; i++)
				Element.click();
		}
		else{
			Element.sendKeys(sendKeys);
		}
	}

	public static String getTextFromElementByXpath (By element){
		getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated(element));
		WebElement Element = getChromeDriver().findElement(element);
		String elementText = Element.getText();
		return elementText;
	}
}
