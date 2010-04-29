# Enhancements to the `Selenium` Object
The `Selenium` instance provided by the `@SeleniumAware` mixin is actually an extension of the standard [Selenium][1] object that enhances it in a number of ways.

* Any Selenium method starting with _is_ has an equivalent _waitFor_ taking the same parameters. For example, to test that the text _"My Title"_ appears on the page you would use `selenium.isTextPresent("My Title")`. The plugin adds the ability to wait until that text appears using `selenium.waitForTextPresent("My Title")`.
* Any method starting with _get_ has an equivalent _waitFor_ taking the same parameters plus an _'expectation'_ String. For example to test that the text _"My Title"_ appears in the page's header you would use `selenium.getText("css=h1") == "My Title"`. The plugin allows you to wait for that condition using `selenium.waitForText("css=h1", "My Title")`.
* Any method starting with _get_ has an equivalent _waitFor_ taking the same parameters plus a [Hamcrest Matcher][2]. For example `selenium.waitForText("css=h1", equalTo("My Title"))`.
* The method `waitForPageToLoad(String)` has an overridden version that requires no arguments and will use the timeout set using `setTimeout` or the default timeout specified in configuration.
* Any method can be appended with _AndWait_ so that it will wait for the page to load before returning. Obviously this only makes sense on commands that trigger a page load. For example `selenium.clickAndWait("css=input.myButton")` will click the button _"myButton"_ and then wait for a new page to finish loading.
* Any _do..._ functions you have defined in a JavaScript user extensions file can be invoked directly from your Groovy code. For example, if you have defined a function `Selenium.prototype.doLogin = function(username, password)` then you can call `selenium.login("rob", "password")` directly from Groovy.
* Any _get..._ functions you have defined in a JavaScript user extension file can be invoked directly from your Groovy code. For example, if you have defined a function `Selenium.prototype.getLoginState = function(locator)` then you can call `selenium.getLoginState(locator)` directly from Groovy.

**Note:** It is _not_ necessary to add _AndWait_ to the `open()` command as it already waits for the page to finish loading before returning. Also be aware that _AndWait_ does _not_ wait for AJAX events. To handle AJAX updates to the page you should wait for a condition such as an element appearing or some text changing.

## Using waitFor directly
As well as using `Selenium` methods with _waitFor..._ and _...AndWait_ you can use the class `grails.plugins.selenium.condition.ClosureEvaluatingWait` directly. For example:

	import static grails.plugins.selenium.condition.ClosureEvaluatingWait.waitFor

	selenium.click("myButton")
	waitFor("Clicking myButtonId should have updated myDiv via AJAX") {
	    selenium.getText("myDiv") == "Some value"
	}

The `waitFor` method will fail if the expected condition does not hold true after the default timeout specified with `selenium.defaultTimeout` in your `SeleniumConfig.groovy` file.

[1]: http://release.seleniumhq.org/selenium-remote-control/1.0-beta-2/doc/java/com/thoughtworks/selenium/Selenium.html "com.thoughtworks.selenium.Selenium"
[2]: http://code.google.com/p/hamcrest/ "Hamcrest matcher library"