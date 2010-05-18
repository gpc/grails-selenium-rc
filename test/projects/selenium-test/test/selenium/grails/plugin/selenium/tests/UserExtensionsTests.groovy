package grails.plugin.selenium.tests

import grails.plugins.selenium.SeleniumAware
import musicstore.auth.Role
import musicstore.auth.User
import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.MatcherAssert.assertThat

@Mixin(SeleniumAware)
class UserExtensionsTests extends GroovyTestCase {

	def authenticateService

	void setUp() {
		super.setUp()
		User.withTransaction {
			def user = new User(username: "blackbeard", userRealName: "Edward Teach", enabled: true, email: "blackbeard@energizedwork.com")
			user.passwd = authenticateService.encodePassword("password")
			user.addToAuthorities Role.findByAuthority("ROLE_USER")
			user.save(failOnError: true)
		}
	}

	void tearDown() {
		super.tearDown()

		selenium.open "/logout"
		assertThat "login state", selenium.getLoginState(), equalTo("Not logged in")

		User.withTransaction {
			User.list().each {User user ->
				user.authorities.each {Role role ->
					user.removeFromAuthorities role
				}
				user.delete()
			}
		}
	}

	void testCanUseSeleniumUserExtensions() {
		selenium.open "/"
		assertThat "login state", selenium.getLoginState(), equalTo("Not logged in")

		selenium.open "/login"
		selenium.login "blackbeard", "password"

		assertThat "login state", selenium.getLoginState(), equalTo("Logged in as blackbeard")
	}

}
