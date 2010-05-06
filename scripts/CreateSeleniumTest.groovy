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
	createArtifact(name: args["name"], suffix: "${args['suffix']}Tests", type: "SeleniumTests", path: "test/selenium", superClass: superClass)
}

setDefaultTarget(main)
