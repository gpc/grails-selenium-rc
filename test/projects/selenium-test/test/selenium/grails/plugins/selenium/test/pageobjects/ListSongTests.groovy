package grails.plugins.selenium.test.pageobjects

import grails.plugins.selenium.test.Song

class ListSongTests extends GroovyTestCase {

	ListSongPage page

	void setUp() {
		super.setUp()

		Song.withTransaction {
			Song.build(title: "Heads Will Roll", artist: "Yeah Yeah Yeahs", album: "It's Blitz!", durationSeconds: 221)
			Song.build(title: "Twilight Galaxy", artist: "Metric", album: "Fantasies", durationSeconds: 293)
			Song.build(title: "I'm Confused", artist: "Handsome Furs", album: "Face Control", durationSeconds: 215)
		}

		page = ListSongPage.open()
	}

	void tearDown() {
		super.tearDown()
		Song.withTransaction {
			Song.list()*.delete()
		}
	}

	void testCorrectColumnsAndRowsAppear() {
		assertEquals(["Id", "Title", "Artist", "Album", "Duration Seconds"], page.columnNames)
		assertEquals 3, page.rowCount
	}

	void testSongsCanBeSortedByTitle() {
		page.sortByColumn "Title"
		assertEquals(["Heads Will Roll", "I'm Confused", "Twilight Galaxy"], page.rows.Title)
	}

	void testSongsCanBeSortedByTitleInReverse() {
		page.sortByColumn "Title"
		page.sortByColumn "Title"
		assertEquals(["Twilight Galaxy", "I'm Confused", "Heads Will Roll"], page.rows.Title)
	}

	void testSongsCanBeSortedByArtits() {
		page.sortByColumn "Artist"
		assertEquals(["Handsome Furs", "Metric", "Yeah Yeah Yeahs"], page.rows.Artist)
	}
}