security {

	// see DefaultSecurityConfig.groovy for all settable/overridable properties

	active = true

	loginUserDomainClass = "musicstore.auth.User"
	authorityDomainClass = "musicstore.auth.Role"

	useRequestMapDomainClass = false
	useControllerAnnotations = true
}
