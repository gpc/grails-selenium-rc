package grails.plugins.selenium.pageobjects

/**
 * A base page object for typical Grails list pages (e.g. scaffolded list pages).
 */
abstract class GrailsListPage extends GrailsPage {

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
	void sortByColumn(String column) {
		selenium.clickAndWait "link=$column"
	}

}