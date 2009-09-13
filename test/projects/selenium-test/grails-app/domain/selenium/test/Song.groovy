package selenium.test

class Song {

	String title
	String artist
	String album
	int durationSeconds

	static constraints = {
		title blank: false
		artist blank: false
		album blank: false
	}
}
