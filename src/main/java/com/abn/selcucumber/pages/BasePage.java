package com.abn.selcucumber.pages;

/**
 * Created by aswathyn on 07/01/17.
 */

import com.abn.selcucumber.exception.*;
import com.google.common.base.Function;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The type Base page.
 *
 * @param <P    > the type parameter
 */
public abstract class BasePage <P extends BasePage> {

    protected WebDriver driver;
    protected WebDriverWait waitTime;
    private static final String PAGE_TITLE = "";
    protected static final long ELEMENT_WAIT = 10;
    protected static final long IMPLICIT_WAIT = 20;
    protected static final int PAGE_LOAD_TIMEOUT = 30;
    protected static final int POLLING_RATE = 2;
    protected static final int SPINNER_TO_APPEAR_TIMEOUT = 5;
    protected static final int SPINNER_TO_DISAPPEAR_TIMEOUT = 30;
    protected static final int SPINNER_POLLING_RATE = 50;
    protected static final int INDEXING_TIMEOUT=40;
    protected static final int INDEXING_POLLING_RATE=5;

    static final Logger LOGGER = LoggerFactory.getLogger(BasePage.class);

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    protected void implicitWaitMethod() {
        driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
    }

    /**
     * Method to wait for page to get loaded
     *
     * @param expectedCondition
     */
    protected void waitForPageToLoad(ExpectedCondition<?> expectedCondition) {
        try {
            LOGGER.trace(">> waitForPageToLoad()");
            Wait wait = new FluentWait(driver)
                    .withTimeout(PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS)
                    .pollingEvery(POLLING_RATE, TimeUnit.SECONDS);
            wait.until(getPageLoadCondition());
        } catch (Exception e) {
            LOGGER.error("-- Error in page loading");
        }
        LOGGER.trace("<< waitForPageToLoad()");
    }


    /**
     * Method to wait for indexing to complete
     *
     * @param webElement
     */
    protected WebElement waitForIndexing(final WebElement webElement) {
        LOGGER.debug(">> waitForIndexing"+ webElement.toString());
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(INDEXING_TIMEOUT,TimeUnit.SECONDS)
                .pollingEvery(INDEXING_POLLING_RATE,TimeUnit.SECONDS);
        WebElement element = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                refreshPage();
                return webElement;
            }
        });
        return element;
    }

    /**
     * Method to get the page title.
     *
     * @return Title for the page loaded
     */
    public String getPageTitle() {
        return PAGE_TITLE;
    }

    /**
     * Method to get the condition for checking the page load
     *
     * @return ExpectedCondition for the element to be verified.
     */
    protected abstract ExpectedCondition<?> getPageLoadCondition();

    /**
     * Method for child page instantiation
     */
    protected abstract void instantiatePage(P page);

    /**
     * Method for waiting for element to be visible
     */
    protected void waitForElement(ExpectedCondition expectedCondition, WebElement element) throws ElementNotVisibleInUIException {
        try {
            LOGGER.trace(">> waitForElement()");
            waitTime = new WebDriverWait(driver, ELEMENT_WAIT);
            waitTime.until(expectedCondition);
        } catch (Exception e) {
            LOGGER.error("-- Error in waiting for element");
            throw new ElementNotVisibleInUIException("Element is not visible in UI");
        }
        LOGGER.trace("<< waitForElement()");
    }

    public void enterText(WebElement webElement, String message) throws TextElementNotFoundException {
        if (!(webElement == null)) {
            if (webElement.isDisplayed()) {
                webElement.clear();
                webElement.sendKeys(message);
            } else {
                LOGGER.error("--"+webElement.toString()+ " not found");
                throw new TextElementNotFoundException("Text element not found");
            }
        }
    }

    public void enterTextBy(By ByElement, String message) {
        driver.findElement(ByElement).sendKeys(message);
    }

    public void clickButton(WebElement webElement) throws ClickElementException, ElementNotFoundException {
        if (isElementPresent(webElement)) {
            if (webElement.isEnabled()) {
                webElement.click();
            } else {
                LOGGER.error("--"+webElement.toString()+ " not clickable");
                throw new ClickElementException(webElement.toString() + " not clickable");
            }
        } else {
            LOGGER.error("--"+webElement.toString()+ " not visible");
            throw new ElementNotFoundException(webElement.toString() + " not visible");
        }
    }

    public void clickByIndex(List<WebElement> element, int index) throws ClickElementException {
        if (!(element == null)) {
            if (element.get(index).isDisplayed()) {
                element.get(index).click();
            } else {
                throw new ClickElementException(element.get(index).toString() + " not clicked");
            }
        }
    }

    public void clickIcon(WebElement element, String message) throws ClickIconNotFoundException {
        if (isElementPresent(element)) {
            element.click();
        } else {
            LOGGER.error("--"+element.toString()+ " not found");
            throw new ClickIconNotFoundException(message + " not found");
        }
    }

    public void selectDropdownText(WebElement element, String textValue) throws SelectDropDownNotFoundException {
        try {
            Select selectValue = new Select(element);
            selectValue.selectByVisibleText(textValue);
        } catch (NoSuchElementException ex) {
            LOGGER.error("--"+element.toString()+ " not found");
            throw new SelectDropDownNotFoundException(textValue + " not found", ex);
        }
    }

    /**
     * Is element present boolean.
     *
     * @param webElement the web element
     * @return the boolean
     */
    public boolean isElementPresent(WebElement webElement) {
        try {
            webElement.isDisplayed();
            return true;
        } catch (NoSuchElementException ex) {
            LOGGER.info("--"+webElement.toString()+ " not displayed");
            return false;
        }
    }

    /**
     * Switch to iframe.
     *
     * @param element the element
     * @throws IFrameNotFoundException the frame not found exception
     */
    public void switchToiFrame(WebElement element) throws IFrameNotFoundException{
        if(isElementPresent(element)){
            driver.switchTo().frame(element);
        }else {
            LOGGER.error("-- iframe not found");
            throw new IFrameNotFoundException(element.toString() + "not found");
        }
    }

    /**
     * Switch back from Iframe.
     *
     * @throws IFrameNotFoundException the frame not found exception
     */
    public void switchBackFromiFrame() throws IFrameNotFoundException {
        driver.switchTo().defaultContent();
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }

    /**
     * Scroll to page bottom.
     *
     * @throws Exception the exception
     */
    public void scrollToPageBottom() throws Exception {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight);");
    }

    /**
     * Scroll to element.
     *
     * @param element the element
     * @throws Exception the exception
     */
    public void scrollToElement(WebElement element) throws Exception {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
    }

    /**
     * Wait for jQuery.
     */
    public void waitForJQuery() {
        (new WebDriverWait(driver, 30)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                JavascriptExecutor js = (JavascriptExecutor) d;
                return (Boolean) js.executeScript("return !!window.jQuery && window.jQuery.active == 0");
            }
        });
    }


}
