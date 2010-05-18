package grails.plugin.selenium.tests

import grails.plugin.selenium.tests.pages.CreateSongPage
import grails.plugin.selenium.tests.pages.ShowSongPage
import musicstore.Artist
import musicstore.Genre
import musicstore.Song
import static com.energizedwork.matcher.IsMatch.isMatch
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*
import grails.plugin.selenium.tests.pages.EditSongPage

class FormSubmissionTests extends GroovyTestCase {

	void tearDown() {
		super.tearDown()
		Artist.withTransaction {
			Artist.list()*.delete()
		}
	}

	void testCanReadErrorMessages() {
		def createPage = CreateSongPage.open()

		createPage = createPage.saveExpectingFailure()

		def errors = createPage.errorMessages
		assertThat "error message", errors, hasItem("Property [title] of class [class musicstore.Song] cannot be blank")
		assertThat "error message", errors, hasItem("Property [artist] of class [class musicstore.Song] cannot be null")
	}

	void testCanSubmitForm() {
		def createPage = CreateSongPage.open()

		createPage.title = "Desert Song"
		createPage.artist = "Edward Sharpe & the Magnetic Zeros"
		createPage.album = "Up From Below"
		createPage.durationSeconds = "270"
		createPage.genre = "Freak Folk"

		def showPage = createPage.save()

		assertThat "flash message", showPage.flashMessage, isMatch(/Song \d+ created/)
		assertThat "title", showPage.Title, equalTo("Desert Song")
		assertThat "artist", showPage.Artist, equalTo("Edward Sharpe & the Magnetic Zeros")
		assertThat "album", showPage.Album, equalTo("Up From Below")
		assertThat "duration", showPage."Duration Seconds", equalTo("270")
		assertThat "genre", showPage.Genre, equalTo("Freak Folk")
	}

	void testCanVerifyCreatedDataAgainstDatabase() {
		def createPage = CreateSongPage.open()

		createPage.title = "Desert Song"
		createPage.artist = "Edward Sharpe & the Magnetic Zeros"
		createPage.album = "Up From Below"
		createPage.durationSeconds = "270"
		createPage.genre = "Freak Folk"

		def showPage = createPage.save()

		def id = showPage.flashMessage.find(/Song (\d+) created/) { match, id -> id.toLong() }
		def song = Song.read(id)
		assertThat "title", song.title, equalTo("Desert Song")
		assertThat "artist", song.artist, hasProperty("name", equalTo("Edward Sharpe & the Magnetic Zeros"))
		assertThat "album", song.album, equalTo("Up From Below")
		assertThat "duration", song.durationSeconds, equalTo(270)
		assertThat "genre", song.genre, equalTo(Genre.FREAK_FOLK)
	}

	void testCanUseDeleteButtonWithConfirmation() {
		def id
		Song.withNewSession {
			id = Song.build(title: "Desert Song", artist: new Artist(name: "Edward Sharpe & the Magnetic Zeros"), album: "Up From Below").id
		}

		def showPage = ShowSongPage.open(id)
		def listPage = showPage.delete()

		assertThat "flash message", listPage.flashMessage, equalTo("Song $id deleted" as String)
		assertThat "songs in database", Song.count(), equalTo(0)
	}

	void testCanUpdateData() {
		def id
		Song.withNewSession {
			id = Song.build(title: "Desert Song", artist: new Artist(name: "Edward Sharpe & the Magnetic Zeros")).id
		}

		def editPage = EditSongPage.open(id)

		assertThat "title form field", editPage.title, equalTo("Desert Song")
		assertThat "artist form field", editPage.artist, equalTo("Edward Sharpe & the Magnetic Zeros")

		editPage.album = "Up From Below"
		editPage.durationSeconds = "270"
		editPage.genre = "Freak Folk"

		def showPage = editPage.save()

		assertThat "flash message", showPage.flashMessage, equalTo("Song $id updated" as String)
		def song = Song.read(id)
		assertThat "album", song.album, equalTo("Up From Below")
		assertThat "duration", song.durationSeconds, equalTo(270)
		assertThat "genre", song.genre, equalTo(Genre.FREAK_FOLK)
	}

}
