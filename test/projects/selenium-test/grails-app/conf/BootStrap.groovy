import grails.plugins.selenium.test.auth.User
import grails.plugins.selenium.test.auth.Role

class BootStrap {

	def authenticateService

	def init = { servletContext ->
		def user = new User(username: "blackbeard", userRealName: "Edward Teach", enabled: true, email: "blackbeard@energizedwork.com")
		user.passwd = authenticateService.encodePassword("password")
		user.save(failOnError: true)

		def role = new Role(description: "A standard user", authority: "ROLE_USER")
		role.addToPeople(user)
		role.save(failOnError: true)
	}

	def destroy = {
	}
} 