package grails.plugins.selenium.test

import grails.plugins.selenium.pageobjects.GrailsCreatePage
import grails.plugins.selenium.test.Song
import spock.lang.Specification

class CreateSongSpecification extends Specification {

	def cleanupSpec() {
		Song.withTransaction {
			Song.list()*.delete()
		}
	}

	def "title and artist are required"() {
		given: "a user is on the create song page"
		def page = GrailsCreatePage.open("/song/create")

		when: "the user clicks create without filling in mandatory data"
		page.saveExpectingFailure()

		then: "error messages are displayed"
		page.errorMessages.contains("Title cannot be blank")
		page.errorMessages.contains("Artist cannot be blank")

		and: "fields in error are highlighted"
		page.hasFieldErrors("title")
		page.hasFieldErrors("artist")
	}

	def "album is optional"() {
		given: "a user is on the create song page"
		def createPage = GrailsCreatePage.open("/song/create")

		when: "the user submits the form with valid data"
		createPage.title = title
		createPage.artist = artist
		createPage.album = album
		def showPage = createPage.save()

		then: "a song is saved"
		def id = showPage.flashMessage.find(/Song (\d+) created/) { match, id -> id }
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