package fr.lovotech.galaxy.qa.backoffice.service.utlis;
 
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;
 
public class TestHelper {
    protected static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();
    public static String remote_url_chrome = "http://62.171.143.49:4444/wd/hub";
 
    public void setupThread(String browserName) throws MalformedURLException
    {

            // Map<String,Number> prefs = new HashMap<String,Number>();
            // prefs.put("profile.default_content_settings.cookies", 2);
            System.out.println("Inside "+browserName);
            ChromeOptions options = new ChromeOptions();
            options.setCapability("se:recordVideo", true);
            
// options.setExperimentalOption("profile.default_content_setting_values.cookies", 2);

            driver.set(new RemoteWebDriver(new URL(remote_url_chrome), options));
        
    }
 
    public WebDriver getDriver()
    {
        return driver.get();
    }
 
    public void tearDownDriver()
    {
        getDriver().quit();
    }
}