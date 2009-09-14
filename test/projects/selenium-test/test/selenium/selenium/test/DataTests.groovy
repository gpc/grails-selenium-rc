package selenium.test

import com.energizedwork.grails.plugins.seleniumrc.GrailsSeleneseTestCase

class DataTests extends GrailsSeleneseTestCase {

	void setUp() {
		super.setUp()

		Song.withTransaction {
			Song.build(title: "Heads Will Roll", artist: "Yeah Yeah Yeahs", album: "It's Blitz!", durationSeconds: 221)
			Song.build(title: "Twilight Galaxy", artist: "Metric", album: "Fantasies", durationSeconds: 293)
			Song.build(title: "I'm Confused", artist: "Handsome Furs", album: "Face Control", durationSeconds: 215)
		}

		selenium.open "/selenium-test/song/list"
	}

	void tearDown() {
		super.tearDown()
		Song.withTransaction {
			Song.list()*.delete()
		}
	}

	void testCorrectColumnsAndRowsAppear() {
		assertEquals "Title", selenium.getText("//table/thead/tr/th[2]")
		assertEquals "Artist", selenium.getText("//table/thead/tr/th[3]")
		assertEquals "Album", selenium.getText("//table/thead/tr/th[4]")
		assertEquals "Duration Seconds", selenium.getText("//table/thead/tr/th[5]")
		assertEquals 3, selenium.getXpathCount("//table/tbody/tr")
	}

	void testColumnsAreSortable() {
		selenium.click("link=Title")
		selenium.waitForPageToLoad("5000")
		assertEquals "Heads Will Roll", selenium.getText("//table/tbody/tr[1]/td[2]")
		assertEquals "I'm Confused", selenium.getText("//table/tbody/tr[2]/td[2]")
		assertEquals "Twilight Galaxy", selenium.getText("//table/tbody/tr[3]/td[2]")

		selenium.click("link=Title")
		selenium.waitForPageToLoad("5000")
		assertEquals "Twilight Galaxy", selenium.getText("//table/tbody/tr[1]/td[2]")
		assertEquals "I'm Confused", selenium.getText("//table/tbody/tr[2]/td[2]")
		assertEquals "Heads Will Roll", selenium.getText("//table/tbody/tr[3]/td[2]")

		selenium.click("link=Artist")
		selenium.waitForPageToLoad("5000")
		assertEquals "Handsome Furs", selenium.getText("//table/tbody/tr[1]/td[3]")
		assertEquals "Metric", selenium.getText("//table/tbody/tr[2]/td[3]")
		assertEquals "Yeah Yeah Yeahs", selenium.getText("//table/tbody/tr[3]/td[3]")
	}

}