package grails.plugins.selenium.test

class Song {

	String title
	String artist
	String album
	int durationSeconds

	static constraints = {
		title blank: false
		artist blank: false
		album nullable: true
	}

	String toString() {
		"$title by $artist"
	}
}
