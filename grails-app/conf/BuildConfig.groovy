grails.project.dependency.resolution = {
	inherits "global"
	log "warn"
	repositories {
		grailsHome()
		mavenCentral()
	}
	dependencies {
		build "org.seleniumhq.selenium.client-drivers:selenium-java-client-driver:1.0.1"
		test "org.gmock:gmock:0.8.0"
	}
}