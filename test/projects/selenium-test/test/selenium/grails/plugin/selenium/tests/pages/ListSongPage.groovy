package grails.plugin.selenium.tests.pages

import grails.plugins.selenium.pageobjects.GrailsListPage

class ListSongPage extends GrailsListPage {

	static ListSongPage open() {
		return new ListSongPage("/song/list")
	}

	ListSongPage() {
		super()
	}

	private ListSongPage(String uri) {
		super(uri)
	}
}
