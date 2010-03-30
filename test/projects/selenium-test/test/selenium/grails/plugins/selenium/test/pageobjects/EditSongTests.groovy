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

class EditSongTests {

	def id

	@Before void setUp() {
		Song.withTransaction {
			Song.withSession {session ->
				def song = Song.build(title: "Rockers To Swallow", artist: "Yeah Yeah Yeahs")
				id = song.id
				session.clear()
			}
		}
	}

	@After void tearDown() {
		Song.withTransaction {
			Song.list()*.delete()
		}
	}

	@Test void cannotRemoveTitleOrArtist() {
		GrailsEditPage editPage = GrailsEditPage.open("/song/edit/$id")
		editPage.title = ""
		editPage.artist = ""

		editPage = editPage.saveExpectingFailure()

		assertThat editPage.errorMessages, hasItem("Title cannot be blank")
		assertThat editPage.errorMessages, hasItem("Artist cannot be blank")

		assertTrue "title field should be highlighted", editPage.hasFieldErrors("title")
		assertTrue "artist field should be highlighted", editPage.hasFieldErrors("artist")
	}

	@Test void addAlbumAndDurationToSong() {
		GrailsEditPage editPage = GrailsEditPage.open("/song/edit/$id")
		editPage.album = "Is Is (EP)"
		editPage.durationSeconds = "193"

		def showPage = editPage.save()

		assertThat showPage.flashMessage, equalTo("Song $id updated")
		def song = Song.read(id)
		assertThat song.album, equalTo("Is Is (EP)")
		assertThat song.durationSeconds, equalTo(193)
	}

	@Test void deleteSong() {
		GrailsEditPage editPage = GrailsEditPage.open("/song/edit/$id")

		def listPage = editPage.delete()

		assertThat listPage.flashMessage, equalTo("Song $id deleted")
		assertThat "Song list is not empty", listPage.rowCount, equalTo(0)
		assertThat "Song has not been deleted from the database", Song.count(), equalTo(0)
	}

	static Matcher<String> equalTo(GString expected) {
		equalTo(expected as String)
	}
}