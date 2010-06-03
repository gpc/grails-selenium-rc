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

package grails.plugins.selenium

import com.thoughtworks.selenium.DefaultSelenium
import com.thoughtworks.selenium.HttpCommandProcessor

class SeleniumRunner {

	SeleniumWrapper startSelenium(ConfigObject seleniumConfig) {
		def host = seleniumConfig.selenium.server.host
		def port = seleniumConfig.selenium.server.port
		def browser = seleniumConfig.selenium.browser
		def url = seleniumConfig.selenium.url
		def maximize = seleniumConfig.selenium.windowMaximize

		def commandProcessor = new HttpCommandProcessor(host, port, browser, url)
		def selenium = new DefaultSelenium(commandProcessor)
		
		SeleniumHolder.selenium = new SeleniumWrapper(selenium, commandProcessor, seleniumConfig)
		SeleniumHolder.selenium.start()
		if (maximize) {
			SeleniumHolder.selenium.windowMaximize()
		}

		return SeleniumHolder.selenium
	}

	void stopSelenium() {
		SeleniumHolder.selenium?.stop()
		SeleniumHolder.selenium = null
	}

}
