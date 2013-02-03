package rcm.phase4;

//for interacting with pellet
import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.PelletOptions;
import org.mindswap.pellet.owlapi.Reasoner;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLRuntimeException;

//standard Java imports
import java.util.Collections;
import java.util.Collection;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.*;
import java.net.URI;
import java.util.Iterator;

//for interacting with OWL
import edu.stanford.smi.protege.model.*;

//import edu.stanford.smi.protegex.*;
import edu.stanford.smi.protegex.owl.*;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.*;
import rcm.main.Settings;

/**
 * ***************************************************************************************************************************
 *  CONSISTENCY CHECKER:
 * ______________________________________________________________________________________
 *  This class is used to find out whether the supplied ontology is logically consistent. 
 *  In particular, a reasoner  (pellet in this case) is needed  in order to check if it is consistent.
 * ***************************************************************************************************************************
 */
public class Explanations {
	public JenaOWLModel entityModel;
	static int count=0;
	int flag=0;
	String ns; String propTable[][],restr[],disJ[];
	BufferedWriter writer;
        String ontologyName = Settings.ONTOLOGY_PATHNAME+Settings.ONTOREG_FILENAME;
        String outFilename = Settings.FILES_PATH + "consistency_result.txt";
        
        public Explanations(){
            File outputFile = new File(outFilename);
            Expl(ontologyName, outputFile);
        }

	//method to retieve logical inconsistencies using Pellet
	public boolean Expl (String ontoFileName, File outFile)
	{
		flag=0;
		String ontoFile=new File(ontoFileName).toURI().toString();
		propTable=new String [20][4];
		restr=new String[20];
		disJ=new String[20];
		count++;
                
		//creating Reasoner instance 		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		//OWLDataFactory factory = manager.getOWLDataFactory();
		Reasoner pellet= new Reasoner( manager );
		try {
			FileInputStream fin = new FileInputStream(ontoFileName); 
			entityModel = ProtegeOWL.createJenaOWLModelFromInputStream(fin);
			fin.close(); 
			ns=entityModel.getNamespaceManager().getDefaultNamespace();	
			writer=new BufferedWriter(new FileWriter(outFile));; 
						
			// read the ontology
			System.out.print( "Reading file " + ontoFile + "..." );
			OWLOntology ontology = manager.loadOntology( URI.create( ontoFile ) );
			System.out.println( "done." );
				
			// IMPORTANT: The option to enable tracing or for that matter, any of the options should be turned on/off before
			// the ontology is loaded to the reasoner!
			PelletOptions.DL_SAFE_RULES=false;
			PelletOptions.USE_TRACING = true;
	        
			// load the ontology
			pellet.loadOntology( ontology );
			// classify the ontology, ABOX inconsistencies are thrown as exceptions, any other inconsistencies have to be inferred using additional statements 
			pellet.getKB().classify();
			
			//this segment is reached only when there are no ABox inconsistencies since if there were the Exception would have transferred control to the Catch block
			writer.newLine();
			writer.write("Inconsistencies Repaired: "+ (this.count-1));
			writer.newLine();
			writer.write("Congratulations!! No inconsistencies found in loaded Ontology. You may proceed to view Validation Plan.");
			writer.newLine();
			writer.close();	
			flag=1;
			this.count=0; //so that when Diagnose or Show Next Inconsistency is clicked, the count starts afresh
		}

		catch(Exception err){
			//inconsistency message is parsed for better reading----SPECIFIC to reasoner being used--at the moment Pellet is the only one that can give an explanation!
			try{
				  //in case there is an exception in the catch block!
				String explSet=pellet.getKB().getExplanationSet().toString();
				explSet=explSet.replaceAll(ns, "");
				int j=0,i=0,k,l,m=0,n=0,d,e=0,f=0;
				k=explSet.indexOf("prop(",j); //instance property relations responsible for inconsistency are returns by pellet
				while(k!=-1)
				{
					//the prop(a,b,c) three tuple is split into three arrays	
					propTable[i][0]=explSet.substring(k+5, explSet.indexOf(",",k));
					propTable[i][1]=explSet.substring(explSet.indexOf(",",k)+1, explSet.indexOf(",",explSet.indexOf(",",k)+1));
					propTable[i][2]=explSet.substring(explSet.indexOf(",",explSet.indexOf(",",k)+1)+1, explSet.indexOf(")",explSet.indexOf(",",explSet.indexOf(",",k)+1)+1));
					propTable[i][3]="N";
					j=k+1;
					i++;
					k=explSet.indexOf("prop(",j);//looking for the next occurence of prop(), k=-1 if none is found 
				}
				
				l=explSet.indexOf("subClassOf(",m); //OWL axioms are returned as subClassOf()
				while(l!=-1)
				{
					if(explSet.indexOf(", ", l+11)!=-1) 	restr[n]=explSet.substring(l+11, explSet.indexOf(", ", l+11)-1);
					else restr[n]=explSet.substring(l+11, explSet.length()-1);				
					m=l+1;
					n++;
					l=explSet.indexOf("subClassOf(",m);
				}

				d=explSet.indexOf("disjointClasses([",e);
				while(d!=-1)
				{
					disJ[f]=explSet.substring(d+17, explSet.indexOf("])", d+17));
					e=d+1;
					f++;
					d=explSet.indexOf("disjointClasses([",e);
				}
				
				//writing a parsed error msg into text file
				int z=0,count=0,flag=0;
				if (i>0)
				{
					writer.write("Inconsistency No. "+ this.count);
					writer.newLine();
					writer.newLine();
					writer.write("**************************************************************************************************************************************************************************");
					writer.newLine();
					writer.write("The Instance chain shown below is causing non compliance with regulations! Fix to continue!");
					writer.newLine();
					writer.write(propTable[z][1]+"  "+propTable[z][0]+"  "+propTable[z][2]);
					propTable[z][3]="Y";
					count++;
					writer.newLine();
					
					/*to display the instance chains in right order
					 *i.e. if the chain is prop(isResponsibilityOf,Tank101CleaningTask,John)...prop(performs,John,Cleaning)  
					 * the first one is parsed and displayed, then the third column value is looked for in the second column values and the msg for the row that matches that criteria is displayed next
					 * this is iteratively done until all prop()'s have been covered
					 * To keep track of the row that has already been printed, a fourth column stores a 'Y' or 'N' signifying a yes or no for already printed 
					 */
					while(count<i)
					{
						flag=0;
						for (int v=0;v<i;v++)
						{
							if (propTable[v][1].equals(propTable[z][2])&&propTable[v][3].equals("N"))
							{
								//to avoid printing property and its inverse...avoids redundant information
								if(entityModel.getOWLProperty(propTable[v][0]).getInverseProperty().equalsStructurally(entityModel.getOWLProperty(propTable[z][0]))&&propTable[v][2].equals(propTable[z][1]))
								{
									flag=1;
									propTable[v][3]="Y";
									z=z+1;
									break;
								}
								writer.write(propTable[v][1]+"  "+propTable[v][0]+"  "+propTable[v][2]);
								writer.newLine();
								propTable[v][3]="Y";
								z=v;
								flag=1;
								break;
							}
								
						}
						//if no link exists, the next prop tuple is automatically displayed and its links are looked for next
						if(flag==0){
							writer.write(propTable[count][1]+"  "+propTable[count][0]+"  "+propTable[count][2]);
							writer.newLine();
							propTable[count][3]="Y";
							z=count;
						}					
						count++;
					}
				}
				//displaying axioms or disjoints
				if(n>0||f>0)
				{
					writer.write("***************************************************************************************************************************************************************************");
					writer.newLine();
					if(n>0)
					{
						writer.write("Constraint group being violated:");				
						writer.newLine();
					}
					for (int q=0;q<n;q++)
					{
						restr[q]=restr[q].replaceFirst(",", ": ");
						writer.write((q+1)+". "+restr[q]);
						writer.newLine();
					}
					if(f>0)
					{
						writer.write("Disjoints: ");
						writer.newLine();
					}
					for (int q=0;q<f;q++)
					{
						disJ[q]=disJ[q].replaceFirst(","," and ");
						writer.write((q+1)+". "+ disJ[q]);
						writer.newLine();
					}
					writer.write("***************************************************************************************************************************************************************************");
					writer.newLine();
					writer.write("Inconsistencies Repaired: "+ (this.count-1));
				}
				writer.close();	
			}
			catch(Exception e)
			{
				System.out.println("err:"+ e);	
			}
		}//end of outer catch
		if (flag==1)
		return true;//implies all inconsistencies have been repaired
		else return false;//implies all inconsistencies havent been repaired
	}
	

}
