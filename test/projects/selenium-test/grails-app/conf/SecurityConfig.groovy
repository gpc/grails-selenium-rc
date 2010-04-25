security {

	// see DefaultSecurityConfig.groovy for all settable/overridable properties

	active = true

	loginUserDomainClass = "grails.plugins.selenium.test.auth.User"
	authorityDomainClass = "grails.plugins.selenium.test.auth.Role"

	useRequestMapDomainClass = false
	useControllerAnnotations = true
}
