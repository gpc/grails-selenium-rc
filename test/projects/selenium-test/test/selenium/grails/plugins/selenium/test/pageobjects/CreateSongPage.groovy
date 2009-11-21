package grails.plugins.selenium.test.pageobjects

import grails.plugins.selenium.pageobjects.GrailsFormPage

class CreateSongPage extends GrailsFormPage {

	static CreateSongPage open() {
		def page = new CreateSongPage()
		page.selenium.open "/song/create"
		return page
	}

	ShowSongPage submit() {
		selenium.clickAndWait "css=.buttons input.save"
		return new ShowSongPage()
	}

	CreateSongPage submitExpectingFailure() {
		selenium.clickAndWait "css=.buttons input.save"
		return this
	}

}
