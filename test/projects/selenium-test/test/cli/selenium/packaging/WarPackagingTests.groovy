package selenium.packaging

import grails.test.AbstractCliTestCase
import java.util.zip.ZipFile

class WarPackagingTests extends AbstractCliTestCase {

	void testSeleniumPluginIsNotBundledInApplicationWar() {
		File warFile = buildWarFile()
		assertTrue warFile.isFile()

		def zipFile = new ZipFile(warFile)
		try {
			def entryNames = zipFile.entries()*.name
			assertEquals("War file should not contain web folders from Selenium plugin", [], entryNames.findAll {
				it =~ /^plugins\/selenium-rc-[\d\.]+\/(css|images|js)/
			})
			assertEquals "War file should not contain Selenium libraries", [], entryNames.findAll {
				it =~ /^WEB-INF\/lib\/(\w+\/)*selenium.*\.jar$/
			}
			assertEquals "War file should not contain classes from Selenium plugin", [], entryNames.findAll {
				it =~ /^WEB-INF\/classes\/grails\/plugins\/selenium\//
			}
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