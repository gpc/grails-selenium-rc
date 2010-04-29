package grails.plugins.selenium.test.pageobjects

import grails.plugins.selenium.pageobjects.GrailsCreatePage

class CreateSongPage extends GrailsCreatePage {

	static CreateSongPage open() {
		return new CreateSongPage("/song/create")
	}

	CreateSongPage() {
		super()
	}

	protected CreateSongPage(String uri) {
		super(uri)
	}

	ShowSongPage submit() {
		selenium.clickAndWait "create"
		return new ShowSongPage()
	}

	CreateSongPage submitExpectingFailure() {
		selenium.clickAndWait "create"
		return this
	}

	protected void verifyPage() {
		pageTitleIs "Create Song"
	}

}
