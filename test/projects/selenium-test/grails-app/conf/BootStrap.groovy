import grails.plugins.selenium.test.auth.Role

class BootStrap {

	def init = { servletContext ->
		if (Role.countByAuthority("ROLE_USER") == 0) {
			new Role(description: "A standard user", authority: "ROLE_USER").save(failOnError: true)
		}
	}

	def destroy = {
	}
} 