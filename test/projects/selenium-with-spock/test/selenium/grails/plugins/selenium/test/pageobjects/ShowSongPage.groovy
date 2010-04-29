package grails.plugins.selenium.test.pageobjects

import grails.plugins.selenium.pageobjects.GrailsShowPage

class ShowSongPage extends GrailsShowPage {

	static ShowSongPage open(id) {
		return new ShowSongPage("/song/show/$id")
	}

	ShowSongPage() {
		super()
	}

	protected ShowSongPage(String uri) {
		super(uri)
	}

}