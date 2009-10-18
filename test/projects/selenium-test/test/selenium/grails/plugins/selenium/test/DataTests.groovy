package grails.plugins.selenium.test

import grails.plugins.selenium.GrailsSeleneseTestCase

class DataTests extends GrailsSeleneseTestCase {

	void setUp() {
		super.setUp()

		Song.withTransaction {
			Song.build(title: "Heads Will Roll", artist: "Yeah Yeah Yeahs", album: "It's Blitz!", durationSeconds: 221)
			Song.build(title: "Twilight Galaxy", artist: "Metric", album: "Fantasies", durationSeconds: 293)
			Song.build(title: "I'm Confused", artist: "Handsome Furs", album: "Face Control", durationSeconds: 215)
		}

		selenium.open "$rootURL/song/list"
	}

	void tearDown() {
		super.tearDown()
		Song.withTransaction {
			Song.list()*.delete()
		}
	}

	void testCorrectColumnsAndRowsAppear() {
		selenium.with {
			assertEquals "Title", getText("//table/thead/tr/th[2]")
			assertEquals "Artist", getText("//table/thead/tr/th[3]")
			assertEquals "Album", getText("//table/thead/tr/th[4]")
			assertEquals "Duration Seconds", getText("//table/thead/tr/th[5]")
			assertEquals 3, getXpathCount("//table/tbody/tr")
		}
	}

	void testColumnsAreSortable() {
		selenium.clickAndWait("link=Title")
		assertText "//table/tbody/tr[1]/td[2]", "Heads Will Roll"
		assertText "//table/tbody/tr[2]/td[2]", "I'm Confused"
		assertText "//table/tbody/tr[3]/td[2]", "Twilight Galaxy"

		selenium.clickAndWait("link=Title")
		assertText "//table/tbody/tr[1]/td[2]", "Twilight Galaxy"
		assertText "//table/tbody/tr[2]/td[2]", "I'm Confused"
		assertText "//table/tbody/tr[3]/td[2]", "Heads Will Roll"

		selenium.clickAndWait("link=Artist")
		assertText "//table/tbody/tr[1]/td[3]", "Handsome Furs"
		assertText "//table/tbody/tr[2]/td[3]", "Metric"
		assertText "//table/tbody/tr[3]/td[3]", "Yeah Yeah Yeahs"
	}

}