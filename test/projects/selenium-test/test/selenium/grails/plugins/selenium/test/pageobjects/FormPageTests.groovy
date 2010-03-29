package grails.plugins.selenium.test.pageobjects

import grails.plugins.selenium.pageobjects.GrailsCreatePage
import grails.plugins.selenium.test.Song
import grails.plugins.selenium.test.Playlist
import grails.plugins.selenium.test.Genre
import grails.plugins.selenium.pageobjects.GrailsEditPage

class FormPageTests extends GroovyTestCase {

	void setUp() {
		super.setUp()

		Song.withTransaction {
			Song.build(title: "Heads Will Roll", artist: "Yeah Yeah Yeahs", album: "It's Blitz!", durationSeconds: 221)
			Song.build(title: "Twilight Galaxy", artist: "Metric", album: "Fantasies", durationSeconds: 293)
			Song.build(title: "I'm Confused", artist: "Handsome Furs", album: "Face Control", durationSeconds: 215)
		}
	}

	void tearDown() {
		super.tearDown()
		Song.withTransaction {
			Playlist.list()*.delete()
			Song.list()*.delete()
		}
	}

	void testPropertySetOnVariousInputTypes() {
		def page = GrailsCreatePage.open("/playlist/create")
		page.name = "My Playlist"
		page.active = true
		page.songs = ["Heads Will Roll by Yeah Yeah Yeahs", "I'm Confused by Handsome Furs"]
		page.genre = Genre.ROCK.name()
		page.save()

		def playlist = Playlist.findByName("My Playlist")
		assertTrue playlist.active
		assertEquals Genre.ROCK, playlist.genre
		assertEquals(["Heads Will Roll", "I'm Confused"], playlist.songs.title)
	}

	void testPropertyGetOnVariousInputTypes() {
		def playlist = Playlist.build(name: "My Playlist", active: true, genre: Genre.ROCK, songs: Song.findAllByAlbumLike("F%"))

		def page = GrailsEditPage.open("/playlist/edit/$playlist.id")
		assertEquals playlist.name, page.name
		assertTrue page.active
		assertEquals playlist.songs*.toString(), page.songs
		assertEquals playlist.genre.name(), page.genre
		page.save()
	}

}
