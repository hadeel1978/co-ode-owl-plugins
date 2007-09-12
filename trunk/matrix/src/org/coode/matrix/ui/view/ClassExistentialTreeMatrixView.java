package org.coode.matrix.ui.view;

import org.coode.matrix.model.api.AbstractTreeMatrixModel;
import org.coode.matrix.model.parser.OWLObjectListParser;
import org.coode.matrix.model.impl.ExistentialTreeMatrixModel;
import org.coode.matrix.ui.editor.OWLObjectListEditor;
import org.coode.matrix.ui.renderer.OWLObjectTreeTableCellRenderer;
import org.coode.matrix.ui.renderer.OWLObjectsRenderer;
import org.coode.matrix.ui.action.AddObjectPropertyAction;
import org.protege.editor.owl.model.hierarchy.OWLObjectHierarchyProvider;
import org.protege.editor.owl.ui.renderer.OWLCellRenderer;
import org.semanticweb.owl.model.*;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
 * Date: Jul 3, 2007<br><br>
 */
public class ClassExistentialTreeMatrixView extends AbstractTreeMatrixView<OWLClass> {

    private int threshold = 10;

    protected void initialiseMatrixView() throws Exception {
        addAction(new AddObjectPropertyAction(getOWLEditorKit(), getMatrixModel()), "A", "B");
    }

    protected OWLObjectHierarchyProvider<OWLClass> getHierarchyProvider() {
        return getOWLModelManager().getOWLClassHierarchyProvider();
    }

    protected AbstractTreeMatrixModel<OWLClass> createMatrixModel(OWLObjectTreeTableCellRenderer tree) {
        return new ExistentialTreeMatrixModel(tree, getOWLModelManager());
    }

    protected boolean isOWLClassView() {
        return true;
    }

    protected TableCellEditor getCellEditor(Object columnObject, OWLClass cls) {
        if (columnObject instanceof OWLObjectProperty){

            // make sure the editor only shows classes
            setEditorType(OWLObjectListParser.CLASS);

            OWLObjectProperty p = (OWLObjectProperty)columnObject;
            if (isFunctional(p)) {
                if (getMatrixModel().isValueRestricted(cls, p)) {
                    Set fillers = getMatrixModel().getSuggestedFillers(cls, p, threshold);
                    if (!fillers.isEmpty()) {
                        java.util.List values = new ArrayList(fillers);
                        values.add(0, ExistentialTreeMatrixModel.NONE);
                        JComboBox dropDown = new JComboBox(values.toArray());
                        Object value = getMatrixModel().getMatrixValue(cls, p);
                        if (((Set<OWLDescription>)value).size() == 1){
                            value = ((Set<OWLDescription>)value).iterator().next();
                            if (values.contains(value)){
                                dropDown.setSelectedItem(value);
                            }
                        }
                        dropDown.setRenderer(new OWLCellRenderer(getOWLEditorKit()));
                        return new DefaultCellEditor(dropDown);
                    }
                }
            }
        }
        else{
            setEditorType(OWLObjectListParser.DATATYPE);
        }
        return super.getCellEditor(columnObject, cls);
    }

    private boolean isFunctional(OWLObjectProperty p) {
        for (OWLOntology ont : getOWLModelManager().getActiveOntologies()){
            if (p.isFunctional(ont)){
                return true;
            }
        }
        return false;
    }
}