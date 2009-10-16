package grails.plugins.selenium.test

import grails.plugins.selenium.GrailsSeleneseTestCase

class DragAndDropTests extends GrailsSeleneseTestCase {

	void testDragToTarget() {
		selenium.open "$rootURL/dragdrop.gsp"
		assertText "css=#droppable p", "Drop here"
		selenium.dragAndDropToObject("draggable", "droppable")
		waitForElementPresent("css=#droppable.ui-state-highlight")
		assertText "css=#droppable p", "Dropped!"
	}

}