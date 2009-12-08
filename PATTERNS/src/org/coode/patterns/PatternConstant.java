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
package org.coode.patterns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.coode.oppl.variablemansyntax.VariableType;
import org.coode.oppl.variablemansyntax.VariableTypeVisitorEx;
import org.coode.oppl.variablemansyntax.bindingtree.BindingNode;
import org.coode.oppl.variablemansyntax.generated.AbstractGeneratedVariable;
import org.coode.oppl.variablemansyntax.generated.GeneratedValue;
import org.coode.oppl.variablemansyntax.generated.GeneratedVariable;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLObject;

/**
 * @author Luigi Iannone
 * 
 *         Jun 24, 2008
 */
public class PatternConstant<P extends OWLEntity> extends
		AbstractGeneratedVariable<OWLObject> {
	private class EmptyConstantGeratedValue implements
			GeneratedValue<OWLObject> {
		private final OWLDataFactory dataFactory;

		/**
		 * @param dataFactory
		 */
		public EmptyConstantGeratedValue(OWLDataFactory dataFactory) {
			this.dataFactory = dataFactory;
		}

		public OWLObject getGeneratedValue(BindingNode node) {
			return PatternConstant.this.getType().buildOWLObject(
					this.dataFactory, PatternConstant.this.getURI(),
					PatternConstant.this.getName());
			// TODO subinterfaces of Variable need to be subinterfaces of
			// GeneratedVariable?
			// VariableType variableType = PatternConstant.this.getType();
			// switch (variableType) {
			// case CLASS:
			// toReturn = this.dataFactory
			// .getOWLClass(PatternConstant.this.getURI());
			// break;
			// case OBJECTPROPERTY:
			// toReturn = this.dataFactory
			// .getOWLObjectProperty(PatternConstant.this.getURI());
			// break;
			// case DATAPROPERTY:
			// toReturn = this.dataFactory
			// .getOWLDataProperty(PatternConstant.this.getURI());
			// break;
			// case INDIVIDUAL:
			// toReturn = this.dataFactory
			// .getOWLIndividual(PatternConstant.this.getURI());
			// break;
			// case CONSTANT:
			// toReturn = this.dataFactory
			// .getOWLUntypedConstant(PatternConstant.this
			// .getName());
			// break;
			// default:
			// break;
			// }
		}

		public List<OWLObject> computePossibleValues() {
			// return new ArrayList<OWLObject>(Collections.singleton(this
			// .getGeneratedValue(null)));
			return Collections.emptyList();
		}
	}

	private static class ConstantGeneratedValue<P extends OWLObject> implements
			GeneratedValue<OWLObject> {
		private final P constantValue;

		/**
		 * @param constantValue
		 */
		public ConstantGeneratedValue(P constantValue) {
			this.constantValue = constantValue;
		}

		public OWLObject getGeneratedValue(BindingNode node) {
			return this.constantValue;
		}

		public List<OWLObject> computePossibleValues() {
			return new ArrayList<OWLObject>(Collections
					.singleton(this.constantValue));
		}
	}

	public PatternConstant(String name, VariableType type,
			GeneratedValue<OWLObject> value) {
		super(name, type, value);
	}

	public PatternConstant(String name, VariableType type,
			OWLDataFactory dataFactory) {
		super(name, type, null);
		this.setValue(new EmptyConstantGeratedValue(dataFactory));
	}

	public static GeneratedValue<OWLObject> createConstantGeneratedValue(
			OWLObject owlObject) {
		return new ConstantGeneratedValue<OWLObject>(owlObject);
	}

	@Override
	protected OWLObject generateObject(OWLObject generatedValue) {
		return generatedValue;
	}

	@Override
	protected GeneratedVariable<OWLObject> replace(GeneratedValue<OWLObject> v) {
		return this;
	}

	public String getOPPLFunction() {
		return this.getValue().toString();
	}

	@Override
	public Set<OWLObject> getPossibleBindings() {
		return new HashSet<OWLObject>(this.getValue().computePossibleValues());
	}

	public <P> P accept(VariableTypeVisitorEx<P> visitor) {
		return visitor.visit(this);
	}
}