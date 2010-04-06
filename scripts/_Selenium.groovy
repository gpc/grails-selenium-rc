seleniumManager = null

target(seleniumInit: "Initialises Selenium manaager") {
	event "StatusUpdate", ["Initialising Selenium"]
	def managerClass = classLoader.loadClass("grails.plugins.selenium.SeleniumManager")
	managerClass.initialize(seleniumConfig, eventListener)
}
