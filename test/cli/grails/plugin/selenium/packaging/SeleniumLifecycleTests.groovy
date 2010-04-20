package grails.plugin.selenium.packaging

import grails.test.AbstractCliTestCase
import org.junit.Test
import static org.junit.Assert.*

class SeleniumLifecycleTests extends AbstractCliTestCase {

	@Test
	capturesScreenshotOnTestFailure() {
		execute(["simulate-test-failure"])
		assertEquals 0, waitForProcess()
		verifyHeader()

		assertSeleniumServerIsRunning()
//		assertSeleniumIsRunning()

		execute(["signal-tests-end"])

		assertSeleniumServerIsStopped()
//		assertSeleniumIsStopped()
	}

	void assertSeleniumServerIsRunning() {
		assertTrue "Selenium server is not running", isServerRunning()
	}

	void assertSeleniumServerIsStopped() {
		assertFalse "Selenium server is still running", isServerRunning()
	}

	boolean isServerRunning() {
		def socket = null
		try {
			socket = new Socket("localhost", serverPort)
			return socket.isConnected()
		} catch (ConnectException e) {
			return false
		} finally {
			socket?.close()
		}
	}

	int getServerPort() {
		return System.properties."selenium.server.port" as Integer ?: 4444
	}
}
