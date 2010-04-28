package grails.plugins.selenium.test.pageobjects

import grails.plugins.selenium.pageobjects.GrailsShowPage

class ShowSongPage extends GrailsShowPage {

	static ShowSongPage open(id) {
		def page = new ShowSongPage()
		page.selenium.open "/song/show/$id"
		return page
	}

}