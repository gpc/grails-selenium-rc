package grails.plugin.selenium.tests.pages

import grails.plugins.selenium.pageobjects.GrailsShowPage

class ShowSongPage extends GrailsShowPage {

	static ShowSongPage open(id) {
		return new ShowSongPage("/song/show/$id")
	}

	def ShowSongPage() {
		super()
	}

	private ShowSongPage(String uri) {
		super(uri)
	}
}
