package grails.plugins.selenium.pageobjects

import grails.plugins.selenium.SeleniumAware
import grails.plugins.selenium.SeleniumWrapper
import java.util.regex.Pattern

/**
 * A base page object that validates that the browser is on the correct page when instances are constructed.
 */
@Mixin(SeleniumAware)
abstract class Page {

	/**
	 * Constructor that expects the browser to be on the correct page at the time the constructor is called.
	 * This constructor should be used by navigation methods in other page objects that perform some action such as
	 * clicking a link that should load the page that this class represents.
	 *
	 * @throws UnexpectedPageException if the browser is not currently on the page that this class represents.
	 */
	Page() throws UnexpectedPageException {
		verifyPage()
	}

	/**
	 * Constructor that expects the browser to be on the correct page _after_ opening the specified URI. This
	 * constructor should be used by factory methods in implementations that open the page. Ideally the URI itself
	 * should not need to be known by tests.
	 *
	 * @throws UnexpectedPageException if opening uri does not take the browser to the page that this class represents.
	 */
	protected Page(String uri) throws UnexpectedPageException {
		if (!uri.startsWith(selenium.contextPath)) {
			uri = selenium.contextPath + uri
		}
		selenium.open(uri)
		verifyPage()
	}

	/**
	 * Implementations should override this method to verify that the Selenium browser is on the correct page.
	 * Typically this is done with a simple check of something like the page title.
	 *
	 * @throws UnexpectedPageException if the current page open in the Selenium browser is not the correct page.
	 */
	protected abstract void verifyPage() throws UnexpectedPageException

	/**
	 * Seems to make code completion work better for sub-classes.
	 */
	protected final SeleniumWrapper getSelenium() {
		return selenium
	}

	/**
	 * Convenience method for {@link #verifyPage} implementations that use the page title.
	 *
	 * @throws UnexpectedPageException if the current page title does not match the specified pattern.
	 */
	protected final void pageTitleMatches(Pattern pattern) throws UnexpectedPageException {
		def title = selenium.title
		if (!(title ==~ pattern)) {
			throw new UnexpectedPageException("Expected page title matching /$pattern/ but found '$title'")
		}
	}

	/**
	 * Convenience method for {@link #verifyPage} implementations that use the page title.
	 *
	 * @throws UnexpectedPageException if the current page title is not equal to the specified String.
	 */
	protected final void pageTitleIs(String expectedTitle) throws UnexpectedPageException {
		def title = selenium.title
		if (title != expectedTitle) {
			throw new UnexpectedPageException("Expected page title '$expectedTitle' but found '$title'")
		}
	}
}