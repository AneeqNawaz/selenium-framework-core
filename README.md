## 🧭 E2E Selenium Test Automation
Java + Selenium + TestNG + Page Object Model (POM) + Allure Reporting + Docker + GitHub Actions CI/CD. This project automates key navigation, page verification and job-filtering flows on insiderone.com using a scalable & maintainable automation architecture.
It is designed to demonstrate clean POM structure, optimized locators, strong assertions, reporting, and CI readiness.

### 🧩 Project Overview

Challenge Goal:

	1️⃣ Visit Insider homepage
	2️⃣ Navigate to Careers
	3️⃣ Validate Locations / Teams / Life at Insider modules
	4️⃣ Open Lever QA Jobs listing
	5️⃣ Apply filters (e.g., Istanbul, Turkiye) and validate jobs
	6️⃣ Open a job → validate Job Detail consistency
	7️⃣ Click Apply for this job → verify Apply page and form
	8️⃣ Ensure all data on career and job listings matches across all pages
  
	The solution is built with clean, maintainable, scalable automation design using:
		○ 🧱 Page Object Model (POM)
		○ 🔁 Reusable Components
		○ 📊 JSON-Sourced Test Data (POJO mapping)
		○ 📌 Allure Integrated Reporting
		○ 🧪 Smoke, Regression, and E2E test categories
		○ 🐳 Docker-ready execution
		○ CI/CD via GitHub Actions


## 📁 Project Structure
<pre>
📁 Project Root/
│
├── 📁 .github/workflows
│   └── 🗃️ selenium.yml           → CI workflow: runs tests + publishes Allure report to GitHub Pages
├── 📁 allure-results             → Auto-generated Allure raw results (JSON, XML, attachments)
│
├── 📁 src                        → Source code for framework + tests
│   ├── 📁 main
│   │   ├── 📁 java/com/insider
│   │   │   ├── 📁 config
│   │   │   │   └── 📄 ConfigManager.java          → Loads config.properties and provides runtime access
│   │   │   └── 📁 driver
│   │   │       └── 📄 DriverFactory.java          → WebDriver factory (Chrome, Firefox, etc.)
│   │   └── 📁 resources
│   │       └── (reserved for future main resources)
│   └── 📁 test
│       ├── 📁 java/com/insider
│       │   ├── 📁 base
│       │   │   ├── 📄 BasePage.java              → Shared wait/scroll helpers
│       │   │   └── 📄 BaseTest.java              → WebDriver lifecycle + config setup
│       │   ├── 📁 listeners
│       │   │   └── 📄 AllureListener.java        → TestNG → Allure integration
│       │   ├── 📁 pages
│       │   │   ├── 📄 HomePage.java              → Homepage POM
│       │   │   ├── 📄 CareersPage.java           → Careers page POM
│       │   │   ├── 📄 LeverQaJobsPage.java       → QA jobs listing POM
│       │   │   ├── 📄 LeverJobDetailPage.java    → Job detail POM
│       │   │   └── 📄 LeverApplyJobPage.java     → Job application POM
│       │   ├── 📁 testdata
│       │   │   ├── 📄 HomePageData.java          → JSON-mapped POJO for homepage
│       │   │   └── 📄 CareersPageData.java       → JSON-mapped POJO for careers page
│       │   ├── 📁 tests
│       │   │   ├── 📄 HomePageTest.java          → Homepage + “We’re hiring” footer link
│       │   │   ├── 📄 CareersPageTest.java       → Locations, Teams, Life at Insider validations
│       │   │   └── 📄 LeverQaJobsTest.java       → QA jobs filtering + Lever details
│       │   │   └── 📄 E2EJourneyTest.java        → Full user journey: Home → Careers → QA Jobs → Job Detail → Apply
│       │   └── 📁 utils
│       │       └── 📄 TestDataLoader.java        → Generic JSON → POJO loader
│       └── 📁 resources
│           ├── 📁 testdata
│           │   ├── 📄 homepage.json         → Homepage test data
│           │   └── 📄 careerspage.json      → Careers page test data
│           ├── 📄 config.properties         → env, baseUrl, browser, timeouts
│           └── 📄 testng.xml                → TestNG suite/group config
├── 📁 target                      → Maven build output (generated)
├── 🐳 Dockerfile                  → Containerized test runner (Java + Maven)
├── 🐳 docker-compose.yml          → Orchestrates Selenium Chrome + test execution
├── 🐳 .dockerignore               → Excludes unnecessary files from Docker image
├── 📄 .gitignore                  → Git exclusions (target/, results/, IDE files)
├── ⚙️ .gitattributes              → Normalizes line endings and diff/merge behavior
├── 📦 pom.xml                     → Maven project metadata, dependencies, plugins
└── 📄 README.md 🤚                → Project overview, setup guide, and usage documentation

</pre>

---
## 🎯 <strong>What This Solution Achieves</strong>

> 🔁 Reusability  
> Shared wait helpers in BasePage, config via ConfigManager, and JSON-driven data loaded through TestDataLoader keep tests lean and generic.

> 🚀 Scalability  
> Modular flows (Teams, Locations, Lever job pipeline) allow seamless expansion without touching core test logic.

> 🧭 Traceability  
> Allure metadata (@Epic, @Feature, @Story, @Owner, @Severity) and step-level reporting provide complete insight into every action executed with screenshots on failure.

> 🛡️ Resilience  
> Dynamic waits for delayed UI updates (e.g., job counts), optional-field handling, and flexible matching for Remote/Hybrid/On-site values guarantee stability.

> 📊 Data-Driven Verification  
> All expected values come from homepage.json and careerspage.json, mapped to POJOs — enabling zero hardcoding and effortless updates.

> 📂 Centralized Initialization  
> All JSON test data and pages(homepage, careers page, Lever flows) is loaded once in BaseTest using TestDataLoader.  
> This acts as a shared test fixture, making data instantly available to every test without repeated file reads.

> ⚙️ CI/CD Ready  
> GitHub Actions workflow builds, runs tests, and publishes Allure reports automatically to GitHub Pages.

> 🐳 Docker Support  
> Dockerized execution ensures consistent, environment-agnostic test runs across local machines and CI pipelines.

<pre>
  | Test File                               | Test Name / ID                                       | Purpose                                                                                  | Key Assertions                                                                                              | Outcome                                                      |
|-----------------------------------------|------------------------------------------------------|------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------|
| `HomePageTest.java`                     | 🏠 T01 – Homepage & Footer Navigation                | Ensures homepage loads correctly and “We’re hiring” link is functional.                  | Header visibility, COMPANY footer validation, footer link text, navigation to Careers page.                 | ✅ Confirms homepage UI readiness & navigation works.       |
| `CareersPageTest.java`                  | 🧩 T02 – Careers Page Component Validation           | Validates all major blocks: Locations, Teams, Life at Insider.                           | Locations title/subtitle, card count, per-card data, Teams collapsed/expanded states, slider image checks. | ✅ Ensures Careers page sections are fully functional.       |
| `LeverQaJobsTest.java`                  | 🔍 T03 – QA Jobs Filtering & Lever Detail Mapping    | Validates Lever QA listings, filters, job details, and apply-page consistency.           | Team filter = QA, location filter = Istanbul, job card fields, detail page mapping, apply page checks.     | ✅ Guarantees QA job flow accuracy across Lever pages.       |
| `InsiderE2EJourneyTest.java`            | 🔁 T04 – Full E2E Hiring Journey                     | Validates full flow: Home → Careers → QA Jobs → Filter → Detail → Apply form.            | Navigation transitions, Istanbul filtering, listing/detail/apply consistency, Allure steps, window handling.| ✅ Demonstrates complete end-to-end hiring journey stability. |

</pre>

----  

## 🚀 How to Run Tests
You can run the tests either **locally** or via **Docker**.

#### ⚙️ Prerequisites
To configure and run the tests, you will need:

- Java 17+
- Maven 3.8+
- Allure commandline (optional for report view)
- Chrome browser


### ▶️ LOCAL INSTALLATION (Step-by-Step)
These instructions let you run Selenium and Allure natively without Docker.

#### 🧩 1. Clone the repository
Open your terminal (or PowerShell on Windows):
```
git clone https://github.com/AneeqNawaz/selenium-framework-core.git
cd selenium-framework-core
```
This downloads the project to your computer and navigates inside the folder.

#### 📦 2. Install dependencies (Maven build)
Install project dependencies using Maven:
```
mvn clean install
```
This will:
- Download all Maven dependencies (Selenium, TestNG, Allure, WebDriverManager, etc.)
- Compile the project
- Run a quick build verification

#### 🧪 3. Run tests locally
Run all TestNG tests (smoke + regression + e2e groups defined in testng.xml):
```
mvn clean test
```
To run a specific E2E test:
```
mvn clean test -Dgroups=e2e
```
> 💡 Groups are configured via @Test(groups = { ... }) and wired in testng.xml.

#### 📊 4. Generate & view Allure report (local mode)
Step 1: Install Allure CLI (needs Java)  
If you don’t have Allure CLI, install it via your OS package manager or npm:
```
npm install -g allure-commandline
```
If you get an error like:
> `ERROR: JAVA_HOME is not set and no 'java' command could be found`

➡️ Install Java 17+ and restart your terminal.

Step 2: Generate report
```
allure serve allure-results
```
This automatically opens the HTML Allure report in your browser.

🧰 Local Troubleshooting
<pre>
| Issue                                         | Cause                                     | Fix                                                                 |
|-----------------------------------------------|-------------------------------------------|---------------------------------------------------------------------|
| ❌ mvn: command not found                    | Maven not installed / not on PATH         | Install Maven and add it to PATH (`mvn -v` should work).            |
| ❌ JAVA_HOME not set                         | Java not properly installed               | Install Java 17+, set JAVA_HOME, restart terminal.                  |
| ⚠️ Browser not opening / failing immediately | WebDriver / browser version mismatch      | Update browser, use WebDriverManager, or update Selenium version.   |
| 🧱 Allure: command not found                 | Allure CLI not installed                  | Run `npm install -g allure-commandline` or use OS package manager.  |
| 📁 No Allure report generated                | `allure-results` is empty                 | Ensure tests actually run                                           |
</pre>

## 🐳 Run via Docker (Recommended)
If you prefer to run everything in an isolated environment — without installing Java, Maven, Chrome, or WebDriver locally — use Docker for a fully containerized setup.

#### 🧱 1. Build the Docker Image
```
docker-compose build
```
This will:
- 🐳 Pull Selenium Chrome Standalone image
- 🧰 Install Maven dependencies
- ☕ Install Java (JDK 17+)
- 🚦 Prepare the test runner container

#### 🧪 2. Run Tests (Maven + Selenium inside Docker)
```
docker-compose up
```
This will:
- 🚀 Run your full Selenium + TestNG suite
- 📊 Generate fresh Allure results inside /allure-results
- 🖼 Capture screenshots on failure

🟢 **Important:**  
Keep the terminal open during execution.
Stopping the container (Ctrl + C) will stop the test run.

#### 📃 3. Generate report
```
allure serve allure-results
```
This automatically opens the HTML Allure report in your browser.

#### 🧹 4. Stop & Clean Up
To stop the running container:
```
docker-compose down
```
----

### 🤖 CI/CD with GitHub Actions

GitHub workflow automatically:
- ⬇️ Checks out the repo
- ☕ Sets up Java 17
- 🧰 Installs Maven dependencies
- 🚀 Runs selenium tests
- 📊 Generates Allure report
- 🌍 Publishes to GitHub Pages
- 📌 Public Allure Report URL

----

💬 **Reviewer Takeaway**  
This solution showcases a clean, scalable, and industry-grade automation framework.  
It demonstrates strong command of the Page Object Model (POM), data-driven testing, modular architecture, and CI/CD readiness.  
The combination of Allure reporting, Docker-based execution, and structured test layering reflects a mature approach to test design, reliability, and maintainability - fully suitable for real-world QA engineering environments.

🙌 Author  
🖍️ Crafted by Aneeq Nawaz  
📍 Berlin, Germany  
