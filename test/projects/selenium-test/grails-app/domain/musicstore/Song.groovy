package musicstore

import org.apache.commons.lang.WordUtils
import org.springframework.context.MessageSourceResolvable

class Song {

	String title
	String album
	int durationSeconds
	Genre genre
	boolean partOfCompilation

	static belongsTo = [artist: Artist]

	static constraints = {
		title blank: false
		album nullable: true
		genre nullable: true
	}

	static mapping = {
		artist cascade: "save-update"
	}

	String toString() {
		"$title by $artist"
	}
}

enum Genre implements MessageSourceResolvable {
	FREAK_FOLK, ALT_COUNTRY, POST_PUNK, ELECTROCLASH

	String[] getCodes() {
		return ["${Genre.name}.${name()}"] as String[]
	}

	Object[] getArguments() {
		return [] as Object[]
	}

	String getDefaultMessage() {
		return WordUtils.capitalizeFully(name().replaceAll(/_+/, " "))
	}

}