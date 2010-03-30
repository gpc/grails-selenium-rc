package grails.plugins.selenium.test.pageobjects

import grails.plugins.selenium.test.Song
import grails.plugins.selenium.pageobjects.GrailsListPage
import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*
import static org.hamcrest.CoreMatchers.*
import static org.junit.matchers.JUnitMatchers.*
import org.junit.After
import org.junit.AfterClass

class ListSongTests {

	@Before void setUp() {
		Song.withTransaction {
			Song.build(title: "Heads Will Roll", artist: "Yeah Yeah Yeahs", album: "It's Blitz!", durationSeconds: 221)
			Song.build(title: "Twilight Galaxy", artist: "Metric", album: "Fantasies", durationSeconds: 293)
			Song.build(title: "I'm Confused", artist: "Handsome Furs", album: "Face Control", durationSeconds: 215)
		}
	}

	@After void tearDown() {
		Song.withTransaction {
			Song.list()*.delete()
		}
	}

	@Test void correctColumnsAndRowsAppear() {
		def listPage = GrailsListPage.open("/song/list")

		assertThat listPage.columnNames, equalTo(["Id", "Title", "Artist", "Album", "Duration Seconds"])
		assertThat listPage.rowCount, equalTo(3)
	}

	@Test void songsCanBeSortedByTitle() {
		def listPage = GrailsListPage.open("/song/list")

		listPage.sortByColumn "Title"

		assertThat listPage.rows.Title, equalTo(["Heads Will Roll", "I'm Confused", "Twilight Galaxy"])
	}

	@Test void songsCanBeSortedByTitleInReverse() {
		def listPage = GrailsListPage.open("/song/list")

		listPage.sortByColumn "Title"
		listPage.sortByColumn "Title"

		assertThat listPage.rows.Title, equalTo(["Twilight Galaxy", "I'm Confused", "Heads Will Roll"])
	}

	@Test void songsCanBeSortedByArtits() {
		def listPage = GrailsListPage.open("/song/list")

		listPage.sortByColumn "Artist"

		assertThat listPage.rows.Artist, equalTo(["Handsome Furs", "Metric", "Yeah Yeah Yeahs"])
	}

	@Test void listIsPaginatedIfManySongsExist() {
		Song.withTransaction {
			["Zero", "Softshock", "Skeletons", "Dull Life", "Shame and Fortune", "Runaway", "Dragon Queen", "Hysteric", "Little Shadow"].each {
				Song.build(title: it, artist: "Yeah Yeah Yeahs", album: "It's Blitz!")
			}
		}

		def listPage = GrailsListPage.open("/song/list")
		assertThat listPage.rowCount, equalTo(10)
		assertThat listPage.nextPage().rowCount, equalTo(2)
		assertThat listPage.previousPage().rowCount, equalTo(10)
	}
}