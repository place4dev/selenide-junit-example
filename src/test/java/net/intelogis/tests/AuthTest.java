package net.intelogis.tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;

import static com.codeborne.selenide.Selenide.*;

public class AuthTest {

    // Элементы страницы в "боевых" условиях обычно выносятся в PageObject
    private static final SelenideElement AUTH_FORM = $(By.xpath("//div[contains(@class,'auth-form')]"));
    private static final SelenideElement RECOVERY_PASSWORD_LINK = $(By.cssSelector("a[href*='recovery']"));
    private static final SelenideElement REGISTER_LINK = $(By.cssSelector("a[href*='register']"));

    private static final SelenideElement RECOVERY_FORM = $(By.xpath("//div[@class='recovery-form']"));
    private static final SelenideElement REGISTER_FORM = $(By.xpath("//div[@class='register-form']"));
    private static final SelenideElement ENTRY_BUTTON = $(By.xpath("//button[@type='submit']"));
    private static final SelenideElement LOGOUT_BUTTON = $(By.xpath("//div[@class='ant-space-item']/button[@type='button']"));
    private static final SelenideElement CURRENT_USER = $(By.xpath("//*[contains(@class,'current-user')]"));

    private static final SelenideElement ALERT_MSG = $(By.xpath("//div[@role='alert'][@class='ant-form-item-explain-error']"));
    private static final SelenideElement AUTH_ERR_MESSAGE = $(By.xpath("//div[contains(@class,'ant-notification-bottomRight')]"));


    private static final String LOGIN = System.getProperty("login");
    private static final String PASSWORD = System.getProperty("password");

    @BeforeAll
    public static void setUp() {
        closeWebDriver();
        Configuration.baseUrl = "https://ilswebreact-develop.azurewebsites.net/";
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));
    }

    @BeforeEach
    public void openUrl() {
        open("");
    }

    @AfterEach
    public void logOut() {
        if(LOGOUT_BUTTON.isDisplayed() && LOGOUT_BUTTON.isEnabled()) {
            LOGOUT_BUTTON.click();
            // Selenide.sleep(1000); // ждём, пока появится форма авторизации
            AUTH_FORM.shouldBe(Condition.visible);
        }
    }

    @Test
    @DisplayName("Успешная авторизация через клавишу 'Enter'")
    void userCanLogInWithEnter() {
        $(By.id("login")).val(LOGIN);
        $(By.id("password")).val(PASSWORD).pressEnter();
        $(By.xpath("//*[contains(@class,'current-user')]")).isEnabled();
        Assertions.assertEquals("Тестер", $(By.xpath("//*[contains(@class,'current-user')]")).getText(),
                "Роль пользователя не определена. Требуется авторизация");
    }

    @Test
    @DisplayName("Успешная авторизация через кнопку 'Войти'")
    void userCanLogInWithEntryButton() {
        $(By.id("login")).val(LOGIN);
        $(By.id("password")).val(PASSWORD);
        if (ENTRY_BUTTON.isEnabled()) {
            ENTRY_BUTTON.click();
        } else
            throw new NoSuchElementException("Кнопка \"Войти\" не найдена на странице");

        CURRENT_USER.isEnabled();
        Assertions.assertEquals("Тестер", CURRENT_USER.getText());
    }

    @Test
    @DisplayName("Ввод пустого логина")
    void userCantLogInWithEmptyLogin() {
        SelenideElement loginField = $(By.id("login")).val("1");
        loginField.sendKeys(Keys.BACK_SPACE);

        Assertions.assertEquals("Пожалуйста, введите логин", ALERT_MSG.getText());
    }

    @Test
    @DisplayName("Ввод пустого пароля")
    void userCantLogInWithEmptyPassword() {
        SelenideElement passwordField = $(By.id("password")).val("1");
        passwordField.sendKeys(Keys.BACK_SPACE);
        Selenide.actions().moveToElement(passwordField, 100, 200).click();

        Assertions.assertEquals("Пожалуйста, введите пароль", ALERT_MSG.getText());
    }

    @Test
    @DisplayName("Логин кириллицей")
    void userCantLogInWithCyrillicChars() {
        $(By.id("login")).val("Гидроэлектростанция");
        $(By.id("password")).val(PASSWORD).pressEnter();

        Assertions.assertEquals("Ошибка авторизации. Не верные логин/пароль", AUTH_ERR_MESSAGE.getText());

    }

    @Test
    @DisplayName("Неверный пароль")
    void userCantLogInWithIncorrectPassword() {
        $(By.id("login")).val(LOGIN);
        $(By.id("password")).val("qwerty").pressEnter();

        Assertions.assertEquals("Ошибка авторизации. Не верные логин/пароль", AUTH_ERR_MESSAGE.getText());
    }

    @Test
    @DisplayName("Ссылка восстановления пароля")
    void passwordRecoveryLink() {
        RECOVERY_PASSWORD_LINK.click();
        Assertions.assertTrue(RECOVERY_FORM.isDisplayed());
    }

    @Test
    @DisplayName("Ссылка восстановления пароля")
    void registerLink() {
        REGISTER_LINK.click();
        Assertions.assertTrue(REGISTER_FORM.isDisplayed());
    }

}
