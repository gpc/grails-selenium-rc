package grails.plugins.selenium.pageobjects

import grails.plugins.selenium.SeleniumTestContext
import grails.plugins.selenium.SeleniumTestContextHolder
import org.gmock.WithGMock
import org.junit.After
import org.junit.Before
import org.junit.Test
import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat
import com.thoughtworks.selenium.Selenium

@WithGMock
class GrailsListPageTests {

	Selenium mockSelenium

	@Before
	void setUp() {
		mockSelenium = mock(Selenium)
		SeleniumTestContextHolder.context = mock(SeleniumTestContext)
		SeleniumTestContextHolder.context.getSelenium().returns(mockSelenium).stub()
	}

	@After
	void tearDown() {
		SeleniumTestContextHolder.context = null
	}

	@Test(expected = InvalidPageStateException)
	void openFailsIfWrongPageLoads() {
		mockSelenium.getTitle().returns("WTF Page is this?")
		play {
			new GrailsListPage()
		}
	}

	@Test
	void columnNamesAreLazyLoaded() {
		mockSelenium.getTitle().returns("Thing List").stub()
		mockSelenium.getXpathCount("//table/thead/tr[1]/th").returns(3)
		(1..3).each {i ->
			mockSelenium.getText("//table/thead/tr/th[$i]").returns("Column $i" as String)
		}
		play {
			def page = new GrailsListPage()
			assertThat page.columnNames, equalTo(["Column 1", "Column 2", "Column 3"])
		}
	}

	@Test
	void getRowsScrapesTable() {
		mockSelenium.getTitle().returns("Thing List").stub()
		mockSelenium.getXpathCount("//table/thead/tr[1]/th").returns(3)
		mockSelenium.getXpathCount("//table/tbody/tr").returns(3)
		(1..3).each {i ->
			mockSelenium.getText("//table/thead/tr/th[$i]").returns("Column $i")
		}
		(1..3).each {y ->
			(1..3).each {x ->
				mockSelenium.getText("//table/tbody/tr[$y]/td[$x]").returns("$x.$y".toString())
			}
		}

		play {
			def page = new GrailsListPage()
			def rows = page.rows
			assertThat rows[0], equalTo(["Column 1": "1.1", "Column 2": "2.1", "Column 3": "3.1"])
			assertThat rows[1], equalTo(["Column 1": "1.2", "Column 2": "2.2", "Column 3": "3.2"])
			assertThat rows[2], equalTo(["Column 1": "1.3", "Column 2": "2.3", "Column 3": "3.3"])
		}
	}

}