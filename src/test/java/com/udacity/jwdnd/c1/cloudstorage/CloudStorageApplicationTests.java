package com.udacity.jwdnd.c1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    @LocalServerPort
    private Integer port;

    private static WebDriver driver;

    private String username;
    private String password;

    @BeforeAll
    public static void beforeAll()
    {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterAll
    public static void afterAll()
    {
        driver.quit();
    }

    @BeforeEach
    public void beforeEach()
    {
        username = "Russ";
        password = "ballsdeep69";
        driver.get("http://localhost:" + port + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        signupPage.signupUser("Russell", "Westbrook", username, password);
        driver.get("http://localhost:" + port + "/login");

    }


    @Test
    public void testUnauthorizedUserAccess()
    {
        driver.get("http://localhost:" + port + "/signup");
        assertEquals("http://localhost:" + port + "/signup", driver.getCurrentUrl());
        driver.get("http://localhost:" + port + "/login");
        assertEquals("http://localhost:" + port + "/login", driver.getCurrentUrl());
        driver.get("http://localhost:" + port + "/home");
        assertNotEquals("http://localhost:" + port + "/home", driver.getCurrentUrl());


    }

    @Test
    public void testAuthorizedUserAccess() throws InterruptedException {


        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginUser(username, password);
        assertEquals("http://localhost:" + port + "/home", driver.getCurrentUrl());

        HomePage homePage = new HomePage(driver);
        Thread.sleep(1000);
        homePage.logout();

        driver.get("http://localhost:" + port + "/home");
        assertNotEquals("http://localhost:" + port + "/home", driver.getCurrentUrl());


    }

    @Test
    public void testCreateNote() throws InterruptedException {


        String title = "Jon Jones";
        String description = "is the goat";

        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginUser(username, password);

        WebDriverWait wait = new WebDriverWait(driver, 10);
        HomePage homePage = new HomePage(driver);
        WebElement notesTab = homePage.getNotesTab();
        wait.until(ExpectedConditions.elementToBeClickable(notesTab)).click();

        WebDriverWait wait1 = new WebDriverWait(driver, 10);
        HomeNotesPage notesPage = new HomeNotesPage(driver);
        WebElement addButton = notesPage.getAddNoteButton();
        wait1.until(ExpectedConditions.elementToBeClickable(addButton)).click();

        Thread.sleep(5000);
        notesPage.enterTitle(title);
        Thread.sleep(5000);
        notesPage.enterDescription(description);
        Thread.sleep(5000);

        WebDriverWait wait2 = new WebDriverWait(driver, 10);
        ResultPage resultPage = new ResultPage(driver);
        WebElement continueLink = resultPage.getContinueLinkSuccess();
        wait2.until(ExpectedConditions.elementToBeClickable(continueLink)).click();

        homePage.goToNotes();
        assertEquals(title, notesPage.getDisplayedTitle());
        assertEquals(description, notesPage.getDisplayedDescription());

    }





}
