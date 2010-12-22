/*
 * Copyright 2009-2010 MBTE Sweden AB.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mbte.grails.compiler

import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.ModuleNode
import org.mbte.groovypp.compiler.languages.LanguageDefinition

import org.mbte.groovypp.compiler.languages.ScriptLanguageProvider
import org.codehaus.groovy.control.SourceUnit

@Typed public class GrailsScriptLanguageProvider extends ScriptLanguageProvider {

    static final String SERVICES_ANCHOR    = "grails-app${File.separatorChar}services${File.separatorChar}"
    static final String CONTROLLERS_ANCHOR = "grails-app${File.separatorChar}controllers${File.separatorChar}"
    static final String DOMAIN_ANCHOR      = "grails-app${File.separatorChar}domain${File.separatorChar}"
    static final String TAGLIB_ANCHOR      = "grails-app${File.separatorChar}taglib${File.separatorChar}"
    static final String UTILS_ANCHOR       = "grails-app${File.separatorChar}utils${File.separatorChar}"
    static final String VIEWS_ANCHOR       = "grails-app${File.separatorChar}views${File.separatorChar}"

    static final Map<String,Class> anchors = [
            (CONTROLLERS_ANCHOR) : ControllerLanguage
    ]

    Class<LanguageDefinition> findScriptLanguage(ModuleNode moduleNode) {
        List<ClassNode> classes = moduleNode.getClasses();
        if (!classes.size())
            return null

        if(moduleNode.context.name.endsWith("_gsp"))
          return GspLanguage

        for(e in anchors.entrySet()) {
            if(isGrailsScript(moduleNode.context, e.key)) {
                return e.value
            }
        }

        return null
    }

    protected boolean isGrailsScript(SourceUnit sourceNode, String anchorPath) {
        sourceNode.name.indexOf(anchorPath) != -1
    }
}