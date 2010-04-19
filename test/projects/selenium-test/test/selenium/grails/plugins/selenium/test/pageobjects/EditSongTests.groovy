package grails.plugins.selenium.test.pageobjects

import grails.plugins.selenium.test.Song
import grails.plugins.selenium.pageobjects.GrailsEditPage
import org.junit.Before
import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*
import static org.hamcrest.CoreMatchers.*
import static org.junit.matchers.JUnitMatchers.*
import org.junit.After
import org.junit.AfterClass
import org.hamcrest.Matcher
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import grails.plugins.selenium.pageobjects.GrailsPage

class EditSongTests {

	def id

	@Before
	void setUp() {
		Song.withTransaction {
			Song.withSession {session ->
				def song = Song.build(title: "Rockers To Swallow", artist: "Yeah Yeah Yeahs")
				id = song.id
				session.clear()
			}
		}
	}

	@After
	void tearDown() {
		Song.withTransaction {
			Song.list()*.delete()
		}
	}

	@Test
	void cannotRemoveTitleOrArtist() {
		GrailsEditPage editPage = GrailsEditPage.open("/song/edit/$id")
		editPage.title = ""
		editPage.artist = ""

		editPage = editPage.saveExpectingFailure()

		assertThat "Error messages", editPage.errorMessages, hasItems("Title cannot be blank", "Artist cannot be blank")

		assertTrue "Title field should be highlighted", editPage.hasFieldErrors("title")
		assertTrue "Artist field should be highlighted", editPage.hasFieldErrors("artist")
	}

	@Test
	void addAlbumAndDurationToSong() {
		GrailsEditPage editPage = GrailsEditPage.open("/song/edit/$id")
		editPage.album = "Is Is (EP)"
		editPage.durationSeconds = "193"

		def showPage = editPage.save()

		assertThat "Flash message", showPage.flashMessage, equalTo("Song $id updated" as String)
		def song = Song.read(id)
		assertThat "Song album name", song.album, equalTo("Is Is (EP)")
		assertThat "Song duration", song.durationSeconds, equalTo(193)
	}

	@Test
	void deleteSong() {
		GrailsEditPage editPage = GrailsEditPage.open("/song/edit/$id")

		def listPage = editPage.delete()

		assertThat "Flash message", listPage.flashMessage, equalTo("Song $id deleted" as String)
		assertThat "Song list size", listPage.rowCount, equalTo(0)
		assertThat "Song entity count", Song.count(), equalTo(0)
	}
}