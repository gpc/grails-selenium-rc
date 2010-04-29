package grails.plugins.selenium.condition

import com.thoughtworks.selenium.Wait

class ClosureEvaluatingWait extends Wait {

	static void waitFor(String timeoutMessage, Closure condition) {
		def wait = new ClosureEvaluatingWait()
		wait.condition = condition
		wait.wait(timeoutMessage)
	}

	Closure condition

	boolean until() {
		condition()
	}
}
