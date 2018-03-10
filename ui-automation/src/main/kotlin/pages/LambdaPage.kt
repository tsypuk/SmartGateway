package pages

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import core.BasePage

class LambdaPage(driver: WebDriver) : BasePage(driver) {

    @FindBy(xpath = ".//*[@class='tabs primary-nav']/li[4]/a")
    lateinit var lambdaTab: WebElement

    @FindBy(xpath = ".//center/div/div/div")
    lateinit var title: WebElement

    init {
        PageFactory.initElements(driver, this)
    }

    fun clickLambdaTab() = lambdaTab.jsClick()

    fun getLambdaTabText(): String = lambdaTab.text

    fun getLambdaTitle(): String = title.text
}
