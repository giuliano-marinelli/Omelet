package omelet;

import java.io.File;
import java.util.Set;
import org.coode.owlapi.turtle.TurtleOntologyFormat;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class HermitDemo {

    public static void main(String[] args) throws Exception {
        OWLOntologyManager manager = new OWLManager().createOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();

        OWLOntology impOntology = manager.loadOntologyFromOntologyDocument(new File("src/files/ontologia-incons.owl"));

        String baseIRI = "localhost";
        OWLOntology ontology = manager.createOntology(IRI.create(baseIRI));

        OWLClass empresaClass = factory.getOWLClass(IRI.create(baseIRI + "#Empresa"));

        OWLAxiom empresaAxiom = factory.getOWLDeclarationAxiom(empresaClass);

        manager.addAxiom(ontology, empresaAxiom);
        File file = new File("src/files/result-ontology-2.owl");

        manager.setOntologyFormat(ontology, new TurtleOntologyFormat());
        manager.saveOntology(ontology, IRI.create(file));

        Reasoner hermit = new Reasoner(ontology);

        //System.out.println(hermit.isConsistent());
        Set<OWLClass> classes = hermit.getUnsatisfiableClasses().getEntities();
        for (OWLClass classe : classes) {
            if (!classe.getNNF().isOWLNothing()) {
                System.out.println(classe.getIRI().getFragment());
            }
        }
    }
}
