package com.udacity.jwdnd.c1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ResultPage {

    @FindBy(id = "cont-link-success")
    private WebElement continueLinkSuccess;

    @FindBy(id = "cont-link-failure")
    private WebElement continueLinkFailure;

    public ResultPage(WebDriver driver)
    {
        PageFactory.initElements(driver, this);
    }

    public WebElement getContinueLinkSuccess() {
        return continueLinkSuccess;
    }

    public WebElement getContinueLinkFailure() {
        return continueLinkFailure;
    }

    public void continueSuccess()
    {
        continueLinkSuccess.click();
    }

    public void continueFailure()
    {
        continueLinkFailure.click();
    }
}
