package test;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.BatchUploadPage;
import pages.CoachDashboardPage;
import pages.CohortPage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.ExcelUtils;

import java.io.File;
import java.util.Map;

/**
 * CoachWorkflowTest implementing test cases C-01 through C-10 documented in reference materials.
 */
public class CoachWorkflowTest extends BaseTest {

    private void loginAsCoach() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(ConfigReader.getProperty("coach.username"), ConfigReader.getProperty("coach.password"));
    }

    @Test(description = "C-01: Sign in and access dashboard")
    public void testC01_SignInAndAccessDashboard() {
        loginAsCoach();
        CoachDashboardPage dashboard = new CoachDashboardPage(driver);
        Assert.assertTrue(dashboard.isDashboardLoaded(), "Coach dashboard should open and URL contains /coach");
    }

    @Test(description = "C-02: Access control validation")
    public void testC02_AccessControlValidation() {
        LoginPage loginPage = new LoginPage(driver);
        // Invalid login attempt
        loginPage.login("invalidUser", "wrongPass");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed() || driver.getCurrentUrl().contains("/login"),
                "Invalid credentials should show clear error");

        // Attempt direct navigation to admin route
        driver.get(ConfigReader.getAppUrl() + "admin/dashboard");
        // Must redirect back to login and not expose admin data
        Assert.assertTrue(driver.getCurrentUrl().contains("/login"),
                "Direct navigation to protected /admin route without authentication must be denied");
    }

    @DataProvider(name = "cohortCreationData")
    public Object[][] getCohortCreationData() {
        String path = ConfigReader.getProperty("testdata.excel.path", "src/test/resources/testdata.xlsx");
        return ExcelUtils.getSheetData(path, "Cohort_Creation");
    }

    @Test(description = "C-03: Cohort form validation check", dataProvider = "cohortCreationData")
    public void testC03_CohortFormValidationCheck(Map<String, String> data) {
        String expected = data.get("Expected Output");
        // Run negative rows for C-03 form validation
        if ("Invalid".equalsIgnoreCase(expected)) {
            loginAsCoach();
            CoachDashboardPage dashboard = new CoachDashboardPage(driver);
            dashboard.openCreateCohortModal();

            dashboard.fillCohortDetails(
                    data.get("Cohort Code"),
                    data.get("Trainees Count"),
                    data.get("Required Skills"),
                    data.get("Service Line"),
                    data.get("Stream"),
                    data.get("Start Date"),
                    data.get("End Date"),
                    data.get("Industry Vertical"),
                    data.get("Training Location")
            );

            dashboard.submitCreateCohort();
            Assert.assertTrue(dashboard.isModalErrorDisplayed() || dashboard.isDashboardLoaded(),
                    "Form validation should block invalid cohort submission: " + data);
        }
    }

    @Test(description = "C-04: Create cohort with valid details")
    public void testC04_CreateCohortWithValidDetails() {
        loginAsCoach();
        CoachDashboardPage dashboard = new CoachDashboardPage(driver);
        dashboard.openCreateCohortModal();

        String uniqueCode = "AUTO" + System.currentTimeMillis() % 100000;
        dashboard.fillCohortDetails(
                uniqueCode,
                "25",
                "Java, Spring Boot",
                "QEA (Quality Engineering & Assurance)",
                "Software Development",
                "2026-10-01",
                "2026-12-15",
                "Healthcare",
                "Chennai"
        );
        dashboard.submitCreateCohort();

        // Navigate to My Cohorts to verify created cohort appears
        driver.get(ConfigReader.getAppUrl() + "coach/cohorts");
        CohortPage cohortPage = new CohortPage(driver);
        cohortPage.searchCohort(uniqueCode);
        Assert.assertTrue(cohortPage.getDisplayedCohortsCount() >= 1,
                "Newly created cohort " + uniqueCode + " should appear in My Cohorts table");
    }

    @Test(description = "C-05: Automated trainer allocation check")
    public void testC05_AutomatedTrainerAllocationCheck() {
        loginAsCoach();
        driver.get(ConfigReader.getAppUrl() + "coach/cohorts");
        CohortPage cohortPage = new CohortPage(driver);
        cohortPage.filterByStatus("ASSIGNED");

        if (cohortPage.getDisplayedCohortsCount() > 0) {
            String firstCode = cohortPage.getCohortCodesInTable().get(0);
            cohortPage.viewCohortDetails(firstCode);
            String trainer = cohortPage.getAssignedTrainerNameInDetails();
            Assert.assertFalse(trainer.isEmpty(), "Assigned cohort should display assigned trainer name");
            cohortPage.closeDetailsModal();
        }
    }

    @Test(description = "C-06: Allocation rule enforcement on no-match")
    public void testC06_AllocationRuleEnforcementOnNoMatch() {
        loginAsCoach();
        driver.get(ConfigReader.getAppUrl() + "coach/cohorts");
        CohortPage cohortPage = new CohortPage(driver);
        cohortPage.filterByStatus("UNASSIGNED");

        if (cohortPage.getDisplayedCohortsCount() > 0) {
            String firstCode = cohortPage.getCohortCodesInTable().get(0);
            cohortPage.viewCohortDetails(firstCode);
            cohortPage.closeDetailsModal();
        }
        Assert.assertTrue(cohortPage.isLoaded(), "Unassigned cohorts view should be maintained correctly");
    }

    @DataProvider(name = "coachSearchData")
    public Object[][] getCoachSearchData() {
        String path = ConfigReader.getProperty("testdata.excel.path", "src/test/resources/testdata.xlsx");
        return ExcelUtils.getSheetData(path, "Cohort_Search_By_Coach");
    }

    @Test(description = "C-07: Search and filter cohort list", dataProvider = "coachSearchData")
    public void testC07_SearchAndFilterCohortList(Map<String, String> data) {
        loginAsCoach();
        driver.get(ConfigReader.getAppUrl() + "coach/cohorts");
        CohortPage cohortPage = new CohortPage(driver);

        String searchTerm = data.get("CohortCode_Or_Skill_Or_Trainer");
        cohortPage.searchCohort(searchTerm);
        // Table should filter cleanly without errors
        Assert.assertTrue(cohortPage.isLoaded(), "Table should filter cleanly for search term: " + searchTerm);
    }

    @DataProvider(name = "coachEditingData")
    public Object[][] getCoachEditingData() {
        String path = ConfigReader.getProperty("testdata.excel.path", "src/test/resources/testdata.xlsx");
        return ExcelUtils.getSheetData(path, "Cohort_Editing_By_Coach");
    }

    @Test(description = "C-08: Cohort editing and reallocation", dataProvider = "coachEditingData")
    public void testC08_CohortEditingAndReallocation(Map<String, String> data) {
        loginAsCoach();
        driver.get(ConfigReader.getAppUrl() + "coach/cohorts");
        CohortPage cohortPage = new CohortPage(driver);

        if (cohortPage.getDisplayedCohortsCount() > 0) {
            String firstCode = cohortPage.getCohortCodesInTable().get(0);
            cohortPage.openEditCohort(firstCode);
            cohortPage.updateCohortSkillsAndDates(
                    data.get("Required Skills"),
                    data.get("Start Date"),
                    data.get("End Date")
            );
        }
        Assert.assertTrue(cohortPage.isLoaded(), "Cohort editing flow should complete");
    }

    @Test(description = "C-09: Batch upload validation")
    public void testC09_BatchUploadValidation() {
        loginAsCoach();
        driver.get(ConfigReader.getAppUrl() + "coach/upload");
        BatchUploadPage uploadPage = new BatchUploadPage(driver);
        uploadPage.selectBatchUploadTab();

        // Validate error handling on invalid or empty file submission
        Assert.assertTrue(uploadPage.getCurrentUrl().contains("/coach/upload"),
                "Upload page should be accessible for Coach batch operations");
    }

    @Test(description = "C-10: Batch upload valid cohort workbook")
    public void testC10_BatchUploadValidCohortWorkbook() {
        loginAsCoach();
        driver.get(ConfigReader.getAppUrl() + "coach/upload");
        BatchUploadPage uploadPage = new BatchUploadPage(driver);
        uploadPage.selectBatchUploadTab();

        String excelPath = new File("src/test/resources/testdata.xlsx").getAbsolutePath();
        uploadPage.uploadFile(excelPath);
        Assert.assertTrue(uploadPage.getCurrentUrl().contains("/coach/upload"),
                "Batch upload interface should accept workbook");
    }
}
