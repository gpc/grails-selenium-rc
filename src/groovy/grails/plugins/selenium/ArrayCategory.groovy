package grails.plugins.selenium

@Category(Object[]) class ArrayCategory {
	Object head() {
		length > 0 ? this[0] : null
	}

	Object[] tail() {
		length > 1 ? this[1..length - 1] : [] as Object[]
	}
}
