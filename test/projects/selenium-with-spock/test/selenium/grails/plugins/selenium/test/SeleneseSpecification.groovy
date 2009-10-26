/* Copyright 2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.plugins.selenium.test

import spock.lang.*
import grails.plugin.spock.*
import grails.plugins.selenium.*
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.codehaus.groovy.grails.commons.ApplicationHolder

class SeleneseSpecification extends Specification {

	ConfigObject getConfig() {
		SeleniumManager.instance.config
	}

	GrailsSelenium getSelenium() {
		SeleniumManager.instance.selenium
	}

	String getRootURL() {
		return "/${ConfigurationHolder.config."web.app.context.path" ?: ApplicationHolder.application.metadata."app.name"}"
	}

}