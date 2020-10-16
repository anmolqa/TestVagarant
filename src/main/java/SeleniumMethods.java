import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class SeleniumMethods {

  WebDriver driver;

  public SeleniumMethods(WebDriver driver) {
    this.driver = driver;
  }

  public void clickOnButton(String locator) {
    driver.findElement(By.xpath(locator)).click();
    System.out.println("Clicked on button with locator: " + locator);
  }

  public void fillTextField(String locator, String value) {
    driver.findElement(By.xpath(locator)).sendKeys(value);
  }

  public void clickOnCityIfUnchecked(String cityName) {
    WebElement element = driver.findElement(By.xpath("//label[@for= '" + cityName + "']/input"));
    String s = element.getAttribute("checked");
    try {
      if (!s.equals("true")) {
        driver.findElement(By.xpath("//label[@for= '" + cityName + "']/input")).click();
        System.out.println("Clicked on city: " + cityName + " since it was unchecked");
      }
    } catch (Exception e) {
      System.out.println("City: " + cityName + " is already selected");
    }
  }

  public void verifyCityIsPresentOnMap(String city) {

    Assert.assertTrue(driver.findElement(By.xpath("//div[text()= '" + city + "']")).isDisplayed());
    System.out.println("Verified successfully that city " + city + " is present on Map");
  }

  public void verifyWeatherIsDisplayed(String city) {
    driver.findElement(By.xpath("//div[text()= '" + city + "']")).click();
    Assert.assertTrue(
        driver.findElement(By.xpath("//span[contains(text(), '" + city + "')]")).isDisplayed());
    System.out.println(
        "Verified successfully that for city " + city + ", weather leaf is getting displayed");
  }

  public void closeNotification(String alert) {
    try {
      driver.findElement(By.xpath(alert)).click();
    } catch (Exception e) {
    }
  }

  public int getCurrentTemp(String city, String tempUnit) {
    String temp = driver.findElement(By.xpath("//span[contains(text(), '"+city+"')]/parent::div/following-sibling::span/b[contains(text(), 'Temp in "+tempUnit+"')]")).getText();
    return Integer.parseInt(temp.substring(temp.lastIndexOf(' ') + 1));
  }
}
