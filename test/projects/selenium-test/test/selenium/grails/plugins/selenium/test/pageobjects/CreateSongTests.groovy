package grails.plugins.selenium.test.pageobjects

import grails.plugins.selenium.SeleniumAware
import grails.plugins.selenium.pageobjects.GrailsCreatePage
import grails.plugins.selenium.test.Song
import org.junit.AfterClass
import org.junit.Test
import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue
import static org.junit.matchers.JUnitMatchers.hasItem

@Mixin(SeleniumAware)
class CreateSongTests {

	@AfterClass static void cleanUpData() {
		Song.withTransaction {
			Song.list()*.delete()
		}
	}

	@Test
	void userMustEnterTitleAndArtist() {
		def page = GrailsCreatePage.open("/song/create")

		page.saveExpectingFailure()

		assertThat page.errorMessages, hasItem("Title cannot be blank")
		assertThat page.errorMessages, hasItem("Artist cannot be blank")

		assertTrue "title field should be highlighted", page.hasFieldErrors("title")
		assertTrue "artist field should be highlighted", page.hasFieldErrors("artist")
	}

	@Test
	void userCanCreateSongWithAlbum() {
		def createPage = GrailsCreatePage.open("/song/create")

		createPage.title = "Queen Bitch"
		createPage.artist = "David Bowie"
		createPage.album = "Hunky Dory"

		def showPage = createPage.save()

		def id = showPage.flashMessage.find(/Song (\d+) created/) {match, id -> id }
		def song = Song.get(id)
		assertThat "Song $id not found", song, notNullValue()
		assertThat song.title, equalTo("Queen Bitch")
		assertThat song.artist, equalTo("David Bowie")
		assertThat song.album, equalTo("Hunky Dory")
	}

	@Test
	void userCanCreateSongWithoutAlbum() {
		def createPage = GrailsCreatePage.open("/song/create")

		createPage.title = "A Song From Under The Floorboards"
		createPage.artist = "Magazine"

		def showPage = createPage.save()

		def id = showPage.flashMessage.find(/Song (\d+) created/) {match, id -> id }
		def song = Song.get(id)
		assertThat "Song $id not found", song, notNullValue()
		assertThat song.title, equalTo("A Song From Under The Floorboards")
		assertThat song.artist, equalTo("Magazine")
		assertThat song.album, nullValue()
	}

	@Test
	void songDetailsShownOnSuccessfulCreate() {
		def createPage = GrailsCreatePage.open("/song/create")

		createPage.title = "Myriad Harbor"
		createPage.artist = "The New Pornographers"
		createPage.album = "Challengers"

		def showPage = createPage.save()

		assertThat showPage.Title, equalTo("Myriad Harbor")
		assertThat showPage.Artist, equalTo("The New Pornographers")
		assertThat showPage.Album, equalTo("Challengers")
	}

}
