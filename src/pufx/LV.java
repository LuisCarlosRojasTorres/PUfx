/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pufx;

import java.util.ArrayList;

/**
 *
 * @author Luis Carlos A. Rojas Torres <luiscarlos.bsf@oceanica.ufrj.br>
 */
public class LV {
    private double E0,Einf;
    private ArrayList<Double> arrayEi;
    private ArrayList<Double> arrayTaui;
    
    private double Tref,c1,c2;
    private double poisson;
    
    //CONSTRUCTORES
    public LV(){
        this.arrayEi = new ArrayList();
        this.arrayTaui = new ArrayList();
        
        this.E0 = 102.11;
        this.Einf = 38.0;
        this.arrayEi.add(20.14);
        this.arrayEi.add(14.3);
        this.arrayEi.add(9.97);
        this.arrayEi.add(7.25);
        this.arrayEi.add(5.15);
        this.arrayEi.add(3.4);
        this.arrayEi.add(2.1);
        this.arrayEi.add(1.8);
        
        this.arrayTaui.add(0.58);
        this.arrayTaui.add(3.13);
        this.arrayTaui.add(18.72);
        this.arrayTaui.add(125.41);
        this.arrayTaui.add(1042.0);
        this.arrayTaui.add(10942.0);
        this.arrayTaui.add(159569.0);
        this.arrayTaui.add(5215397.0);
        this.Tref = 23;
        this.c1 = 7.32;
        this.c2 = 93.62;
        this.poisson = 0.45;
    }
    
    public LV(double E0,ArrayList<Double> Ei,ArrayList<Double> Taui,double Tref,double c1,double c2){
        this.arrayEi = Ei;        
        this.arrayTaui = Taui;
        
        this.E0 = E0;
        this.Einf = E0;
        for(double i:Ei){
            this.Einf-=i;
        }
        
        this.Tref = Tref;
        this.c1 = c1;
        this.c2 = c2;
        
        this.poisson = 0.45;
        
    }
    public LV(LV dummy){
        this(dummy.getE0(),dummy.getArrayEi(),dummy.getArrayTaui(),dummy.getTref(),dummy.getc1(),dummy.getc2());
    }
    //FIN de CONSTRUCTORES
    //SETTERS
    public void setPoisson(double v){this.poisson = v;}
    public void setE0(double E0){this.E0 = E0;}
    public void setTref(double Tref){this.Tref = Tref;}
    public void setc1(double c1){this.c1 = c1;}
    public void setc2(double c2){this.c2 = c2;}
    public void setArrayEi(ArrayList<Double> arrayEi){
        if(!arrayEi.isEmpty()){
            this.arrayEi = arrayEi;
            this.Einf = this.E0;
            for(double i:arrayEi){
            this.Einf-=i;
        }
            
        }
    }
    public void setArrayTaui(ArrayList<Double> arrayTaui){
        if(!arrayTaui.isEmpty()){this.arrayTaui = arrayTaui;}
    }
    //getters
    public double getE0(){return this.E0;}
    public double getTref(){return this.Tref;}
    public double getc1(){return this.c1;}
    public double getc2(){return this.c2;}
    public ArrayList<Double> getArrayEi(){return this.arrayEi;}
    public ArrayList<Double> getArrayTaui(){return this.arrayTaui;}
    
    //Viscoelastic Properties
    public double getShiftFactor(double temperature){
        return Math.pow(10,(-this.c1*(temperature-this.Tref)/(this.c2+temperature-this.Tref)));
    }
    public double getStorageModulus(double frequency,double temperature){
        double Ep=0;
        double omega=2*Math.PI*frequency;
        double at = this.getShiftFactor(temperature);
        for(int i=0;i<this.arrayEi.size();i++){
            Ep+=arrayEi.get(i)*Math.pow((omega*at*arrayTaui.get(i)),2)/(1+Math.pow((omega*at*arrayTaui.get(i)),2));
        }
        return this.Einf+Ep;
    }
    public double getLossModulus(double frequency,double temperature){
        double Epp=0;
        double omega=2*Math.PI*frequency;
        double at = this.getShiftFactor(temperature);
        for(int i=0;i<this.arrayEi.size();i++){
            Epp+=arrayEi.get(i)*(omega*at*arrayTaui.get(i))/(1+Math.pow((omega*at*arrayTaui.get(i)),2));
        }
        return Epp;
    }
    
    public double getJpp(double frequency,double temperature){
        double Ep = this.getStorageModulus(frequency, temperature);
        double Epp = this.getLossModulus(frequency, temperature);
        double Gp = Ep/(2*(1+this.poisson));
        double Gpp = Epp/(2*(1+this.poisson));
        
        return Math.pow(10, -6)*Gpp/(Math.pow(Gp, 2)+Math.pow(Gpp, 2));
    }
    
    public ArrayList<Double> getEpVSFrec(ArrayList<Double> frequencies,double temperature){
        ArrayList<Double> result = new ArrayList();
        
        for(double i:frequencies){
            result.add(this.getStorageModulus(i, temperature));
        }        
        return result;
    }
    
    public ArrayList<Double> getEppVSFrec(ArrayList<Double> frequencies,double temperature){
        ArrayList<Double> result = new ArrayList();
        
        for(double i:frequencies){
            result.add(this.getLossModulus(i, temperature));
        }        
        return result;
    }
    
    public ArrayList<Double> getEpVSTemp(double frequency,ArrayList<Double> temperatures){
        ArrayList<Double> result = new ArrayList();
        
        for(double i:temperatures){
            result.add(this.getStorageModulus(frequency, i));
        }        
        return result;
    }
    
    public ArrayList<Double> getEppVSTemp(double frequency,ArrayList<Double> temperatures){
        ArrayList<Double> result = new ArrayList();
        
        for(double i:temperatures){
            result.add(this.getLossModulus(frequency, i));
        }        
        return result;
    }
    //Funciones Auxiliares
    public void printALL(){
        System.out.println("----------------New object created----------------");
        System.out.printf("E(0) = %.2f%n",this.E0);
        System.out.printf("Einf = %.2f%n",this.Einf);
        System.out.println("Array size: "+arrayEi.size());
        for(int i=0;i<this.arrayEi.size();i++){
            System.out.printf("E%d: %.2f   Tau%d: %.2f%n",i+1,this.arrayEi.get(i),i+1,this.arrayTaui.get(i));
        }
        System.out.println("Tref: "+this.Tref+" c1: "+this.c1+" c2: "+this.c2);
    }
    public static ArrayList<Double> linspace(double startV,double endV,int n){
        ArrayList<Double> result = new ArrayList();
        if(startV<endV && n>0){
            for(int i=0;i<n;i++){
                result.add(startV+i*(endV-startV)/(n-1));
            }
        }
        return result;
    }
    
}
