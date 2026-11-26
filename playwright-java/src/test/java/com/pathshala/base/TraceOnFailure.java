package com.pathshala.base;

import com.microsoft.playwright.Tracing;
import io.qameta.allure.Allure;
import io.qameta.allure.AttachmentOptions;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Runs right after the test body (before @AfterEach): keeps evidence only when the test failed. */
public class TraceOnFailure implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext ctx) throws IOException {
        BaseTest test = (BaseTest) ctx.getRequiredTestInstance();
        if (ctx.getExecutionException().isEmpty()) {
            test.context.tracing().stop();
            return;
        }
        byte[] png = test.page.screenshot();
        Allure.attachment("Failure screenshot", "image/png", new ByteArrayInputStream(png),
                AttachmentOptions.withFileExtension(".png"));

        String name = ctx.getRequiredTestClass().getSimpleName() + "_" + ctx.getDisplayName();
        Path trace = Path.of("target", "traces", name.replaceAll("[^A-Za-z0-9]+", "_") + ".zip");
        test.context.tracing().stop(new Tracing.StopOptions().setPath(trace));
        Allure.attachment("Playwright trace", "application/zip",
                Files.newInputStream(trace), AttachmentOptions.withFileExtension(".zip"));
    }
}
