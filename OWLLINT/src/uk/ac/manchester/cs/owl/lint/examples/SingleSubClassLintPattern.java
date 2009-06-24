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
package uk.ac.manchester.cs.owl.lint.examples;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.protege.editor.owl.model.inference.NoOpReasoner;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.lint.InferenceLintPattern;
import org.semanticweb.owl.lint.LintException;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLObject;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

import uk.ac.manchester.cs.owl.lint.LintManagerFactory;
import uk.ac.manchester.cs.owl.lint.commons.OntologyWiseLintPattern;

/**
 * @author Luigi Iannone
 * 
 *         The University Of Manchester<br>
 *         Bio-Health Informatics Group<br>
 *         Feb 13, 2008
 */
public class SingleSubClassLintPattern extends OntologyWiseLintPattern {
	protected InferenceLintPattern inferenceLintPattern;

	public SingleSubClassLintPattern(OWLOntologyManager ontologyManager) {
		super(ontologyManager);
		try {
			this.inferenceLintPattern = LintManagerFactory.getLintManager(
					ontologyManager).getLintFactory()
					.createInferenceLintPattern();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the set of {@link OWLClass} that have just one <b>asserted</b>
	 *         subclass in the input {@link OWLOntology}
	 * @throws LintException
	 * @see org.semanticweb.owl.lint.LintPattern#matches(org.semanticweb.owl.model.OWLOntology)
	 */
	@Override
	public Set<OWLObject> matches(OWLOntology ontology) throws LintException {
		Set<OWLObject> toReturn = new HashSet<OWLObject>();
		Set<OWLClass> nothing = new HashSet<OWLClass>();
		try {
			OWLReasoner reasoner = this.inferenceLintPattern.getOWLReasoner();
			reasoner.loadOntologies(this.ontologyManager
					.getImportsClosure(ontology));
			if (!reasoner.isClassified()) {
				reasoner.classify();
			}
			nothing.add(this.ontologyManager.getOWLDataFactory()
					.getOWLNothing());
			nothing.addAll(reasoner.getEquivalentClasses(this.ontologyManager
					.getOWLDataFactory().getOWLNothing()));
			if (reasoner instanceof NoOpReasoner) {
				for (OWLClass cls : ontology.getReferencedClasses()) {
					Set<OWLDescription> subClasses = cls
							.getSubClasses(ontology);
					int count = 0;
					Iterator<OWLDescription> it = subClasses.iterator();
					while (count <= 1 && it.hasNext()) {
						OWLDescription anSubClassOWLDescription = it.next();
						if (!anSubClassOWLDescription.isAnonymous()) {
							count++;
						}
					}
					if (count == 1) {
						toReturn.add(cls);
					}
				}
			} else {
				// Subclasses are equivalence classes rather than OWLClass
				// therefore
				// they are presented in sets
				for (OWLClass cls : ontology.getReferencedClasses()) {
					Set<Set<OWLClass>> subClasses = reasoner.getSubClasses(cls);
					subClasses.remove(nothing);
					if (subClasses.size() == 1) {
						Set<OWLClass> subclassEquivalenceClass = subClasses
								.iterator().next();
						if (subclassEquivalenceClass.size() == 1) {
							toReturn.add(cls);
						}
					}
				}
			}
		} catch (LintException e) {
			Logger logger = Logger.getLogger(this.getClass().getName());
			logger
					.log(Level.WARNING,
							"Unable to create reasoner... only asserted taxonomy will be used");
			for (OWLClass cls : ontology.getReferencedClasses()) {
				if (cls.getSubClasses(ontology).size() == 1) {
					toReturn.add(cls);
				}
			}
		} catch (OWLReasonerException e) {
			throw new LintException(e);
		}
		return toReturn;
	}
}
