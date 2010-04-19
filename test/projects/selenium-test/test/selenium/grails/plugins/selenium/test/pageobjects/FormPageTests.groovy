package grails.plugins.selenium.test.pageobjects

import grails.plugins.selenium.pageobjects.GrailsCreatePage
import grails.plugins.selenium.pageobjects.GrailsEditPage
import grails.plugins.selenium.test.Genre
import grails.plugins.selenium.test.Playlist
import grails.plugins.selenium.test.Song
import org.junit.After
import org.junit.AfterClass
import org.junit.BeforeClass
import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat

class FormPageTests {

	@BeforeClass
	static void setUpSongs() {
		Song.withTransaction {
			Song.build(title: "Heads Will Roll", artist: "Yeah Yeah Yeahs", album: "It's Blitz!", durationSeconds: 221)
			Song.build(title: "Twilight Galaxy", artist: "Metric", album: "Fantasies", durationSeconds: 293)
			Song.build(title: "I'm Confused", artist: "Handsome Furs", album: "Face Control", durationSeconds: 215)
		}
	}

	@AfterClass
	static void tearDownSongs() {
		Song.withTransaction {
			Song.list()*.delete()
		}
	}

	@After
	void tearDownPlaylists() {
		Song.withTransaction {
			Playlist.list()*.delete()
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
		def id
		Playlist.withNewSession {
			id = Playlist.build(name: "My Playlist", active: true, genre: Genre.ROCK, songs: Song.findAllByAlbumLike("F%")).id
		}

		def page = GrailsEditPage.open("/playlist/edit/$id")
		assertThat "name", page.name, equalTo("My Playlist")
		assertThat "active", page.active, equalTo(true)
		assertThat "songs", page.songs, equalTo(["Twilight Galaxy by Metric", "I'm Confused by Handsome Furs"])
		assertThat "genre", page.genre, equalTo("ROCK")
		page.save()
	}

}
