package pages

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import core.BasePage

class AnalyticsPage(driver: WebDriver) : BasePage(driver) {

    @FindBy(xpath = "//*[@class='tabs primary-nav']/li[5]/a")
    lateinit var analyticTab: WebElement

    @FindBy(xpath = ".//center/div/div/div")
    lateinit var title: WebElement

    init {
        PageFactory.initElements(driver, this)
    }

    fun clickAnalyticsTab() = analyticTab.jsClick()

    fun getAnalyticsTabText(): String = analyticTab.text

    fun getAnalyticsTitle(): String = title.text

}
