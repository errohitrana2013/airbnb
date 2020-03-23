package airBnb;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AirBnb {
	
	// object of properties class
	static Properties prop=new Properties();
	

	public static void main(String[] args) throws InterruptedException, IOException {
		// Object of FileInputStream class 
		FileInputStream conf= new FileInputStream(System.getProperty("user.dir")+"\\configFile\\config.properties");
		// load property file
		prop.load(conf);
		// set property of chrome driver
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\Exefile\\chromedriver.exe");
		WebDriver driver=new ChromeDriver();
		// delete all the cookies
		driver.manage().deleteAllCookies();
		// maximize the browser
		driver.manage().window().maximize();
		//implicity wait of 10 second
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		//calling trip method
		trip(driver,prop.getProperty("url"));
		// close the browser
		driver.close();
	}
	public static void trip(WebDriver driver,String url) throws InterruptedException {
		/**The Description of the method to explain what the method does
		@param  trip method argument are WebDriver and URl*/
		
		// Open the url
		driver.get(url);
		// Where element is stored in where variable 
		WebElement where=driver.findElement(By.xpath(prop.getProperty("whereXpath")));
		// Created the object of WebDriverWait 
		WebDriverWait wait=new WebDriverWait(driver,2);
		JavascriptExecutor js=(JavascriptExecutor)driver;
		// wait till page is loaded 
		js.executeScript("return document.readyState").equals("complete");
		// wait till the where element visible 
//		wait.until(ExpectedConditions.visibilityOf(where));
		// enter the text in where textbox
		where.sendKeys(prop.getProperty("whereProp"));

		// where elements list stored in dropDownlist  
		List<WebElement> dropDownList=driver.findElements(By.xpath(prop.getProperty("whereListXpath")));
		for(int i=0; i<dropDownList.size();i++) {
			// checking condition where element is match with given element
			if(dropDownList.get(i).getText().equalsIgnoreCase(prop.getProperty("whereProp")))
			{
				// If element match then click on that element
				dropDownList.get(i).click();
				break;
			}
		}
		try {
			// click on ok to close the cookies popup 
			driver.findElement(By.xpath(prop.getProperty("ok_cookiesPopUpXpath"))).click();
		}
		catch(Exception e){
		}

		// click on check in option
		driver.findElement(By.cssSelector(prop.getProperty("checkInCss"))).click(); 
		String monthYearIn=prop.getProperty("checkIn_MonthYear");
		String monthDateYearIn=prop.getProperty("checkIn_monthDateYear");
		// set the date in check in widget pop up
		setDate(driver,monthYearIn,monthDateYearIn,wait);
		
		String monthYearOut=prop.getProperty("checkOut_MonthYear");
		String monthDateYearOut=prop.getProperty("checkOut_monthDateYear");
		// set the date in check out widget pop up
		setDate(driver,monthYearOut,monthDateYearOut,wait);

		// click on Guests option for selecting the guests
		driver.findElement(By.xpath(prop.getProperty("guestsXpath"))).click();
		// selected the adults
		guest(driver,prop.getProperty("adults_Guests"),prop.getProperty("no_OfAdults_Guests")); 
		// selected the children
		guest(driver,prop.getProperty("children_Guests"),prop.getProperty("no_OfChildren_Guests"));
		// click on Guests option close the dropdown
		driver.findElement(By.xpath(prop.getProperty("closeguestsXpath"))).click();
		// click on submit button
		driver.findElement(By.xpath(prop.getProperty("submitButtonXpath"))).click();
		// wait till page to load
		js.executeScript("return document.readyState").equals("complete");
		
		// stored the Price button element in price variable 
		WebElement price=driver.findElement(By.xpath(prop.getProperty("priceButtonXpath")));
		// waiting for price element to visible 
		wait.until(ExpectedConditions.visibilityOf(price));
		// click on price button which open price pop up
		price.click();

		// max and min price element test box list 
		List<WebElement> priceBox=driver.findElements(By.cssSelector(prop.getProperty("priceBoxListCss")));
		// waiting for price element test box list to visible 
		wait.until(ExpectedConditions.visibilityOfAllElements(priceBox));
		// set the max and min price range
		setPrice(driver,priceBox,prop.getProperty("minPrice"), prop.getProperty("maxPrice"));
		// wait till page to load
		js.executeScript("return document.readyState").equals("complete");
		
		try {
		// List of places to stay is store in Webelement 
		List<WebElement> listPlacesTOStay=driver.findElements(By.cssSelector(prop.getProperty("listPlacesTOStayCss")));
		// waiting for list Places to Stay to visible 
		wait.until(ExpectedConditions.visibilityOfAllElements(listPlacesTOStay));

		for(int i=0; i<listPlacesTOStay.size();i++) {
			if(i==0 || i==2) {
				// print the 1 and 3 element from the displayed places to stay for given selection
				System.out.println(" Places to Stay "+i+" => "+listPlacesTOStay.get(i).getText());
				System.out.println("=================");
			}
		}}
		catch(Exception e){
			// print the no place to stay found
			System.out.println(driver.findElement(By.cssSelector(prop.getProperty("noResultsFoundCss"))).getText());
		}
	}

	public static void setDate(WebDriver driver,String monthYear,String monthDateYear,WebDriverWait wait) {

		String monthYear_1="";
		boolean status=false;
		while(!status) {
			// month and year element present in widget 
			WebElement monthYear_UI=driver.findElement(By.xpath(prop.getProperty("monthYearInWidgetXpath")));
			// waiting for month and year element to visible 
			wait.until(ExpectedConditions.visibilityOf(monthYear_UI));
			// get a text from month and year element
			monthYear_1=monthYear_UI.getText();
			if(monthYear.equalsIgnoreCase(monthYear_1)) {
				status=true;
				// click on the date present in widget
				driver.findElement(By.xpath("//*[@class='_1svux14' and @data-visible='true']//td[contains(@aria-label,'"+monthDateYear+"')]")).click();
			}
			else{
				// click on arrow on widget 
				driver.findElement(By.cssSelector(prop.getProperty("arrowInWidgetMonthCss"))).click();
			}
		}
	}
	
	public static void guest(WebDriver driver,String typeOfGuest,String noOfGuest) {
		int number=Integer.parseInt(noOfGuest);
		for(int i=0;i<number;i++) {
			// adding the guest in guest drop down list
			driver.findElement(By.xpath("//div[contains(text(),'"+typeOfGuest+"')]//parent::div//parent::div//parent::div//div[@class='_1a72ixey']")).click();
		}
	}
	
	public static void setPrice(WebDriver driver,List<WebElement> priceBox,String lowPrice, String highPrice) throws InterruptedException {

		// select the present text from minimum price 
		priceBox.get(0).sendKeys(Keys.CONTROL+"a");
		// delete the selected price
		priceBox.get(0).sendKeys(Keys.DELETE);
		// enter the price in minimum price text box.
		priceBox.get(0).sendKeys(lowPrice);

		// select the present text from maximum price 
		priceBox.get(1).sendKeys(Keys.CONTROL+"a");
		// delete the selected price
		priceBox.get(1).sendKeys(Keys.DELETE);
		// enter the price in maximum price text box.
		priceBox.get(1).sendKeys(highPrice);
		// click on save button on price pop up
		driver.findElement(By.xpath(prop.getProperty("priceSaveButtonXpath"))).click();
		Thread.sleep(200);

	}
}
