# 🐛 SonarCloud Automated Bug

Use this template to describe SonarCloud findings promoted to GitHub issues.

## 🐛 Description
{{DESCRIPTION}}

## 🔍 Location
- **SonarCloud Issue Key**: {{ISSUE_KEY}}
- **Rule**: {{RULE}}
- **Severity**: {{SEVERITY}}
- **File**: {{FILE_PATH}}
- **Line**: {{LINE}}
- **Branch**: {{BRANCH}}
- **Link**: {{SONAR_LINK}}

## ✅ Expected Behaviour
Code complies with the referenced SonarCloud rule and passes the quality gate.

## ❌ Detected Behaviour
{{MESSAGE}}

## 🧪 Validation
- Detected by: SonarCloud analysis
- Project: {{PROJECT_KEY}}
- Scope: Static analysis

## 💡 Proposed Fix
Review the rule guidance and refactor the code to satisfy the quality gate.
