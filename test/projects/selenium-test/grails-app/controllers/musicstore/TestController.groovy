package musicstore

class TestController {

    def index = { }

	def alert = {
		render {
			script(type: "text/javascript", "alert('O HAI!');")
		}
	}

	def message = {
		render {
			div("class": "message", "This is a message from an AJAX call!")
		}
	}
}
