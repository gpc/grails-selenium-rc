package grails.plugins.selenium.test

class Playlist {

	String name	// text field
	List songs	 // multi-select
	boolean active // checkbox
	Genre genre	// single select

	static hasMany = [songs: Song]

	static constraints = {
		name blank: false, unique: true
	}

	String toString() {
		def buffer = new StringBuilder()
		buffer << "Playlist '" << name << "' ["
		buffer << songs*.toString().join(", ")
		buffer << "]"
		return buffer as String
	}

}

enum Genre {
	ROCK, POP, COUNTRY
}
