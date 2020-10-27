/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pufx;

import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 *
 * @author Luis Carlos A. Rojas Torres <luiscarlos.bsf@oceanica.ufrj.br>
 */
public class FXMLPUfxController {
    @FXML private TableView<PUTable> tableView;
    @FXML private TextField txt_Ei;
    @FXML private TextField txt_Taui;
    
    @FXML private TextField txt_E0,txt_Tref,txt_c1,txt_c2;
    @FXML private TextField txt_startF,txt_endF,txt_nF,txt_temp;
    @FXML private TextField txt_startT,txt_endT,txt_nT,txt_freq;
    
    LV PU;
    //ArrayList utilizados para generar CHARTS
    ArrayList<Double> temperatures,frequencies;
    ArrayList<Double> EpVSTemp,EppVSTemp,EpVSFreq,EppVSFreq;
    //Axis
    //Series que contienen los datos
    XYChart.Series seriesEpVSTemp  = new XYChart.Series();
    XYChart.Series seriesEppVSTemp = new XYChart.Series();
    XYChart.Series seriesEpVSFreq  = new XYChart.Series();
    XYChart.Series seriesEppVSFreq  = new XYChart.Series();
    //Objetos chart
    @FXML private LineChart<Number,Number> chart_EpVSTemp;
    @FXML private LineChart<Number,Number> chart_EppVSTemp;
    @FXML private LineChart<Number,Number> chart_EpVSFreq;
    @FXML private LineChart<Number,Number> chart_EppVSFreq;
    
    double temp,freq;
    boolean firstTime = true;
        
    @FXML protected void add(ActionEvent event){
        ObservableList<PUTable> data = tableView.getItems();
        if(!txt_Ei.getText().equals("") && !txt_Taui.getText().equals("")){
            data.add(new PUTable(txt_Ei.getText(),txt_Taui.getText()));
        }
        txt_Ei.setText("");
        txt_Taui.setText("");
    }
    
    @FXML protected void clear(ActionEvent event){
        ObservableList<PUTable> data = tableView.getItems();
        data.clear();
    }
     
    @FXML protected void generate(ActionEvent event){
        
        PU = LVmaker();
        PU.printALL();
        EpppVSTempMaker();
        EpppVSFreqMaker();
        
        System.out.println("Storage and Loss Moduli VS Freq[Hz]");
        System.out.println("# of Frequencies: "+frequencies.size()+" at Temperature: "+temp);
        System.out.println("Storage and Loss Moduli VS Temp[°C]");
        System.out.println("# of Temperatures: "+temperatures.size()+" at Frequency; "+freq);
        seriesEpppVSTempMaker();
        seriesEpppVSFreqMaker();
        
        if(firstTime){
            chart_EpVSTemp.getData().addAll(seriesEpVSTemp);
            chart_EppVSTemp.getData().addAll(seriesEppVSTemp);
            chart_EpVSFreq.getData().add(seriesEpVSFreq);
            chart_EppVSFreq.getData().add(seriesEppVSFreq);
            firstTime = false;
        }
    }
    
    private LV LVmaker(){
        //Contruye el objeto PU apartir de la GUI si hay algun valor que no se definio
        // toma el de la tesis
        LV PU = new LV();
        double E0,Tref,c1,c2;
        
        if(!txt_E0.getText().equals("")){
        PU.setE0(Double.parseDouble(txt_E0.getText()));}
        
        if(!txt_Tref.getText().equals("")){
        PU.setTref(Double.parseDouble(txt_Tref.getText()));}
        
        if(!txt_c1.getText().equals("")){
        PU.setc1(Double.parseDouble(txt_c1.getText()));}
        
        if(!txt_c2.getText().equals("")){
        PU.setc2(Double.parseDouble(txt_c2.getText()));}
        
        //Los arrays de Ei y Tau son cargados
        ObservableList<PUTable> data = tableView.getItems();
        
        if(!data.isEmpty()){
            ArrayList<Double> arrayEi = new ArrayList();
            ArrayList<Double> arrayTaui = new ArrayList();
            for(int i=0;i<data.size();i++){
                arrayEi.add(Double.parseDouble(data.get(i).getEiItem()));
                arrayTaui.add(Double.parseDouble(data.get(i).getTauiItem()));
            }
            //System.out.println(arrayEi);
            //System.out.println(arrayTaui);
            PU.setArrayEi(arrayEi);
            PU.setArrayTaui(arrayTaui);
        }else{System.out.println("Empty Table-NO DATA");}
        
        
        return PU;
    }
       
    public void EpppVSTempMaker(){
        temperatures = LV.linspace(20,60,41);
        freq = 0.05;
        if(!txt_startT.getText().equals("") && !txt_endT.getText().equals("") && !txt_nT.getText().equals("") && !txt_freq.getText().equals("")){
            temperatures = 
                    LV.linspace(Double.parseDouble(txt_startT.getText()),
                            Double.parseDouble(txt_endT.getText()),
                            Integer.parseInt(txt_nT.getText()));
            freq = Double.parseDouble(txt_freq.getText());
        }
        
        EpVSTemp  = PU.getEpVSTemp(freq, temperatures);
        EppVSTemp = PU.getEppVSTemp(freq, temperatures);
    }
    
    public void EpppVSFreqMaker(){
        frequencies = LV.linspace(0.05,0.25,21);
        temp = 23;
        if(!txt_startF.getText().equals("") && !txt_endF.getText().equals("") && !txt_nF.getText().equals("") && !txt_temp.getText().equals("")){
            frequencies = 
                    LV.linspace(Double.parseDouble(txt_startF.getText()),
                            Double.parseDouble(txt_endF.getText()),
                            Integer.parseInt(txt_nF.getText()));
            temp = Double.parseDouble(txt_temp.getText());
        }
        
        EpVSFreq  = PU.getEpVSFrec(frequencies, temp);
        EppVSFreq = PU.getEppVSFrec(frequencies, temp);
    }
    
    public void seriesEpppVSTempMaker(){
                
        System.out.println("BEFORE:");
        System.out.println("size series: "+seriesEpVSTemp.getData().size()+"-"+seriesEppVSTemp.getData().size());
        //System.out.println("chart series: "+chart_EpVSTemp.getData().size()+"-"+chart_EppVSTemp.getData().size());
        
        
        seriesEpVSTemp.getData().clear();
        seriesEppVSTemp.getData().clear();
        
        for(int i=0;i<temperatures.size();i++){
            seriesEpVSTemp.getData().add(new XYChart.Data(temperatures.get(i),EpVSTemp.get(i)));
            seriesEppVSTemp.getData().add(new XYChart.Data(temperatures.get(i),EppVSTemp.get(i)));
        }
        seriesEpVSTemp.setName(Double.toString(freq)+" [Hz]");
        seriesEppVSTemp.setName(Double.toString(freq)+" [Hz]");
        
        
        System.out.println("AFTER:");
        System.out.println("size series: "+seriesEpVSTemp.getData().size()+"-"+seriesEppVSTemp.getData().size());
        //System.out.println("chart series: "+chart_EpVSTemp.getData().size()+"-"+chart_EppVSTemp.getData().size());
        System.out.println("");
        
    }
    
    public void seriesEpppVSFreqMaker(){
        seriesEpVSFreq.getData().clear();
        seriesEppVSFreq.getData().clear();
                
        for(int i=0;i<frequencies.size();i++){
            seriesEpVSFreq.getData().add(new XYChart.Data(frequencies.get(i),EpVSFreq.get(i)));
            seriesEppVSFreq.getData().add(new XYChart.Data(frequencies.get(i),EppVSFreq.get(i)));
        }
        seriesEpVSFreq.setName(Double.toString(temp)+" [°C]");
        seriesEppVSFreq.setName(Double.toString(temp)+" [°C]");
        
        
        
    }
    @FXML public void undo(ActionEvent event){
        if(!firstTime){
            ObservableList<PUTable> data = tableView.getItems();
            data.remove(data.size()-1);
            
        }
    }
    @FXML public void print(){
        if(!firstTime){
        System.out.println("PDF generated");
        }
    }
    @FXML public void exportCSV(){
        if(!firstTime){
        System.out.println("CSV file generated!");
        }
    }
}
