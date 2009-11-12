package grails.plugins.selenium.test

import grails.plugins.selenium.SeleniumTest
import spock.lang.Specification

@Mixin(SeleniumTest)
class CreateSongSpecification extends Specification {

	def cleanupSpeck() {
		Song.list()*.delete()
	}

	def "title and artist are required"() {
		given: "a user is on the create song page"
		selenium.open "/song/create"

		when: "the user clicks create without filling in mandatory data"
		selenium.clickAndWait "create"

		then: "error messages are displayed"
		selenium.isTextPresent "Property [title] of class [class grails.plugins.selenium.test.Song] cannot be blank"
		selenium.isTextPresent "Property [artist] of class [class grails.plugins.selenium.test.Song] cannot be blank"

		and: "fields in error are highlighted"
		selenium.isElementPresent "css=.errors input[name=title]"
		selenium.isElementPresent "css=.errors input[name=artist]"
	}

	def "album is optional"() {
		given: "a user is on the create song page"
		selenium.open "/song/create"

		when: "the user submits the form with valid data"
		selenium.type "title", title
		selenium.type "artist", artist
		selenium.type "album", album
		selenium.clickAndWait "create"

		then: "a song is saved"
		def id = selenium.getText("css=.message").find(/Song (\d+) created/) { match, id -> id }
		id != null
		def song = Song.read(id)
		song.title == title
		song.artist == artist
		song.album == (album ?: null)

		where:
		title << ["40 Day Dream", "Judy is a Punk"]
		artist << ["Edward Sharpe and the Magnetic Zeros", "Yeah Yeah Yeahs"]
		album << ["Up From Below", ""]
	}
	
}