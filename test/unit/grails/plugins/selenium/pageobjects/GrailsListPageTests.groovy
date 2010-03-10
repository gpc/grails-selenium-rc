package grails.plugins.selenium.pageobjects

import org.gmock.WithGMock
import grails.plugins.selenium.GrailsSelenium
import grails.plugins.selenium.SeleniumManager
import grails.test.GrailsUnitTestCase

@WithGMock
class GrailsListPageTests extends GrailsUnitTestCase {

	def selenium

	void setUp() {
		super.setUp()

		mockConfig "grails.app.context='/'"

		selenium = mock(GrailsSelenium)
		SeleniumManager.instance.selenium = selenium
	}

	void testOpenFailsIfWrongPageLoads() {
		selenium.open("/thing/list")
		selenium.getTitle().returns("WTF Page is this?")
		play {
			shouldFail(InvalidPageStateException) {
				GrailsListPage.open("/thing/list")
			}
		}
	}

	void testColumnNamesAreLazyLoaded() {
		selenium.getTitle().returns("Thing List")
		selenium.getXpathCount("//table/thead/tr[1]/th").returns(3)
		(1..3).each {i ->
			selenium.getText("//table/thead/tr/th[$i]").returns("Column $i")
		}
		play {
			def page = new GrailsListPage()
			assertEquals(["Column 1", "Column 2", "Column 3"], page.columnNames)
		}
	}

	void testGetRowsScrapesTable() {
		selenium.getTitle().returns("Thing List")
		selenium.getXpathCount("//table/thead/tr[1]/th").returns(3)
		selenium.getXpathCount("//table/tbody/tr").returns(3)
		(1..3).each {i ->
			selenium.getText("//table/thead/tr/th[$i]").returns("Column $i")
		}
		(1..3).each {y ->
			(1..3).each {x ->
				selenium.getText("//table/tbody/tr[$y]/td[$x]").returns("$x.$y".toString())
			}
		}

		play {
			def page = new GrailsListPage()
			def rows = page.rows
			assertEquals(["Column 1": "1.1", "Column 2": "2.1", "Column 3": "3.1"], rows[0])
			assertEquals(["Column 1": "1.2", "Column 2": "2.2", "Column 3": "3.2"], rows[1])
			assertEquals(["Column 1": "1.3", "Column 2": "2.3", "Column 3": "3.3"], rows[2])
		}
	}

}