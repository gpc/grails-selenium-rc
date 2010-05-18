package grails.plugin.selenium.tests.pages

import grails.plugins.selenium.pageobjects.GrailsEditPage

class EditSongPage extends GrailsEditPage {

	static EditSongPage open(id) {
		return new EditSongPage("/song/edit/$id")
	}

	EditSongPage() {
		super()
	}

	private EditSongPage(String uri) {
		super(uri)
	}
}
