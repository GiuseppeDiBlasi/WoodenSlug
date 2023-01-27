/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package livelli;

import armi.Arma;
import armi.Mitra;
import armi.Pistola;
import personaggi.Proiettile;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import personaggi.Ammo;
import personaggi.Cane;
import personaggi.Medikit;
import personaggi.MitraPickUp;
import personaggi.Mostro;
import personaggi.Protagonista;



/**
 *
 * @author Giuseppe
 */

/** 
 * 
 * la classe WorldContactListener si occupa di
 * trovare e identificare quali corpi hanno delle collisioni
 * e come gestirle a seconda dei corpi che vengono trovati
 * implementa la classe ContactListener della libreria di Box2D
 * 
 */
public class WorldContactListener implements ContactListener
{
    
    //metodo per gestire l'inizio di una collisione tra 2 Fixture
    //che contengono i corpi fisici presenti nel mondo fisico
    @Override
    public void beginContact(Contact contact)
    {
        //le Fixture ad avere una collisione saranno sempre 2 e mai maggiori
        
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        
        Body corpoA = fixA.getBody();
        Body corpoB = fixB.getBody();
        
        //si ottiene l'user data del body, che contiene il riferimento alla classe
        // alla quale appartiene, permettendo di riconoscere quale corpo
        //ha avuto una collisione con un altro corpo
        Object oggettoA = corpoA.getUserData();
        Object oggettoB = corpoB.getUserData();              
        
        proiettiliMondo(oggettoA, oggettoB);
        
        proiettileMostro(oggettoA, oggettoB);
        
        proiettileCane(oggettoA, oggettoB);
        
        protagonistaMedikit(oggettoA, oggettoB);
        
        protagonistaAmmo(oggettoA, oggettoB);
        
        protagonistaMitra(oggettoA, oggettoB);
    }

    
    //collisione tra il Protagonista e il Medikit
    private void protagonistaMedikit(Object oggettoA, Object oggettoB)
    {
        //se l'oggettoA e di classe Protagonista e oggettoB e di classe Medikit 
        //allora l'oggettoA e il protagonista mentre l'oggettoB e il Medikit
        //questo metodo sarà utilizzato anche per i Mostri e i Proiettili
        
        
        //quindi verrà poi eseguito il processo per gestire le 2 collisioni
        //in questo caso il Protagonista con la collisione con il Medikit
        //acquisice 3 vite
        //se il protagonista ha già 4 vite, ne verrà aggiunta solamente una
        if(oggettoA.getClass() == Protagonista.class && oggettoB.getClass() == Medikit.class)
        {
            if( ((Protagonista) oggettoA).getVite() > 4 )
                ((Protagonista) oggettoA).setVite( ((Protagonista) oggettoA).getVite() +  1);
            else
                ((Protagonista) oggettoA).setVite( ((Protagonista) oggettoA).getVite() + ((Medikit) oggettoB).getViteRigenerate());
            
            ((Medikit) oggettoB).setDaEliminare(true);
        }
        else if(oggettoA.getClass() == Medikit.class && oggettoB.getClass() == Protagonista.class)
        {
            ((Protagonista) oggettoB).setVite( ((Protagonista) oggettoB).getVite() + ((Medikit) oggettoA).getViteRigenerate());
            ((Medikit) oggettoA).setDaEliminare(true);
        }
    }
    
    private void proiettiliMondo(Object oggettoA, Object oggettoB)
    {
        //se i Proiettili colpiscono una Fixture di userData Gioco
        //allora hanno colpito il mondo di gioco(il terreno) e quindi devono essere eliminati
        if(oggettoA.getClass() == Gioco.class && oggettoB.getClass() == Proiettile.class)
        {
            ((Proiettile)oggettoB).impatto();
        }
        else if(oggettoA.getClass() == Proiettile.class && oggettoB.getClass() == Gioco.class)
        {
            ((Proiettile) oggettoA).impatto();
        }
        
    }
    
    private void proiettileMostro(Object oggettoA, Object oggettoB)
    {
        //collisione dei proiettili con i  nemici
        if(oggettoA.getClass() == Mostro.class && oggettoB.getClass() == Proiettile.class)
        {
            ( (Mostro) oggettoA).setVite( ( (Mostro)oggettoA).getVite() -
                                        ((Proiettile) oggettoB).getDanno());
            
            ( (Proiettile) oggettoB).impatto();
        }
        else if(oggettoA.getClass() == Proiettile.class && oggettoB.getClass() == Mostro.class)
        {
            
            ( (Mostro) oggettoB).setVite( ( (Mostro)oggettoB).getVite() - 
                                        ( (Proiettile) oggettoA).getDanno() );
            
            ( (Proiettile) oggettoA).impatto();
        }
            
    }
    
    //stesso principio del metodo per la collisione tra Proiettile e Mostro
    private void proiettileCane(Object oggettoA, Object oggettoB)
    {
        if(oggettoA.getClass() == Proiettile.class && oggettoB.getClass() == Cane.class)
        {
            ((Proiettile) oggettoA).impatto();
            ((Cane) oggettoB).setVite(((Cane) oggettoB).getVite() - ((Proiettile) oggettoA).getDanno());
        }
        else if (oggettoA.getClass() == Cane.class && oggettoB.getClass() == Proiettile.class)
        {
            ((Proiettile) oggettoB).impatto();
            ((Cane) oggettoA).setVite( ((Cane) oggettoA).getVite() - ((Proiettile) oggettoB).getDanno());
        }
    }
    
    
    //assegna delle munizioni aggiuntive a tutte le armi diverse dalla Pistola
    private void protagonistaAmmo(Object oggettoA, Object oggettoB)
    {
        if(oggettoA.getClass() == Protagonista.class && oggettoB.getClass() == Ammo.class)
        {
           Protagonista protagonista = ( (Protagonista) oggettoA);
           Ammo munizioni = ((Ammo) oggettoB);
           
           if(protagonista.getArmi().size > 1)
           {
               Array<Arma> armi = protagonista.getArmi();
               
               for(int i = 0; i < armi.size; i++)              
                   if(armi.get(i).getClass() != Pistola.class)                 
                       armi.get(i).setMunizioni(armi.get(i).getMunizioni() + munizioni.getMunizioniRigenerate() );
                                        
           }
           
           ((Ammo) oggettoB).setDaEliminare(true);
           
        }
        else if(oggettoA.getClass() == Ammo.class && oggettoB.getClass() == Protagonista.class)
        {
            Protagonista protagonista = ((Protagonista) oggettoB);
            Ammo munizioni = ((Ammo) oggettoA);
            
            if(protagonista.getArmi().size > 1)
            {
               Array<Arma> armi = protagonista.getArmi();
               
               for(int i = 0; i < armi.size; i++)              
                   if(armi.get(i).getClass() != Pistola.class)                 
                       armi.get(i).setMunizioni(munizioni.getMunizioniRigenerate() );
            }
            
            ((Ammo) oggettoA).setDaEliminare(true);
        }
    }
    
    //dà un mitra al protagonista, e se già ne ha uno
    //aumenta le sue munzioni
    private void protagonistaMitra(Object oggettoA, Object oggettoB)
    {
        if(oggettoA.getClass() == Protagonista.class && oggettoB.getClass() == MitraPickUp.class)
        {
            Protagonista protagonista = ((Protagonista) oggettoA);
            MitraPickUp mitra = ((MitraPickUp) oggettoB);
            
            Array<Arma> armi = protagonista.getArmi();
            
            boolean aggiunto = false;
            
            for(int i = 0; i < armi.size; i++)
            {
                if(armi.get(i).getClass() == Mitra.class)
                {               
                    armi.get(i).setMunizioni(armi.get(i).getMunizioni() + mitra.getMunizioniRigenerate());
                    aggiunto = true;
                    break;
                }                            
                
            }
            
            if(!aggiunto)
                armi.add(new Mitra());
            
            ((MitraPickUp) oggettoB).setDaEliminare(true);
        }
        else if(oggettoA.getClass() == MitraPickUp.class && oggettoB.getClass() == Protagonista.class)
        {
            Protagonista protagonista = ((Protagonista) oggettoB);
            MitraPickUp mitra = ((MitraPickUp) oggettoA);
            
             Array<Arma> armi = protagonista.getArmi();
            
            for(int i = 0; i < armi.size; i++)
            {
                if(armi.get(i).getClass() == Mitra.class)
                {
                    armi.get(i).setMunizioni(armi.get(i).getMunizioni() + mitra.getMunizioniRigenerate());
                }               
                else
                    armi.add(new Mitra());
            }
            
            ((MitraPickUp) oggettoA).setDaEliminare(true);
        }
                    
    }
    
    //gestire la collisione tra 2 Fixture dopo che la collisione è finita
    //(non utilizzata)
    @Override
    public void endContact(Contact cntct)
    {
        
    }

    //gestire la collisione tra 2 prima che finisca
    //(non utilizzata)
    @Override
    public void preSolve(Contact cntct, Manifold mnfld)
    {
    }

    //gestire la collisione tra 2 Fixture dopo è finita
    // e quali ripercussioni avrà per tutta l'esistenza della Fixture nel mondo di gioco
    //(non utilizzata)
    @Override
    public void postSolve(Contact cntct, ContactImpulse ci)
    {
        
    }
    
}
