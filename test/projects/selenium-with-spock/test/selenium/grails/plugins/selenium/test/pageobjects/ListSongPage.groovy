package grails.plugins.selenium.test.pageobjects

import grails.plugins.selenium.pageobjects.GrailsListPage

class ListSongPage extends GrailsListPage {

	ListSongPage() {
		super()
	}

	private ListSongPage(String uri) {
		super(uri)
	}

	static ListSongPage open() {
		return new ListSongPage("/song/list")
	}

	protected void verifyPage() {
		pageTitleIs "Song List"
	}

}