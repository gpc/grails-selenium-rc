package grails.plugins.selenium.test

import grails.plugins.selenium.pageobjects.GrailsListPage
import grails.plugins.selenium.test.Song
import spock.lang.Specification

class ListSongSpecification extends Specification {


	void cleanupSpec() {
		Song.list()*.delete()
	}

	def "all songs are displayed in list"() {
		given: "some songs exist"
		Song.withTransaction {
			Song.build(title: "Myriad Harbor", artist: "The New Pornographers", album: "Challengers")
			Song.build(title: "Patty Lee", artist: "Les Savy Fav", album: "Let's Stay Friends")
			Song.build(title: "Hospital Beds", artist: "Cold War Kids", album: "Robbers and Cowards")
		}
		
		when: "a user visits the song list page"
		def page = GrailsListPage.open("/song/list")
		
		then: "the songs are displayed in the list"
		page.rowCount == 3
		page.rows.Title == ["Myriad Harbor", "Patty Lee", "Hospital Beds"]
		page.rows.Artist == ["The New Pornographers", "Les Savy Fav", "Cold War Kids"]
		page.rows.Album == ["Challengers", "Let's Stay Friends", "Robbers and Cowards"]
	}
}
