package core

import org.openqa.selenium.Dimension
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.remote.BrowserType.*
import java.util.concurrent.TimeUnit

import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.safari.SafariDriver

class WebDriverFactory {

    fun getWebDriver(browser: String): WebDriver {
        return when (browser) {
            GOOGLECHROME -> ChromeDriver()
            FIREFOX -> {
                System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null")
                FirefoxDriver()
            }
            IEXPLORE -> InternetExplorerDriver()
            SAFARI -> SafariDriver()
            else -> ChromeDriver()
        }.apply { configure() }
    }

    private fun RemoteWebDriver.configure() {
        this.manage().window().size = Dimension(1920, 1080)
        this.manage().window().maximize()
        this.manage().timeouts().pageLoadTimeout(2, TimeUnit.SECONDS)
        this.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS)
    }

}
