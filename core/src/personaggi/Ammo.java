/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package personaggi;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.WoodenSlug;
import livelli.Gioco;

/**
 *
 * @author Giuseppe
 */
public class Ammo extends Sprite
{
    public  World mondo;
    public  Body b2Corpo;
    
    private int munizioniRigenerate;
    
    private  boolean daEliminare;
    private  boolean eliminato;
    private TextureRegion Ammo;
    
    public Ammo(World mondo, Gioco gioco, float x)
    {
        //le texture sono tutte nel file personaggi.pack
        //contenute nel TextureAtlas di Gioco
        //ogni texture ha una propria regione con nome e indice
        super(gioco.atlas.findRegion("ammo", -1));
                
        this.mondo = mondo;
        
        daEliminare = false;
        eliminato = false;
        
        munizioniRigenerate = 25;
        
        //texture posizionata in base alle coordinate contenute nel protagonisti.pack
        Ammo = new TextureRegion(getTexture(), 693, 88, 50, 50);
        
        setBounds(0, 0, 50 / WoodenSlug.PPM, 50 /WoodenSlug.PPM);
        setRegion(Ammo);
        setSize(.32f, .32f);
        
        creaAmmo(x);
    }
    
    //parametro x per la posizione random del Ammo
    private void creaAmmo(float x)
    {
        //BodyDef, permette la creazione degli oggetti Body, contenuti nel mondo di gioco
        //il Body contiente tutte le proprietà fisiche: velocità, posizione, accelerazione etc...  
        BodyDef bDef = new BodyDef();
        
        bDef.position.set(x / WoodenSlug.PPM, 350f / WoodenSlug.PPM);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2Corpo = mondo.createBody(bDef);
        
        //FixtureDef, permette la creazione della Fixture, contenitore che contiene il body
        FixtureDef fDef = new FixtureDef();
        
        //CircleShape, specifica la forma da far assumere al body
        CircleShape shape = new CircleShape();   
        shape.setRadius(10 / WoodenSlug.PPM);
        
        fDef.shape = shape;

        //indice gruppo come per gli altri personaggi nel mondo
        fDef.filter.groupIndex = -8;
        
        b2Corpo.createFixture(fDef);
        
        //setUserData(), permette di riconoscere a quale classe appartiene il corpo creato
        b2Corpo.setUserData(this);    
        
    }

    public int getMunizioniRigenerate()
    {
        return munizioniRigenerate;
    }
    
    
    
    public boolean isDaEliminare()
    {
        return daEliminare;
    }

    public void setDaEliminare(boolean daEliminare)
    {
        this.daEliminare = daEliminare;
    }

    public boolean isEliminato()
    {
        return eliminato;
    }

    public void setEliminato(boolean eliminato)
    {
        this.eliminato = eliminato;
    }
    
    
    //l'update si limita ad aggiornare la texture della cassa di munizioni
    public void update(float delta)
    {
        setPosition(b2Corpo.getPosition().x - getWidth() / 2, b2Corpo.getPosition().y - getHeight() / 2);
    }
}
