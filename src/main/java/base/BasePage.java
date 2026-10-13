package base;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;
import utils.LoggerUtils;
import utils.WaitUtils;

import java.time.Duration;
import java.util.List;

/**
 * BasePage implementing reliable, reusable Selenium interactions with explicit waits,
 * JavaScript fallbacks, alerts handling, and scrolling.
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        int explicitTimeout = ConfigReader.getIntProperty("timeout.explicit", 15);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitTimeout));
    }

    protected WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected boolean waitForInvisibility(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected void click(By locator) {
        try {
            waitForClickable(locator).click();
        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            LoggerUtils.warn(getClass(), "Standard click intercepted/stale on " + locator + ", attempting JS click.");
            WebElement element = driver.findElement(locator);
            clickViaJs(element);
        }
    }

    protected void clickViaJs(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected void enterText(By locator, String text) {
        WebElement element = waitForVisibility(locator);
        element.clear();
        if (text != null && !text.isEmpty()) {
            element.sendKeys(text);
        }
    }

    protected void clearField(By locator) {
        WebElement element = waitForVisibility(locator);
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(Keys.BACK_SPACE);
        element.clear();
    }

    protected String getText(By locator) {
        try {
            return waitForVisibility(locator).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    protected boolean isElementDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    protected boolean isElementEnabled(By locator) {
        try {
            return driver.findElement(locator).isEnabled();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    protected void selectByVisibleText(By locator, String visibleText) {
        WebElement element = waitForVisibility(locator);
        Select select = new Select(element);
        select.selectByVisibleText(visibleText);
    }

    protected void selectByValue(By locator, String value) {
        WebElement element = waitForVisibility(locator);
        Select select = new Select(element);
        select.selectByValue(value);
    }

    protected String getSelectedOptionText(By locator) {
        WebElement element = waitForVisibility(locator);
        Select select = new Select(element);
        return select.getFirstSelectedOption().getText().trim();
    }

    protected void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    protected void uploadFile(By fileInputLocator, String absoluteFilePath) {
        WebElement fileInput = driver.findElement(fileInputLocator);
        fileInput.sendKeys(absoluteFilePath);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public void navigateTo(String url) {
        LoggerUtils.info(getClass(), "Navigating to URL: " + url);
        driver.get(url);
    }

    public List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }
}
