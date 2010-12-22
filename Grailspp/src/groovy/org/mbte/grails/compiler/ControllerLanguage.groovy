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

import org.codehaus.groovy.ast.ModuleNode
import org.mbte.groovypp.compiler.TypeUtil
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode

@Typed class ControllerLanguage extends GrailsLanguage {

  public static final ClassNode CONTROLLER_METHODS = ClassHelper.make("org.mbte.grails.ControllerMethods")

  ControllerLanguage() {
    super(GrailsScriptLanguageProvider.CONTROLLERS_ANCHOR)
  }

  @Override
  void apply(ModuleNode moduleNode) {
    super.apply(moduleNode)

    improveControllerInheritance(moduleNode)
    improveServiceFields(moduleNode)
  }

  private def improveControllerInheritance(ModuleNode moduleNode) {
    def clazz = moduleNode.classes[0]
    def typedList = clazz.getAnnotations(TypeUtil.TYPED)
    def typed = typedList && !typedList.empty

    if (!typed) {
      typedList = moduleNode.package?.getAnnotations(TypeUtil.TYPED)
      typed = typedList && !typedList.empty
    }

    if (typed) {
      clazz.addInterface CONTROLLER_METHODS
    }
  }
}
