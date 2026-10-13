package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * TrainerCohortsPage POM for Trainer's assigned batches (/trainer/cohorts).
 */
public class TrainerCohortsPage extends BasePage {

    private final By pageTitle = By.cssSelector(".page-title");
    private final By searchInput = By.cssSelector("input[placeholder*='Search']");
    private final By statusFilterSelect = By.cssSelector("select");
    private final By cohortRows = By.cssSelector("table tbody tr");
    private final By emptyStateRow = By.xpath("//table//tr//td[contains(text(), 'No matching cohorts') or contains(text(), 'No cohorts')]");

    // Modal: Cohort Details
    private final By detailsModal = By.cssSelector("app-cohort-details-modal");
    private final By modalCohortCode = By.cssSelector("app-cohort-details-modal .modal-title");
    private final By modalSkills = By.xpath("//app-cohort-details-modal//small[contains(text(), 'Primary Skill')]/..//span");
    private final By modalLocation = By.xpath("//app-cohort-details-modal//small[contains(text(), 'Location')]/..//span");
    private final By modalDuration = By.xpath("//app-cohort-details-modal//small[contains(text(), 'Duration')]/..//span");
    private final By modalCloseBtn = By.xpath("//app-cohort-details-modal//button[contains(text(), 'Close') or contains(@class, 'btn-close')]");

    public TrainerCohortsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return getCurrentUrl().contains("/trainer/cohorts") && isElementDisplayed(pageTitle);
    }

    public void searchCohort(String query) {
        clearField(searchInput);
        if (query != null) {
            enterText(searchInput, query);
        }
    }

    public void filterByStatus(String status) {
        selectByVisibleText(statusFilterSelect, status);
    }

    public int getDisplayedCohortsCount() {
        List<WebElement> rows = findElements(cohortRows);
        if (rows.size() == 1 && isElementDisplayed(emptyStateRow)) {
            return 0;
        }
        return rows.size();
    }

    public void viewCohortDetails(String cohortCode) {
        By viewBtn = By.xpath("//tr[.//td[contains(text(), '" + cohortCode + "')]]//button[contains(@class, 'btn-view') or .//i[contains(@class, 'bi-eye')]]");
        click(viewBtn);
        waitForVisibility(detailsModal);
    }

    public String getModalCohortCode() {
        return getText(modalCohortCode);
    }

    public String getModalSkills() {
        return getText(modalSkills);
    }

    public String getModalLocation() {
        return getText(modalLocation);
    }

    public String getModalDuration() {
        return getText(modalDuration);
    }

    public void closeModal() {
        click(modalCloseBtn);
    }
}
