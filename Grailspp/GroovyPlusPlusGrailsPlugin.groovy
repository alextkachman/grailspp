class GroovyPlusPlusGrailsPlugin {
    // the plugin version
    def version = "#version"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.5 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def author = "Alex Tkachman"
    def authorEmail = "alex.tkachman@gmail.com"
    def title = "Groovy++ Grails Integration"
    def description = '''\\
The plugin integrating Groovy++ in to Grails
'''
}
