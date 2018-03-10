package pages

import org.openqa.selenium.*
import org.openqa.selenium.By.xpath
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import core.BasePage
import org.openqa.selenium.WebElement

class ConfigPage(driver: WebDriver) : BasePage(driver) {

    @FindBy(xpath = ".//center/div/div/div[3]/div/button")
    lateinit var addDeviceButton: WebElement

    @FindBy(xpath = ".//*[@class='tabs primary-nav']/li[6]/a")
    lateinit var configurationTab: WebElement

    @FindBy(xpath = ".//*[@id='name']")
    lateinit var nameField: WebElement

    @FindBy(xpath = ".//*[@id='port']")
    lateinit var portField: WebElement

    @FindBy(xpath = ".//tbody/tr[4]/td[1]/div/button")
    lateinit var addButton: WebElement

    @FindBy(xpath = ".//tbody/tr[*]/td[last()]//button")
    lateinit var deleteButtons: MutableList<WebElement>

    @FindBy(xpath = ".//center//tr[1]/td[5]/div/button")
    lateinit var editButton: WebElement

    init {
        PageFactory.initElements(driver, this)
    }

    fun getConfigurationTabText(): String = configurationTab.text

    fun clickConfigurationTab() = configurationTab.jsClick()

    fun clickAddDevice() = addDeviceButton.jsClick()

    fun clickEditConfig() = editButton.jsClick()

    fun inputDeviceName(name: String) = nameField.inputText(name)

    fun inputDevicePort(port: String) = portField.inputText(port)

    fun clickOnAddButton() = addButton.click()

    fun getUUID(value: String): String {
        return driver.findElement(
                xpath(".//table//td[text()='$value']/preceding-sibling::td")).text
    }

    fun getName(value: String): String {
        return driver.findElement(
                xpath(".//table//td[text()='$value']")).text
    }

    fun getIp(value: String): String {
        return driver.findElement(
                xpath(".//table//td[text()='$value']/following-sibling::td[1]")).text
    }

    fun getPort(value: String): String {
        return driver.findElement(
                xpath(".//table//td[text()='$value']/following-sibling::td[2]")).text
    }

    private fun clearConf() {
        var deleteNext: Boolean = true

        while (deleteNext) {
            driver.navigate().refresh();
            val deleteButtons = driver.findElements(
                    xpath(".//tbody/tr[*]/td[last()]//button"))
            if (deleteButtons.isEmpty()) {
                deleteNext = false
            } else {
                deleteButtons[0]?.click()
            }
        }
    }

    private fun WebElement.inputText(text: String) {
        this.clear()
        this.sendKeys(text)
    }
}
