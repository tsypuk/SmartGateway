package pages

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import core.BasePage

class UPnPPage(driver: WebDriver) : BasePage(driver) {

    @FindBy(xpath = ".//center/div/div/div/div")
    lateinit var title: WebElement

    @FindBy(xpath = ".//*[@class='tabs primary-nav']/li[2]/a")
    lateinit var uPnPTab: WebElement

    init {
        PageFactory.initElements(driver, this)
    }

    fun clickUPnPTab() = uPnPTab.click()

    fun getUPnPTitle() : String = title.text

}
