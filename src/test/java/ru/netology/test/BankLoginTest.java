package ru.netology.test;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class BankLoginTest {
    @AfterAll
    static void teardown() {
        SQLHelper.cleanDataBase();
    }

    @Test
    void shouldSuccessfulLogin() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.makeValidLogin(authInfo);
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @SneakyThrows
    @Test
    void shouldBlockedUser() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.generateRandomPassworForUserVasya();
        loginPage.makeInvalidLogin(authInfo);
        loginPage.clearFields();
        loginPage.makeInvalidLogin(authInfo);
        loginPage.clearFields();
        loginPage.makeInvalidLogin(authInfo);
        String blockedName = SQLHelper.checkingForABlockedUser();
        String name = "vasya";
        Assertions.assertEquals(blockedName, name);
    }
}