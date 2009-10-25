grails.project.dependency.resolution = {
	inherits "global" // inherit Grails' default dependencies
	repositories {
		grailsHome()
		mavenCentral()
	}
	dependencies {
		provided "org.seleniumhq.selenium.client-drivers:selenium-java-client-driver:1.0.1"
		test "org.gmock:gmock:0.8.0"
	}
}