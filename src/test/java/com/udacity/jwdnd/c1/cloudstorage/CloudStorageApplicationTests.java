package com.udacity.jwdnd.c1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    @LocalServerPort
    private Integer port;

    private static WebDriver driver;

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
    public void testAuthorizedUserAccess(){
        String username = "Russ";
        String password = "ballsdeep69";
        driver.get("http://localhost:" + port + "/signup");

        SignupPage signupPage = new SignupPage(driver);
        signupPage.signupUser("Russell", "Westbrook", username, password);
        driver.get("http://localhost:" + port + "/login");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginUser(username, password);
        assertEquals("http://localhost:" + port + "/home", driver.getCurrentUrl());

        HomePage homePage = new HomePage(driver);
        homePage.logout();

        driver.get("http://localhost:" + port + "/home");
        assertNotEquals("http://localhost:" + port + "/home", driver.getCurrentUrl());


    }





}
