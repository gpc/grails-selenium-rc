package grails.plugins.selenium.pageobjects

/**
 * A base page object for typical Grails show pages (e.g. scaffolded show pages).
 */
class GrailsShowPage extends GrailsPage {

	@Lazy List fieldNames = (0..<fieldCount).collect {i ->
		selenium.getTable "//table.$i.0"
	}

	int getFieldCount() {
		selenium.getXpathCount("//table/tbody/tr")
	}

	/**
	 * Intercepts property getters to return data from table based on the field name.
	 */
	def propertyMissing(String name) {
		if (!fieldNames) readFieldNames()
		def i = fieldNames.indexOf(name)
		selenium.getTable "//table.$i.1"
	}
}