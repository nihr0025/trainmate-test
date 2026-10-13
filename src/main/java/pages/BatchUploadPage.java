package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * BatchUploadPage POM for Coach batch Excel uploads (/coach/upload).
 */
public class BatchUploadPage extends BasePage {

    private final By manualTabBtn = By.xpath("//button[contains(text(), 'Create Single Cohort')]");
    private final By batchTabBtn = By.xpath("//button[contains(text(), 'Batch Upload')]");
    private final By fileInput = By.cssSelector("input[type='file']");
    private final By uploadAndAllocateBtn = By.xpath("//button[contains(text(), 'Upload & Allocate All Rows') or contains(text(), 'Uploading Excel')]");
    private final By downloadTemplateBtn = By.xpath("//button[contains(text(), 'Download Sample Excel')]");
    private final By uploadSuccessSummary = By.xpath("//h3[contains(text(), 'Upload Completed')]");
    private final By uploadedRowsCount = By.xpath("//div[text()='Uploaded']/following-sibling::div");
    private final By failedRowsCount = By.xpath("//div[text()='Failed']/following-sibling::div");
    private final By validationErrorsTable = By.xpath("//h6[contains(text(), 'Validation Errors')]/..//table");
    private final By errorMessageAlert = By.cssSelector(".alert-danger");

    public BatchUploadPage(WebDriver driver) {
        super(driver);
    }

    public void selectBatchUploadTab() {
        click(batchTabBtn);
    }

    public void uploadFile(String absoluteFilePath) {
        selectBatchUploadTab();
        uploadFile(fileInput, absoluteFilePath);
    }

    public void clickUploadAndAllocate() {
        click(uploadAndAllocateBtn);
    }

    public boolean isUploadCompleted() {
        return isElementDisplayed(uploadSuccessSummary);
    }

    public int getSuccessfulUploadedCount() {
        try {
            return Integer.parseInt(getText(uploadedRowsCount).trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public int getFailedRowsCount() {
        try {
            return Integer.parseInt(getText(failedRowsCount).trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean areValidationErrorsDisplayed() {
        return isElementDisplayed(validationErrorsTable) || isElementDisplayed(errorMessageAlert);
    }

    public String getErrorMessage() {
        return getText(errorMessageAlert);
    }
}
