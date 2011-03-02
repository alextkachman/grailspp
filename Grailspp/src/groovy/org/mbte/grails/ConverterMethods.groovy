/*
 * Copyright 2009-2011 MBTE Sweden AB.
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
package org.mbte.grails

import org.codehaus.groovy.grails.web.converters.Converter
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.converters.exceptions.ConverterException
import grails.util.GrailsWebUtil

@Trait abstract class ConverterMethods implements CommonWebProperties {
  void render(Converter converter) {
      if(converter instanceof JSON) {
        def resp = response
        resp.setContentType(GrailsWebUtil.getContentType("application/json", "UTF-8" /* @todo: need encoding of converter */));
        try {
            resp.getWriter().print converter.toString()
        }
        catch (IOException e) {
            throw new ConverterException(e);
        }
      }
      else {
        converter.render(response)
      }

      // Prevent Grails from looking for a view if this method is used.
      webRequest.renderView = false
  }

  void headerMethod(String key, def value) {
      if (value)
        response?.setHeader(key, value.toString())
  }

  void jsonHeader(def value) {
      def json = (value instanceof JSON || value instanceof JSONObject || value instanceof JSONArray ||
          value instanceof String) ? value : (new JSON(value))
      if (value) {
          response?.setHeader("X-JSON", value.toString())
      }
  }
}
