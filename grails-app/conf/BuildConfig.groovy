grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.dependency.resolution = {
	inherits "global"
	log "warn"
	repositories {
		grailsHome()
		grailsPlugins()
		mavenCentral()
	}
	dependencies {
		test("org.seleniumhq.selenium.client-drivers:selenium-java-client-driver:1.0.2") {
			excludes "selenium-server", "gmaven-runtime-default", "junit"
		}
		test("org.gmock:gmock:0.8.0") {
			excludes "junit"
			exported = false
		}
		test("org.hamcrest:hamcrest-all:1.1") {
			excludes "jmock", "easymock", "junit"
		}
	}
}