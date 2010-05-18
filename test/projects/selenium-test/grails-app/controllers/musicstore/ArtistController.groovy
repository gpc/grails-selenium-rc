package musicstore

class ArtistController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	static scaffold = true

	def lookup = {
		def names = Artist.withCriteria {
			projections {
				property "name"
			}
			ilike "name", "$params.artist%"
			order "name", "asc"
		}
		render {
			ul {
				names.each { name ->
					li(name)
				}
			}
		}
	}
	
}
