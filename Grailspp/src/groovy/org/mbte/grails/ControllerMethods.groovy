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

import org.codehaus.groovy.grails.web.metaclass.ForwardMethod
import org.codehaus.groovy.grails.web.metaclass.WithFormMethod
import org.codehaus.groovy.grails.web.metaclass.BindDynamicMethod
import org.codehaus.groovy.grails.web.metaclass.RenderDynamicMethod
import org.codehaus.groovy.grails.web.metaclass.RedirectDynamicMethod
import org.springframework.web.servlet.ModelAndView
import org.springframework.validation.Errors
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.codehaus.groovy.grails.web.metaclass.ChainMethod
import org.codehaus.groovy.grails.web.servlet.mvc.TokenResponseHandler
import org.codehaus.groovy.grails.web.mapping.UrlMappingsHolder

@Trait abstract class ControllerMethods implements CommonWebProperties, org.mbte.grails.RenderMethods, org.mbte.grails.BindMethods {
    String getActionUri() {
        "/$controllerName/$actionName"
    }

    String getControllerUri () {
        "/$controllerName"
    }

    String getTemplateUri (String name) {
        def wr = webRequest
        wr.attributes.getTemplateUri(name, wr.currentRequest)
    }

    String getViewUri(String name) {
        def wr = webRequest
        wr.attributes.getViewUri(name, wr.currentRequest)
    }

    void setErrors(Errors errors) {
        webRequest.setAttribute(GrailsApplicationAttributes.ERRORS, errors, 0)
    }

    Errors getErrors() {
        webRequest.getAttribute(GrailsApplicationAttributes.ERRORS, 0)
    }

    void setModelAndView(org.springframework.web.servlet.ModelAndView mav) {
        webRequest.setAttribute(GrailsApplicationAttributes.MODEL_AND_VIEW, mav, 0)
    }

    ModelAndView getModelAndView() {
        webRequest.getAttribute(GrailsApplicationAttributes.MODEL_AND_VIEW, 0)
    }

    Map getChainModel() {
        webRequest.flashScope["chainModel"]
    }

    boolean hasErrors() {
        errors?.hasErrors()
    }

    def chain(Map args) {
        ChainMethod.invoke this, args
    }

    // the withForm method
    static final WithFormMethod withFormMethod = []

    TokenResponseHandler withForm(Closure callable) {
        withFormMethod.withForm(request, callable)
    }

    static RedirectDynamicMethod redirect
    def redirect(Map args) {
        if(!redirect)
            redirect = [grailsApplication.mainContext]
        redirect.invoke(this, "redirect", args)
    }

    static ForwardMethod forwardMethod
    def forward(Map params) {
        if(!forwardMethod)
            forwardMethod = new ForwardMethod((UrlMappingsHolder)grailsApplication.parentContext.getBean("grailsUrlMappingsHolder"))
        forwardMethod.forward(request,response, params)
    }
}
