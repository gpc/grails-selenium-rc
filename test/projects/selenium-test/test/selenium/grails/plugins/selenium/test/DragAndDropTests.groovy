package grails.plugins.selenium.test

import grails.plugins.selenium.SeleniumAware
import org.junit.Test
import static grails.plugins.selenium.condition.ClosureEvaluatingWait.waitFor
import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat

@Mixin(SeleniumAware)
class DragAndDropTests {

	@Test
	void dragToTarget() {
		selenium.open "/dragdrop.gsp"

		assertThat selenium.getText("css=#droppable p"), equalTo("Drop here")

		selenium.dragAndDropToObject("draggable", "droppable")
		waitFor("Timed out waiting for drag and drop to complete") {
			selenium.isElementPresent("css=#droppable.ui-state-highlight")
		}
		
		assertThat selenium.getText("css=#droppable p"), equalTo("Dropped!")
	}

}
