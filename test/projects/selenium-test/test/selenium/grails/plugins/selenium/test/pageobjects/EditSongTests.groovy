package grails.plugins.selenium.test.pageobjects

import grails.plugins.selenium.test.Song
import grails.plugins.selenium.pageobjects.GrailsEditPage

class EditSongTests extends GroovyTestCase {

	def id

	void setUp() {
		super.setUp()
		Song.withTransaction {
			Song.withSession {session ->
				def song = Song.build(title: "Rockers To Swallow", artist: "Yeah Yeah Yeahs")
				id = song.id
				session.clear()
			}
		}
	}

	void tearDown() {
		super.tearDown()
		Song.withTransaction {
			Song.list()*.delete()
		}
	}

	void testCannotRemoveTitleOrArtist() {
		GrailsEditPage editPage = GrailsEditPage.open("/song/edit/$id")
		editPage.title = ""
		editPage.artist = ""

		editPage = editPage.saveExpectingFailure()

		assertTrue editPage.errorMessages.contains("Title cannot be blank")
		assertTrue editPage.errorMessages.contains("Artist cannot be blank")

		assertTrue "title field should be highlighted", editPage.hasFieldErrors("title")
		assertTrue "artist field should be highlighted", editPage.hasFieldErrors("artist")
	}

	void testAddAlbumAndDurationToSong() {
		GrailsEditPage editPage = GrailsEditPage.open("/song/edit/$id")
		editPage.album = "Is Is (EP)"
		editPage.durationSeconds = "193"

		def showPage = editPage.save()

		assertEquals "Song $id updated", showPage.flashMessage
		def song = Song.read(id)
		assertEquals "Is Is (EP)", song.album
		assertEquals 193, song.durationSeconds
	}

	void testDeleteSong() {
		GrailsEditPage editPage = GrailsEditPage.open("/song/edit/$id")

		def listPage = editPage.delete()

		assertEquals "Song $id deleted", listPage.flashMessage
		assertEquals "Song list is not empty", 0, listPage.rowCount
		assertEquals 0, Song.count()
	}

}