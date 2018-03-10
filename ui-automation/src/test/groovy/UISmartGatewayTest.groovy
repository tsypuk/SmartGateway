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
    private WebDriver driver

    def setupSpec() {
        driver = new WebDriverFactory().getWebDriver("GOOGLECHROME")
        def configLoader = new ConfigLoader("local")
        driver.get(configLoader.pageUrl())
    }

    def cleanupSpec() {
        driver.close()
    }

    def "navigate to Toggles page"() {
        given:
        def togglePage = new TogglePage(driver)

        when:
        togglePage.clickToggleTab()

        then:
        togglePage.getToggleTabText() == "Toggles"
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
        lambdaPage.getLambdaTitle().contains("Lambdas")
        lambdaPage.getLambdaTabText() == "Lambda"
    }

    def "navigate to Analytics page"() {
        given:
        def analyticsPage = new AnalyticsPage(driver)

        when:
        analyticsPage.clickAnalyticsTab()

        then:
        analyticsPage.getAnalyticsTitle().contains("Analytics of toggle usage now this is dummy")
        analyticsPage.getAnalyticsTabText() == "Analytics"
    }

    def "navigate to Configuration page"() {
        given:
        def configPage = new ConfigPage(driver)

        when:
        configPage.clickConfigurationTab()

        then:
        configPage.getConfigurationTabText() == "Configuration"
    }

    @Unroll
    def "create #data.size() devices"() {
        given:
        def configPage = new ConfigPage(driver)
        def togglePage = new TogglePage(driver)
        configPage.clickConfigurationTab()

        devices.forEach({
            configPage.clickAddDevice()
            configPage.inputDeviceName(it.name)
            configPage.inputDevicePort(it.port)
            configPage.clickOnAddButton()
        })

        togglePage.clickToggleTab()

        expect:
        devices.forEach({
            togglePage.getDeviceText(it.name) == it.name
            togglePage.getToggleStatus(it.name) == state
        })

        where:
        devices                           | state
        [[port: "6761", name: "device1"],
         [port: "6762", name: "device2"],
         [port: "6763", name: "device3"]] | "OFF"
    }

    def "create device, turn/ON toggle"() {
        given:
        def configPage = new ConfigPage(driver)
        def togglePage = new TogglePage(driver)

        configPage.clickConfigurationTab()

        and: "create new device"
        configPage.clickAddDevice()
        configPage.inputDeviceName(name)
        configPage.inputDevicePort(port)
        configPage.clickOnAddButton()

        and: "navigate to Toggle page, turn/ON switch"
        togglePage.clickToggleTab()
        togglePage.changeStatus(name)

        expect:
        togglePage.getDeviceText(name) == name
        togglePage.getToggleStatus(name) == state

        where:
        name         | port   | state
        "FakeDevice" | "6565" | "ON"
    }

    def "create device, turn/ON, turn/OFF"() {
        given:
        def configPage = new ConfigPage(driver)
        def togglePage = new TogglePage(driver)

        configPage.clickConfigurationTab()

        and: "create new device"
        configPage.clickAddDevice()
        configPage.inputDeviceName(name)
        configPage.inputDevicePort(port)
        configPage.clickOnAddButton()

        and: "navigate to Toggle page, turn/ON switch"
        togglePage.clickToggleTab()
        togglePage.changeStatus(name)

        and: "turn/OFF switch"
        togglePage.changeStatus(name)

        expect:
        togglePage.getDeviceText(name) == name
        togglePage.getToggleStatus(name) == state

        where:
        name            | port   | state
        "TriggerDevice" | "6569" | "OFF"
    }

    def "create device, update its properties"() {
        given:
        def configPage = new ConfigPage(driver)
        def togglePage = new TogglePage(driver)
        configPage.clickConfigurationTab()

        and: "create new device"
        configPage.clickAddDevice()
        configPage.inputDeviceName(deviceToCreate.name)
        configPage.inputDevicePort(deviceToCreate.port)
        configPage.clickOnAddButton()

        and: "update device settings"
        configPage.clickEditConfig()
        configPage.inputDeviceName(deviceToUpdate.name)
        configPage.inputDevicePort(deviceToUpdate.port)
        configPage.clickOnAddButton()

        and: "navigate to toggle page"
        togglePage.clickToggleTab()

        expect:
        togglePage.getDeviceText(deviceToUpdate.name) == deviceToUpdate.name
        togglePage.getToggleStatus(deviceToUpdate.name) == state

        where:
        deviceToCreate                  | deviceToUpdate                  | state
        [port: "5454", name: "device1"] | [port: "6767", name: "device2"] | "OFF"
    }

    def "navigate to Configuration page, delete all device"() {
        given:
        def configPage = new ConfigPage(driver)

        when:
        configPage.clickConfigurationTab()
        configPage.clearConf()

        then:
        configPage.getConfigurationTabText() == "Configuration"
    }

    def "navigate to Boot page"() {
        given:
        def bootPage = new BootPage(driver)

        when:
        bootPage.clickBootTab()

        then:
        bootPage.getBootTabText() == "Boot"
        bootPage.getReloadButtonText() == "RELOAD DEVICES"
    }

}
