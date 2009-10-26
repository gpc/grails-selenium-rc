package grails.plugins.selenium.test

import grails.plugins.selenium.GrailsSeleneseTestCase

class DragAndDropTests extends GrailsSeleneseTestCase {

	void testDragToTarget() {
		selenium.open "$contextPath/dragdrop.gsp"
		assertText "Drop here", "css=#droppable p"
		selenium.dragAndDropToObject("draggable", "droppable")
		waitForElementPresent("css=#droppable.ui-state-highlight")
		assertText "Dropped!", "css=#droppable p"
	}

}