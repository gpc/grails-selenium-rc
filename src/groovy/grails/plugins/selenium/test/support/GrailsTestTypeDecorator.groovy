package grails.plugins.selenium.test.support

import org.codehaus.groovy.grails.test.GrailsTestTargetPattern
import org.codehaus.groovy.grails.test.GrailsTestType
import org.codehaus.groovy.grails.test.GrailsTestTypeResult
import org.codehaus.groovy.grails.test.event.GrailsTestEventPublisher

class GrailsTestTypeDecorator implements GrailsTestType {

	private final GrailsTestType delegate

	GrailsTestTypeDecorator(GrailsTestType delegate) {
		this.delegate = delegate
	}

	String getName() {
		return delegate.getName()
	}

	String getRelativeSourcePath() {
		return delegate.getRelativeSourcePath()
	}

	int prepare(GrailsTestTargetPattern[] testTargetPatterns, File compiledClassesDir, Binding buildBinding) {
		return delegate.prepare(testTargetPatterns, compiledClassesDir, buildBinding)
	}

	GrailsTestTypeResult run(GrailsTestEventPublisher eventPublisher) {
		return delegate.run(eventPublisher)
	}

	void cleanup() {
		delegate.cleanup()
	}
}
