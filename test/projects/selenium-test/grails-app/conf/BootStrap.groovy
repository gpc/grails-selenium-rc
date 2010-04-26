import grails.plugins.selenium.test.auth.Role

class BootStrap {

	def authenticateService

	def init = { servletContext ->
		new Role(description: "A standard user", authority: "ROLE_USER").save(failOnError: true)
	}

	def destroy = {
	}
} 