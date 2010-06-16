/*
 * Copyright 2010 Rob Fletcher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
		"1.0.2" // TODO: work this out rather than hardcoding
	}

}