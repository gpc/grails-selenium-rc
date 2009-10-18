package grails.plugins.selenium

import com.thoughtworks.selenium.DefaultSelenium
import com.thoughtworks.selenium.CommandProcessor
import java.util.regex.Matcher

/**
 * Extends capabilities of {@link DefaultSelenium}. Because it extends DefaultSelenium unlike GroovySelenium code
 * completion works in IDEs.
 */
class GrailsSelenium extends DefaultSelenium {

	int defaultTimeout = 60000

	GrailsSelenium(String serverHost, int serverPort, String browserStartCommand, String browserURL) {
		super(serverHost, serverPort, browserStartCommand, browserURL)
	}

	GrailsSelenium(CommandProcessor processor) {
		super(processor)
	}

	def methodMissing(String name, args) {
		Matcher match = name =~ /^(.+)AndWait$/
		if (match.find()) {
			def command = match[0][1]
			def result = super."$command"(*args)
			super.waitForPageToLoad "$defaultTimeout"
			return result
		}
	}
}
