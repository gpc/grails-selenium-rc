package grails.plugin.selenium.tests.pages

import grails.plugins.selenium.pageobjects.GrailsCreatePage
import static grails.plugins.selenium.condition.ClosureEvaluatingWait.waitFor

class CreateSongPage extends GrailsCreatePage {

	static CreateSongPage open() {
		return new CreateSongPage("/song/create")
	}

	CreateSongPage() {
		super()
	}

	private CreateSongPage(String uri) {
		super(uri)
	}

	void autocompleteArtist(String text) {
		selenium.focus "artist"
		selenium.typeKeys "artist", text
		selenium.waitForVisible "artist_autocomplete_choices"
	}

	List<String> getSuggestions() {
		def autocompleteCount = selenium.getXpathCount("//div[@id='artist_autocomplete_choices']/ul/li")
		def options = (1..autocompleteCount).collect { index ->
			selenium.getText("//div[@id='artist_autocomplete_choices']/ul/li[$index]")
		}
		return options
	}

	void selectSuggestion(String text) {
		int index = suggestions.indexOf(text) + 1
		selenium.click "//div[@id='artist_autocomplete_choices']/ul/li[$index]"
		waitFor("selection made") {
			!selenium.isVisible("artist_autocomplete_choices")
		}
	}

	List<String> getGenreOptions() {
		return selenium.getSelectOptions("genre") as List
	}

	protected void verifyPage() {
		pageTitleIs "Create Song"
	}

}
