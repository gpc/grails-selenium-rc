includeTargets << grailsScript("_GrailsInit")
includeTargets << grailsScript("_GrailsCreateArtifacts")

target(main: "Creates a new Grails Selenium RC test") {
	depends(checkVersion, parseArguments)

    promptForName(type: "Selenium test")

    def name = argsMap["params"][0]
    createSeleniumTest(name: name, suffix: "")
}

createSeleniumTest = { Map args = [:] ->
    def superClass = args["superClass"] ?: "GroovyTestCase"
	def template = grailsVersion.startsWith("1.2") ? "JUnit3SeleniumTests" : "JUnit4SeleniumTests"
	createArtifact(name: args["name"], suffix: "${args['suffix']}Tests", type: template, path: "test/selenium", superClass: superClass)
}

setDefaultTarget(main)
