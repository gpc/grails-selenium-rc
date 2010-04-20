package selenium.packaging

import grails.test.AbstractCliTestCase
import java.util.regex.Pattern
import java.util.zip.ZipFile
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.junit.Test
import static org.hamcrest.CoreMatchers.not
import static org.junit.Assert.assertThat
import static org.junit.matchers.JUnitMatchers.hasItem
import static selenium.packaging.FileExistsMatcher.exists
import static selenium.packaging.RegexMatcher.matching

class WarPackagingTests extends AbstractCliTestCase {

	@Test
	void seleniumPluginIsNotBundledInApplicationWar() {
		File warFile = buildWarFile()
		assertThat "War file", warFile, exists()

		def zipFile = new ZipFile(warFile)
		try {
			def entryNames = zipFile.entries()*.name
			assertThat "War file entries", entryNames, not(hasItem(matching(/^plugins\/selenium-rc-[\d\.]+\/(css|images|js)/)))
			assertThat "War file entries", entryNames, not(hasItem(matching(/^WEB-INF\/lib\/(\w+\/)*selenium.*\.jar$/)))
			assertThat "War file entries", entryNames, not(hasItem(matching(/^WEB-INF\/classes\/grails\/plugins\/selenium\//)))
		} finally {
			zipFile.close()
		}
	}

	private File buildWarFile() {
		execute(["war"])
		enterInput "y" // Grails now nags about inline plugins and --non-interactive defaults to "n"!
		assertEquals 0, waitForProcess()
		verifyHeader()

		return new File("target/selenium-test-0.1.war") // TODO: work this out rather than hardcoding
	}

}

class FileExistsMatcher extends BaseMatcher<File> {

	static FileExistsMatcher exists() {
		new FileExistsMatcher()
	}

	boolean matches(Object o) {
		return o instanceof File && o.isFile()
	}

	void describeTo(Description description) {
		description.appendText("a file that exists")
	}
}

class RegexMatcher extends BaseMatcher<String> {

	static RegexMatcher matching(String pattern) {
		new RegexMatcher(pattern)
	}

	static RegexMatcher matching(Pattern pattern) {
		new RegexMatcher(pattern)
	}

	private final Pattern pattern

	RegexMatcher(String pattern) {
		this.pattern = Pattern.compile(pattern)
	}

	RegexMatcher(Pattern pattern) {
		this.pattern = pattern
	}

	boolean matches(Object o) {
		return o.toString() =~ pattern
	}

	void describeTo(Description description) {
		description.appendText("a String matching ").appendValue(pattern)
	}
}