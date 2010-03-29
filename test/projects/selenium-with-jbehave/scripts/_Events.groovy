eventCompileEnd = {
	ant.copy(todir: grailsSettings.testClassesDir, verbose: true) {
		fileset(dir: "test/resources")
	}
}