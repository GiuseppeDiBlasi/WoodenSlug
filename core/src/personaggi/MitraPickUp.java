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
public class MitraPickUp  extends Sprite
{
    public  World mondo;
    public  Body b2Corpo;
    
    private int munizioniRigenerate;
    
    private  boolean daEliminare;
    private  boolean eliminato;
    private TextureRegion mitra;
    
    public MitraPickUp(World mondo, Gioco gioco, float x)
    {
        //le texture sono tutte nel file personaggi.pack
        //contenute nel TextureAtlas di Gioco
        //ogni texture ha una propria regione con nome e indice
        super(gioco.atlas.findRegion("mitra", -1));
                
        this.mondo = mondo;
        
        daEliminare = false;
        eliminato = false;
        
        munizioniRigenerate = 40;
        
        //texture posizionata in base alle coordinate contenute nel protagonisti.pack
        mitra = new TextureRegion(getTexture(), 797, 155, 40, 40);
        
        setBounds(0, 0, 40 / WoodenSlug.PPM, 38 /WoodenSlug.PPM);
        setRegion(mitra);
        setSize(.32f, .32f);
        
        creaMitra(x);
    }
    
    //parametro x per la posizione random del mitra
    private void creaMitra(float x)
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

    public int getMunizioniRigenerate()
    {
        return munizioniRigenerate;
    }
    
    
    
    //l'update si limita ad aggiornare la texture del mitra
    public void update(float delta)
    {
        setPosition(b2Corpo.getPosition().x - getWidth() / 2, b2Corpo.getPosition().y - getHeight() / 2);
    }
}
