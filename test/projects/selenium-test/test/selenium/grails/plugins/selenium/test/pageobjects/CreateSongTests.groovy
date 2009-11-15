package grails.plugins.selenium.test.pageobjects

import grails.plugins.selenium.SeleniumTest
import grails.plugins.selenium.pageobjects.GrailsFormPage
import grails.plugins.selenium.pageobjects.GrailsPage
import grails.plugins.selenium.test.Song

@Mixin (SeleniumTest)
class CreateSongTests extends GroovyTestCase {

	void tearDown() {
		super.tearDown()
		Song.withTransaction {
			Song.list()*.delete()
		}
	}

	void testUserMustEnterTitleAndArtist() {
		def page = CreateSongPage.open()

		page.submitExpectingFailure()

		assertTrue page.errorMessages.contains("Property [title] of class [class grails.plugins.selenium.test.Song] cannot be blank")
		assertTrue page.errorMessages.contains("Property [artist] of class [class grails.plugins.selenium.test.Song] cannot be blank")

		assertTrue "title field should be highlighted", page.hasFieldErrors("title")
		assertTrue "artist field should be highlighted", page.hasFieldErrors("artist")
	}

	void testUserCanCreateSongWithAlbum() {
		def page = CreateSongPage.open()

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
		def page = CreateSongPage.open()

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

	void testSongAppearsInListOnceCreated() {
		def createPage = CreateSongPage.open()

		createPage.title = "Myriad Harbor"
		createPage.artist = "The New Pornographers"
		createPage.album = "Challengers"

		def showPage = createPage.submit()

		assertEquals "Myriad Harbor", showPage.Title
		assertEquals "The New Pornographers", showPage.Artist
		assertEquals "Challengers", showPage.Album
	}

}
