/*
 * Copyright 2010 Rob Fletcher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

seleniumServer = null

target(startSeleniumServer: "Starts the Selenium server") {
	// The Selenium server needs to be loaded into a clean class
	// loader because the "selenium-server.jar" includes its own
	// dependencies which conflict with some of the Grails ones.
	def serverClassLoader = new URLClassLoader(
			[serverJar.toURI().toURL()] as URL[],
			ClassLoader.systemClassLoader)

	// HACK - It seems that Jetty will load classes using the thread
	// context class loader, so we temporarily make the server class#
	// loader the context CL.
	def oldContextCL = Thread.currentThread().contextClassLoader
	Thread.currentThread().contextClassLoader = serverClassLoader

	// Create a configuration
	def conf = serverClassLoader.loadClass("org.openqa.selenium.server.RemoteControlConfiguration").newInstance()
	initRemoteControlConfiguration(conf)

	// Load the server class and start the server
	seleniumServer = serverClassLoader.loadClass("org.openqa.selenium.server.SeleniumServer").newInstance(runInSlowMode(), conf)
	event "StatusUpdate", ["Starting Selenium server on port $conf.port"]
	seleniumServer.boot()

	// Jetty is now listening on separate threads, so it appears
	// to be safe to reset the context class loader for this thread.
	// That's the theory anyway.
	Thread.currentThread().contextClassLoader = oldContextCL
}

target(stopSeleniumServer: "Stops the Selenium server") {
	event "StatusUpdate", ["Stopping Selenium server"]
	seleniumServer?.stop()
}

private void initRemoteControlConfiguration(conf) {
	conf.port = seleniumConfig.selenium.server.port
	conf.singleWindow = seleniumConfig.selenium.singleWindow
	conf.userExtensions = getUserExtensionsFile()
	// some nasty browser specific forced config
	if (isSafari() && !conf.singleWindow) {
		event "StatusError", ["selenium.singleWindow=false is not supported in Safari"]
		conf.singleWindow = true
	} else if (isInternetExplorer() && conf.singleWindow) {
		event "StatusError", ["selenium.singleWindow=true is not supported in Internet Explorer"]
		conf.singleWindow = false
	}
}

private File getUserExtensionsFile() {
	if (seleniumConfig.selenium.userExtensions) {
		def userExtensionsFile = new File(seleniumConfig.selenium.userExtensions)
		if (userExtensionsFile.isFile()) {
			return userExtensionsFile
		} else {
			event "StatusError", ["Specified user extensions file $userExtensionsFile.canonicalPath not found."]
			return null
		}
	} else {
		return null
	}
}

private boolean isInternetExplorer() {
	return seleniumConfig.selenium.browser =~ /^\*iexplore/
}

private boolean isSafari() {
	return seleniumConfig.selenium.browser =~ /^\*safari/
}

private File getServerJar() {
	new File("$seleniumRcPluginDir/lib/server/selenium-server.jar")
}

private boolean runInSlowMode() {
	return config.selenium.slow ?: false
}
