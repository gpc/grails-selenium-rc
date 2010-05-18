package grails.plugin.selenium.tests

import grails.plugin.selenium.tests.pages.ListSongPage
import musicstore.Artist
import musicstore.Song
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.hasItems

class TabularDataTests extends GroovyTestCase {

	void setUp() {
		super.setUp()
		Song.withTransaction {
			Song.build(title: "Heads Will Roll", artist: new Artist(name: "Yeah Yeah Yeahs"), album: "It's Blitz!", durationSeconds: 221)
			Song.build(title: "Twilight Galaxy", artist: new Artist(name: "Metric"), album: "Fantasies", durationSeconds: 293)
			Song.build(title: "I'm Confused", artist: new Artist(name: "Handsome Furs"), album: "Face Control", durationSeconds: 215)
		}
	}

	void tearDown() {
		super.tearDown()
		Artist.withTransaction {
			Artist.list()*.delete()
		}
	}

	void testCanReadColumnNames() {
		def listPage = ListSongPage.open()

		assertThat listPage.columnNames, hasItems("Id", "Title", "Artist", "Album", "Duration Seconds")
	}

	void testCanReadRowCount() {
		def listPage = ListSongPage.open()

		assertThat listPage.rowCount, equalTo(3)
	}

	void testCanSortTableByColumn() {
		def listPage = ListSongPage.open()

		listPage.sortByColumn "Title"

		assertThat listPage.rows.Title, equalTo(["Heads Will Roll", "I'm Confused", "Twilight Galaxy"])
	}

	void testCanReverseSortTable() {
		def listPage = ListSongPage.open()

		listPage.sortByColumn "Title"
		listPage.sortByColumn "Title"

		assertThat listPage.rows.Title, equalTo(["Twilight Galaxy", "I'm Confused", "Heads Will Roll"])
	}

	void testCanPaginateList() {
		Song.withTransaction {
			def artist = Artist.findByName("Yeah Yeah Yeahs")
			["Zero", "Softshock", "Skeletons", "Dull Life", "Shame and Fortune", "Runaway", "Dragon Queen", "Hysteric", "Little Shadow"].each {
				Song.build(title: it, artist: artist, album: "It's Blitz!")
			}
		}

		def listPage = ListSongPage.open()
		assertThat listPage.rowCount, equalTo(10)
		assertThat listPage.nextPage().rowCount, equalTo(2)
		assertThat listPage.previousPage().rowCount, equalTo(10)
	}
}
