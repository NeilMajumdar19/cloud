package com.udacity.jwdnd.c1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(id = "title-display")
    private WebElement titleDisplay;

    @FindBy(id = "description-display")
    private WebElement descriptionDisplay;

    @FindBy(id = "url-display")
    private WebElement urlDisplay;

    @FindBy(id = "username-display")
    private WebElement usernameDisplay;

    @FindBy(id = "password-display")
    private WebElement passwordDisplay;


    public HomePage(WebDriver driver)
    {
        PageFactory.initElements(driver, this);
    }

    public void logout()
    {
        logoutButton.click();
    }

    public WebElement getNotesTab() {
        return notesTab;
    }

    public WebElement getCredentialsTab() {
        return credentialsTab;
    }

    public String getDisplayedTitle()
    {
        return titleDisplay.getText();
    }

    public String getDisplayedDescription()
    {
        return descriptionDisplay.getText();
    }

    public String getDisplayedUrl()
    {
        return urlDisplay.getText();
    }

    public String getDisplayedUsername()
    {
        return usernameDisplay.getText();
    }

    public String getDisplayedPassword()
    {
        return passwordDisplay.getText();
    }

}
