package musicstore

class Artist {

	String name

	static hasMany = [songs: Song]

    static constraints = {
		name unique: true, blank: false
    }

	String toString() { name }

}
