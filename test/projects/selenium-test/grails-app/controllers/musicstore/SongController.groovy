package musicstore

class SongController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	static scaffold = true

	def beforeInterceptor = {
		println "SongController.$params.action"
		params.each {k, v ->
			println "   $k = $v"
		}
	}

}
