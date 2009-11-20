package grails.plugins.selenium.test.pageobjects

import grails.plugins.selenium.pageobjects.GrailsListPage

class ListSongPage extends GrailsListPage {

	static ListSongPage open() {
		def page = new ListSongPage()
		page.selenium.open "/song/list"
		return page
	}

}