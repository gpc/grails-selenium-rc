@artifact.package@import grails.plugins.selenium.*
import org.junit.*
import static org.junit.Assert.*
import static org.hamcrest.Matchers.*

@Mixin(SeleniumAware)
class @artifact.name@ {
    @Before void setUp() {
    }

    @After void tearDown() {
        super.tearDown()
    }

    @Test void something() {

    }
}
