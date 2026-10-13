package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * TrainerDashboardPage POM for Trainer portal (/trainer/dashboard).
 */
public class TrainerDashboardPage extends BasePage {

    private final By pageTitle = By.cssSelector(".page-title");
    private final By activeCohortsStat = By.xpath("//div[contains(@class, 'stat-card')][.//div[text()='Active Cohorts']]//div[contains(@class, 'stat-value')]");
    private final By completedCohortsStat = By.xpath("//div[contains(@class, 'stat-card')][.//div[text()='Completed Cohorts']]//div[contains(@class, 'stat-value')]");
    private final By mailsStat = By.xpath("//div[contains(@class, 'stat-card')][.//div[text()='Mails & Alerts']]//div[contains(@class, 'stat-value')]");
    private final By assignedCohortsTableRows = By.cssSelector("table tbody tr");
    private final By emptyStateRow = By.xpath("//table//tr//td[contains(text(), 'No cohorts have been assigned to you yet')]");
    private final By viewAllCohortsLink = By.xpath("//a[contains(@href, '/trainer/cohorts')]");
    private final By mailboxNavLink = By.xpath("//a[contains(@href, '/trainer/mail')]");
    private final By coachTabsHidden = By.xpath("//a[contains(@href, '/coach')]");
    private final By adminTabsHidden = By.xpath("//a[contains(@href, '/admin')]");

    public TrainerDashboardPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return getCurrentUrl().contains("/trainer") && isElementDisplayed(pageTitle);
    }

    public int getActiveCohortsCount() {
        try {
            return Integer.parseInt(getText(activeCohortsStat).trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public int getCompletedCohortsCount() {
        try {
            return Integer.parseInt(getText(completedCohortsStat).trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public int getMailsCount() {
        try {
            return Integer.parseInt(getText(mailsStat).trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isCoachOrAdminTabsVisible() {
        return !findElements(coachTabsHidden).isEmpty() || !findElements(adminTabsHidden).isEmpty();
    }

    public boolean isEmptyStateDisplayed() {
        return isElementDisplayed(emptyStateRow);
    }

    public void navigateToCohorts() {
        click(viewAllCohortsLink);
    }

    public void navigateToMailbox() {
        click(mailboxNavLink);
    }
}
