package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$$;
import static ru.netology.delivery.data.DataGenerator.generateDate;

public class DeliveryCardTest {


    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = generateDate(daysToAddForSecondMeeting);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $("button.button").click();
        $(".notification__title").shouldBe(visible, Duration.ofSeconds(10))
                .shouldHave(exactText("Успешно!"));
        $(".notification__content").shouldBe(visible, Duration.ofSeconds(10))
                .shouldHave(exactText("Встреча успешно запланирована на " + firstMeetingDate));
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $(byText("Запланировать")).click();
        $("[data-test-id='replan-notification'] .notification__content").shouldBe(visible)
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $(byText("Перепланировать")).click();
        $(".notification__content").shouldBe(visible, Duration.ofSeconds(10))
                .shouldHave(exactText("Встреча успешно запланирована на " + secondMeetingDate));
    }
}




// TODO: добавить логику теста в рамках которого будет выполнено планирование и перепланирование встречи.
// Для заполнения полей формы можно использовать пользователя validUser и строки с датами в переменных
// firstMeetingDate и secondMeetingDate. Можно также вызывать методы generateCity(locale),
// generateName(locale), generatePhone(locale) для генерации и получения в тесте соответственно города,
// имени и номера телефона без создания пользователя в методе generateUser(String locale) в датагенераторе