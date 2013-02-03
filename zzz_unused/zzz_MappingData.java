
package zzz_unused;

import java.util.ArrayList;
import helper.util.MyReader;
import helper.util.Print;
import helper.util.RegEx;
import rcm.main.Settings;

/**
 *
 * @author Krishna Sapkota, 15-Nov-2011,   15:01:43
 * A PhD project at Oxford Brookes University
 */
public class zzz_MappingData {

    protected String regulation;
    protected String validationTask;
    protected String regSubject;
    protected String taskSubject;
    protected String regAction;
    protected String taskAction;
    protected double subjectScore;
    protected double actionScore;
    protected String[] columnNames = getDataHeader();
    protected ArrayList<zzz_MappingData> mdList;
    protected String fileName = Settings.FILES_PATH+"mapping_table.txt";

    public zzz_MappingData() {
        super();
//        getDataHeader();
//        readDataFromFile(fileName);
    }

    public String[]  getDataHeader(){
        String [] columnNames = {"Regulation","ValidationTask","RegSubject",
                                "TaskSubject","subjectScore", "RegAction",
                                "TaskAction","actionScore","mapped","Detail"};
        return columnNames;
    }

    /**
     * Get the value of actionScore
     *
     * @return the value of actionScore
     */
    public double getActionScore() {
        return actionScore;
    }

    /**
     * Set the value of actionScore
     *
     * @param actionScore new value of actionScore
     */
    public void setActionScore(double actionScore) {
        this.actionScore = actionScore;
    }


    /**
     * Get the value of subjectScore
     *
     * @return the value of subjectScore
     */
    public double getSubjectScore() {
        return subjectScore;
    }

    /**
     * Set the value of subjectScore
     *
     * @param subjectScore new value of subjectScore
     */
    public void setSubjectScore(double subjectScore) {
        this.subjectScore = subjectScore;
    }


    /**
     * Get the value of taskAction
     *
     * @return the value of taskAction
     */
    public String getTaskAction() {
        return taskAction;
    }

    /**
     * Set the value of taskAction
     *
     * @param taskAction new value of taskAction
     */
    public void setTaskAction(String taskAction) {
        this.taskAction = taskAction;
    }


    /**
     * Get the value of regAction
     *
     * @return the value of regAction
     */
    public String getRegAction() {
        return regAction;
    }

    /**
     * Set the value of regAction
     *
     * @param regAction new value of regAction
     */
    public void setRegAction(String regAction) {
        this.regAction = regAction;
    }


    /**
     * Get the value of taskSubject
     *
     * @return the value of taskSubject
     */
    public String getTaskSubject() {
        return taskSubject;
    }

    /**
     * Set the value of taskSubject
     *
     * @param taskSubject new value of taskSubject
     */
    public void setTaskSubject(String taskSubject) {
        this.taskSubject = taskSubject;
    }


    /**
     * Get the value of regSubject
     *
     * @return the value of regSubject
     */
    public String getRegSubject() {
        return regSubject;
    }

    /**
     * Set the value of regSubject
     *
     * @param regSubject new value of regSubject
     */
    public void setRegSubject(String regSubject) {
        this.regSubject = regSubject;
    }


    /**
     * Get the value of validationTask
     *
     * @return the value of validationTask
     */
    public String getValidationTask() {
        return validationTask;
    }

    /**
     * Set the value of validationTask
     *
     * @param validationTask new value of validationTask
     */
    public void setValidationTask(String validationTask) {
        this.validationTask = validationTask;
    }


    /**
     * Get the value of regulation
     *
     * @return the value of regulation
     */
    public String getRegulation() {
        return regulation;
    }

    /**
     * Set the value of regulation
     *
     * @param regulation new value of regulation
     */
    public void setRegulation(String regulation) {
        this.regulation = regulation;
    }

    public ArrayList<zzz_MappingData> readDataFromFile(String fileName){
        ArrayList<String> dataList = MyReader.fileToArrayList(fileName);
        mdList = new ArrayList<zzz_MappingData>();
        
        for (String data: dataList){
            zzz_MappingData md = new zzz_MappingData();
            mdList.add(md);
            ArrayList<String> datumList =RegEx.getTokenizedList(data, ",");
            int i = 0 ;
            for (String datum: datumList){
                Print.pr(datum+"+ ");//++++++++++++++++++++++
                if (i==0){
                    md.setRegulation(datum);
                }else if (i==1){
                    md.setValidationTask(datum);
                }else if (i==2){
                    md.setRegSubject(datum);
                }else if (i==3){
                    md.setTaskSubject(datum);
                }else if (i==4){
                    md.setSubjectScore(Double.parseDouble(datum));
                }else if (i==5){
                    md.setRegAction(datum);
                }else if (i==6){
                    md.setTaskAction(datum);
                }else if (i==7){
                    md.setActionScore(Double.parseDouble(datum));
                }
                i++;

            }// for each datum
            Print.prln(";");//+++++++++++
        }// for each data
        return mdList;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public Object[][] getDataArrays(){
            readDataFromFile(fileName);
            Object [][] dataArray = new Object[mdList.size()][columnNames.length];
            for (zzz_MappingData md: mdList){
                int i = mdList.indexOf(md);
                dataArray[i][0]=md.getRegulation();
                dataArray[i][1]=md.getValidationTask();
                dataArray[i][2]=md.getRegSubject();
                dataArray[i][3]=md.getTaskSubject();
                dataArray[i][4]=md.getSubjectScore();
                dataArray[i][5]=md.getRegAction();
                dataArray[i][6]=md.getTaskAction();
                dataArray[i][7]=md.getActionScore();
                dataArray[i][8]= false;
                dataArray[i][9]="Detail";
            }
//            Print.printDoubleArray(dataArray);
            return dataArray;

    }
}
