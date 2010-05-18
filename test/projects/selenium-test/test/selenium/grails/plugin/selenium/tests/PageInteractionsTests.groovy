package grails.plugin.selenium.tests

import grails.plugins.selenium.SeleniumAware
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.equalTo

@Mixin(SeleniumAware)
class PageInteractionsTests extends GroovyTestCase {

	void setUp() {
		super.setUp()
		selenium.open "/test"
	}

	void testUsingSortableList() {
		def list = {->
			(1..3).collect { i ->
				selenium.getText("css=ul#sortable-list li:nth-child($i)")
			}
		}

		assertThat list(), equalTo(["Item 1", "Item 2", "Item 3"])
		selenium.dragAndDrop("css=ul#sortable-list li:nth-child(1)", "0,+40")
		assertThat list(), equalTo(["Item 2", "Item 3", "Item 1"])
	}

	void testDragAndDrop() {
		selenium.dragAndDropToObject("css=#draggable-box", "css=#droppable-box")

		assertThat selenium.getText("css=#droppable-box"), equalTo("Win!")
	}
}
