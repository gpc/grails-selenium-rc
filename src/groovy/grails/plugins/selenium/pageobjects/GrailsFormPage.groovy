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

package grails.plugins.selenium.pageobjects

import com.thoughtworks.selenium.SeleniumException
import org.apache.commons.lang.StringUtils

/**
 * A base page object for scaffolded Grails form pages (i.e. create & edit).
 */
abstract class GrailsFormPage extends GrailsPage {

	GrailsFormPage() {
		super()
	}

	protected GrailsFormPage(String uri) {
		super(uri)
	}

	/**
	 * Returns standard Grails form error messages if present, otherwise empty list.
	 */
	List getErrorMessages() {
		def errorCount = selenium.getXpathCount("//div[@class='errors']/ul/li")
		if (errorCount > 0) {
			return (1..errorCount).collect {i ->
				selenium.getText "//div[@class='errors']/ul/li[$i]"
			}
		} else {
			return []
		}
	}

	/**
	 * Returns true if the named form input is highlighted for errors.
	 */
	boolean hasFieldErrors(String name) {
		selenium.isElementPresent "css=.errors input[name=$name]"
	}

	/**
	 * Intercepts property getters to return value of form field.
	 */
	def propertyMissing(String name) {
		def type = getFieldType(name)
		switch (type) {
			case null:
				throw new MissingPropertyException(name)
			case FieldType.TEXT:
			case FieldType.TEXTAREA:
			case FieldType.HIDDEN:
			case FieldType.PASSWORD:
				return selenium.getValue(name)
			case FieldType.CHECKBOX:
				return selenium.isChecked(name)
			case FieldType.SELECT:
				if (isMultiSelect(name)) {
					return selenium.isSomethingSelected(name) ? selenium.getSelectedLabels(name) as List : []
				} else {
					return selenium.isSomethingSelected(name) ? selenium.getSelectedLabel(name) : null
				}
			case FieldType.RADIO:
				// TODO: support radio buttons
			default:
				throw new UnsupportedOperationException("Can't handle $type")
		}
	}

	/**
	 * Intercepts property setters to set value of form field.
	 */
	def propertyMissing(String name, value) {
		def type = getFieldType(name)
		switch (type) {
			case null:
				throw new MissingPropertyException(name)
			case FieldType.TEXT:
			case FieldType.TEXTAREA:
			case FieldType.PASSWORD:
				selenium.type(name, value)
				break
			case FieldType.CHECKBOX:
				value ? selenium.check(name) : selenium.uncheck(name)
				break
			case FieldType.SELECT:
				if (value instanceof Collection || value.getClass().isArray()) {
					selenium.removeAllSelections(name)
					value.each { selenium.addSelection(name, it) }
				} else {
					selenium.select(name, value)
				}
				break
			case FieldType.HIDDEN:
				throw new ReadOnlyPropertyException(name, getClass())
			case FieldType.RADIO:
				// TODO: support radio buttons
			default:
				throw new UnsupportedOperationException("Can't handle $type")
		}
	}

	private FieldType getFieldType(String name) {
		def fieldType = null
		if (selenium.isElementPresent("css=input[name=$name]")) {
			def type = selenium.getAttribute("css=input[name=$name]@type")
			fieldType = type ? FieldType.valueOf(type.toUpperCase()) : FieldType.TEXT
		}
		else if (selenium.isElementPresent("css=select[name=$name]")) {
			fieldType = FieldType.SELECT
		} else if (selenium.isElementPresent("css=textarea[name=$name]")) {
			fieldType = FieldType.TEXTAREA
		}
		return fieldType
	}

	private boolean isMultiSelect(String name) {
		return isAttributePresent("$name@multiple") && selenium.getAttribute("$name@multiple") in ["multiple", "true", "yes"]
	}

	/** TODO: seriously Selenium, thanks for not providing an isAttributePresent  */
	private boolean isAttributePresent(String locator) {
		if (!selenium.isElementPresent(StringUtils.substringBefore(locator, "@"))) {
			return false
		}
		try {
			return selenium.getAttribute(locator)
		} catch (SeleniumException e) {
			return false
		}
	}

}

enum FieldType {
	TEXT, HIDDEN, CHECKBOX, RADIO, FILE, IMAGE, PASSWORD, RESET, SUBMIT, BUTTON, SELECT, TEXTAREA
}