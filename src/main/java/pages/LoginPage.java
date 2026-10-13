package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.LoggerUtils;

/**
 * LoginPage POM representing https://train-mate-blond.vercel.app/login
 */
public class LoginPage extends BasePage {

    // Locators verified from Angular app source code
    private final By loginIdInput = By.id("loginId");
    private final By passwordInput = By.id("password");
    private final By signInButton = By.cssSelector("button.btn-signin");
    private final By ssoButton = By.cssSelector("button.btn-sso");
    private final By errorMessageAlert = By.cssSelector("div.login-alert.alert-danger span");
    private final By infoMessageAlert = By.cssSelector("div.login-alert.alert-info span");
    private final By togglePasswordBtn = By.cssSelector("button.eye-btn");
    private final By demoAccountsToggle = By.cssSelector("button.demo-toggle");
    private final By demoPills = By.cssSelector("button.demo-pill");
    private final By loginCard = By.cssSelector(".login-card");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void enterUsername(String username) {
        enterText(loginIdInput, username);
    }

    public void enterPassword(String password) {
        enterText(passwordInput, password);
    }

    public void clickSignIn() {
        click(signInButton);
    }

    public void login(String username, String password) {
        LoggerUtils.info(getClass(), "Attempting login for user: " + username);
        enterUsername(username);
        enterPassword(password);
        clickSignIn();
    }

    public String getErrorMessage() {
        try {
            return getText(errorMessageAlert);
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessageAlert);
    }

    public boolean isLoginPageLoaded() {
        return isElementDisplayed(loginIdInput) && isElementDisplayed(signInButton);
    }

    public void clickQuickDemoAccount(String role) {
        if (!isElementDisplayed(demoPills)) {
            click(demoAccountsToggle);
        }
        By pillLocator = By.xpath("//button[contains(@class, 'demo-pill') and contains(text(), '" + role + "')]");
        click(pillLocator);
    }
}
