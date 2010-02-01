/**
 * Copyright (C) 2008, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.coode.oppl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.coode.oppl.variablemansyntax.Variable;
import org.semanticweb.owl.model.OWLObject;

/**
 * Constraint that verifies whether a variable values are contained in a
 * collection
 * 
 * @author Luigi Iannone
 * 
 */
public class InCollectionRegExpConstraint<P extends OWLObject> implements
		AbstractConstraint {
	private final Variable variable;
	private final Map<P, List<String>> collection;
	private final String source;

	/**
	 * @param variable
	 * @param collection
	 * @param constraintSystem
	 */
	public InCollectionRegExpConstraint(String source, Variable variable,
			Map<P, List<String>> collection) {
		this.variable = variable;
		this.collection = collection;
		this.source = source;
	}

	/**
	 * Visitor pattern required method
	 * 
	 * @return the specific output of the visit (dependent on the implementation
	 *         of the visitor input instance)
	 * @see org.coode.oppl.AbstractConstraint#accept(org.coode.oppl.ConstraintVisitorEx)
	 */
	public <O> O accept(ConstraintVisitorEx<O> visitor) {
		return visitor.visit(this);
	}

	/**
	 * @return the variable
	 */
	public Variable getVariable() {
		return this.variable;
	}

	/**
	 * @return the collection
	 */
	public Collection<P> getCollection() {
		return this.collection.keySet();
	}

	@Override
	public int hashCode() {
		return 3 * this.variable.hashCode() * 5
				* this.collection.keySet().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		boolean toReturn = false;
		if (obj instanceof InCollectionRegExpConstraint<?>) {
			InCollectionRegExpConstraint<?> toCompare = (InCollectionRegExpConstraint<?>) obj;
			toReturn = this.getVariable().equals(toCompare.variable)
					&& this.getCollection().equals(toCompare.getCollection());
		}
		return toReturn;
	}

	@Override
	public String toString() {
		return "Match \"" + this.source + "\"";
		// StringBuffer buffer = new StringBuffer();
		// buffer.append(this.variable.getName());
		// buffer.append(" IN {");
		// boolean first = true;
		// String comma;
		// SimpleVariableShortFormProvider simpleVariableShortFormProvider = new
		// SimpleVariableShortFormProvider(
		// this.constraintSystem);
		// for (P p : this.collection) {
		// comma = !first ? ", " : "";
		// first = false;
		// buffer.append(comma);
		// if (p instanceof OWLEntity) {
		// buffer.append(simpleVariableShortFormProvider
		// .getShortForm((OWLEntity) p));
		// } else {
		// buffer.append(p.toString());
		// }
		// }
		// buffer.append('}');
		// return buffer.toString();
	}

	public String render() {
		return "Match \"" + this.source + "\"";
		// StringBuffer buffer = new StringBuffer();
		// buffer.append(this.variable.getName());
		// buffer.append(" IN {");
		// boolean first = true;
		// String comma;
		// for (P p : this.collection) {
		// comma = !first ? ", " : "";
		// first = false;
		// buffer.append(comma);
		// ManchesterSyntaxRenderer renderer = ParserFactory.getInstance()
		// .getOPPLFactory().getManchesterSyntaxRenderer(
		// this.constraintSystem);
		// p.accept(renderer);
		// buffer.append(renderer.toString());
		// }
		// buffer.append('}');
		// return buffer.toString();
	}

	public void accept(ConstraintVisitor visitor) {
		visitor.visitInCollectionConstraint(this);
	}
}
