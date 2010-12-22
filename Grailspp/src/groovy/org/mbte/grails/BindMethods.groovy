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

import org.codehaus.groovy.grails.web.metaclass.BindDynamicMethod

@Trait abstract class BindMethods {
  static final BindDynamicMethod bind = []

  def bindData(Object target, Object args) {
      bind.invoke(this, "bindData", [target, args] as Object[])
  }

  def bindData(Object target, Object args, List disallowed) {
      bind.invoke(this, "bindData", [target, args, [exclude: disallowed]] as Object[])
  }

  def bindData(Object target, Object args, List disallowed, String filter) {
      bind.invoke(this, "bindData", [target, args, [exclude: disallowed], filter] as Object[])
  }

  def bindData(Object target, Object args, Map includeExclude) {
      bind.invoke(this, "bindData", [target, args, includeExclude] as Object[])
  }

  def bindData(Object target, Object args, Map includeExclude, String filter) {
      bind.invoke(this, "bindData", [target, args, includeExclude, filter] as Object[])
  }

  def bindData(Object target, Object args, String filter) {
      bind.invoke(this, "bindData", [target, args, filter] as Object[])
  }
}
