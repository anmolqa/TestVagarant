import java.util.function.Function;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumWaits {

  WebDriverWait wait;

  public SeleniumWaits(WebDriverWait wait) {
    this.wait = wait;
  }

  public void waitForPageLoad() {

    wait.until(
        driver -> String.valueOf(
                ((JavascriptExecutor) driver).executeScript("return document.readyState"))
            .equals("complete"));
  }
}
