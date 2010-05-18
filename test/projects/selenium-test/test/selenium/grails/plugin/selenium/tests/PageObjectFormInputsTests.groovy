package grails.plugin.selenium.tests

import grails.plugin.selenium.tests.pages.CreateSongPage
import musicstore.Genre
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

class PageObjectFormInputsTests extends GroovyTestCase {

	void testCanEnterDataInTextFields() {
		def createPage = CreateSongPage.open()

		createPage.title = "Desert Song"
		createPage.artist = "Edward Sharpe & the Magnetic Zeros"
		createPage.album = "Up From Below"
		createPage.durationSeconds = "270"

		assertThat "title field", createPage.title, equalTo("Desert Song")
		assertThat "artist field", createPage.artist, equalTo("Edward Sharpe & the Magnetic Zeros")
		assertThat "album field", createPage.album, equalTo("Up From Below")
		assertThat "duration field", createPage.durationSeconds, equalTo("270")
	}

	void testCanReadSelectOptions() {
		def createPage = CreateSongPage.open()

		assertThat "selected option", createPage.genre, equalTo("")
		assertThat "select options", createPage.genreOptions, hasItems(* Genre.values().defaultMessage)
		assertThat "select options", createPage.genreOptions, hasItem("")
	}

	void testCanChooseFromSelectOptions() {
		def createPage = CreateSongPage.open()

		assertThat "selected option", createPage.genre, equalTo("")

		createPage.genre = "Freak Folk"

		assertThat "selected option", createPage.genre, equalTo("Freak Folk")
	}

	void testCanUseCheckboxes() {
		def createPage = CreateSongPage.open()

		assertThat "checkbox", createPage.partOfCompilation, equalTo(false)

		createPage.partOfCompilation = true

		assertThat "checkbox", createPage.partOfCompilation, equalTo(true)
	}

}
