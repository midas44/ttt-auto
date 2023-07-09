package pages.ttt;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;
import pages.BasePage;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

//@ExtendWith(BasePage.class)
public class LoginPage extends BasePage {

    public void verifyPage(){
        verifyPageTitle();
        verifyPageContent();
    }
    public void verifyPageTitle(){
        $("title").shouldHave(Condition.attribute("text","CAS - Central Authentication Service"));
    }
    public void verifyPageContent(){
        $(By.name("submit")).shouldHave(Condition.exist);
    }

    public MyTasksPage login(String username, String password){
        $(By.name("username")).val(username);
        $(By.name("password")).val(password);
        $(By.name("submit")).click();
        return page(MyTasksPage.class);
    }

}
