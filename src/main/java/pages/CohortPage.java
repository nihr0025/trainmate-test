package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LoggerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * CohortPage POM for Coach's My Cohorts view (/coach/cohorts).
 */
public class CohortPage extends BasePage {

    private final By pageTitle = By.cssSelector(".page-title");
    private final By searchInput = By.cssSelector("input[placeholder*='Search by cohort code']");
    private final By statusFilterSelect = By.cssSelector("select.form-select");
    private final By clearFiltersBtn = By.xpath("//button[contains(text(), 'Clear Filters') or contains(text(), 'Reset')]");
    private final By cohortTableRows = By.cssSelector("table tbody tr");
    private final By emptyStateRow = By.xpath("//table//tr//td[contains(text(), 'No cohorts found') or .//h6[contains(text(), 'No cohorts found')]]");
    private final By createCohortBtn = By.xpath("//button[contains(text(), 'Create Cohort')]");

    // Action buttons inside rows
    private final By viewDetailsBtn = By.cssSelector("button.btn-view");
    private final By editCohortBtn = By.cssSelector("button.btn-edit");
    private final By reallocateBtn = By.cssSelector("button.btn-reallocate");
    private final By deleteBtn = By.cssSelector("button.btn-delete");

    // Modal: Edit Cohort
    private final By editModalTitle = By.xpath("//h5[contains(text(), 'Edit Cohort')]");
    private final By editSkillsInput = By.cssSelector("input[name='requiredSkill']");
    private final By editTraineesInput = By.cssSelector("input[name='numberOfTrainees']");
    private final By editStartDateInput = By.cssSelector("input[name='startDate']");
    private final By editEndDateInput = By.cssSelector("input[name='endDate']");
    private final By editLocationSelect = By.cssSelector("select[name='location']");
    private final By editSaveBtn = By.xpath("//button[contains(text(), 'Save Changes')]");

    // Modal: Cohort Details (100-Point breakdown)
    private final By detailsModalTitle = By.cssSelector("app-cohort-details-modal .modal-title");
    private final By detailsAssignedTrainer = By.xpath("//app-cohort-details-modal//small[contains(text(), 'Assigned Trainer')]/..//h6");
    private final By detailsScoreCircle = By.cssSelector(".score-hero-value");
    private final By detailsCloseBtn = By.xpath("//app-cohort-details-modal//button[contains(text(), 'Close') or contains(@class, 'btn-close')]");

    public CohortPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return getCurrentUrl().contains("/coach/cohorts") && isElementDisplayed(pageTitle);
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
        List<WebElement> rows = findElements(cohortTableRows);
        if (rows.size() == 1 && isElementDisplayed(emptyStateRow)) {
            return 0;
        }
        return rows.size();
    }

    public List<String> getCohortCodesInTable() {
        List<String> codes = new ArrayList<>();
        List<WebElement> rows = findElements(cohortTableRows);
        for (WebElement row : rows) {
            try {
                WebElement codeCell = row.findElement(By.cssSelector("td[data-label='Cohort Code']"));
                codes.add(codeCell.getText().trim());
            } catch (Exception ignored) {}
        }
        return codes;
    }

    public void openEditCohort(String cohortCode) {
        By editBtnLocator = By.xpath("//tr[.//td[contains(text(), '" + cohortCode + "')]]//button[contains(@class, 'btn-edit')]");
        click(editBtnLocator);
        waitForVisibility(editModalTitle);
    }

    public void updateCohortSkillsAndDates(String newSkills, String newStartDate, String newEndDate) {
        if (newSkills != null) enterText(editSkillsInput, newSkills);
        if (newStartDate != null) enterText(editStartDateInput, newStartDate);
        if (newEndDate != null) enterText(editEndDateInput, newEndDate);
        click(editSaveBtn);
    }

    public void viewCohortDetails(String cohortCode) {
        By viewBtnLocator = By.xpath("//tr[.//td[contains(text(), '" + cohortCode + "')]]//button[contains(@class, 'btn-view')]");
        click(viewBtnLocator);
        waitForVisibility(detailsModalTitle);
    }

    public String getAssignedTrainerNameInDetails() {
        return getText(detailsAssignedTrainer);
    }

    public void closeDetailsModal() {
        click(detailsCloseBtn);
    }

    public void clickReallocate(String cohortCode) {
        By reallocBtnLocator = By.xpath("//tr[.//td[contains(text(), '" + cohortCode + "')]]//button[contains(@class, 'btn-reallocate')]");
        click(reallocBtnLocator);
    }
}
