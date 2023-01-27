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
public class Mitra extends Arma
{
    
    public Mitra()
    {
        super();
        
        danno = 2;     
        delayColpi = 10;
        munizioni = 40;
        nome = "MITRA";
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
