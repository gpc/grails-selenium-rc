package com.energizedwork.grails.binding

import java.beans.PropertyEditorSupport
import static org.apache.commons.lang.StringUtils.capitalize

class DomainLookupPropertyEditor extends PropertyEditorSupport {

	private final Class domainType
	private final String propertyName

	DomainLookupPropertyEditor(Class domainType, String propertyName) {
		this.domainType = domainType
		this.propertyName = propertyName
	}

	void setAsText(String text) {
		if (text) {
			value = domainType."findBy${capitalize(propertyName)}"(text) ?: domainType.newInstance("$propertyName": text)
		} else {
			value = null
		}
	}

	String getAsText() {
		return value?."$propertyName"
	}

}
