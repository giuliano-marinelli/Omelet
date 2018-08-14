package omelet;

/* Copyright 2008, 2009, 2010 by the Oxford University Computing Laboratory

   This file is part of HermiT.

   HermiT is free software: you can redistribute it and/or modify
   it under the terms of the GNU Lesser General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   HermiT is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public License
   along with HermiT.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.File;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * This example demonstrates how HermiT can be used to check the consistency of
 * the Pizza ontology
 */
public class HermitDemo {

    public static void main(String[] args) throws Exception {
        // First, we create an OWLOntologyManager object. The manager will load and save ontologies.
        OWLOntologyManager m = OWLManager.createOWLOntologyManager();
        // We use the OWL API to load the Pizza ontology.
        OWLOntology o = m.loadOntologyFromOntologyDocument(new File("src/files/pizza.owl"));
        // Now, we instantiate HermiT by creating an instance of the Reasoner class in the package org.semanticweb.HermiT.
        Reasoner hermit = new Reasoner(o);
        // Finally, we output whether the ontology is consistent.
        System.out.println(hermit.isConsistent());
    }
}
