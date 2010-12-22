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

import org.mbte.groovypp.compiler.languages.LanguageDefinition
import org.codehaus.groovy.ast.ModuleNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.ClassHelper

@Typed class GrailsLanguage extends LanguageDefinition {

    protected final String anchor

    GrailsLanguage(String anchor) {
        this.anchor = anchor

        def oldConversion = conversion
        conversion = { moduleNode ->
            improveGrailsPackage moduleNode
            oldConversion?.execute moduleNode
        }
    }

    void apply(ModuleNode moduleNode) {
        conversion?.execute(moduleNode)
    }

    void improveServiceFields(ModuleNode moduleNode) {
      for(c in moduleNode.classes) {
        for(f in c.fields) {
          if(f.name.endsWith("Service") && f.type == ClassHelper.DYNAMIC_TYPE) {
            def name = ArtefactCache.findArtefactClass("${f.name[0].toUpperCase()}${f.name.substring(1)}")
            if(name)
              f.type = ClassHelper.make(name)
          }
        }
      }
    }

    void improveGrailsPackage(ModuleNode moduleNode) {
        def packageNode = moduleNode.package
        if(!packageNode) {
            def pname = moduleNode.context.name
            def ind = pname.indexOf(anchor)
            if(ind != -1) {
                pname = pname.substring(ind + anchor.length())
                ind = pname.lastIndexOf(File.separator)
                if(ind != -1) {
                    pname = pname.substring(0, ind).replace(File.separatorChar, '.')
                    moduleNode.package = [pname]
                    for(cls in moduleNode.classes) {
                        cls.setName("$pname.${cls.name}")
                    }
                }
            }
        }
    }
}
