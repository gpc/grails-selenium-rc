# Running Selenium RC Tests
Selenium tests are placed in `test/selenium` and are executed as part of the functional test phase with the standard `grails test-app` or `grails test-app functional:` commands. As with unit and integration tests you can run individual tests or groups of tests by specifying the test names without the _"Tests"_ suffix. Again, like unit and integration tests you can use `grails test-app -rerun` to rerun failing tests.

In Grails 1.2+ you can use the new syntax to isolate Selenium tests, for example `grails test-app :selenium`

The plugin will automatically start Selenium Server and Selenium instances at the start of the suite and close them down cleanly at the end so your tests do not need to (and indeed should not) do so.

## Browser Peculiarities
The table below summarises browser/OS combinations that the plugin has been tested with and any issues known to exist with particular combinations. Please note these are likely issues of the Selenium library or the underlying browser rather than anything the plugin can do anything about.

<table>
	<thead>
		<tr><th>Browser</th><th>selenium.browser</th><th>Operating System</th><th>Comments</th></tr>
	</thead>
	<tbody>
		<tr><td>Firefox 3</td><td>*firefox</td><td>Linux</td><td>Works perfectly.</td></th>
		<tr><td>Firefox 3.5</td><td>*firefox</td><td>Linux</td><td>Works perfectly.</td></th>
		<tr><td>Firefox 3.5</td><td>*firefox</td><td>OSX 10.6</td><td>Works perfectly.</td></th>
		<tr><td>Firefox 3.6</td><td>*firefox</td><td>OSX 10.6</td><td>Works perfectly.</td></th>
		<tr><td>Safari 4</td><td>*safari</td><td>OSX 10.6</td><td>Multi-window mode doesn't work.</td></th>
		<tr><td>Firefox 3.5</td><td>*firefox</td><td>Windows XP</td><td>Works perfectly.</td></th>
		<tr><td>Google Chrome</td><td>*googlechrome</td><td>Windows XP</td><td>Works perfectly.</td></th>
		<tr><td>Internet Explorer 6</td><td>*iexplore</td><td>Windows XP</td><td>Javascript errors in single window mode every time a page opens. The <code>dragAndDropToObject</code> command can fail if the target is outside the viewport - suggested workaround is to have your test call <code>selenium.windowMaximize()</code>.</td></th>
		<tr><td>Internet Explorer 7</td><td>*iexplore</td><td>Windows XP</td><td>See Internet Explorer 6.</td></th>
		<tr><td>Opera 9</td><td>*opera</td><td>Windows XP</td><td>Works perfectly.</td></th>
		<tr><td>Safari 4</td><td>*safari</td><td>Windows XP</td><td>Multi-window mode doesn't work.</td></th>
	</tbody>
</table>

If you are able to add to this list, whether to report issues or confirm success with particular browser/OS combinations please contact the plugin author.

## Capturing screenshots on test failures
By setting `selenium.screenshot.onFail` to `true` you can have Selenium capture a screenshot when a test fails. By default screenshots are saved to `target/test-reports/screenshots` and named according to the test case and test that failed.

**Note:** Selenium's screenshot functionality is imperfect. It does not grab an image of the browser window but of the entire desktop. This may be useful so long as the browser that Selenium is driving is visible (i.e. not hidden behind other windows) when the screenshot is taken.