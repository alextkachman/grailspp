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
package org.mbte.grails

import javax.servlet.ServletContext
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.codehaus.groovy.grails.web.servlet.mvc.*
import org.codehaus.groovy.grails.web.servlet.FlashScope
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.web.context.request.RequestContextHolder

@Trait abstract class CommonWebProperties {

    GrailsWebRequest getWebRequest() {
        RequestContextHolder.currentRequestAttributes()
    }

    GrailsParameterMap getParams () {
        webRequest.params
    }

    FlashScope getFlash () {
        webRequest.flashScope
    }

    GrailsHttpSession getSession () {
        webRequest.session
    }

    HttpServletRequest getRequest () {
        webRequest.currentRequest
    }

    ServletContext getServletContext () {
        webRequest.servletContext
    }

    HttpServletResponse getResponse () {
        webRequest.currentResponse
    }

    GrailsApplicationAttributes getGrailsAttributes () {
        webRequest.attributes
    }

    GrailsApplication getGrailsApplication () {
        webRequest.attributes.grailsApplication
    }

    String getActionName () {
        webRequest.actionName
    }

    String getControllerName () {
        webRequest.controllerName
    }
}
