package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.junit5.ScreenShooterExtension;
import com.codeborne.selenide.logevents.SelenideLogger;
import conf.Config;
import data.BaseData;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;


public class BaseTest extends BaseSetupExtension  {

    @RegisterExtension
    static ScreenShooterExtension screenshotEmAll = new ScreenShooterExtension(true);

    public static Config config;
    public static BaseData test_data;

    @BeforeAll
    @Step("Before Class") //before class
    static void setUpAll() {
    }

    @AfterAll
    @Step("After Class") //after class
    static void tearDownAll(){
    }

    @BeforeEach
    @Step("Before") //before method
    public void setupTest(){
        config.testNumber++;
        config.screenNumber=0;
        closeWebDriver();
        open(Configuration.baseUrl+"/logout");
        open(Configuration.baseUrl);
    }

    @AfterEach
    @Step("After") //after method
    public void tearDownTest(){
        verifyVisual();
    }

    @Step("Verify Visual")
    public void verifyVisual(){
        config.screenNumber++;
        String screen = "screen_"+config.testNumber+"-"+config.screenNumber;
        Allure.addAttachment(screen, new ByteArrayInputStream(((TakesScreenshot)getWebDriver()).getScreenshotAs(OutputType.BYTES)));
    }

    @Step("Set Selenide Properties")
    public static void setSelenideProps(Config config){
        Configuration.browser = config.browserName;
        Configuration.browserPosition = config.browserPosition;
        Configuration.browserSize = config.browserSize;
        Configuration.baseUrl = config.baseUrl;
        Configuration.timeout = config.globalTimeout;
        //Configuration.fastSetValue = true; //Experimental!!!
    }

    @Step("Get Browser Info")
    public static String getBrowserInfo(){
        open("https://www.google.com/"); //coz always alive
        WebDriver driver = WebDriverRunner.getWebDriver();
        Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
        String browserName = (String)cap.getCapability("browserName");
        String browserVersion = (String)cap.getCapability("browserVersion");
        String osName = String.valueOf(cap.getPlatformName());
        return browserName + "-" + browserVersion + " (" + osName + ")";
    }


    @Step("Save Test Data")
    public void saveTestDataTxt(List<String> testData, String dir, String filename){
        config.fileMan.saveStringListToTextFile(testData, dir, filename);
        Path content = Paths.get(dir, filename+".txt");
        try (InputStream is = Files.newInputStream(content)) {
            Allure.addAttachment(filename+".txt", is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    void setup() {
        config = new Config();
        config.cleanResultsDir();
        test_data = new BaseData(config);
        setSelenideProps(config);
        AllureSelenide report = new AllureSelenide();
        SelenideLogger.addListener("AllureSelenide", report
                .screenshots(true)
                .includeSelenideSteps(true)
                .savePageSource(true)
        );
    }

    @Override
    public void close() throws Throwable {
        config.browserInfo = getBrowserInfo();
        //config.createReportProps();
        // TODO: enable when report generation ready (in run.bat)
        SelenideLogger.removeListener("allure");
    }
}
