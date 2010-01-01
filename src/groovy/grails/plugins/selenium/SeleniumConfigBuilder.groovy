package grails.plugins.selenium

import grails.util.GrailsUtil
import org.slf4j.LoggerFactory

class SeleniumConfigBuilder {
	
	private static final log = LoggerFactory.getLogger(SeleniumConfigBuilder)

	ConfigObject config
	
	SeleniumConfigBuilder loadDefaultConfiguration() {
		def defaultConfig = """
selenium {
	server {
		host = "localhost"
		port = 4444
	}
	browser = "${getDefaultBrowser()}"
	defaultTimeout = 60000
	slow = false
	singleWindow = true
	windowMaximize = false
	alwaysCaptureScreenshots = false
	captureScreenshotOnFailure = false
	screenshotDir = "./test/reports/screenshots"
}
		"""
		config = new ConfigSlurper(GrailsUtil.environment).parse(defaultConfig)
		return this
	}

	SeleniumConfigBuilder mergeApplicationConfig() {
		def configClass = getSeleniumConfigClass()
		if (configClass) {
			def slurper = new ConfigSlurper(GrailsUtil.environment)
			config.merge slurper.parse(configClass)
		}
		return this
	}

	SeleniumConfigBuilder mergeSystemProperties() {
		// override settings with system properties
		def sysProps = new StringBuilder()
		System.properties.each {
			if (it.key =~ /^selenium\./) {
				sysProps << it.key << "="
				if (it.value in ["true", "false"] || it.value.isInteger()) sysProps << it.value
				else sysProps << "'" << it.value << "'"
				sysProps << "\n"
			}
		}
		if (sysProps) {
			def slurper = new ConfigSlurper(GrailsUtil.environment)
			config.merge slurper.parse(sysProps.toString())
		}
		return this
	}

	private static getDefaultBrowser() {
		switch (System.properties."os.name") {
			case ~/^Mac OS.*/:
				return "*safari"
			case ~/^Windows.*/:
				return "*iexplore"
			default:
				return "*firefox"
		}
	}

	private static Class getSeleniumConfigClass() {
		GroovyClassLoader classLoader = new GroovyClassLoader(SeleniumManager.classLoader)
		try {
			return classLoader.loadClass('SeleniumConfig')
		} catch (ClassNotFoundException ex) {
			log.warn "SeleniumConfig.groovy not found, proceeding without config file"
			return null
		}
	}

}