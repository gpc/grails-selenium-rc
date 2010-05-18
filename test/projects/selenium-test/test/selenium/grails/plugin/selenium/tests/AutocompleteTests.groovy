package grails.plugin.selenium.tests

import grails.plugin.selenium.tests.pages.CreateSongPage
import musicstore.Artist
import musicstore.Song
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*
import static grails.plugins.selenium.condition.ClosureEvaluatingWait.*

class AutocompleteTests extends GroovyTestCase {

	List<String> artistNames

	void setUp() {
		super.setUp()
		artistNames = ["Edward Sharpe & the Magnetic Zeros", "Emmy The Great", "Elvis Costello & the Attractions", "Editors"]
		artistNames.each {
			Artist.build(name: it)
		}
	}

	void tearDown() {
		super.tearDown()
		Artist.withTransaction {
			Artist.list()*.delete()
		}
	}

	void testCanWaitForSuggestions() {
		def createPage = CreateSongPage.open()
		
		createPage.autocompleteArtist("e")

		assertThat createPage.suggestions, hasItems(* artistNames)
	}

	void testCanSelectFromSuggestions() {
		def createPage = CreateSongPage.open()

		createPage.autocompleteArtist("e")
		createPage.selectSuggestion("Edward Sharpe & the Magnetic Zeros")

		assertThat createPage.artist, equalTo("Edward Sharpe & the Magnetic Zeros")
	}

	void testCanFilterDownSuggestions() {
		def createPage = CreateSongPage.open()

		createPage.autocompleteArtist("e")
		assertThat createPage.suggestions, hasItems(* artistNames)

		createPage.autocompleteArtist("d")
		waitFor("autocomplete suggestions") {
			not(hasItems("Elvis Costello & the Attractions", "Emmy The Great")).matches(createPage.suggestions)
		}
		assertThat createPage.suggestions, hasItems("Edward Sharpe & the Magnetic Zeros", "Editors")

		createPage.autocompleteArtist("w")
		waitFor("autocomplete suggestions") {
			not(hasItem("Editors")).matches(createPage.suggestions)
		}
		assertThat createPage.suggestions, hasItem("Edward Sharpe & the Magnetic Zeros")
	}

}
