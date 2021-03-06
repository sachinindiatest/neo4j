/**
 * Copyright (c) 2002-2014 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.cypher.internal.compiler.v2_0.functions

import org.neo4j.cypher.internal.compiler.v2_0._
import org.neo4j.cypher.internal.compiler.v2_0.symbols._
import org.neo4j.cypher.internal.compiler.v2_0.commands.{expressions => commandexpressions}

case object Range extends Function {
  def name = "range"

  def semanticCheck(ctx: ast.Expression.SemanticContext, invocation: ast.FunctionInvocation) : SemanticCheck =
    checkMinArgs(invocation, 2) then checkMaxArgs(invocation, 3) then
    when(invocation.arguments.length >= 2) {
      invocation.arguments(0).constrainType(LongType()) then
      invocation.arguments(1).constrainType(LongType())
    } then when(invocation.arguments.length == 3) {
      invocation.arguments(2).constrainType(LongType())
    } then invocation.specifyType(CollectionType(LongType()))

  def toCommand(invocation: ast.FunctionInvocation) = {
    val commands = invocation.arguments.map(_.toCommand)
    commandexpressions.RangeFunction(commands(0), commands(1), commands.lift(2).getOrElse(commandexpressions.Literal(1)))
  }
}
