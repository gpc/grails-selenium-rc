package com.energizedwork.grails.binding

import org.springframework.beans.PropertyEditorRegistrar
import org.springframework.beans.PropertyEditorRegistry
import musicstore.Artist

class CustomPropertyEditorRegistrar implements PropertyEditorRegistrar {

	void registerCustomEditors(PropertyEditorRegistry registry) {
		registry.registerCustomEditor Artist, new DomainLookupPropertyEditor(Artist, "name")
	}
}
