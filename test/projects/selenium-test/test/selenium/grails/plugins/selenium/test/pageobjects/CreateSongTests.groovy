package grails.plugins.selenium.test.pageobjects

import grails.plugins.selenium.SeleniumTest
import grails.plugins.selenium.pageobjects.GrailsFormPage
import grails.plugins.selenium.pageobjects.GrailsPage
import grails.plugins.selenium.test.Song
import grails.plugins.selenium.pageobjects.GrailsCreatePage

@Mixin (SeleniumTest)
class CreateSongTests extends GroovyTestCase {

	void tearDown() {
		super.tearDown()
		Song.withTransaction {
			Song.list()*.delete()
		}
	}

	void testUserMustEnterTitleAndArtist() {
		def page = GrailsCreatePage.open("/song/create")

		page.saveExpectingFailure()

		assertTrue page.errorMessages.contains("Title cannot be blank")
		assertTrue page.errorMessages.contains("Artist cannot be blank")

		assertTrue "title field should be highlighted", page.hasFieldErrors("title")
		assertTrue "artist field should be highlighted", page.hasFieldErrors("artist")
	}

	void testUserCanCreateSongWithAlbum() {
		def createPage = GrailsCreatePage.open("/song/create")

		createPage.title = "Queen Bitch"
		createPage.artist = "David Bowie"
		createPage.album = "Hunky Dory"

		def showPage = createPage.save()

		def id = showPage.flashMessage.find(/Song (\d+) created/) {match, id -> id }
		def song = Song.get(id)
		assertNotNull "Song $id not found", song
		assertEquals "Queen Bitch", song.title
		assertEquals "David Bowie", song.artist
		assertEquals "Hunky Dory", song.album
	}

	void testUserCanCreateSongWithoutAlbum() {
		def createPage = GrailsCreatePage.open("/song/create")

		createPage.title = "A Song From Under The Floorboards"
		createPage.artist = "Magazine"

		def showPage = createPage.save()

		def id = showPage.flashMessage.find(/Song (\d+) created/) {match, id -> id }
		def song = Song.get(id)
		assertNotNull "Song $id not found", song
		assertEquals "A Song From Under The Floorboards", song.title
		assertEquals "Magazine", song.artist
		assertNull song.album
	}

	void testSongDetailsShownOnSuccessfulCreate() {
		def createPage = GrailsCreatePage.open("/song/create")

		createPage.title = "Myriad Harbor"
		createPage.artist = "The New Pornographers"
		createPage.album = "Challengers"

		def showPage = createPage.save()

		assertEquals "Myriad Harbor", showPage.Title
		assertEquals "The New Pornographers", showPage.Artist
		assertEquals "Challengers", showPage.Album
	}

}
