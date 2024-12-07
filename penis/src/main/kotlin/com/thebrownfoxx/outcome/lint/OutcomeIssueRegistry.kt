package com.thebrownfoxx.outcome.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.Issue

public class OutcomeIssueRegistry : IssueRegistry() {
    override val issues: List<Issue> = listOf(BlockContextRequiredDetector.ISSUE)

    override val vendor: Vendor =
        Vendor(
            vendorName = "The Brown Foxx",
            feedbackUrl = "https://github.com/the-brown-foxx/outcome/issues",
        )
}