package grails.plugins.selenium

import com.thoughtworks.selenium.Wait

class ClosureEvaluatingWait extends Wait {

	Closure condition

	boolean until() {
		condition()
	}
}
