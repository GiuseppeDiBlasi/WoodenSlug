/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armi;

/**
 *
 * @author Giuseppe
 */
public abstract class Arma // classe astratta per le armi
{
    protected int munizioni; // variabile per le munizioni (non utilizzata)
    protected int danno; // variabile per il danno dell'arma
    protected float delayColpi;  // intervallo tra un colpo e l'altro
    protected String nome;
    
    protected Arma()
    {
        munizioni = 0;
        danno = 0;
        delayColpi = 0;
        nome = "";
    }

    //metodi astratti da implementare per le classi che estendono o implementano la classe 
    public abstract  int getMunizioni();
    public abstract  void setMunizioni(int munizioni);
    public abstract int getDanno();
    public abstract void setDanno(int danno);
    public abstract float getDelayColpi();
    public abstract void setDelayColpi(float delayColpi);
    public abstract String getNome();
    
    
    
}
