/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * UIMain.java
 *
 * Created on 25-Aug-2011, 22:05:32
 */

package rcm.main;


import helper.gui.RcmExtendedFrame;
import helper.gui.RcmFrame;
import rcm.phase1._4_UIStyleSelector_V3;
import rcm.phase2._1_EntityAnnotator_V3;
import rcm.phase2._2_EntityReader_V3;
import rcm.phase1._1_FeatureAnnotator;
import rcm.phase1._2_FeatureReader;
import rcm.phase1._3_StructurePredictor;
import java.awt.event.ItemEvent;
import zzz_unused.zzz_Mapping_V2;
import rcm.phase2._3_SemReg_V3_Populator;
import zzz_unused.OntoReg_V2_Populator;
import zzz_unused.TaskAnnotator;
import rcm.phase34.ui._4_UIMappingSelector;

/**
 *
 * @author Krish
 */
public class UIMain extends RcmExtendedFrame {
    String styleText = "";
    String ontologyText = "";
    String mappingText = "";

    /** Creates new form UIMain */
    public UIMain() {
        super();
        initComponents();
        init();
        setVisible(true);
        pack();
    }

    private void init(){
        getLblTitle().setText(title);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        cbFeatureAnnotator = new javax.swing.JCheckBox();
        cbFeatureReader = new javax.swing.JCheckBox();
        cbStructurePredictor = new javax.swing.JCheckBox();
        cbStyleSelector = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        taStyle = new javax.swing.JTextArea();
        btnStyleProcessing = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        cbEntityAnnotator = new javax.swing.JCheckBox();
        cbEntityExtractor = new javax.swing.JCheckBox();
        cbSemregPopulator = new javax.swing.JCheckBox();
        cbOntoregPopulator = new javax.swing.JCheckBox();
        jScrollPane4 = new javax.swing.JScrollPane();
        taOntology = new javax.swing.JTextArea();
        btnOntologyPopulation = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        cbMapping = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        taMapping = new javax.swing.JTextArea();
        btnMapping = new javax.swing.JButton();
        cbMappingAlgorithm = new javax.swing.JCheckBox();
        cbOther1 = new javax.swing.JCheckBox();
        cbOther2 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 255, 204));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Document Structure Indentification", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Georgia", 1, 18), java.awt.Color.gray)); // NOI18N

        cbFeatureAnnotator.setBackground(new java.awt.Color(0, 204, 204));
        cbFeatureAnnotator.setFont(new java.awt.Font("Tahoma", 0, 18));
        cbFeatureAnnotator.setLabel("Feature Annotator");
        cbFeatureAnnotator.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbFeatureAnnotatorItemStateChanged(evt);
            }
        });
        cbFeatureAnnotator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbFeatureAnnotatorActionPerformed(evt);
            }
        });

        cbFeatureReader.setBackground(new java.awt.Color(0, 204, 204));
        cbFeatureReader.setFont(new java.awt.Font("Tahoma", 0, 18));
        cbFeatureReader.setText("Feature Reader");
        cbFeatureReader.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbFeatureReaderItemStateChanged(evt);
            }
        });

        cbStructurePredictor.setBackground(new java.awt.Color(0, 204, 204));
        cbStructurePredictor.setFont(new java.awt.Font("Tahoma", 0, 18));
        cbStructurePredictor.setLabel("Structure Predictor");
        cbStructurePredictor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbStructurePredictorItemStateChanged(evt);
            }
        });

        cbStyleSelector.setBackground(new java.awt.Color(0, 204, 204));
        cbStyleSelector.setFont(new java.awt.Font("Tahoma", 0, 18));
        cbStyleSelector.setLabel("Style Selector (GUI)");
        cbStyleSelector.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbStyleSelectorItemStateChanged(evt);
            }
        });

        taStyle.setColumns(20);
        taStyle.setRows(5);
        jScrollPane1.setViewportView(taStyle);

        btnStyleProcessing.setText("Process  Style Processing Tasks");
        btnStyleProcessing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStyleProcessingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(cbFeatureReader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbFeatureAnnotator, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                    .addComponent(cbStructurePredictor, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbStyleSelector, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnStyleProcessing, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnStyleProcessing)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(cbFeatureAnnotator)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbFeatureReader)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbStructurePredictor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbStyleSelector)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnStyleProcessing.getAccessibleContext().setAccessibleName("Process");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Entity Annotation and Ontology Population", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Georgia", 1, 18), java.awt.Color.gray)); // NOI18N

        cbEntityAnnotator.setBackground(new java.awt.Color(0, 204, 204));
        cbEntityAnnotator.setFont(new java.awt.Font("Tahoma", 0, 18));
        cbEntityAnnotator.setText("Entity Annotator");
        cbEntityAnnotator.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbEntityAnnotatorItemStateChanged(evt);
            }
        });

        cbEntityExtractor.setBackground(new java.awt.Color(0, 204, 204));
        cbEntityExtractor.setFont(new java.awt.Font("Tahoma", 0, 18));
        cbEntityExtractor.setText("Entity Reader");
        cbEntityExtractor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbEntityReaderItemStateChanged(evt);
            }
        });

        cbSemregPopulator.setBackground(new java.awt.Color(0, 204, 204));
        cbSemregPopulator.setFont(new java.awt.Font("Tahoma", 0, 18));
        cbSemregPopulator.setText("SemReg Ontology Populator");
        cbSemregPopulator.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbSemregPopulatorItemStateChanged(evt);
            }
        });

        cbOntoregPopulator.setBackground(new java.awt.Color(0, 204, 204));
        cbOntoregPopulator.setFont(new java.awt.Font("Tahoma", 0, 18));
        cbOntoregPopulator.setText("OntoReg Ontology Populator ");
        cbOntoregPopulator.setEnabled(false);
        cbOntoregPopulator.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbOntoregPopulatorItemStateChanged(evt);
            }
        });

        taOntology.setColumns(20);
        taOntology.setRows(5);
        jScrollPane4.setViewportView(taOntology);

        btnOntologyPopulation.setText("Process  Ontology Population Tasks");
        btnOntologyPopulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOntologyPopulationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(cbSemregPopulator, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbEntityExtractor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbEntityAnnotator, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                    .addComponent(cbOntoregPopulator, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnOntologyPopulation, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(cbEntityAnnotator)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbEntityExtractor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbSemregPopulator)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbOntoregPopulator))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnOntologyPopulation)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Mapping (Regulation vs Validation Task)", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Georgia", 1, 18), java.awt.Color.gray)); // NOI18N

        cbMapping.setBackground(new java.awt.Color(0, 204, 204));
        cbMapping.setFont(new java.awt.Font("Tahoma", 0, 18));
        cbMapping.setText("Mapping Selector");
        cbMapping.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbMappingItemStateChanged(evt);
            }
        });

        taMapping.setColumns(20);
        taMapping.setRows(5);
        jScrollPane2.setViewportView(taMapping);

        btnMapping.setText("Process All Mapping Tasks");
        btnMapping.setActionCommand("Process Mapping Tasks");
        btnMapping.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMappingActionPerformed(evt);
            }
        });

        cbMappingAlgorithm.setBackground(new java.awt.Color(0, 204, 204));
        cbMappingAlgorithm.setFont(new java.awt.Font("Tahoma", 0, 18));
        cbMappingAlgorithm.setText("Mapping Algorithm");
        cbMappingAlgorithm.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbMappingAlgorithmItemStateChanged(evt);
            }
        });

        cbOther1.setBackground(new java.awt.Color(0, 204, 204));
        cbOther1.setFont(new java.awt.Font("Tahoma", 0, 18));
        cbOther1.setText("Other 1 :");
        cbOther1.setEnabled(false);
        cbOther1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbOther1ItemStateChanged(evt);
            }
        });

        cbOther2.setBackground(new java.awt.Color(0, 204, 204));
        cbOther2.setFont(new java.awt.Font("Tahoma", 0, 18));
        cbOther2.setText("Other 2 : ");
        cbOther2.setEnabled(false);
        cbOther2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbOther2ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(cbMapping, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbMappingAlgorithm, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(cbOther1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbOther2, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                    .addComponent(btnMapping, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(cbMappingAlgorithm)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbMapping)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbOther1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbOther2))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnMapping)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(99, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbFeatureAnnotatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFeatureAnnotatorActionPerformed

        
    }//GEN-LAST:event_cbFeatureAnnotatorActionPerformed

    private void btnStyleProcessingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStyleProcessingActionPerformed
        styleText = "";
        if (featureAnnotator){
            new _1_FeatureAnnotator();
            styleText += "... feature annotation COMPLETED !!! \n";
            taStyle.setText(styleText);
        }
        if (featureReader){
            new _2_FeatureReader();
            styleText += "... feature reading COMPLETED !!! \n";
            taStyle.setText(styleText);
        }
        if (structurePredictor){
            new _3_StructurePredictor();
            styleText += "... structure prediction COMPLETED !!! \n";
            taStyle.setText(styleText);
        }
        if (styleSelector){
            new _4_UIStyleSelector_V3();
            styleText += "... style selection COMPLETED !!! \n";
            taStyle.setText(styleText);
        }
        
        
    }//GEN-LAST:event_btnStyleProcessingActionPerformed

    private void btnOntologyPopulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOntologyPopulationActionPerformed
        ontologyText = "";
        if (entityAnnotator){
            new _1_EntityAnnotator_V3();
            ontologyText += "... entity annotation COMPLETED !!! \n";
            taOntology.setText(ontologyText);
        }
        if (entityReader){
            new _2_EntityReader_V3();
            ontologyText += "... entity reading COMPLETED !!! \n";
            taOntology.setText(ontologyText);
        }
        if (semregPopulator){
            new _3_SemReg_V3_Populator();
            ontologyText += "... SemReg ontology population COMPLETED !!! \n";
            taOntology.setText(ontologyText);
        }
        if (ontoregPopulator){
            //new OntoReg_V2_Populator();
            ontologyText += "... OntoReg ontology population COMPLETED !!! \n";
            taOntology.setText(ontologyText);
        }
    }//GEN-LAST:event_btnOntologyPopulationActionPerformed

    private void btnMappingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMappingActionPerformed
        mappingText = "";
        if (mappingAlgorithm){
           // new _1_MappingAlgorithm();
            mappingText += "... mapping algorithm COMPLETED !!! \n";
            taMapping.setText(styleText);
        }
        if (mapping){
            mappingText += "... mapping selector window STARTING ... \n";
            new _4_UIMappingSelector();
            mappingText += "... mapping  COMPLETED !!! \n";
            taMapping.setText(mappingText);
        }
        if (other1){

            mappingText += "... other1 COMPLETED !!! \n";
            taMapping.setText(mappingText);
        }
        if (other2){

            mappingText += "... other2 COMPLETED !!! \n";
            taMapping.setText(mappingText);
        }
    }//GEN-LAST:event_btnMappingActionPerformed

    private void cbFeatureAnnotatorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbFeatureAnnotatorItemStateChanged

        if (evt.getStateChange() == ItemEvent.SELECTED){
            styleText += "Feature annotation SELECTED \n";
            taStyle.setText(styleText);
            featureAnnotator = true;
        } else {
            styleText += "x... Feature annotation UNSELECTED \n";
            taStyle.setText(styleText);
            featureAnnotator = false;
        }
    }//GEN-LAST:event_cbFeatureAnnotatorItemStateChanged

    private void cbFeatureReaderItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbFeatureReaderItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED){
            styleText += "Feature reader SELECTED \n";
            taStyle.setText(styleText);
            featureReader = true;
        } else {
            styleText += "x... Feature reader UNSELECTED \n";
            taStyle.setText(styleText);
            featureReader = false;
        }
    }//GEN-LAST:event_cbFeatureReaderItemStateChanged

    private void cbStructurePredictorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbStructurePredictorItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED){
            styleText += "Structure predictor SELECTED \n";
            taStyle.setText(styleText);
            structurePredictor = true;
        } else {
            styleText += "x... Structure predictor UNSELECTED \n";
            taStyle.setText(styleText);
            structurePredictor = false;
        }
    }//GEN-LAST:event_cbStructurePredictorItemStateChanged

    private void cbStyleSelectorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbStyleSelectorItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED){
            styleText += "Style selection SELECTED \n";
            taStyle.setText(styleText);
            styleSelector = true;
        } else {
            styleText += "x... Style selection UNSELECTED \n";
            taStyle.setText(styleText);
            styleSelector = false;
        }
    }//GEN-LAST:event_cbStyleSelectorItemStateChanged

    private void cbEntityAnnotatorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbEntityAnnotatorItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED){
            ontologyText += "Entity annotation SELECTED \n";
            taOntology.setText(ontologyText);
            entityAnnotator = true;
        } else {
            ontologyText += "x... entity annotation UNSELECTED \n";
            taOntology.setText(ontologyText);
            entityAnnotator = false;
        }
    }//GEN-LAST:event_cbEntityAnnotatorItemStateChanged

    private void cbEntityReaderItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbEntityReaderItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED){
            ontologyText += "Entity reading SELECTED \n";
            taOntology.setText(ontologyText);
            entityReader = true;
        } else {
            ontologyText += "x... entity reading UNSELECTED \n";
            taOntology.setText(ontologyText);
            entityReader = false;
        }
    }//GEN-LAST:event_cbEntityReaderItemStateChanged

    private void cbSemregPopulatorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbSemregPopulatorItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED){
            ontologyText += "SemReg ontology populator SELECTED \n";
            taOntology.setText(ontologyText);
            semregPopulator = true;
        } else {
            ontologyText += "x... SemReg ontology populator UNSELECTED \n";
            taOntology.setText(ontologyText);
            semregPopulator = false;
        }
    }//GEN-LAST:event_cbSemregPopulatorItemStateChanged

    private void cbOntoregPopulatorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbOntoregPopulatorItemStateChanged
         if (evt.getStateChange() == ItemEvent.SELECTED){
            ontologyText += "OntoReg ontology populator SELECTED \n";
            taOntology.setText(ontologyText);
            ontoregPopulator = true;
        } else {
            ontologyText += "x... OntoReg ontology populator UNSELECTED \n";
            taOntology.setText(ontologyText);
            ontoregPopulator = false;
        }
    }//GEN-LAST:event_cbOntoregPopulatorItemStateChanged

    private void cbMappingAlgorithmItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbMappingAlgorithmItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED){
            mappingText += "Mapping algorithm SELECTED \n";
            taMapping.setText(mappingText);
            mappingAlgorithm = true;
        } else {
            mappingText += "x... mapping algorithm UNSELECTED \n";
            taMapping.setText(mappingText);
            mappingAlgorithm = false;
        }
    }//GEN-LAST:event_cbMappingAlgorithmItemStateChanged

    private void cbMappingItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbMappingItemStateChanged
         if (evt.getStateChange() == ItemEvent.SELECTED){
            mappingText += "Mapping SELECTED \n";
            taMapping.setText(mappingText);
            mapping = true;
        } else {
            mappingText += "x... mapping UNSELECTED \n";
            taMapping.setText(mappingText);
            mapping = false;
        }
    }//GEN-LAST:event_cbMappingItemStateChanged

    private void cbOther1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbOther1ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED){
            mappingText += "Other1 SELECTED \n";
            taMapping.setText(mappingText);
            other1 = true;
        } else {
            mappingText += "x... other1 UNSELECTED \n";
            taMapping.setText(mappingText);
            other1 = false;
        }
    }//GEN-LAST:event_cbOther1ItemStateChanged

    private void cbOther2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbOther2ItemStateChanged
       if (evt.getStateChange() == ItemEvent.SELECTED){
            mappingText += "Other2 SELECTED \n";
            taMapping.setText(mappingText);
            other2 = true;
        } else {
            mappingText += "x... other2 UNSELECTED \n";
            taMapping.setText(mappingText);
            other2 = false;
        }
    }//GEN-LAST:event_cbOther2ItemStateChanged

    /**
    * @param args the command line arguments
    */
    /*
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UIMain().setVisible(true);
            }
        });
    }
     *
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMapping;
    private javax.swing.JButton btnOntologyPopulation;
    private javax.swing.JButton btnStyleProcessing;
    private javax.swing.JCheckBox cbEntityAnnotator;
    private javax.swing.JCheckBox cbEntityExtractor;
    private javax.swing.JCheckBox cbFeatureAnnotator;
    private javax.swing.JCheckBox cbFeatureReader;
    private javax.swing.JCheckBox cbMapping;
    private javax.swing.JCheckBox cbMappingAlgorithm;
    private javax.swing.JCheckBox cbOntoregPopulator;
    private javax.swing.JCheckBox cbOther1;
    private javax.swing.JCheckBox cbOther2;
    private javax.swing.JCheckBox cbSemregPopulator;
    private javax.swing.JCheckBox cbStructurePredictor;
    private javax.swing.JCheckBox cbStyleSelector;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea taMapping;
    private javax.swing.JTextArea taOntology;
    private javax.swing.JTextArea taStyle;
    // End of variables declaration//GEN-END:variables

    /* Style processing related */
    private boolean featureAnnotator = false;
    private boolean featureReader = false;
    private boolean structurePredictor = false;
    private boolean styleSelector = false;

    /*  Ontologies population related */
    private boolean entityAnnotator = false;
    private boolean entityReader = false;
    private boolean semregPopulator = false;
    private boolean ontoregPopulator = false;

    /*  Mapping and others related */
    private boolean mappingAlgorithm = false;
    private boolean mapping = false;
    private boolean other1= false;
    private boolean other2 = false;

    private String title = "RegCMantic Main Window";


}
