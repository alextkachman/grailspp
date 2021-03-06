/*
 * Copyright 2009-2010 MBTE Sweden AB.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mbte.grails

import org.codehaus.groovy.grails.web.metaclass.RenderDynamicMethod

@Trait abstract class RenderMethods {
  static final RenderDynamicMethod render = []

  def render(Object o) {
      render.invoke(this, "render", [o?.inspect()] as Object[])
  }

  def render(String txt) {
      render.invoke(this, "render", [txt] as Object[])
  }

  def render(Map args) {
      render.invoke(this, "render", [args] as Object[])
  }

  def render(Closure c) {
      render.invoke(this, "render", [c] as Object[])
  }

  def render(Map args, Closure c) {
      render.invoke(this, "render", [args, c] as Object[])
  }
}
