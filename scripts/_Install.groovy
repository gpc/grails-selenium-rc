def sourceConfigFile = "${seleniumRcPluginDir}/src/templates/conf/SeleniumConfigTemplate.groovy"
def targetConfigFile = "${basedir}/grails-app/conf/SeleniumConfig.groovy"
ant.copy(file: sourceConfigFile, tofile: targetConfigFile, overwrite: false)
ant.mkdir(dir: "${basedir}/test/selenium")
