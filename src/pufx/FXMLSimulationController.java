/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pufx;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Luis Carlos A. Rojas Torres <luiscarlos.bsf@oceanica.ufrj.br>
 */
public class FXMLSimulationController implements Initializable {

    /**
     * Initializes the controller class.
     */
    ArrayList<Double> array_cs1 = new ArrayList<Double>();
    ArrayList<Double> array_cs2 = new ArrayList<Double>();
    XYChart.Series series_cs1  = new XYChart.Series();
    XYChart.Series series_cs2 = new XYChart.Series();
    @FXML private LineChart<Number,Number> chart_cs1;
    @FXML private LineChart<Number,Number> chart_cs2;
    
    @FXML private TextField txt_sigma0,txt_f,txt_totalTime,txt_T0;
    @FXML private TextField txt_lamb,txt_rho,txt_ce;
    private LV PU;
    private ShearAnalysis case1;
    boolean firstTime = true;
    
    @FXML private NumberAxis yCS1;
    
    public void initData(LV PU){
        this.PU = PU;
    }
    
    @FXML protected void RunCS1(ActionEvent event){
        System.out.println("Run CS1");
        double totalTime = 60;
        double f = 0.05;
        double lamb = 0.214;
        double rho = 1150;
        double ce = 2200;
        double sigma0 = 150000;
        double T0 = 23;
        
        if(!txt_totalTime.getText().equals("")){totalTime = Double.parseDouble(txt_totalTime.getText());}
        if(!txt_f.getText().equals("")){f = Double.parseDouble(txt_f.getText());}
        
        if(!txt_lamb.getText().equals("")){lamb = Double.parseDouble(txt_lamb.getText());}
        if(!txt_rho.getText().equals("")){rho = Double.parseDouble(txt_rho.getText());}
        if(!txt_ce.getText().equals("")){totalTime = Double.parseDouble(txt_ce.getText());}
        
        if(!txt_sigma0.getText().equals("")){sigma0 = Double.parseDouble(txt_sigma0.getText());}
        if(!txt_T0.getText().equals("")){T0 = Double.parseDouble(txt_T0.getText());}
            
        case1 = new ShearAnalysis(PU,totalTime,f,lamb,rho,ce,sigma0,T0);
        array_cs1 = case1.explicitSolver();
        System.out.println(array_cs1);
        
        setSeries_cs1();
        series_cs1.setName("T0: "+Double.toString(T0)+" [°C], f: "+f+" [Hz]");
        
        if(firstTime){
            chart_cs1.getData().addAll(series_cs1);
            firstTime = false;
        }
        
        yCS1.setAutoRanging(false);
        yCS1.setLowerBound(getMin(array_cs1));
        yCS1.setUpperBound(getMax(array_cs1));
        yCS1.setTickUnit((getMax(array_cs1)-getMin(array_cs1))/10);
        
    }
    public void setSeries_cs1(){
        series_cs1.getData().clear();
        for(int i=0;i<array_cs1.size();i++){
            series_cs1.getData().add(new XYChart.Data(case1.getL().get(i), case1.getT().get(i)));
        }
        
    }
    
    public static double getMax(ArrayList<Double> array){
        double max = 0;
        for(double i:array){
            if(i>max){max=i;}
        }
        return max;
    }
    public static double getMin(ArrayList<Double> array){
        double min = array.get(0);
        for(double i:array){
            if(i<min){min=i;}
        }
        return min;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
