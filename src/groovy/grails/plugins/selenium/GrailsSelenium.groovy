package grails.plugins.selenium

import com.thoughtworks.selenium.CommandProcessor
import com.thoughtworks.selenium.DefaultSelenium

/**
 * Extends capabilities of <tt>DefaultSelenium</tt> adding the ability to use <tt>*AndWait</tt> command such as
 * <tt>clickAndWait</tt> or <tt>fireEventAndWait</tt>.
 * Because it extends DefaultSelenium unlike GroovySelenium code completion works in IDEs.
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
		def match = name =~ /^(.+)AndWait$/
		if (match.find()) {
			def command = match[0][1]
			def result = "$command"(* args)
			waitForPageToLoad "$defaultTimeout"
			return result
		}
		throw new MissingMethodException(name, GrailsSelenium, args)
	}
}
