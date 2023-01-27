/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package personaggi;

import armi.Mitra;
import com.badlogic.gdx.math.MathUtils;

import livelli.Gioco;

/**
 *
 * @author Giuseppe
 */

/**
*   la classe GestioreGioco si occupa di gestire le ondate
*   generando solamente un tot di nemici a ogni ondata
*   e decidi quali nemici generare in ogni ondata
*   per ogni ondata c'e un massimo di 40 mostri nelle ondate
*   in cui vengono generate i mostri
*   mentre nelle ondate in cui vengono generati i cani solamente 3
*   per via della loro elevata velocità
*/ 
public class GestoreGioco
{
    
    //costanti per la creazione dei mostri, cani e medikit
    public static final int QUANTITA_MOSTRI_ONDATA = 40;
    public static final int MOSTRI_GENERATI_MAX = 10;
    public static final int CANI_GENERARE_MAX = 3;
    public static final int MEDIKIT_MAX = 1;
    
    
    
    private int mostriGenerati;
    private int mostriDaGenerare;
    private int caniGenerati;
    private int caniDaGenerare;
 
    
    private int ondate;
    public static int ondataAttuale;

    private float tempoTrascorsoOndata;

    
    private boolean fineOndata;
    private boolean fineGioco;
    private boolean ondataInCorso;


    private Gioco gioco;
    
    //parametro Gioco gioco, ovvero quale stato crea il GestoreGioco
    public GestoreGioco(Gioco gioco)
    {
        ondate = 6;
        ondataAttuale = 0;

        caniGenerati = 0;
        
        //cani che devono ancora essere generati
        caniDaGenerare = CANI_GENERARE_MAX;
        
        mostriGenerati = 0;
        
        //mostri che devono ancora essere generati
        mostriDaGenerare = QUANTITA_MOSTRI_ONDATA;

        fineOndata = false;
        fineGioco = false;
        ondataInCorso = false;

        tempoTrascorsoOndata = 0f;


        this.gioco = gioco;

    }

    //chiamato alla fine di ogni ondata
    private void reset()
    {
        caniGenerati = 0;
        caniDaGenerare = CANI_GENERARE_MAX;
        
        mostriGenerati = 0;
        mostriDaGenerare = QUANTITA_MOSTRI_ONDATA;
        
        tempoTrascorsoOndata = 0f;
    }
    
    public int getMostriDaGenerare()
    {
        return mostriDaGenerare;
    }

    public void setMostriGenerati(int mostriGenerati)
    {
        this.mostriGenerati = mostriGenerati;
    }

    public boolean isFineOndata()
    {
        return fineOndata;
    }

    public void setFineOndata(boolean fineOndata)
    {
        this.fineOndata = fineOndata;
    }

    public int getMostriGenerati()
    {
        return mostriGenerati;
    }

    public int getOndate()
    {
        return ondate;
    }

    public void setOndate(int ondate)
    {
        this.ondate = ondate;
    }

    public boolean isFineGioco()
    {
        return fineGioco;
    }

    
    //chiamato ogni frame
    public void update(float delta)
    {

            
        if (fineOndata || ondataAttuale == 0 && !ondataInCorso)
        {
            //se la condizione è vera inzia il timer per l'inizio dell'ondata
            tempoTrascorsoOndata += delta;          
        }

        if(tempoTrascorsoOndata >= 6f)
        {         
            //se il timer ha finito
            //comincia l'ondata e  i mostri venogno generati
            ondataInCorso = true;
            Gioco.generaMostri = true;           
        }
        
        if(ondataInCorso)
        {
            
            //generazione dei cani
            if(ondataAttuale % 2 == 0)
            {
                generaCani();
                caniGenerati = gioco.getCani().size;
            }
            else // generazione dei mostri
            {
                generaMostri();
                mostriGenerati = gioco.getMostri().size;
            }
            
            //se l'ondata è finita la variabile per fine ondata viene messa a true
            //e viene creato un medikit
            //una scatola di munizioni o un mitra
            //in un punto random della mappa
            if(mostriDaGenerare == 0 && mostriGenerati == 0 || caniDaGenerare == 0 && caniGenerati == 0)
            {
                if(gioco.getMedikit() == null)                         
                    gioco.setMedikit(new Medikit(gioco.getMondo(), gioco, posizionamento()));
                
                int x = MathUtils.random(0, 1);
                
                //scelta casuale per far creare il mitra o la scatola di munizioni
                switch(x)
                {
                    case 0:
                        if(gioco.getMunizioni() == null)
                            gioco.setMunizioni(new Ammo(gioco.getMondo(), gioco, posizionamento()));
                        break;
                        
                    case 1:
                        if(gioco.getMitra() == null)
                            gioco.setMitra(new MitraPickUp(gioco.getMondo(), gioco, posizionamento()));
                        break;
                }
              
                ondataInCorso = false;
                fineOndata = true;
                ondataAttuale++;
                
                //se l'ondata è maggiore del numero masimo di ondate
                //allora il gioco finisce ed è stato completato
                if(ondataAttuale > ondate)
                    fineGioco = true;
                
                reset();                               
            }
        }
       
    }

    private float posizionamento()
    {
        //restituisce un numero compreso tra i 2 parametri (estremi inclusi)
        return MathUtils.random(200f, 3600f);
    }

    private void generaMostri()
    {
       //viene aggiunto un mostro ogni volta che ne viene ucciso uno finche
       //non esaurisco e si passa alla prossima ondata
       while(gioco.getMostri().size < MOSTRI_GENERATI_MAX && mostriDaGenerare > 0)
       {     
           gioco.getMostri().add(new Mostro(gioco.getMondo(), gioco, posizionamento() ));
           mostriDaGenerare--;
       }
       
    }

    private void generaCani()
    {
       //tutti i cani vengono generati in un colpo solo
       while(gioco.getCani().size < CANI_GENERARE_MAX && caniDaGenerare > 0)
       {
           gioco.getCani().add(new Cane(gioco.getMondo(), gioco, posizionamento() ) );
           caniDaGenerare--;
       }
    }
}
