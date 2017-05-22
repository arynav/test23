package com.jfbuilds.craigslist;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.jfbuilds.jf.BasicAutoTest;

import java.util.regex.Pattern;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

public class For23andMe extends BasicAutoTest {
	public For23andMe() {
		// Set baseURL super class variable to For23andMe
		super("http://store.23andme.com/en-us/");
	}

	@Test
	public void test23andme() {

		String kitName ="test";
		int kitNumber = 5;
		double ancestryKitPrice= 99.0;
		//modify to extract the shipping charge from 
		//getDriver().findElement(By.id("id_shipping_method"))).selectByVisibleText(shipMethod);
		double shipping = 29.95;
		String firstName= "test";
		String lastName= "test";
		String address ="123 A st";
		String city ="mountain view";
		String state="California";
		String zip ="94532";
		String email ="test@gmail.com";
		String phone ="1 415-666-7744";
		String shipMethod ="Standard (5-8 days) - $29.95";
		String addServiceName = "Add a service below.";
		String expectedAddress = firstName+" "+lastName+" "+address+" "+city+", "+state+" "+zip+" US";

		
		//Assert/Verify Store page elements
		Assert.assertEquals(addServiceName,getDriver().findElement(By.cssSelector("h2")).getText(), "Add a service below.");	
		Assert.assertTrue(isElementPresent(By.xpath("//*[@class='quantity-control-button js-add-ancestry-kit']")));
		getLog().info("Landed to the Order page");

		//Select number of kits and provide kit names on Store page
		addKitAndName(kitNumber, kitName);
		
		//Assert Order form on Store page submitted successfully and test reached Shipping page
		assertEquals(getDriver().findElement(By.cssSelector("h2")).getText(), "Shipping address");
		getLog().info("Landed to the Shipping page.");
		
		//Fill in shipping info on Shipping page
		addShippingInfo(firstName,lastName,address,city,state,zip,email,phone,shipMethod);
		
		//Verify provided address. For this test, check Unverified address
		verifyAddress(expectedAddress);
		
		//Assert Shipping form submitted successfully and test reached Billing page
	    assertEquals(getDriver().findElement(By.id("progress-label")).getText(), "BILLING");
	     
	    billing(ancestryKitPrice, kitNumber, expectedAddress, shipping);
	}
	
	private void addKitAndName(int numberOfkits, String kitName) {
		//Add Ancestry kits
	    for(int i =0; i<numberOfkits; i++){
		    WebElement mapObject = getDriver().findElement(By.xpath("(//*[name()='svg']/*[name()='path'and contains(@d, 'M20')])[2]"));
		    ((JavascriptExecutor) getDriver()).executeScript("arguments[0].dispatchEvent(new MouseEvent('click', {view: window, bubbles:true, cancelable: true}))", mapObject);
	    }
	    //Assert number Of kits added
	    String count =Integer.toString(numberOfkits);
	   	//assertEquals(getDriver().findElement(By.id("text-ancestry-kit-count")).getText(), count);
	    try {
	        assertEquals(getDriver().findElement(By.id("text-ancestry-kit-count")).getText(), count);
	      } catch (Error e) {
	        getLog().info("Assert number Of kits added: " + e.toString());
	      }
	    //Provide names for added kits
		By input = By.xpath("//input[contains(@name, 'name')]");
		List<WebElement> eList = getDriver().findElements(input);
		int tmp =1;
			for(WebElement e : eList) {
					//System.out.println(e.getAttribute("placeholder"));
					e.clear();
					e.sendKeys("test" + tmp);
				tmp++;
				}
		//Assert 'continue' button is available
		Assert.assertTrue(isElementPresent(By.cssSelector("input.submit.button-continue")));
		getLog().info("Added "+numberOfkits+ " Ancestry kits with names");

		// Submit the form with added kits and names
		getDriver().findElement(By.cssSelector("input.submit.button-continue")).submit();
	}
	private void addShippingInfo(String fName, String lName, String address,String city,String state,String zip,String email,String phone,String shipMethod) {
		//Clear input fields and fill in shipping info
		getDriver().findElement(By.id("id_first_name")).clear();
		getDriver().findElement(By.id("id_first_name")).sendKeys(fName);
		getDriver().findElement(By.id("id_last_name")).clear();
		getDriver().findElement(By.id("id_last_name")).sendKeys(lName);
		getDriver().findElement(By.id("id_address")).clear();
		getDriver().findElement(By.id("id_address")).sendKeys(address);
		getDriver().findElement(By.id("id_city")).clear();
		getDriver().findElement(By.id("id_city")).sendKeys(city);
	    new Select(getDriver().findElement(By.id("id_state"))).selectByVisibleText(state);
	    getDriver().findElement(By.id("id_postal_code")).clear();
	    getDriver().findElement(By.id("id_postal_code")).sendKeys(zip);
	    getDriver().findElement(By.id("id_email")).clear();
	    getDriver().findElement(By.id("id_email")).sendKeys(email);
	    getDriver().findElement(By.id("id_int_phone")).clear();
	    getDriver().findElement(By.id("id_int_phone")).sendKeys(phone);
	    for (int second = 0;; second++) {
	    	if (second >= 60) fail("timeout");
	    	try { if (isElementPresent(By.id("id_shipping_method"))) break; } catch (Exception e) {}
	    	
	    	getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    }
	    new Select(getDriver().findElement(By.id("id_shipping_method"))).selectByVisibleText(shipMethod);

	    //Assert submit button is available
	    try {
	        assertTrue(isElementPresent(By.cssSelector("input.submit.button-continue")));
	      } catch (Error e) {
		    getLog().info("Assert isElementPresent: submit button: " + e.toString());
	      }
	    
	    //Submit Shipping Info form
	    getDriver().findElement(By.cssSelector("input.submit.button-continue")).submit();    

	}
	private void verifyAddress(String expectedAddress) {
		String actuaAddress = getDriver().findElement(By.cssSelector("div.address")).getText();

		//Verify address info. For this test, check 'Unverified address'
		
	    //Assert 'Unverified address' heading h2 and address info text
	    assertTrue(isElementPresent(By.cssSelector("div.verify.unverified > h2")));
	    assertEquals(getDriver().findElement(By.cssSelector("div.verify.unverified > h2")).getText(), "Unverified Address:");
	    try {
	      assertEquals(actuaAddress.replace("\n", " "), expectedAddress);
    
	    } catch (Error e) {
		      getLog().info("Assert 'Unverified address' info :" + e.toString());
		    }
	    try {
	    	getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	        assertTrue(isElementPresent(By.cssSelector("input.button-continue.use-unverified")));
	      } catch (Error e) {
		    getLog().info("Assert continue button for 'Unverified address' page:" + e.toString());

	      }
	    //Submit Unverified Address form
	    getDriver().findElement(By.cssSelector("input.button-continue.use-unverified")).submit();
	}
	private void billing(double kitPrice, int numberOfkits,String expectedAddress, double shipping) {
		double orderTotal = kitPrice * numberOfkits + shipping;
		String billAddressHeader="Billing address";
		String actualBillAddress = getDriver().findElement(By.cssSelector("div.address")).getText();
		//verify total order amount
	    try {
	        assertEquals(getDriver().findElement(By.cssSelector("strong.payment-total")).getText(), "Order total: $" +orderTotal);
	      } catch (Error e) {
			    getLog().info("Assert 'Order total': " + e.toString());
	      }
	    //Assert billing address
	    assertEquals(getDriver().findElement(By.cssSelector("section.js-billing-address > aside > h2")).getText(), billAddressHeader);
	    assertEquals(actualBillAddress.replace("\n", " "),expectedAddress );



	}

	private boolean isElementPresent(By by) {
	    try {
	    	getDriver().findElement(by);
	      return true;
	    } catch (NoSuchElementException e) {
	      return false;
	    }
	  }


}
