package grails.plugins.selenium.test

import grails.plugins.selenium.SeleniumTest
import spock.lang.Specification

@Mixin(SeleniumTest)
class ListSongSpecification extends Specification {

	void setupSpeck() {
		Song.build(title: "Myriad Harbor", artist: "The New Pornographers", album: "Challengers")
		Song.build(title: "Patty Lee", artist: "Les Savy Fav", album: "Let's Stay Friends")
		Song.build(title: "Hospital Beds", artist: "Cold War Kids", album: "Robbers and Cowards")
	}

	void cleanupSpeck() {
		Song.list()*.delete()
	}

}