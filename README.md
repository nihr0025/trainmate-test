# TrainMate Automation Testing Framework

Comprehensive end-to-end Test Automation Framework built with **Java**, **Selenium WebDriver**, **TestNG**, **Page Object Model (POM)**, **Apache POI**, and **ExtentReports** for the **TrainMate - Cohort & Trainer Automatic Allocation System**.

---

## 1. Application Overview
- **Application URL:** https://train-mate-blond.vercel.app/
- **Backend API:** https://trainmate-o6tv.onrender.com/api
- **Core Capabilities:**
  - Automated 100-point deterministic trainer allocation algorithm
  - Role-based access control for 3 distinct user personas: **Coach**, **Trainer**, and **Admin**
  - Batch Excel cohort onboarding and validation
  - Workload tracking and manual override reassignment

---

## 2. Test User Accounts
| Role | Username | Password | Purpose |
| :--- | :--- | :--- | :--- |
| **Coach** | `coach01` | `coach123` | Cohort creation, batch Excel upload, cohort schedule oversight |
| **Trainer** | `trainer01` | `trainer123` | Assigned cohort calendar, skill profile, assignment mailbox |
| **Admin** | `admin01` | `admin123` | Master oversight, trainer registration, manual reassignment |

---

## 3. Technology Stack & Architecture
- **Language:** Java 17+
- **Build Tool:** Apache Maven
- **Automation Engine:** Selenium WebDriver (v4.21.0) with Selenium Manager
- **Test Runner:** TestNG (v7.10.2)
- **Design Pattern:** Page Object Model (POM)
- **Data-Driven Engine:** Apache POI OOXML (v5.2.5) reading `src/test/resources/testdata.xlsx`
- **Reporting:** ExtentReports 5 Spark Reporter + Maven Surefire XML/HTML reports
- **Logging:** Log4j2
- **Execution Mode:** Headless Chrome by default (configurable)

---

## 4. Directory Structure
```text
trainmate-test/
├── pom.xml                               # Maven project dependencies and build configuration
├── README.md                             # Documentation and user guide
├── testng.xml                            # TestNG suite discovery configuration
├── .gitignore                            # Git ignore configuration
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── base/
│   │       │   └── BasePage.java         # Common Selenium interaction abstractions
│   │       ├── pages/
│   │       │   ├── LoginPage.java        # Authentication page object
│   │       │   ├── CoachDashboardPage.java # Coach dashboard and modal controls
│   │       │   ├── CohortPage.java       # Coach managed cohorts table
│   │       │   ├── BatchUploadPage.java  # Excel batch upload and validation
│   │       │   ├── TrainerDashboardPage.java # Trainer dashboard and metrics
│   │       │   ├── TrainerCohortsPage.java # Trainer assigned cohorts view
│   │       │   ├── TrainerMailboxPage.java # Trainer mailbox and notifications
│   │       │   ├── AdminDashboardPage.java # Admin master oversight dashboard
│   │       │   ├── AdminCohortsPage.java # Admin cohort pool and manual reassignment
│   │       │   └── TrainerManagementPage.java # Admin trainer registration & pool
│   │       ├── utils/
│   │       │   ├── ConfigReader.java     # Centralized property reader
│   │       │   ├── DriverFactory.java    # ThreadLocal browser factory
│   │       │   ├── ExcelUtils.java       # Apache POI Excel dynamic data provider
│   │       │   ├── ExcelDataInitializer.java # Testdata generator matching reference specs
│   │       │   ├── WaitUtils.java        # Explicit wait utilities
│   │       │   ├── ScreenshotUtils.java  # Automatic failure screenshot generator
│   │       │   └── LoggerUtils.java      # Safe logging without exposing secrets
│   │       └── listeners/
│   │           └── TestListener.java     # TestNG listener for ExtentReports and screenshots
│   └── test/
│       ├── java/
│       │   ├── base/
│       │   │   └── BaseTest.java         # Test lifecycle (@BeforeMethod, @AfterMethod)
│       │   └── test/
│       │       ├── LoginTest.java        # Role authentication and negative login tests
│       │       ├── CoachWorkflowTest.java # C-01 to C-10 test cases
│       │       ├── TrainerWorkflowTest.java # T-01 to T-09 test cases
│       │       └── AdminWorkflowTest.java # A-01 to A-10 test cases
│       └── resources/
│           ├── config.properties         # Environment settings and credentials
│           ├── testdata.xlsx             # Data-driven workbook with 13 worksheets
│           ├── testng.xml                # Resources-level testng suite definition
│           └── log4j2.xml                # Logging configuration
├── reports/
│   └── ExtentReport.html                 # Comprehensive ExtentReports HTML dashboard
└── screenshots/                          # Timestamped failure screenshots
```

---

## 5. Excel Data Workbook Structure (`testdata.xlsx`)
The framework reads test data directly from `src/test/resources/testdata.xlsx` using Apache POI across 13 distinct sheets:
1. `LoginData` - Positive & negative login credentials
2. `Cohort_Creation` - Valid cohort creation and boundary field validations
3. `Cohort_Editing_By_Coach` - Skill sets, date ranges, and stream edits
4. `Cohort_Search_By_Coach` - Coach search queries (Cohort code, skill, trainer)
5. `CohortStatusfilter_By_Coach` - Status filters (Assigned, Unassigned, Active, etc.)
6. `Cohort_or_Skill_SearchByTrainer` - Trainer cohort schedule search
7. `CohortStatusFilter_By_Trainer` - Trainer status dropdown filter
8. `Cohort_Search_By_Admin` - Global cohort search across coach, skill, and code
9. `CohortStatusfilter_By_Admin` - Admin status filters
10. `CohortSLfilter_By_Admin` - Service line filter (ADM, QEA)
11. `Trainer_Search_By_Admin` - Trainer pool search by name, skill, service line
12. `TrainerFilterByStatus_By_Admin` - Trainer availability status filters
13. `TrainerRegistration_By_Admin` - Form validation & valid trainer registration rows

---

## 6. Test Scenario Coverage Matrix
| Module | Test Case ID | Scenario Name | Test Method | Excel Sheet |
| :--- | :--- | :--- | :--- | :--- |
| **Auth** | L-01 | Role Authentication | `LoginTest.testLoginWithExcelData` | `LoginData` |
| **Coach** | C-01 | Sign in and access dashboard | `CoachWorkflowTest.testC01_SignInAndAccessDashboard` | Config |
| **Coach** | C-02 | Access control validation | `CoachWorkflowTest.testC02_AccessControlValidation` | Config |
| **Coach** | C-03 | Cohort form validation check | `CoachWorkflowTest.testC03_CohortFormValidationCheck` | `Cohort_Creation` |
| **Coach** | C-04 | Create cohort with valid details | `CoachWorkflowTest.testC04_CreateCohortWithValidDetails` | `Cohort_Creation` |
| **Coach** | C-05 | Automated trainer allocation check | `CoachWorkflowTest.testC05_AutomatedTrainerAllocationCheck` | App / Engine |
| **Coach** | C-06 | Allocation rule enforcement on no-match | `CoachWorkflowTest.testC06_AllocationRuleEnforcementOnNoMatch` | App / Engine |
| **Coach** | C-07 | Search and filter cohort list | `CoachWorkflowTest.testC07_SearchAndFilterCohortList` | `Cohort_Search_By_Coach` |
| **Coach** | C-08 | Cohort editing and reallocation | `CoachWorkflowTest.testC08_CohortEditingAndReallocation` | `Cohort_Editing_By_Coach` |
| **Coach** | C-09 | Batch upload validation | `CoachWorkflowTest.testC09_BatchUploadValidation` | App UI |
| **Coach** | C-10 | Batch upload valid cohort workbook | `CoachWorkflowTest.testC10_BatchUploadValidCohortWorkbook` | `testdata.xlsx` |
| **Trainer**| T-01 | Trainer login & role access | `TrainerWorkflowTest.testT01_TrainerLoginAndRoleAccess` | Config |
| **Trainer**| T-02 | Trainer unauthorized route check | `TrainerWorkflowTest.testT02_TrainerUnauthorizedRouteCheck` | Config |
| **Trainer**| T-03 | Trainer empty state dashboard check | `TrainerWorkflowTest.testT03_TrainerEmptyStateDashboard` | App UI |
| **Trainer**| T-04 | Trainer metrics after assignment | `TrainerWorkflowTest.testT04_TrainerMetricsAfterAssignment` | App UI |
| **Trainer**| T-05 | View assigned cohort details | `TrainerWorkflowTest.testT05_ViewAssignedCohortDetails` | App UI |
| **Trainer**| T-06 | Search assigned cohort schedule | `TrainerWorkflowTest.testT06_SearchAssignedCohortSchedule` | `Cohort_or_Skill_SearchByTrainer` |
| **Trainer**| T-07 | Filter cohorts by status | `TrainerWorkflowTest.testT07_FilterCohortsByStatus` | `CohortStatusFilter_By_Trainer` |
| **Trainer**| T-08 | Mailbox assignment notification | `TrainerWorkflowTest.testT08_MailboxAssignmentNotification` | App UI |
| **Trainer**| T-09 | Real-time updates on reassignment | `TrainerWorkflowTest.testT09_RealTimeUpdatesOnReassignment` | App UI |
| **Admin** | A-01 | Admin sign in & system oversight | `AdminWorkflowTest.testA01_AdminSignInAndSystemOversight` | Config |
| **Admin** | A-02 | Dashboard data integrity check | `AdminWorkflowTest.testA02_DashboardDataIntegrityCheck` | App UI |
| **Admin** | A-03 | Global cohort search & filter | `AdminWorkflowTest.testA03_GlobalCohortSearchAndFilter` | `Cohort_Search_By_Admin` |
| **Admin** | A-04 | Export allocation report | `AdminWorkflowTest.testA04_ExportAllocationReport` | App UI |
| **Admin** | A-05 | Trainer registration validation | `AdminWorkflowTest.testA05_TrainerRegistrationValidation` | `TrainerRegistration_By_Admin` |
| **Admin** | A-06 | Register new trainer | `AdminWorkflowTest.testA06_RegisterNewTrainer` | Config / Unique |
| **Admin** | A-07 | Trainer pool filter and search | `AdminWorkflowTest.testA07_TrainerPoolFilterAndSearch` | `Trainer_Search_By_Admin` |
| **Admin** | A-08 | Edit trainer workload and skills | `AdminWorkflowTest.testA08_EditTrainerWorkloadAndSkills` | App UI |
| **Admin** | A-09 | Admin manual reassignment & sync | `AdminWorkflowTest.testA09_AdminManualReassignmentAndWorkloadSync` | App UI |
| **Admin** | A-10 | System-wide status update sync | `AdminWorkflowTest.testA10_SystemWideStatusUpdateSync` | App UI |

---

## 7. Execution Commands

### Compile the Test Suite
```bash
mvn clean test-compile
```

### Run All Tests via TestNG Suite
```bash
mvn clean test
```

### Run Role-Specific Test Classes
- **Coach Workflow Only:**
  ```bash
  mvn test -Dtest=CoachWorkflowTest
  ```
- **Trainer Workflow Only:**
  ```bash
  mvn test -Dtest=TrainerWorkflowTest
  ```
- **Admin Workflow Only:**
  ```bash
  mvn test -Dtest=AdminWorkflowTest
  ```
- **Authentication Only:**
  ```bash
  mvn test -Dtest=LoginTest
  ```

---

## 8. Test Reports and Diagnostic Artifacts
- **ExtentReports HTML Dashboard:** `reports/ExtentReport.html`
- **Maven Surefire XML/Text Reports:** `target/surefire-reports/`
- **Failure Screenshots:** `screenshots/*.png`
- **Application Execution Logs:** `logs/trainmate-test.log`
