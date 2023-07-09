package pages.ttt;

import com.codeborne.selenide.Condition;
import pages.BasePage;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class MyTasksPage extends BasePage {

    public void verifyPage(){
        verifyPageTitle();
        verifyPageContent();
        verifyPageLoaded();
    }

    public boolean checkPage() {
    return $("title").has(Condition.attribute("text","Мои задачи - TTT | Noveo"));
    }

    public void verifyPageTitle(){
        $("title").shouldHave(Condition.attribute("text","Мои задачи - TTT | Noveo"));
    }

    public void verifyPageContent(){
        $(withText("Сообщить разработчикам об ошибке")).shouldHave(Condition.exist);
    }

    public void verifyPageLoaded() {
        $(withText("Загрузка данных...")).shouldHave(Condition.not(Condition.exist));
    }

    public String getAppVersion(){
        return $("span[class='build']").getText();
    }

    public LogoutPage logout(){
        $("button[class*='exit']").click();
        $(withText("Выйти")).click();
        return new LogoutPage();
    }
}
