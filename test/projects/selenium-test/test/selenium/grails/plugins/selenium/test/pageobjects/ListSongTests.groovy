package grails.plugins.selenium.test.pageobjects

import grails.plugins.selenium.test.Song
import grails.plugins.selenium.pageobjects.GrailsListPage

class ListSongTests extends GroovyTestCase {

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
			Song.list()*.delete()
		}
	}

	void testCorrectColumnsAndRowsAppear() {
		def listPage = GrailsListPage.open("/song/list")

		assertEquals(["Id", "Title", "Artist", "Album", "Duration Seconds"], listPage.columnNames)
		assertEquals 3, listPage.rowCount
	}

	void testSongsCanBeSortedByTitle() {
		def listPage = GrailsListPage.open("/song/list")

		listPage.sortByColumn "Title"

		assertEquals(["Heads Will Roll", "I'm Confused", "Twilight Galaxy"], listPage.rows.Title)
	}

	void testSongsCanBeSortedByTitleInReverse() {
		def listPage = GrailsListPage.open("/song/list")

		listPage.sortByColumn "Title"
		listPage.sortByColumn "Title"

		assertEquals(["Twilight Galaxy", "I'm Confused", "Heads Will Roll"], listPage.rows.Title)
	}

	void testSongsCanBeSortedByArtits() {
		def listPage = GrailsListPage.open("/song/list")

		listPage.sortByColumn "Artist"

		assertEquals(["Handsome Furs", "Metric", "Yeah Yeah Yeahs"], listPage.rows.Artist)
	}

	void testListIsPaginatedIfManySongsExist() {
		Song.withTransaction {
			["Zero", "Softshock", "Skeletons", "Dull Life", "Shame and Fortune", "Runaway", "Dragon Queen", "Hysteric", "Little Shadow"].each {
				Song.build(title: it, artist: "Yeah Yeah Yeahs", album: "It's Blitz!")
			}
		}

		def listPage = GrailsListPage.open("/song/list")
		assertEquals 10, listPage.rowCount
		assertEquals 2, listPage.nextPage().rowCount
		assertEquals 10, listPage.previousPage().rowCount
	}
}