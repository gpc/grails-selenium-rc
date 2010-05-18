import musicstore.auth.*

class BootStrap {

	def init = { servletContext ->
		[ROLE_USER: "A standard user", ROLE_ADMIN: "An administrator"].each { authority, description ->
			if (Role.countByAuthority(authority) == 0) {
				new Role(authority: authority, description: description).save(failOnError: true, flush: true)
			}
		}
	}

	def destroy = {
	}
} 