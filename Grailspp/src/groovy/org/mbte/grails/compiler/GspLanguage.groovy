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
package org.mbte.grails.compiler

import org.codehaus.groovy.ast.ModuleNode
import org.mbte.groovypp.compiler.TypeUtil
import org.objectweb.asm.Opcodes
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.expr.DeclarationExpression
import org.codehaus.groovy.syntax.Types
import org.codehaus.groovy.syntax.Token
import org.codehaus.groovy.ast.ClassHelper
import org.mbte.grails.CommonWebProperties
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.expr.PropertyExpression
import org.codehaus.groovy.ast.expr.ClassExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.ClassCodeExpressionTransformer
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.classgen.Verifier
import org.mbte.groovypp.compiler.transformers.ConstructorCallExpressionTransformer
import org.codehaus.groovy.ast.expr.ConstructorCallExpression
import org.codehaus.groovy.ast.expr.BinaryExpression
import org.codehaus.groovy.ast.expr.ClosureExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.mbte.groovypp.compiler.transformers.MethodCallExpressionTransformer
import org.codehaus.groovy.ast.expr.ArgumentListExpression

@Typed class GspLanguage extends GrailsLanguage {

  GspLanguage() {
      super(GrailsScriptLanguageProvider.VIEWS_ANCHOR)
  }

  @Override
  void apply(ModuleNode moduleNode) {
      def clazz = moduleNode.classes[0]

      def run = clazz.getMethods("run")[0]
      BlockStatement bs = run.code

//    buildMeasurement(bs)
//    return

    bs.statements.add(0, new ExpressionStatement(
            new DeclarationExpression(
                    new VariableExpression("binding"),
                    Token.newSymbol(Types.EQUAL, -1, -1),
                    VariableExpression.THIS_EXPRESSION)))

      clazz.addInterface(ClassHelper.make(CommonWebProperties))

      AnnotationNode ann = [TypeUtil.TYPED]
//      ann.addMember "debug", new ConstantExpression(true)
      ann.addMember "value", new PropertyExpression(new ClassExpression(ClassHelper.make("TypePolicy")), "MIXED")
      clazz.addAnnotation(ann)

      ClassCodeExpressionTransformer bodyVarsEnhancer = new ClassCodeExpressionTransformer() {
          protected SourceUnit getSourceUnit() {
              return moduleNode.context;
          }

          public Expression transform(Expression exp) {
              if (exp instanceof BinaryExpression) {
                BinaryExpression bin = exp
                if(bin.operation.type == Types.EQUAL) {
                  if(bin.rightExpression instanceof ConstructorCallExpression) {
                    ConstructorCallExpression newCall = bin.rightExpression
                    if(newCall.type.name == 'GroovyPageTagBody') {
                      if(bin.leftExpression instanceof VariableExpression) {
                        VariableExpression ve = bin.leftExpression
                        if(ve.name.startsWith('body')) {
                          if(!clazz.getField(ve.name)) {
                            clazz.addProperty(ve.name, Opcodes.ACC_PUBLIC, ClassHelper.CLOSURE_TYPE, null, null, null);
                          }
                          return new BinaryExpression(ve, Token.newSymbol(Types.EQUAL, -1, -1), transform(bin.rightExpression))
                        }
                      }
                    }
                  }
                  else {
                    if(bin.rightExpression instanceof MethodCallExpression) {
                      MethodCallExpression mc = bin.rightExpression
                      if(mc.methodAsString == 'createClosureForHtmlPart') {
                        if(bin.leftExpression instanceof VariableExpression) {
                          VariableExpression ve = bin.leftExpression
                          if(ve.name.startsWith('body')) {
                            if(!clazz.getField(ve.name)) {
                              clazz.addProperty(ve.name, Opcodes.ACC_PUBLIC, ClassHelper.CLOSURE_TYPE, null, null, null);
                            }
                            return new BinaryExpression(ve, Token.newSymbol(Types.EQUAL, -1, -1), transform(bin.rightExpression))
                          }
                        }
                      }
                    }
                  }
                }
              }

              if(exp instanceof ClosureExpression) {
                ClosureExpression ce = exp
                ce.code.visit(this)
                return ce
              }

              return super.transform(exp)
          }
      }
      bodyVarsEnhancer.visitMethod(run)
  }

  private def buildMeasurement(BlockStatement bs) {
    bs.statements.add(0, new ExpressionStatement(
            new DeclarationExpression(
                    new VariableExpression("__start"),
                    Token.newSymbol(Types.EQUAL, -1, -1),
                    new MethodCallExpression(new ClassExpression(ClassHelper.make("System")), "currentTimeMillis", new ArgumentListExpression()))))

    bs.statements.add(new ExpressionStatement(
            new MethodCallExpression(
                    new PropertyExpression(new ClassExpression(ClassHelper.make("System")), "err"),
                    "println",
                    new ArgumentListExpression(
                            new BinaryExpression(
                                    new MethodCallExpression(
                                            new ClassExpression(ClassHelper.make("System")),
                                            "currentTimeMillis",
                                            new ArgumentListExpression()
                                    ),
                                    Token.newSymbol(Types.MINUS, -1, -1),
                                    new VariableExpression("__start")
                            )
                    )
            )
    ))
  }
}

