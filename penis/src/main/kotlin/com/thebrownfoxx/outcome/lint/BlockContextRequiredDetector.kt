package com.thebrownfoxx.outcome.lint

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression

public class BlockContextRequiredDetector : Detector(), SourceCodeScanner {
    public companion object {
        public val ISSUE: Issue = Issue.create(
            id = "BlockContextRequiredIssue",
            briefDescription = "BlockContext is required.",
            explanation = "Provide a `com.thebrownfoxx.outcome.BlockContext` for this function",
            category = Category.CUSTOM_LINT_CHECKS,
            priority = 7,
            severity = Severity.WARNING,
            implementation = Implementation(
                BlockContextRequiredDetector::class.java,
                Scope.JAVA_FILE_SCOPE,
            ),
        )
    }

    override fun getApplicableCallNames(): List<String> = listOf(
        "com.thebrownfoxx.outcome.Failure.mapError",
        "com.thebrownfoxx.outcome.Failure.asMappedFailure",
    )

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        if (node.valueArguments.none { it.getExpressionType()?.canonicalText == "com.thebrownfoxx.outcome.BlockContext" }) {
            context.report(ISSUE, context.getLocation(node), "Please provide a `com.thebrownfoxx.outcome.BlockContext`")
        }
    }
}