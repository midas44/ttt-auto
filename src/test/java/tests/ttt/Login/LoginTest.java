package tests.ttt.Login;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import conf.Config;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.ttt.LoginPage;
import pages.ttt.MyTasksPage;

//@Tags({"Regress", "Smoke"})
//@Tag("regression")
@Tag("Login")
public class LoginTest {

    static Config config;

   // static Stream<String> dataProvider() {
   //     return Stream.of(test_data.users.testAccounts.toArray(new String[0]));
   // }

  /*
    @ParameterizedTest
    @Disabled
    @MethodSource("dataProvider")
    @Epic("Login")
    @Story("Positive")
    @DisplayName("Login: with valid credentials")
    public void login_positive(String login) {
        MyTasksPage my_tasks_page = login(login, login);
        logout(my_tasks_page);
    }
*/
   // @ParameterizedTest
    //@Disabled
    //@Me
  //
  // thodSource("dataProvider")



    //@Override
    @BeforeAll
    static void setup() {
        config = new Config();


        setSelenideProps(config);
        AllureSelenide report = new AllureSelenide();
        SelenideLogger.addListener("AllureSelenide", report
                .screenshots(true)
                .includeSelenideSteps(true)
                .savePageSource(true)
        );
    }

    //@Step("Set Selenide Properties")
    public static void setSelenideProps(Config config){
        Configuration.browser = config.browserName;
        Configuration.browserPosition = config.browserPosition;
        Configuration.browserSize = config.browserSize;
        Configuration.baseUrl = config.baseUrl;
        Configuration.timeout = config.globalTimeout;
        //Configuration.fastSetValue = true; //Experimental!!!
    }


    @Test
    @Epic("Login")
    @Story("Positive")
    @DisplayName("Login: with valid credentials")
    //public void login_positive(String username) {
    public void login_positive() {
        //no steps! po + test
        String username = "vulyanov";
        LoginPage login_page = Selenide.open("", LoginPage.class);
        login_page.verifyPage();
        //verifyVisual();
        String password = username;
        MyTasksPage my_tasks_page = login_page.login(username, password);
        my_tasks_page.verifyPage();
        //verifyVisual();
    }

    /*
    @Test
    //@Disabled
    @Epic("Login")
    @Story("Negative")
    @DisplayName("Login: attempt with invalid username")
    public void login_invalid_username() {
        login("XXXXXXX", "vulyanov");
    }

    @Test
    //@Disabled
    @Epic("Login")
    @Story("Negative")
    @DisplayName("Login: attempt with invalid password")
    public void login_invalid_password() {
        login("vulyanov", "XXXXXXX");
    }

    @Test
    //@Disabled
    @Epic("Login")
    @Story("Negative")
    @DisplayName("Login: attempt with invalid credentials")
    public void login_invalid_creds() {
        login("ZZZZZZZ", "XXXXXXX");
    }

     */

}
