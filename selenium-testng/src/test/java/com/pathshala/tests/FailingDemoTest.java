package com.pathshala.tests;

import static org.testng.Assert.assertEquals;

import com.pathshala.base.BaseTest;
import com.pathshala.listeners.RetryAnalyzer;
import io.qameta.allure.Description;
import org.testng.annotations.Test;

public class FailingDemoTest extends BaseTest {

    @Test(groups = "failing-demo", retryAnalyzer = RetryAnalyzer.class)
    @Description("Intentional failure: expects a wrong page title, to show a screenshot and a retry in Allure")
    public void wrongTitleFailsOnPurpose() {
        assertEquals(loginAsStandardUser().title(), "All Products");
    }
}
