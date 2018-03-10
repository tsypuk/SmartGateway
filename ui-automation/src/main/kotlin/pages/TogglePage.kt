package pages

import org.openqa.selenium.By.xpath
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import core.BasePage

class TogglePage(driver: WebDriver) : BasePage(driver) {

    @FindBy(xpath = ".//*[@class='tabs primary-nav']/li[1]/a")
    lateinit var togglesTab: WebElement

    @FindBy(xpath = ".//*[@class='reloadDevice']//span[text()='Refresh']")
    lateinit var refreshButton: WebElement

    @FindBy(xpath = ".//center//td[1]")
    lateinit var statusText: WebElement

    init {
        PageFactory.initElements(driver, this)
    }

    fun clickToggleTab() = togglesTab.jsClick()

    fun getToggleTabText() : String = togglesTab.text

    fun getDeviceText(value: String): String {
        return driver.findElement(
                xpath(".//td[text()='$value']")).text
    }

    fun getToggleStatus(value: String): String {
        return driver.findElement(
                xpath(".//td[text()='$value']/preceding-sibling::td[2]")).text
    }

    fun changeStatus(value: String) {
        val toggleButton = driver.findElement(
                xpath(".//td[text()='$value']/preceding-sibling::td[1]//input"))
        toggleButton.click()
    }

}
