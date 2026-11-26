package com.pathshala.tests;

import com.pathshala.base.BaseTest;
import com.pathshala.pages.InventoryPage;
import com.pathshala.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/** Fails ON PURPOSE to show the failure screenshot + trace. Run with: mvn test -Pfailing-demo */
@Feature("Failure demo")
@Tag("failing-demo")
class FailingDemoTest extends BaseTest {

    @Test
    @DisplayName("DEMO: expects a wrong page title (fails on purpose)")
    @Description("Intentional failure: the real title is 'Products'")
    void wrongTitleOnPurpose() {
        InventoryPage inventory = new LoginPage(page).open().loginAs("standard_user", PASSWORD);

        assertThat(inventory.title()).hasText("Our Products");
    }
}
