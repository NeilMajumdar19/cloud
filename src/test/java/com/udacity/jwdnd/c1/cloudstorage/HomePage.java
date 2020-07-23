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

    public void goToNotes()
    {
        notesTab.click();
    }

    public void goToCredentials()
    {
        credentialsTab.click();
    }
}
