package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * TrainerManagementPage POM for Admin's trainer pool management (/admin/trainers).
 */
public class TrainerManagementPage extends BasePage {

    private final By pageTitle = By.cssSelector(".page-title");
    private final By registerTrainerBtn = By.xpath("//button[contains(text(), 'Register New Trainer')]");
    private final By searchInput = By.cssSelector("input[placeholder*='Search by trainer name']");
    private final By statusFilterSelect = By.cssSelector("select.form-select");
    private final By trainerRows = By.cssSelector("table tbody tr");
    private final By emptyStateRow = By.xpath("//table//tr//td[contains(text(), 'No trainers found')]");

    // Modal: Trainer Form (Create & Edit)
    private final By formModal = By.cssSelector("app-trainer-form-modal");
    private final By modalTitle = By.cssSelector("app-trainer-form-modal .modal-title");
    private final By nameInput = By.cssSelector("app-trainer-form-modal input[name='name']");
    private final By emailInput = By.cssSelector("app-trainer-form-modal input[name='email']");
    private final By skillSetInput = By.cssSelector("app-trainer-form-modal input[name='skillSet']");
    private final By experienceYearsInput = By.cssSelector("app-trainer-form-modal input[name='experienceYears']");
    private final By availableFromInput = By.cssSelector("app-trainer-form-modal input[name='availableFrom']");
    private final By availableTillInput = By.cssSelector("app-trainer-form-modal input[name='availableTill']");
    private final By maxWorkloadInput = By.cssSelector("app-trainer-form-modal input[name='maxWorkload']");
    private final By submitTrainerBtn = By.cssSelector("app-trainer-form-modal button[type='submit']");
    private final By modalErrorAlert = By.cssSelector("app-trainer-form-modal .alert-danger");

    public TrainerManagementPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return getCurrentUrl().contains("/admin/trainers") && isElementDisplayed(pageTitle);
    }

    public void openRegisterTrainerModal() {
        click(registerTrainerBtn);
        waitForVisibility(formModal);
    }

    public void searchTrainer(String query) {
        clearField(searchInput);
        if (query != null) {
            enterText(searchInput, query);
        }
    }

    public void filterByAvailabilityStatus(String status) {
        selectByVisibleText(statusFilterSelect, status);
    }

    public int getDisplayedTrainersCount() {
        List<WebElement> rows = findElements(trainerRows);
        if (rows.size() == 1 && isElementDisplayed(emptyStateRow)) {
            return 0;
        }
        return rows.size();
    }

    public void fillTrainerDetails(String name, String email, String skills, String exp, String from, String till, String maxWorkload) {
        if (name != null) enterText(nameInput, name);
        if (email != null) enterText(emailInput, email);
        if (skills != null) enterText(skillSetInput, skills);
        if (exp != null) enterText(experienceYearsInput, exp);
        if (from != null) enterText(availableFromInput, from);
        if (till != null) enterText(availableTillInput, till);
        if (maxWorkload != null) enterText(maxWorkloadInput, maxWorkload);
    }

    public void submitTrainerForm() {
        click(submitTrainerBtn);
    }

    public String getModalErrorMessage() {
        return getText(modalErrorAlert);
    }

    public boolean isModalErrorDisplayed() {
        return isElementDisplayed(modalErrorAlert);
    }

    public void openEditTrainer(String trainerName) {
        By editBtn = By.xpath("//tr[.//td[contains(text(), '" + trainerName + "')]]//button[contains(@class, 'btn-edit')]");
        click(editBtn);
        waitForVisibility(formModal);
    }
}
