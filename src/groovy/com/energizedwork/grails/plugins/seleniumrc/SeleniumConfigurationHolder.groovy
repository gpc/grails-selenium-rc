package com.energizedwork.grails.plugins.seleniumrc

import grails.util.GrailsUtil
import org.slf4j.LoggerFactory

class SeleniumConfigurationHolder {

	private static final log = LoggerFactory.getLogger(SeleniumConfigurationHolder)

	static ConfigObject config

	static void loadSeleniumConfig() {
		config = loadDefaultConfiguration()

		def configClass = getDefaultSeleniumConfigClass()
		if (configClass) {
			def slurper = new ConfigSlurper(GrailsUtil.environment)
			config.merge(slurper.parse(configClass))
		}
	}

	private static ConfigObject loadDefaultConfiguration() {
		def defaultConfig = """
selenium {
	host = "localhost"
	port = 4444
	browser = "*firefox"
	slowResources = false
	singleWindow = true
	screenshots = "no"
	screenshotDir = "./test/reports/screenshots"
}
		"""
		return new ConfigSlurper(GrailsUtil.environment).parse(defaultConfig)
	}

	private static Class getDefaultSeleniumConfigClass() {
		GroovyClassLoader classLoader = new GroovyClassLoader(SeleniumConfigurationHolder.classLoader)
		try {
			return classLoader.loadClass('SeleniumConfig')
		} catch (ClassNotFoundException ex) {
			log.warn "SeleniumConfig.groovy not found, proceeding without config file"
		}
	}

}