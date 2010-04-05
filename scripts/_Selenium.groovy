seleniumManager = null

target(seleniumInit: "Initialises Selenium manaager") {
	event "StatusUpdate", ["Initialising Selenium"]
	def managerClass = classLoader.loadClass("grails.plugins.selenium.SeleniumManager")
	seleniumManager = managerClass.instance
	seleniumManager.config = seleniumConfig
	eventListener.addGrailsBuildListener(seleniumManager)
}
