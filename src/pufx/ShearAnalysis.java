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
public class ShearAnalysis {
    private LV PU;
    private double totalTime,totalTimeFDM,f;
    private double lamb,rho,ce;
    //load
    private double sigma0;
    //BC
    private double T0;
    
    //DATA SOLVER
    private double totalLength = 0.1;
    private int numelem = 20;
    private double he = totalLength/numelem;
    private double dTime = 60;
    private double C1,C2,C3;
    
    //OUTPUT
    private ArrayList<Double> L;
    private ArrayList<Double> T = new ArrayList<Double>();
    
    public ShearAnalysis(){
        
        this(new LV(),60,0.05,0.214,1150,2200,150000,23);
    }
    public ShearAnalysis(LV PU,double totalTime,double f,double lamb,double rho,double ce,double sigma0,double T0){
        this.PU = PU;
        
        this.totalTime = totalTime;  //horas
        this.f = f; //Hz
        
        this.lamb = lamb; //conductivity
        this.rho = rho; //density
        this.ce = ce; //specific heat
        
        this.sigma0 = sigma0; //
        
        this.T0 = T0; //BC at free end
        
        //internal variables
        this.C1 = this.lamb*this.dTime/(this.rho*this.ce*Math.pow(this.he, 2));
        this.C2 = this.dTime/(this.rho*this.ce);
        this.C3 = this.C2*2*Math.PI*this.f*Math.pow(this.sigma0, 2);
        
        this.totalTimeFDM = this.totalTime*3600/this.dTime;        
    }
    
    
    
    public void initL(){
        this.L = LV.linspace(0, this.totalLength, this.numelem+1);
        System.out.println("Bar:");
        System.out.println(this.L);
    }
    public ArrayList<Double> getL(){
        return this.L;
    }
    
    public void initT(){
        for(int i=0;i<this.L.size();i++){
            this.T.add(this.T0);
        }
        System.out.println("Initial temperature:");
        System.out.println(this.T);
    }
    public ArrayList<Double> getT(){
        return this.T;
    }
    public double fx(double T){
        double w = 2*Math.PI*this.f;
        return 0.5*w*Math.pow(this.sigma0, 2)*PU.getJpp(this.f, T)/this.lamb;
    }
    
    public ArrayList<Double> explicitSolver(){
        System.out.println("");
        System.out.println("Uncouble thermal simulation");
        System.out.println("---------------------------");
        this.initL();
        this.initT();
        for(int i=0;i<(int)this.totalTimeFDM;i++){
            for(int j=1;j<this.T.size()-1;j++){
                this.T.set(j,this.C1*(this.T.get(j+1)-2*this.T.get(j)+this.T.get(j-1))+
                        this.C3*PU.getJpp(f,this.T.get(j))+this.T.get(j));               
            }
            this.T.set(0, this.C1*(this.T.get(1)-2*this.T.get(0)+this.T.get(1))+
                        this.C3*PU.getJpp(f,this.T.get(0))+this.T.get(0));
        }
        System.out.println("Temperature Field along the bar after "+this.totalTime+" hours.");
        return this.T;
    }
    
    
}
