package com.udacity.jwdnd.c1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomeNotesPage {

    @FindBy(id = "notes-button")
    private WebElement addNoteButton;

    @FindBy(id = "note-title")
    private WebElement titleField;

    @FindBy(id = "note-description")
    private WebElement descriptionField;

    @FindBy(id = "save-button")
    private WebElement saveButton;

    @FindBy(id = "title-display")
    private WebElement titleDisplay;

    @FindBy(id = "description-display")
    private WebElement descriptionDisplay;

    public HomeNotesPage(WebDriver driver)
    {
        PageFactory.initElements(driver, this);
    }

    public WebElement getAddNoteButton() {
        return addNoteButton;
    }

    public void addNote()
    {
        addNoteButton.click();
    }

    public void enterTitle(String title)
    {
        titleField.sendKeys(title);
    }

    public void enterDescription(String description)
    {
        descriptionField.sendKeys(description);
    }

    public void saveChanges()
    {
        saveButton.click();
    }

    public String getDisplayedTitle()
    {
        return titleDisplay.getText();
    }

    public String getDisplayedDescription()
    {
        return descriptionDisplay.getText();
    }
}
