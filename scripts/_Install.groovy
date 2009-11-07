def sourceConfigFile = "${seleniumRcPluginDir}/grails-app/conf/SeleniumConfigTemplate.groovy"
def targetConfigFile = "${basedir}/src/templates/conf/SeleniumConfig.groovy"
ant.copy(file: sourceConfigFile, tofile: targetConfigFile, overwrite: false)
ant.mkdir(dir: "${basedir}/test/selenium")
