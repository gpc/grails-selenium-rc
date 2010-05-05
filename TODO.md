## Bugs
* Hamcrest dependency isn't getting exported properly
* Can we resolve selenium server using the dependency DSL?

## Testing
* Move spock coverage into main test project
* Test against different Grails versions 1.2.0+

## Enhancements
* GrailsFormPage needs to support radio buttons
* SeleniumWrapper.waitForNot*
* Better access to waitFor method
* Overrides of waitFor method to make message optional, etc.
* Run against running Selenium Server
* JBehave needs different context setting listener

## Long term
* support for comparing text with message key to make i18n based testing less brittle
* Upgrade to Selenium 2.0 / Webdriver
* Selenium Grid
* Support additional test frameworks e.g. TestNG, EasyB, JBehave

## Docs
* Tips - using context /
* Using a fixture controller
* Is withNewSession always a good idea for setup?