package com.abn.selcucumber.pages.login;

import com.abn.selcucumber.exception.ClickElementException;
import com.abn.selcucumber.exception.ElementNotFoundException;
import com.abn.selcucumber.exception.TextElementNotFoundException;
import com.abn.selcucumber.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by aswathyn on 07/01/17.
 */
public class LoginPage extends BasePage<LoginPage> {

    @FindBy(id = "ember314")
    private WebElement email;

    @FindBy(css = ".btn.btn-primary.msg_page-button")
    private WebElement nextButton;

    public static final String PAGETITLE = "QAWorks";
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginPage.class);

    public LoginPage(WebDriver driver) {
        super(driver);
        instantiatePage(this);
    }

    @Override
    public String getPageTitle() {
        return this.PAGETITLE;
    }

    @Override
    protected ExpectedCondition getPageLoadCondition() {

        return ExpectedConditions.titleIs(getPageTitle());
    }

    @Override
    public void instantiatePage(LoginPage page) {
        try {
            LOGGER.info("** instantiatePage(): "+ page.getClass().getSimpleName());
            PageFactory.initElements(driver, page);
        } catch(Exception e) {
            LOGGER.error("--- Error instantiating :"+page.getClass().getSimpleName());
        }
    }

    public CredentialsPage enterEmail(String userName) {
        LOGGER.trace(">> enterEmail()");
        try {
            LOGGER.info("-- Emailid passed",userName);
            enterText(email, userName);
            clickButton(nextButton);
            implicitWaitMethod();
        } catch(TextElementNotFoundException | ClickElementException | ElementNotFoundException ex ) {
            LOGGER.error("Sign in failed", ex);
        }
        return new CredentialsPage(driver);
    }

    /*********************GET/SET METHODS***************************/
}
