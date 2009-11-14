package grails.plugins.selenium.test

import grails.plugins.selenium.SeleniumTest

@Mixin(SeleniumTest)
class DragAndDropTests extends GroovyTestCase {

	void testDragToTarget() {
		selenium.open "/dragdrop.gsp"
		assertEquals "Drop here", selenium.getText("css=#droppable p")
		selenium.dragAndDropToObject("draggable", "droppable")
		waitFor {
			selenium.isElementPresent("css=#droppable.ui-state-highlight")
		}
		assertEquals "Dropped!", selenium.getText("css=#droppable p")
	}

}