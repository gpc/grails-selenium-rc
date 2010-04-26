grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
	inherits "global"
	log "warn"
	repositories {
		grailsPlugins()
		grailsHome()
		grailsCentral()
		mavenLocal()
		mavenCentral()
	}
	dependencies {
		test("org.hamcrest:hamcrest-all:1.1") {
			excludes "jmock", "easymock", "junit"
		}
	}
	plugins {
		compile ":acegi:0.5.3"
		runtime ":hibernate:1.3.0.RC2"
		runtime ":tomcat:1.3.0.RC2"
		test ":build-test-data:1.1.0"
		test ":spock:0.4-SNAPSHOT"
	}
}

grails.plugin.location."selenium-rc" = "../../.."