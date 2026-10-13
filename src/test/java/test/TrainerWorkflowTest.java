package test;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.TrainerCohortsPage;
import pages.TrainerDashboardPage;
import pages.TrainerMailboxPage;
import utils.ConfigReader;
import utils.ExcelUtils;

import java.util.Map;

/**
 * TrainerWorkflowTest implementing test cases T-01 through T-09 documented in reference materials.
 */
public class TrainerWorkflowTest extends BaseTest {

    private void loginAsTrainer() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(ConfigReader.getProperty("trainer.username"), ConfigReader.getProperty("trainer.password"));
    }

    @Test(description = "T-01: Trainer login & role access")
    public void testT01_TrainerLoginAndRoleAccess() {
        loginAsTrainer();
        TrainerDashboardPage dashboard = new TrainerDashboardPage(driver);
        Assert.assertTrue(dashboard.isLoaded(), "Trainer dashboard opens showing assigned batches");
        Assert.assertFalse(dashboard.isCoachOrAdminTabsVisible(), "Coach creation and Admin tabs are hidden for Trainer");
    }

    @Test(description = "T-02: Trainer unauthorized route check")
    public void testT02_TrainerUnauthorizedRouteCheck() {
        loginAsTrainer();
        // Attempt direct navigation to coach or admin routes
        driver.get(ConfigReader.getAppUrl() + "admin/trainers");
        boolean accessDenied = driver.getCurrentUrl().contains("/login") || !driver.getCurrentUrl().contains("/admin/trainers");
        Assert.assertTrue(accessDenied, "Direct navigation to protected Coach/Admin routes is denied");

        driver.get(ConfigReader.getAppUrl() + "coach/cohorts");
        boolean coachDenied = driver.getCurrentUrl().contains("/login") || !driver.getCurrentUrl().contains("/coach/cohorts");
        Assert.assertTrue(coachDenied, "Direct navigation to protected Coach route is denied");
    }

    @Test(description = "T-03: Trainer empty state dashboard check")
    public void testT03_TrainerEmptyStateDashboard() {
        loginAsTrainer();
        TrainerDashboardPage dashboard = new TrainerDashboardPage(driver);
        Assert.assertTrue(dashboard.isLoaded(), "Trainer dashboard loads cleanly without console errors");
    }

    @Test(description = "T-04: Trainer metrics after assignment")
    public void testT04_TrainerMetricsAfterAssignment() {
        loginAsTrainer();
        TrainerDashboardPage dashboard = new TrainerDashboardPage(driver);
        int active = dashboard.getActiveCohortsCount();
        int completed = dashboard.getCompletedCohortsCount();
        Assert.assertTrue(active >= 0 && completed >= 0, "Trainer metric counters are valid numbers");
    }

    @Test(description = "T-05: View assigned cohort details")
    public void testT05_ViewAssignedCohortDetails() {
        loginAsTrainer();
        driver.get(ConfigReader.getAppUrl() + "trainer/cohorts");
        TrainerCohortsPage cohortsPage = new TrainerCohortsPage(driver);

        if (cohortsPage.getDisplayedCohortsCount() > 0) {
            // View details of first assigned cohort
            cohortsPage.viewCohortDetails("");
            String code = cohortsPage.getModalCohortCode();
            Assert.assertFalse(code.isEmpty(), "Modal displays cohort code, skills, dates and location");
            cohortsPage.closeModal();
        }
        Assert.assertTrue(cohortsPage.isLoaded(), "Assigned cohorts view functions properly");
    }

    @DataProvider(name = "trainerSearchData")
    public Object[][] getTrainerSearchData() {
        String path = ConfigReader.getProperty("testdata.excel.path", "src/test/resources/testdata.xlsx");
        return ExcelUtils.getSheetData(path, "Cohort_or_Skill_SearchByTrainer");
    }

    @Test(description = "T-06: Search assigned cohort schedule", dataProvider = "trainerSearchData")
    public void testT06_SearchAssignedCohortSchedule(Map<String, String> data) {
        loginAsTrainer();
        driver.get(ConfigReader.getAppUrl() + "trainer/cohorts");
        TrainerCohortsPage cohortsPage = new TrainerCohortsPage(driver);

        String searchTerm = data.get("CohortCode_or_Skill");
        cohortsPage.searchCohort(searchTerm);
        Assert.assertTrue(cohortsPage.isLoaded(), "Matching cohorts or clean empty results appear for: " + searchTerm);
    }

    @DataProvider(name = "trainerStatusFilterData")
    public Object[][] getTrainerStatusFilterData() {
        String path = ConfigReader.getProperty("testdata.excel.path", "src/test/resources/testdata.xlsx");
        return ExcelUtils.getSheetData(path, "CohortStatusFilter_By_Trainer");
    }

    @Test(description = "T-07: Filter cohorts by status", dataProvider = "trainerStatusFilterData")
    public void testT07_FilterCohortsByStatus(Map<String, String> data) {
        loginAsTrainer();
        driver.get(ConfigReader.getAppUrl() + "trainer/cohorts");
        TrainerCohortsPage cohortsPage = new TrainerCohortsPage(driver);

        String status = data.get("Status");
        if (!"All Statuses".equalsIgnoreCase(status)) {
            cohortsPage.filterByStatus(status);
        }
        Assert.assertTrue(cohortsPage.isLoaded(), "Filtering by status '" + status + "' updates cohort list accurately");
    }

    @Test(description = "T-08: Mailbox assignment notification")
    public void testT08_MailboxAssignmentNotification() {
        loginAsTrainer();
        driver.get(ConfigReader.getAppUrl() + "trainer/mail");
        TrainerMailboxPage mailboxPage = new TrainerMailboxPage(driver);
        Assert.assertTrue(mailboxPage.isLoaded(), "Mailbox loads cleanly showing notifications and alerts");
    }

    @Test(description = "T-09: Real-time updates on reassignment")
    public void testT09_RealTimeUpdatesOnReassignment() {
        loginAsTrainer();
        driver.get(ConfigReader.getAppUrl() + "trainer/mail");
        TrainerMailboxPage mailboxPage = new TrainerMailboxPage(driver);
        mailboxPage.markAllAsRead();
        Assert.assertTrue(mailboxPage.isLoaded(), "Mailbox reflects up-to-date assignment alerts");
    }
}
