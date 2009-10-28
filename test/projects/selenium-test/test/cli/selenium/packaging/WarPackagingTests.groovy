package selenium.packaging

import grails.test.AbstractCliTestCase
import java.util.zip.ZipFile

class WarPackagingTests extends AbstractCliTestCase {

	void testSeleniumPluginIsNotBundledInApplicationWar() {
		execute(["war"])
		assertEquals 0, waitForProcess()
		verifyHeader()

		def warFile = new File("target/selenium-test-0.1.war") // TODO: work this out rather than hardcoding
		assertTrue warFile.isFile()

		def zipFile = new ZipFile(warFile)
		try {
			def entryNames = zipFile.entries()*.name
			assertEquals("War file should not contain web folders from Selenium plugin", [], entryNames.findAll {
				it =~ /^plugins\/selenium-rc-[\d\.]+\/(css|images|js)/
			})
			assertFalse "War file should not contain Selenium libraries", entryNames.any {
				it == "WEB-INF/lib/selenium-java-client-driver.jar"
			}

			// TODO: can't get this to work
//			assertFalse "War file should not contain Selenium plugin classes", entryNames.any {
//				it == "WEB-INF/classes/grails/plugins/selenium/Selenese.class"
//			}
		} finally {
			zipFile.close()
		}
	}

}