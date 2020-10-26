/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pufx;
import javafx.beans.property.SimpleStringProperty;
/**
 *
 * @author Luis Carlos A. Rojas Torres <luiscarlos.bsf@oceanica.ufrj.br>
 */
public class PUTable {
    private final SimpleStringProperty EiItem = new SimpleStringProperty("");
    private final SimpleStringProperty TauiItem = new SimpleStringProperty("");
    
    public PUTable(String Ei,String Taui){
        setEiItem(Ei);
        setTauiItem(Taui);
    }
    public PUTable(){
        this("","");
    }
    
    public String getEiItem(){
        return EiItem.get();
    }
    public void setEiItem(String Ei){
        this.EiItem.set(Ei);
    }
    
    public String getTauiItem(){
        return TauiItem.get();
    }
    public void setTauiItem(String Taui){
        this.TauiItem.set(Taui);
    }
    
}
