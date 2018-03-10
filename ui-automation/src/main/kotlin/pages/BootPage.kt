package pages

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import core.BasePage

class BootPage(driver: WebDriver) : BasePage(driver) {

    init {
        PageFactory.initElements(driver, this)
    }

    @FindBy(xpath = ".//*[@class='tabs primary-nav']/li[7]/a")
    lateinit var bootTab: WebElement

    @FindBy(xpath = ".//center//button")
    lateinit var reloadDevicesButton: WebElement

    fun clickBootTab() = bootTab.jsClick()

    fun getBootTabText(): String = bootTab.text

    fun getReloadButtonText(): String = reloadDevicesButton.text

}
