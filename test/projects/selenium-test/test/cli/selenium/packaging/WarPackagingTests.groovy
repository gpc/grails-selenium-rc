package selenium.packaging

import grails.test.AbstractCliTestCase
import java.util.regex.Pattern
import java.util.zip.ZipFile
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import static org.hamcrest.CoreMatchers.not
import static selenium.packaging.FileExistsMatcher.exists
import static com.energizedwork.matcher.IsMatch.*
import static org.hamcrest.MatcherAssert.*
import static org.hamcrest.Matchers.*

class WarPackagingTests extends AbstractCliTestCase {

	void testSeleniumPluginIsNotBundledInApplicationWar() {
		File warFile = buildWarFile()
		assertThat "War file", warFile, exists()

		def zipFile = new ZipFile(warFile)
		try {
			def entryNames = zipFile.entries()*.name
			assertThat "War file entries", entryNames, not(hasItem(isMatch(/^plugins\/selenium-rc-[\d\.]+\/(css|images|js)/)))
			assertThat "War file entries", entryNames, not(hasItem(isMatch(/^WEB-INF\/lib\/(\w+\/)*selenium.*\.jar$/)))
			assertThat "War file entries", entryNames, not(hasItem(isMatch(/^WEB-INF\/classes\/grails\/plugins\/selenium\//)))
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
