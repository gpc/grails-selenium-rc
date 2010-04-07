package grails.plugins.selenium.condition

import com.thoughtworks.selenium.Wait

class ClosureEvaluatingWait extends Wait {

	Closure condition

	boolean until() {
		condition()
	}
}
