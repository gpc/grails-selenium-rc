def sourceConfigFile = "${seleniumRcPluginDir}/grails-app/conf/SeleniumConfigTemplate.groovy"
def targetConfigFile = "${basedir}/grails-app/conf/SeleniumConfig.groovy"
ant.copy(file: sourceConfigFile, tofile: targetConfigFile, overwrite: false)
