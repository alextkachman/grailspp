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

import org.codehaus.groovy.grails.commons.GrailsResourceUtils
import org.codehaus.groovy.grails.plugins.GrailsPluginUtils

@Typed class ArtefactCache {

    static String findArtefactClass(String simpleName) {
        def basedir = System.getProperty("base.dir") ?: "."
        def resources = GrailsPluginUtils.getArtefactResources(basedir)
        for(r in resources) {
            def root = GrailsResourceUtils.getPathFromRoot(r.getURL().toString())[0..-8].replace('/','.')
            println "$simpleName $root"
            if(root.endsWith(simpleName)) {
                return root
            }
        }
        return null
    }
}
