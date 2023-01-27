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
public class Pistola extends Arma // la classe Pistola estende la classe astratta Arma
{
    public String nome;
    
    public Pistola() //costruttore per l'arma, non viene chiamato super per via della diversita di valori
    {
        super();
        
        munizioni = -1;
        delayColpi = 18;
        danno = 1;
        nome = "PISTOLA";
    }
    
    @Override
    public int getMunizioni()
    {
        return munizioni;
    }

    @Override
    public void setMunizioni(int munizioni)
    {
      this.munizioni = munizioni;
    }

    @Override
    public int getDanno()
    {
       return danno;
    }

    @Override
    public void setDanno(int danno)
    {
        this.danno = danno;
    }

    @Override
    public float getDelayColpi()
    {
        return delayColpi;
    }

    @Override
    public void setDelayColpi(float delayColpi)
    {
        this.delayColpi = delayColpi;
    }

    @Override
    public String getNome()
    {
        return nome;
    }

    
    
}
