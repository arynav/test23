//
package com.jfbuilds.helpers;

import java.io.*;
import java.util.*;

import org.apache.commons.io.*;
import org.apache.log4j.*;
import org.openqa.selenium.*;

/**
 * AutoBasics //ADDD (description of class)
 * <p>
 * //ADDD (description of core fields)
 * <p>
 * //ADDD (description of core methods)
 * 
 * @author LastName, FirstName
 * @version 1.0.0
 * @since 1.0
 */
public class AutoBasics {

	public static final String DEFAULT_CONFIG_PROPERTIES_LOCATION = "src/main/resources/config.properties";

	public static final String DEFAULT_SCREENSHOT_FILENAME = "screenshot";

	public static final String DEFAULT_SCREENSHOT_SAVE_LOCATION = "screenshots/";

	public static final String FILE_EXTENSION = ".jpg";

	public static Properties evalProperties(String fileLocation) throws IOException {
		// Create Properties object
		Properties props = new Properties();
		// Create a File with passed fileLocation details
		File file = new File("src/main/resources/config.properties");
		// Create a FileInputStream based File object
		FileInputStream fis = new FileInputStream(fileLocation);
		// Load Properties based on FileInputStream
		props.load(fis);
		// Return loadednPorperties object
		return props;
	}

	public static String evalProperty(Properties props, String propKey) {
		// Set the value of the prop key from the Properties object
		String value = props.getProperty(propKey);
		// Return the stored String value
		return value;
	}

	// Overloaded method, using default value, to get a config property
	public static String evalProperty(String propKey) throws IOException {
		return evalProperty(DEFAULT_CONFIG_PROPERTIES_LOCATION, propKey);
	}

	// Main method for getting a property from config file
	public static String evalProperty(String fileLocation, String propKey) throws IOException {
		Properties props = evalProperties("src/main/resources/config.properties");
		String value = evalProperty(props, propKey);
		return value;
	}

	// Built in method to get all elements by a specific tag
	public static List<WebElement> getByTagName(WebDriver driver, String tagName) {
		List<WebElement> elements = driver.findElements(By.tagName(tagName));
		return elements;
	}

	// Built in method to get all elements by a specific value of a CSS property
	public static List<WebElement> getCSSPropertyBasedElements(WebDriver driver, By locator, String prop,
			String value) {
		List<WebElement> elements = driver.findElements(locator);
		List<WebElement> matchingElements = new ArrayList<WebElement>();
		for (int i = 0; i < elements.size(); i++) {
			String elementValue = elements.get(i).getCssValue(prop);
			if (elementValue.equalsIgnoreCase(value)) {
				matchingElements.add(elements.get(i));
			}
		}
		return elements;
	}

	// Built in method to get all elements to get all links on a page and return them
	// as a collection of WebElement objects
	public static List<WebElement> getLinks(WebDriver driver) {
		List<WebElement> elements = getByTagName(driver, "a");
		return elements;
	}
	
	// Built in method to get all elements to get all images on a page and return them
	// as a collection of WebElement objects
	public static List<WebElement> getPictures(WebDriver driver) {
		List<WebElement> elements = getByTagName(driver, "img");
		return elements;
	}

	// Built in method to get all elements' text based on a specific By locator and
	// return them as a collection of String objects
	public static List<String> getTextContents(WebDriver driver, By locator) {
		List<WebElement> elements = driver.findElements(locator);
		List<String> elementTexts = new ArrayList<String>();
		for (int i = 0; i < elements.size(); i++) {
			String text = elements.get(i).getText();
			if (!text.equals("")) {
				elementTexts.add(text);
			}
		}
		return elementTexts;
	}

	
	// Another built in overloaded method to take a Screenshot, allows the use of alternative defaults
	public static boolean takeScreenshot(WebDriver driver) {
		return takeScreenshot(driver, DEFAULT_SCREENSHOT_SAVE_LOCATION, DEFAULT_SCREENSHOT_FILENAME, null);
	}

	// Built in overloaded method to take a Screenshot, allows the use of defaults
	public static boolean takeScreenshot(WebDriver driver, String location, String filename) {
		return takeScreenshot(driver, location, filename, null);
	}

	// Built in main method to take a Screenshot
	public static boolean takeScreenshot(WebDriver driver, String location, String filename, Logger logger) {
		File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(srcFile, new File(location + filename + FILE_EXTENSION));
		} catch (IOException e) {
			if (logger != null) {
				logger.error("Failed to save screenshot at " + location + filename + FILE_EXTENSION);
			}
			return false;
		}
		return true;
	}
}
