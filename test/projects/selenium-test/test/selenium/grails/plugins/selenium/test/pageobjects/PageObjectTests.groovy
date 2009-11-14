package grails.plugins.selenium.test.pageobjects

import grails.plugins.selenium.SeleniumTest
import grails.plugins.selenium.test.Song
import grails.plugins.selenium.test.pageobjects.Page

@Mixin (SeleniumTest)
class PageObjectTests extends GroovyTestCase {

	void testUserMustEnterTitleAndArtist() {
		def page = CreateSongPage.open()

		page.submitExpectingFailure()

		assertTrue page.errorMessages.contains("Property [title] of class [class grails.plugins.selenium.test.Song] cannot be blank")
		assertTrue page.errorMessages.contains("Property [artist] of class [class grails.plugins.selenium.test.Song] cannot be blank")

		assertTrue "title field should be highlighted", page.hasFieldErrors("title")
		assertTrue "artist field should be highlighted", page.hasFieldErrors("artist")
	}

	void testUserCanCreateSongWithAlbum() {
		def page = CreateSongPage.open()

		page.title = "Queen Bitch"
		page.artist = "David Bowie"
		page.album = "Hunky Dory"

		page.submit()

		def id = page.flashMessage.find(/Song (\d+) created/) {match, id -> id }
		def song = Song.get(id)
		assertNotNull "Song $id not found", song
		assertEquals "Queen Bitch", song.title
		assertEquals "David Bowie", song.artist
		assertEquals "Hunky Dory", song.album
	}

	void testUserCanCreateSongWithoutAlbum() {
		def page = CreateSongPage.open()

		page.title = "A Song From Under The Floorboards"
		page.artist = "Magazine"

		page.submit()

		def id = page.flashMessage.find(/Song (\d+) created/) {match, id -> id }
		def song = Song.get(id)
		assertNotNull "Song $id not found", song
		assertEquals "A Song From Under The Floorboards", song.title
		assertEquals "Magazine", song.artist
		assertNull song.album
	}

}

class CreateSongPage extends Page {

	static CreateSongPage open() {
		def page = new CreateSongPage()
		page.selenium.open "/song/create"
		return page
	}

	ListSongPage submit() {
		selenium.clickAndWait "create"
	}

	CreateSongPage submitExpectingFailure() {
		selenium.clickAndWait "create"
	}

}

class ListSongPage extends Page {

	static ListSongPage open() {
		def page = new ListSongPage()
		page.selenium.open "/song/list"
		return page
	}

}