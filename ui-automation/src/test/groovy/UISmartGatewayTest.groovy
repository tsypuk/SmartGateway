import core.ConfigLoader
import org.openqa.selenium.WebDriver
import pages.AnalyticsPage
import pages.BootPage
import pages.ConfigPage
import pages.LambdaPage
import pages.StatPage
import pages.TogglePage
import pages.UPnPPage
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Unroll
import core.WebDriverFactory

@Stepwise
class UISmartGatewayTest extends Specification {

    @Shared
    static WebDriver driver = new WebDriverFactory().getWebDriver("GOOGLECHROME")

    @Delegate
    static ConfigPage configPage = new ConfigPage(driver)

    @Delegate
    static TogglePage togglePage = new TogglePage(driver)

    def setupSpec() {
        def configLoader = new ConfigLoader("local")
        driver.get(configLoader.pageUrl())
    }

    def cleanupSpec() {
        driver.close()
    }

    def "navigate to Toggles page"() {
        when:
        clickToggleTab()

        then:
        getToggleTabText() == "Toggles"
    }

    def "navigate to UpnP page"() {
        given:
        def uPnPPage = new UPnPPage(driver)

        when:
        uPnPPage.clickUPnPTab()

        then:
        uPnPPage.getUPnPTitle().contains("UPnP Discovery")
    }

    def "navigate to Statistics page"() {
        given:
        def statPage = new StatPage(driver)

        when:
        statPage.clickStatTab()

        then:
        with(statPage) {
            getStatTitle().contains("Stat")
            getStatTabText() == "Statistics"
        }
    }

    def "navigate to Lambda page"() {
        given:
        def lambdaPage = new LambdaPage(driver)

        when:
        lambdaPage.clickLambdaTab()

        then:
        with(lambdaPage) {
            getLambdaTitle().contains("Lambdas")
            getLambdaTabText() == "Lambda"
        }
    }

    def "navigate to Analytics page"() {
        given:
        def analyticsPage = new AnalyticsPage(driver)

        when:
        analyticsPage.clickAnalyticsTab()

        then:
        with(analyticsPage) {
            getAnalyticsTitle().contains("Analytics of toggle usage now this is dummy")
            getAnalyticsTabText() == "Analytics"
        }
    }

    def "navigate to Configuration page"() {
        when:
        clickConfigurationTab()

        then:
        getConfigurationTabText() == "Configuration"
    }

    @Unroll
    def "create #devices.size() devices"() {
        given:
        clickConfigurationTab()

        devices.forEach({
            clickAddDevice()
            inputDeviceName(it.name)
            inputDevicePort(it.port)
            clickOnAddButton()
        })

        clickToggleTab()

        expect:
        devices.forEach({
            getDeviceText(it.name) == it.name
            getToggleStatus(it.name) == state
        })

        where:
        devices                           | state
        [[port: "6761", name: "device1"],
         [port: "6762", name: "device2"],
         [port: "6763", name: "device3"]] | "OFF"
    }

    def "create device, turn/ON toggle"() {
        given:
        clickConfigurationTab()

        and: "create new device"
        clickAddDevice()
        inputDeviceName(name)
        inputDevicePort(port)
        clickOnAddButton()

        and: "navigate to Toggle page, turn/ON switch"
        clickToggleTab()
        changeStatus(name)

        expect:
        getDeviceText(name) == name
        getToggleStatus(name) == state

        where:
        name         | port   | state
        "FakeDevice" | "6565" | "ON"
    }

    def "create device, turn/ON, turn/OFF"() {
        given:
        clickConfigurationTab()

        and: "create new device"
        clickAddDevice()
        inputDeviceName(name)
        inputDevicePort(port)
        clickOnAddButton()

        and: "navigate to Toggle page, turn/ON switch"
        clickToggleTab()
        changeStatus(name)

        and: "turn/OFF switch"
        changeStatus(name)

        expect:
        getDeviceText(name) == name
        getToggleStatus(name) == state

        where:
        name            | port   | state
        "TriggerDevice" | "6569" | "OFF"
    }

    def "create device, update its properties"() {
        given:
        clickConfigurationTab()

        and: "create new device"
        clickAddDevice()
        inputDeviceName(deviceToCreate.name)
        inputDevicePort(deviceToCreate.port)
        clickOnAddButton()

        and: "update device settings"
        clickEditConfig()
        inputDeviceName(deviceToUpdate.name)
        inputDevicePort(deviceToUpdate.port)
        clickOnAddButton()

        and: "navigate to toggle page"
        clickToggleTab()

        expect:
        getDeviceText(deviceToUpdate.name) == deviceToUpdate.name
        getToggleStatus(deviceToUpdate.name) == state

        where:
        deviceToCreate                     | deviceToUpdate                       | state
        [port: "5454", name: "deviceInit"] | [port: "6767", name: "deviceUpdate"] | "OFF"
    }

    def "navigate to Configuration page, delete all device"() {
        when:
        clickConfigurationTab()
        clearConf()

        then:
        getConfigurationTabText() == "Configuration"
    }

    def "navigate to Boot page"() {
        given:
        def bootPage = new BootPage(driver)

        when:
        bootPage.clickBootTab()

        then:
        with(bootPage) {
            getBootTabText() == "Boot"
            getReloadButtonText() == "RELOAD DEVICES"
        }
    }

}
