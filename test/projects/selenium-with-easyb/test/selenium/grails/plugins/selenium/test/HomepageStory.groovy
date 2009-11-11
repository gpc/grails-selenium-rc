package grails.plugins.selenium.test

import grails.plugins.selenium.SeleniumManager

before "connect to selenium", {
	given "selenium is available", {
		selenium = SeleniumManager.instance.selenium
	}
}

scenario "user visits homepage", {
	when "a user visits the site", {
		selenium.open("/")
	}
	then "the homepage is displayed", {
		selenium.isTextPresent("Welcome to Grails").shouldBe true
	}
}