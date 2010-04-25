## 1.0
### New Features
* Upgrades to Selenium Server 1.0.3 for Firefox 3.6 / Snow Leopard compatibility
* Added ability to specify `selenium.userExtensions`
* Added `-remote` command line option
* Added ability to capture screenshots on test failures

## 0.2
### New Features
* Added base classes for using the page object pattern with standard Grails pages
* Added `selenium.remote` config option to allow tests to be run against a remote host
### Changes
* The config `selenium.slowResources` is now simply `selenium.slow`

## 0.1.1
### Fixes
* Fixed default Selenium URL to respect `server.port` setting, etc.
* Fixed contextPath handling when `grails.app.context` is set in Config
* Removed `ArrayCategory` class causing `VerifyError` in some odd circumstances
### New Features
* Can now negate dynamic assert/verify/waitFor calls in `GrailsSeleniumTestCase`

## 0.1
* Initial Release
