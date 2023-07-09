package pages.ttt;

import com.codeborne.selenide.Condition;
import pages.BasePage;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class LogoutPage extends BasePage {

    public void verifyPage(){
        verifyPageTitle();
        verifyPageContent();
    }
    public void verifyPageTitle(){
        $("title").shouldHave(Condition.attribute("text","CAS - Central Authentication Service"));
    }
    public void verifyPageContent(){
        $(byText("Logout successful")).shouldHave(Condition.exist);
    }
}
