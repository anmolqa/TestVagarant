import io.restassured.response.Response;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.json.JSONObject;

public class TestCityWeather {
  WebDriver driver;
  WebDriverWait wait;
  SeleniumMethods sm;
  SeleniumWaits sw;
  String city = "Bengaluru";
  private int tempAPI;
  private int tempUI;

  @BeforeClass
  public void launchBrowser() {
    // System Property for Chrome Driver
    System.setProperty(
        "webdriver.chrome.driver", System.getProperty("user.dir")+ File.separator+"drivers"+File.separator+"chromedriver.exe");
    // Instantiate a ChromeDriver class.
    driver = new ChromeDriver();
    driver.manage().window().maximize();
    // Add Implicit Wait
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    wait = new WebDriverWait(driver, 30);
    sw = new SeleniumWaits(wait);
    sm = new SeleniumMethods(driver);
  }

  @Test(priority = 1)
  public void weatherUITest() {
    driver.get("https://www.ndtv.com");
    sw.waitForPageLoad();
    sm.closeNotification("//a[@class = 'notnow']");
    sm.clickOnButton("//a[@class = 'topnavmore']");
    sm.clickOnButton("//a[text() = 'WEATHER']");
    sw.waitForPageLoad();
    sm.fillTextField("//input[@class = 'searchBox']", city);
    sm.clickOnCityIfUnchecked(city);
    sm.verifyCityIsPresentOnMap(city);
    sm.verifyWeatherIsDisplayed(city);
    String unit = "Degrees";
    tempUI = sm.getCurrentTemp(city, unit);
    System.out.println("Temp received through API is "+ tempUI);

  }

  @Test(priority = 2)
  public void getWeatherThroughAPI() {
    String endpoint = "https://api.openweathermap.org/data/2.5/weather";
    Map<String, String> queryParams = new HashMap();
    queryParams.put("q", city);
    queryParams.put("appid", "7fe67bf08c80ded756e598d6f8fedaea");
    queryParams.put("units", "metric");
    ApiRunner apiRunner = new ApiRunner(endpoint, "application/json", queryParams);
    Response response = apiRunner.submitGet();
    Assert.assertTrue(response.getStatusCode() == 200);
    System.out.println("Status Code for weather API is 200");
    tempAPI = getCurrentWeather(response);
    System.out.println("Temp received through API is "+ tempAPI);
  }

  @Test(
      priority = 3,
      dependsOnMethods = {"weatherUITest", "getWeatherThroughAPI"})
  public void compareTemps() {
    if (tempAPI == tempUI) {
      Assert.assertTrue(true);
      System.out.println("Temps fetched from UI and API are same.");
    }
    else if (tempAPI > tempUI) {
      Assert.assertTrue(tempAPI - 5 <= tempUI);
      System.out.println("Temp diff is: " + (tempAPI - tempUI));
    } else Assert.assertTrue(tempUI - 5 <= tempAPI);
    System.out.println("Temp diff is: " + (tempUI - tempAPI));
  }

  private int getCurrentWeather(Response response) {
    String responseBody = response.getBody().asString();
    return new JSONObject(responseBody).getJSONObject("main").getInt("temp");
  }

  @AfterClass
  public void quitDriver() {
    driver.quit();
  }
}
