package pages

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import core.BasePage

class StatPage(driver: WebDriver) : BasePage(driver) {

    @FindBy(xpath = ".//*[@class='tabs primary-nav']/li[3]/a")
    lateinit var statTab: WebElement

    @FindBy(xpath = ".//center/div/div/div")
    lateinit var title: WebElement

    init {
        PageFactory.initElements(driver, this)
    }

    fun clickStatTab() = statTab.click()

    fun getStatTabText(): String = statTab.text

    fun getStatTitle(): String = title.text
}
