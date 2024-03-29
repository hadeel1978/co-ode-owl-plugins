package org.coode.cardinality.util;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;
/*
* Copyright (C) 2007, University of Manchester
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

/**
 * Author: Nick Drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Jul 25, 2007<br><br>
 */
public class MinCardinalityFinder extends OWLObjectVisitorAdapter {

    public static final int INFINITE = -1;

    private int min;

    public int getMin(OWLObject obj){
        min = INFINITE;
        obj.accept(this);
        return min;
    }

    public void visit(OWLObjectSomeValuesFrom owlObjectSomeRestriction) {
        min = 1;
    }

    public void visit(OWLObjectHasValue owlObjectValueRestriction) {
        min = 1;
    }

    public void visit(OWLObjectMinCardinality cardi) {
        min = cardi.getCardinality();
    }

    public void visit(OWLObjectExactCardinality cardi) {
        min = cardi.getCardinality();
    }

    public void visit(OWLDataSomeValuesFrom owlDataSomeRestriction) {
        min = 1;
    }

    public void visit(OWLDataHasValue owlDataValueRestriction) {
        min = 1;
    }

    public void visit(OWLDataMinCardinality cardi) {
        min = cardi.getCardinality();
    }

    public void visit(OWLDataExactCardinality cardi) {
        min = cardi.getCardinality();
    }
}
