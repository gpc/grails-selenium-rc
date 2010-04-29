# Extending `GrailsSeleniumTestCase`

**Note:**
The `GrailsSeleniumTestCase` class is considered _deprecated_. It is still available for backwards compatibility but will not be enhanced any further and may be removed in a future plugin version. The plugin now decorates the `Selenium` object itself with _waitFor_ methods and [Hamcrest matchers][3] provide a clearer alternative to the assertion methods provided by `SeleneseTestBase`.

Instead of using the `SeleniumAware` mixin you can extend `grails.plugins.selenium.GrailsSeleniumTestCase` which provides everything the mixin does with some additional capabilities. Firstly you can use some of the more advanced assertions provided by the [SeleneseTestBase][1] class as well as the various _verify_ methods, the _seleniumEquals_ method, etc. See the documentation for `SeleneseTestBase` for details.

## Using assert, verify and waitFor convenience methods
Extending `GrailsSeleniumTestCase` enables you to directly _assert, verify_ or _waitFor_ certain Selenium conditions. Any [Selenium][2] method starting with _is_ can be used as a boolean assertion by replacing the _is_ with _assert, verify_ or _waitFor_ in the method call. Likewise any `Selenium` method starting with _get_ can be used as an equality assertion. Some examples:

* `assertTextPresent("Welcome to Grails")` is equivalent to `assertTrue selenium.isTextPresent("Welcome to Grails")`
* `verifyVisible("myElement")` is equivalent to `verifyTrue selenium.isVisible("myElement")`
* `waitForAlertPresent()` is equivalent to `ClosureEvaluatingWait.waitFor { selenium.isAlertPresent() }`
* `assertText("Expected text", "myElement")` is equivalent to `assertEquals "Expected text", selenium.getText("myElement")`
* `waitForXpathCount 3, "//ul/li"` is equivalent to `ClosureEvaluatingWait.waitFor { selenium.getXpathCount("//ul/li") == 3 }`

You can also use _assertNot..._, _verifyNot..._ and _waitForNot..._ to test negated conditions.

[1]: http://release.seleniumhq.org/selenium-remote-control/1.0-beta-2/doc/java/com/thoughtworks/selenium/SeleneseTestBase.html "com.thoughtworks.selenium.SeleneseTestBase"
[2]: http://release.seleniumhq.org/selenium-remote-control/1.0-beta-2/doc/java/com/thoughtworks/selenium/Selenium.html "com.thoughtworks.selenium.Selenium"
[3]: http://code.google.com/p/hamcrest/ "Hamcrest"