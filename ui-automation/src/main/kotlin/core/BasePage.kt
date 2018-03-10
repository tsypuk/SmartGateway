package core

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

abstract class BasePage(val driver: WebDriver) {

    fun WebElement.jsClick() = (driver as JavascriptExecutor)
            .executeScript("arguments[0].click();", this)

}
