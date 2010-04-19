package grails.plugins.selenium.test.pageobjects

import grails.plugins.selenium.pageobjects.GrailsCreatePage
import grails.plugins.selenium.pageobjects.GrailsEditPage
import grails.plugins.selenium.test.Genre
import grails.plugins.selenium.test.Playlist
import grails.plugins.selenium.test.Song
import org.junit.After
import org.junit.Before
import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat

class FormPageTests {

	@Before
	void setUp() {
		Song.withTransaction {
			Song.build(title: "Heads Will Roll", artist: "Yeah Yeah Yeahs", album: "It's Blitz!", durationSeconds: 221)
			Song.build(title: "Twilight Galaxy", artist: "Metric", album: "Fantasies", durationSeconds: 293)
			Song.build(title: "I'm Confused", artist: "Handsome Furs", album: "Face Control", durationSeconds: 215)
		}
	}

	@After
	void tearDown() {
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
		assertThat "active", playlist.active, equalTo(true)
		assertThat "genre", playlist.genre, equalTo(Genre.ROCK)
		assertThat "song titles", playlist.songs.title, equalTo(["Heads Will Roll", "I'm Confused"])
	}

	void testPropertyGetOnVariousInputTypes() {
		def playlist
		Playlist.withTransaction {
			playlist = Playlist.build(name: "My Playlist", active: true, genre: Genre.ROCK, songs: Song.findAllByAlbumLike("F%"))
		}

		def page = GrailsEditPage.open("/playlist/edit/$playlist.id")
		assertThat "name", page.name, equalTo(playlist.name)
		assertThat "active", page.active, equalTo(true)
		assertThat "songs", page.songs, equalTo(playlist.songs*.toString())
		assertThat "genre", page.genre, equalTo(playlist.genre.name())
		page.save()
	}

}
