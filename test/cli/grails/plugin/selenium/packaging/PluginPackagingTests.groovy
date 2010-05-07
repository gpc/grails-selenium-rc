package grails.plugin.selenium.packaging

import grails.test.AbstractCliTestCase
import java.util.zip.ZipFile
import org.codehaus.groovy.grails.plugins.*

class PluginPackagingTests extends AbstractCliTestCase {

	void testGmockIsNotBundledWithPlugin() {
		execute(["package-plugin"])
		assertEquals 0, waitForProcess()
		verifyHeader()

		def packagedPlugin = new File("grails-selenium-rc-${pluginVersion}.zip") 
		assertTrue packagedPlugin.isFile()

		def zipFile = new ZipFile(packagedPlugin)
		try {
			def entryNames = zipFile.entries()*.name
			assertFalse "Packaged plugin should not contain GMock library", entryNames.any {
				it =~ /^lib\/gmock.*?\.jar/
			}

			assertFalse "Packaged plugin should not contain web-app resources", entryNames.any {
				it =~ /^web-app\//
			}
		} finally {
			zipFile.close()
		}
	}
	
	def getPluginVersion() {
		"1.0.2-SNAPSHOT" // TODO: work this out rather than hardcoding
	}

}