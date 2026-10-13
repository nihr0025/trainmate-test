package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * AdminDashboardPage POM for Admin oversight portal (/admin/dashboard).
 */
public class AdminDashboardPage extends BasePage {

    private final By pageTitle = By.cssSelector(".page-title");
    private final By exportReportBtn = By.xpath("//button[contains(text(), 'Export Report')]");
    private final By manageCohortsBtn = By.xpath("//a[contains(@href, '/admin/cohorts')]");
    private final By inspectTrainersBtn = By.xpath("//a[contains(@href, '/admin/trainers')]");

    // Stats Grid
    private final By totalCohortsStat = By.xpath("//div[contains(@class, 'stat-card')][.//div[text()='Total Cohorts']]//div[contains(@class, 'stat-value')]");
    private final By assignedCohortsStat = By.xpath("//div[contains(@class, 'stat-card')][.//div[text()='Assigned Cohorts']]//div[contains(@class, 'stat-value')]");
    private final By unassignedCohortsStat = By.xpath("//div[contains(@class, 'stat-card')][.//div[text()='Unassigned Cohorts']]//div[contains(@class, 'stat-value')]");
    private final By availableTrainersStat = By.xpath("//div[contains(@class, 'stat-card')][.//div[text()='Available Trainers']]//div[contains(@class, 'stat-value')]");

    // Distribution
    private final By activeCohortsDistribution = By.xpath("//span[contains(text(), 'Active (In Progress)')]/following-sibling::span");
    private final By totalRegisteredTrainers = By.xpath("//span[contains(text(), 'Total Registered Trainers')]/following-sibling::span");

    public AdminDashboardPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return getCurrentUrl().contains("/admin") && isElementDisplayed(pageTitle);
    }

    public void clickExportReport() {
        click(exportReportBtn);
    }

    public void navigateToCohorts() {
        click(manageCohortsBtn);
    }

    public void navigateToTrainers() {
        click(inspectTrainersBtn);
    }

    public int getTotalCohorts() {
        try {
            return Integer.parseInt(getText(totalCohortsStat).trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public int getAssignedCohorts() {
        try {
            return Integer.parseInt(getText(assignedCohortsStat).trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public int getUnassignedCohorts() {
        try {
            return Integer.parseInt(getText(unassignedCohortsStat).trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public String getAvailableTrainersRatio() {
        return getText(availableTrainersStat).trim();
    }

    public int getTotalRegisteredTrainers() {
        try {
            return Integer.parseInt(getText(totalRegisteredTrainers).trim());
        } catch (Exception e) {
            return 0;
        }
    }
}
