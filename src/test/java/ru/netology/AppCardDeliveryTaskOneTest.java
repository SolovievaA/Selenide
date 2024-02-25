package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AppCardDeliveryTaskOneTest {
    private String generateDate(int addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }
    @Test
    void sendFormTest() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Оренбург");
        String planningDate = generateDate( 4, "dd.MM.yyyy");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] [class='input__box'] [placeholder='Дата встречи']").setValue(planningDate);
        $("[data-test-id='name'] [type=text]").setValue("Сомин Сергей");
        $("[data-test-id='phone'] [type=tel]").setValue("+79845785654");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id='notification'] [class='notification__content']").should(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='notification'] [class='notification__content']").shouldHave(Condition.exactText("Встреча успешно забронирована на " + planningDate));
    }
    @Test
    void shouldEnterAnIncorrectName() {
        open("http://localhost:9999");
        String planningDate = generateDate( 4, "dd.MM.yyyy");
        $("[data-test-id=city] input").setValue("Уфа");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] [class='input__box'] [placeholder='Дата встречи']").setValue(planningDate);
        $("[data-test-id=name] input").setValue("Ivanov Stepan");
        $("[data-test-id=phone] input").setValue("+79810553498");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=name].input_invalid").shouldHave(exactText("Фамилия и имя Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldEnterIncorrectPhoneNumber() {
        open("http://localhost:9999");
        String planningDate = generateDate( 4, "dd.MM.yyyy");
        $("[data-test-id=city] input").setValue("Оренбург");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] [class='input__box'] [placeholder='Дата встречи']").setValue(planningDate);
        $("[data-test-id=name] input").setValue("Иванов Степан");
        $("[data-test-id=phone] input").setValue("8921013345");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=phone].input_invalid").shouldHave(exactText("Мобильный телефон Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldEnterAnIncorrectCity() {
        open("http://localhost:9999");
        String planningDate = generateDate( 4, "dd.MM.yyyy");
        $("[data-test-id=city] input").setValue("Актюбинск");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=name] input").setValue("Иванов Степан");
        $("[data-test-id=phone] input").setValue("+79819123466");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=city].input_invalid").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldEnterTheIncorrectDate() {
        open("http://localhost:9999");
        String planningDate = generateDate( 4, "dd.MM.yyyy");
        $("[data-test-id=city] input").setValue("Оренбург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "1", Keys.BACK_SPACE));
        $("[data-test-id=date] input").setValue(LocalDate.now().minusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id=name] input").setValue("Иванов Степан");
        $("[data-test-id=phone] input").setValue("+79814123466");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=date]").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldAllFieldsEmpty() {
        open("http://localhost:9999");
        String planningDate = generateDate( 4, "dd.MM.yyyy");
        $(".button").click();
        $("[data-test-id=city]").shouldHave(exactText("Поле обязательно для заполнения"));

    }

}
