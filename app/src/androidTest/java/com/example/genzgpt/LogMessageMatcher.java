package com.example.genzgpt;

import android.util.Log;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LogMessageMatcher extends TypeSafeMatcher<View> {
    private final String expectedLogMessage;

    public LogMessageMatcher(String expectedLogMessage) {
        this.expectedLogMessage = expectedLogMessage;
    }

    @Override
    protected boolean matchesSafely(View item) {
        String logcatOutput = getLogcatOutput();
        return logcatOutput.contains(expectedLogMessage);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Log message containing: ").appendValue(expectedLogMessage);
    }

    @Override
    protected void describeMismatchSafely(View item, Description mismatchDescription) {
        mismatchDescription.appendText("Log message not found: ").appendValue(expectedLogMessage);
    }

    private String getLogcatOutput() {
        StringBuilder logBuilder = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                logBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            Log.e("LogMessageMatcher", "Error reading logcat output", e);
        }
        return logBuilder.toString();
    }
}
