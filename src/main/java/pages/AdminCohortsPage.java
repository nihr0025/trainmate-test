package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * AdminCohortsPage POM for Admin's global cohort management and manual reassignment (/admin/cohorts).
 */
public class AdminCohortsPage extends BasePage {

    private final By pageTitle = By.cssSelector(".page-title");
    private final By searchInput = By.cssSelector("input[placeholder*='Search code']");
    private final By statusFilterSelect = By.xpath("(//select[contains(@class, 'form-select')])[1]");
    private final By serviceLineFilterSelect = By.xpath("(//select[contains(@class, 'form-select')])[2]");
    private final By exportReportBtn = By.xpath("//button[contains(text(), 'Export Allocation Report')]");
    private final By cohortRows = By.cssSelector("table tbody tr");
    private final By emptyStateRow = By.xpath("//table//tr//td[contains(text(), 'No matching cohorts')]");

    // Modal: Reassign Trainer
    private final By reassignModal = By.cssSelector("app-reassign-modal");
    private final By reassignTrainerSelect = By.cssSelector("app-reassign-modal select.form-select");
    private final By reassignReasonInput = By.cssSelector("app-reassign-modal input[type='text']");
    private final By reviewReassignmentBtn = By.xpath("//app-reassign-modal//button[contains(text(), 'Review Reassignment')]");
    private final By confirmReassignmentBtn = By.xpath("//app-reassign-modal//button[contains(text(), 'Confirm Reassignment')]");

    public AdminCohortsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return getCurrentUrl().contains("/admin/cohorts") && isElementDisplayed(pageTitle);
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

    public void filterByServiceLine(String serviceLine) {
        selectByVisibleText(serviceLineFilterSelect, serviceLine);
    }

    public int getDisplayedCohortsCount() {
        List<WebElement> rows = findElements(cohortRows);
        if (rows.size() == 1 && isElementDisplayed(emptyStateRow)) {
            return 0;
        }
        return rows.size();
    }

    public void clickExportAllocationReport() {
        click(exportReportBtn);
    }

    public void openReassignModal(String cohortCode) {
        By reassignBtn = By.xpath("//tr[.//td[contains(text(), '" + cohortCode + "')]]//button[contains(@class, 'btn-reassign') or .//i[contains(@class, 'bi-arrow-left-right')]]");
        click(reassignBtn);
        waitForVisibility(reassignModal);
    }

    public void reassignTrainer(String trainerName, String reason) {
        selectByVisibleText(reassignTrainerSelect, trainerName);
        if (reason != null) {
            enterText(reassignReasonInput, reason);
        }
        click(reviewReassignmentBtn);
        click(confirmReassignmentBtn);
    }

    public void changeCohortStatus(String cohortCode, String newStatus) {
        By statusSelectLocator = By.xpath("//tr[.//td[contains(text(), '" + cohortCode + "')]]//select[contains(@class, 'status-select')]");
        selectByVisibleText(statusSelectLocator, newStatus);
    }
}
