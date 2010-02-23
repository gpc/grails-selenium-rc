package grails.plugins.selenium.pageobjects

/**
 * A page object for Grails scaffolded list pages.
 */
class GrailsListPage extends GrailsPage {

	static GrailsListPage open(String uri) {
		GrailsPage.open(uri)
		return new GrailsListPage()
	}

	@Lazy List columnNames = (1..columnCount).collect {i ->
		selenium.getText("//table/thead/tr/th[$i]")
	}

	/**
	 * Returns the number of columns in the table.
	 */
	int getColumnCount() {
		selenium.getXpathCount("//table/thead/tr[1]/th")
	}

	/**
	 * Returns the number of rows in the table body (not including any <tt>thead</tt> rows).
	 */
	int getRowCount() {
		selenium.getXpathCount("//table/tbody/tr")
	}

	/**
	 * Gets the data from the list table, each element in the list is a Map whose keys correspond to the column names
	 * and whose values are the text contained in the table cells on the page.
	 */
	List getRows() {
		(1..rowCount).collect {row ->
			def data = [:]
			columnNames.eachWithIndex {key, column ->
				data."$key" = selenium.getText("//table/tbody/tr[$row]/td[${column + 1}]")
			}
			return data
		}
	}

	/**
	 * Clicks the column heading to sort the table.
	 */
	GrailsListPage sortByColumn(String column) {
		selenium.clickAndWait "link=$column"
		return this
	}

	GrailsListPage nextPage() {
		selenium.clickAndWait "css=a.nextLink"
		return this
	}

	GrailsListPage previousPage() {
		selenium.clickAndWait "css=a.prevLink"
		return this
	}

	protected void validate() {
		def title = selenium.title
		if (!(title ==~ /.+ List/)) {
			throw new InvalidPageStateException("Incorrect page with title '$title' found")
		}
	}

}