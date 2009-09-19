package selenium.test

import com.energizedwork.grails.plugins.seleniumrc.GrailsSeleneseTestCase

class DragAndDropTests extends GrailsSeleneseTestCase {

	void testDragToTarget() {
		selenium.open "$rootURL/dragdrop.gsp"
		assertText "css=#droppable p", "Drop here"
		selenium.dragAndDropToObject("draggable", "droppable")
		waitForElementPresent("css=#droppable.ui-state-highlight")
		assertText "css=#droppable p", "Dropped!"
	}

}