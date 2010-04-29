# Selenium RC Configuration
Installing the plugin should create an empty configuration file in `grails-app/conf/SeleniumConfig.groovy` that can be customised according to your requirements. Configuration values are:

* `selenium.browser`: The browser Selenium will use. Defaults to `*firefox` on Linux, `*safari` on OSX and `*iexplore` on Windows.
* `selenium.defaultTimeout`: The timeout in milliseconds after which an _...AndWait_ or _waitFor..._ method will fail. Default to `60000`.
* `selenium.remote`: If set to `true` the test will run without starting the application. This is useful for running tests against an instance running on a remote host. Defaults to `false`.
* `selenium.server.host`: The host the Selenium Server will run on. Defaults to `localhost`.
* `selenium.server.port`: The port the Selenium Server will run on. Defaults to `4444`.
* `selenium.singleWindow`: If `false` Selenium will run in multiple windows which can be useful if your app has frame escape code. **Note:** this config value is ignored when selenium.browser is `*iexplore` or `*safari` as certain modes do not work with those browsers. Defaults to `true` on all browsers other than Internet Explorer.
* `selenium.slow`: If set to `true` the Selenium tests will run slowly. Defaults to `false`.
* `selenium.url`: The base URL where the tests will be run. Defaults to The Grails server URL (`grails.serverURL` in the application config).
* `selenium.windowMaximize`: If set to `true` the browser window will be maximized when opened. This has no effect in single window mode. Defaults to `false`.
* `selenium.screenshot.dir`: The directory where screenshots will be stored. Defaults to `target/test-reports/screenshots`.
* `selenium.screenshot.onFail`: If set to `true` a screenshot will be taken when a test fails. Defaults to `false`.
* `selenium.userExtensions`: The path to an optional user extension JavaScript file. Not set by default.

## Environment specific configuration
Environment specific configuration can be included in `SeleniumConfig.groovy` just as it can in Grails' regular `Config.groovy` file. For example:

	selenium {
	    host = "localhost"
	    port = 1234
	}
	environments {
	    test {
	        selenium.host = "www.pirates.bv"
	        selenium.port = 5678
	    }
	}

## Using system properties
All configuration values can be overridden using system properties. This is probably most useful for overriding the browser used on individual developers' machines or on continuous integration environments without affecting the project-wide settings. For example to run Selenium tests using Firefox instead of your platform's default browser you can use `grails -Dselenium.browser=*firefox test-app` directly on the command line.

To make such settings permanent on an individual machine add the required settings to the `JAVA_OPTS` environment variable. For example, add `export JAVA_OPTS="$JAVA_OPTS -Dselenium.browser=*firefox"` to your `~/.profile` file (on Windows environment variables are set in the _Advanced_ tab of the _System Properties_ window).
