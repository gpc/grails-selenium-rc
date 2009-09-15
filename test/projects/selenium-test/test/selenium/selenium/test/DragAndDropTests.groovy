package selenium.test

import com.energizedwork.grails.plugins.seleniumrc.GrailsSeleneseTestCase

class DragAndDropTests extends GrailsSeleneseTestCase {

	void testDragToTarget() {
		selenium.open "$rootURL/dragdrop.gsp"
		assertEquals "Drop here", selenium.getText("css=#droppable p")
		selenium.dragAndDropToObject("draggable", "droppable")
		waitFor {
			selenium.isElementPresent("css=#droppable.ui-state-highlight")
		}
		assertEquals "Dropped!", selenium.getText("css=#droppable p")
	}

}