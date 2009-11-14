package grails.plugins.selenium.test

import grails.plugins.selenium.*

class DataTests extends GrailsSeleniumTestCase {

	void setUp() {
		super.setUp()

		Song.withTransaction {
			Song.build(title: "Heads Will Roll", artist: "Yeah Yeah Yeahs", album: "It's Blitz!", durationSeconds: 221)
			Song.build(title: "Twilight Galaxy", artist: "Metric", album: "Fantasies", durationSeconds: 293)
			Song.build(title: "I'm Confused", artist: "Handsome Furs", album: "Face Control", durationSeconds: 215)
		}

		selenium.open "/song/list"
	}

	void tearDown() {
		super.tearDown()
		Song.withTransaction {
			Song.list()*.delete()
		}
	}

	void testCorrectColumnsAndRowsAppear() {
		// This demonstrates using the selenium instance and regular assertions
		selenium.with {
			assertEquals "Title", getText("//table/thead/tr/th[2]")
			assertEquals "Artist", getText("//table/thead/tr/th[3]")
			assertEquals "Album", getText("//table/thead/tr/th[4]")
			assertEquals "Duration Seconds", getText("//table/thead/tr/th[5]")
			assertEquals 3, getXpathCount("//table/tbody/tr")
		}
	}

	void testColumnsAreSortable() {
		// this demonstrates using dynamic assertion methods from GrailsSeleneseTestCase
		selenium.clickAndWait("link=Title")
		assertText "Heads Will Roll", "//table/tbody/tr[1]/td[2]"
		assertText "I'm Confused", "//table/tbody/tr[2]/td[2]"
		assertText "Twilight Galaxy", "//table/tbody/tr[3]/td[2]"

		selenium.clickAndWait("link=Title")
		assertText "Twilight Galaxy", "//table/tbody/tr[1]/td[2]"
		assertText "I'm Confused", "//table/tbody/tr[2]/td[2]"
		assertText "Heads Will Roll", "//table/tbody/tr[3]/td[2]"

		selenium.clickAndWait("link=Artist")
		assertText "Handsome Furs", "//table/tbody/tr[1]/td[3]"
		assertText "Metric", "//table/tbody/tr[2]/td[3]"
		assertText "Yeah Yeah Yeahs", "//table/tbody/tr[3]/td[3]"
	}

}