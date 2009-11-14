package grails.plugins.selenium.test

import grails.plugins.selenium.SeleniumTest
import grails.plugins.selenium.GrailsSelenium

@Mixin (SeleniumTest)
class PageObjectTests extends GroovyTestCase {

	CreateSongPage page

	void setUp() {
		super.setUp()
		page = new CreateSongPage(selenium)
	}

	void testUserMustEnterTitleAndArtist() {
		page.open()

		page.submit()

		assertTrue page.errorMessages.contains("Property [title] of class [class grails.plugins.selenium.test.Song] cannot be blank")
		assertTrue page.errorMessages.contains("Property [artist] of class [class grails.plugins.selenium.test.Song] cannot be blank")

		assertTrue "title field should be highlighted", page.isHighlighted("title")
		assertTrue "artist field should be highlighted", page.isHighlighted("artist")
	}

	void testUserCanCreateSongWithAlbum() {
		page.open()

		page.title = "Queen Bitch"
		page.artist = "David Bowie"
		page.album = "Hunky Dory"

		page.submit()

		def id = page.flashMessage.find(/Song (\d+) created/) {match, id -> id }
		def song = Song.get(id)
		assertNotNull "Song $id not found", song
		assertEquals "Queen Bitch", song.title
		assertEquals "David Bowie", song.artist
		assertEquals "Hunky Dory", song.album
	}

	void testUserCanCreateSongWithoutAlbum() {
		page.open()

		page.title = "A Song From Under The Floorboards"
		page.artist = "Magazine"

		page.submit()

		def id = page.flashMessage.find(/Song (\d+) created/) {match, id -> id }
		def song = Song.get(id)
		assertNotNull "Song $id not found", song
		assertEquals "A Song From Under The Floorboards", song.title
		assertEquals "Magazine", song.artist
		assertNull song.album
	}

}

class CreateSongPage {

	private GrailsSelenium selenium

	CreateSongPage(GrailsSelenium selenium) {
		this.selenium = selenium
	}

	void open() {
		selenium.open "/song/create"
	}

	void submit() {
		selenium.clickAndWait "create"
	}

	List<String> getErrorMessages() {
		def messages = []
		def i = 1
		while (selenium.isElementPresent("css=.errors ul li:nth-child($i)")) {
			messages << selenium.getText("css=.errors ul li:nth-child($i)")
			i++
		}
		return messages
	}

	String getFlashMessage() {
		selenium.getText "css=.message"
	}

	boolean isHighlighted(String field) {
		selenium.isElementPresent "css=.errors input[name=$field]"
	}

	def propertyMissing(String name) {
		selenium.getValue(name)
	}

	def propertyMissing(String name, value) {
		selenium.type(name, value)
	}

}