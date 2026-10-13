package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LoggerUtils;

import java.util.List;

/**
 * CoachDashboardPage POM for Coach role dashboard (/coach/dashboard).
 */
public class CoachDashboardPage extends BasePage {

    private final By pageTitle = By.cssSelector(".page-title");
    private final By createCohortBtn = By.xpath("//button[contains(text(), 'Create Cohort') or .//i[contains(@class, 'bi-plus-circle-fill')]]");
    private final By uploadBatchBtn = By.xpath("//a[contains(@href, '/coach/upload') or contains(text(), 'Upload')]");
    private final By totalCohortsStat = By.xpath("//div[contains(@class, 'stat-card')][.//div[text()='Total Cohorts']]//div[contains(@class, 'stat-value')]");
    private final By assignedCohortsStat = By.xpath("//div[contains(@class, 'stat-card')][.//div[text()='Assigned Cohorts']]//div[contains(@class, 'stat-value')]");
    private final By unassignedCohortsStat = By.xpath("//div[contains(@class, 'stat-card')][.//div[text()='Unassigned Cohorts']]//div[contains(@class, 'stat-value')]");
    private final By recentCohortsTableRows = By.cssSelector("table tbody tr");
    private final By userRoleBadge = By.cssSelector("span.badge");

    // Modal: Create Cohort
    private final By modalTitle = By.cssSelector(".modal-title");
    private final By cohortCodeInput = By.cssSelector("input[name='cohortCode']");
    private final By numberOfTraineesInput = By.cssSelector("input[name='numberOfTrainees']");
    private final By requiredSkillInput = By.cssSelector("input[name='requiredSkill']");
    private final By serviceLineSelect = By.cssSelector("select[name='serviceLine']");
    private final By streamSelect = By.cssSelector("select[name='stream']");
    private final By startDateInput = By.cssSelector("input[name='startDate']");
    private final By endDateInput = By.cssSelector("input[name='endDate']");
    private final By verticalSelect = By.cssSelector("select[name='vertical']");
    private final By locationSelect = By.cssSelector("select[name='location']");
    private final By modalSubmitBtn = By.cssSelector("button[type='submit']");
    private final By modalCancelBtn = By.xpath("//div[contains(@class, 'modal-footer')]//button[contains(text(), 'Cancel')]");
    private final By modalErrorAlert = By.cssSelector(".modal-body .alert-danger");

    public CoachDashboardPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDashboardLoaded() {
        return getCurrentUrl().contains("/coach") && isElementDisplayed(pageTitle);
    }

    public String getPageTitleText() {
        return getText(pageTitle);
    }

    public int getTotalCohorts() {
        try {
            return Integer.parseInt(getText(totalCohortsStat).replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getAssignedCohorts() {
        try {
            return Integer.parseInt(getText(assignedCohortsStat).replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getUnassignedCohorts() {
        try {
            return Integer.parseInt(getText(unassignedCohortsStat).replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    public void openCreateCohortModal() {
        click(createCohortBtn);
        waitForVisibility(modalTitle);
    }

    public void fillCohortDetails(String code, String trainees, String skills, String serviceLine,
                                  String stream, String startDate, String endDate, String vertical, String location) {
        if (code != null) enterText(cohortCodeInput, code);
        if (trainees != null) enterText(numberOfTraineesInput, trainees);
        if (skills != null) enterText(requiredSkillInput, skills);
        if (serviceLine != null) selectByVisibleText(serviceLineSelect, serviceLine);
        if (stream != null) selectByVisibleText(streamSelect, stream);
        if (startDate != null) enterText(startDateInput, startDate);
        if (endDate != null) enterText(endDateInput, endDate);
        if (vertical != null) selectByVisibleText(verticalSelect, vertical);
        if (location != null) selectByVisibleText(locationSelect, location);
    }

    public void submitCreateCohort() {
        click(modalSubmitBtn);
    }

    public String getModalErrorMessage() {
        return getText(modalErrorAlert);
    }

    public boolean isModalErrorDisplayed() {
        return isElementDisplayed(modalErrorAlert);
    }

    public void navigateToUpload() {
        click(uploadBatchBtn);
    }

    public List<WebElement> getRecentCohortRows() {
        return findElements(recentCohortsTableRows);
    }
}
