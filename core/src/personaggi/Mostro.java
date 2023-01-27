/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package personaggi;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import com.mygdx.game.WoodenSlug;
import livelli.Gioco;


/**
 *
 * @author Giuseppe
 */
public class Mostro extends Sprite
{

    public World mondo;
    public Body b2Corpo;

    public static final float VELOCITA_MAX = 1f;

    public static final int SINISTRA = 0;
    public static final int DESTRA = 1;

    private boolean daEliminare;
    private boolean eliminato;

    private int vite;
    private final int punti;
    private int direzione;
    private int direzionePrecedente;

    //timer dello stato in cui il mostro si trova
    private float statoTimer;

    //animazione di movimento del mostro
    private Animation movimento;

    
    //World, mondo in cui l'oggetto esiste, Gioco ( stato) nel quale esiste
    //float x è utilizzato per il posizionamento casuale del mostro all'interno dell mappa
    public Mostro(World mondo, Gioco gioco, float x)
    { 
        //le texture sono tutte nel file personaggi.pack  
        //contenute nel TextureAtlas di Gioco
        //ogni texture ha una propria regione con nome e indice
        //se si apre il personaggi.pack con un editor di testo
        //si potrà leggere tutte le informazioni relative
        //a tutte le regioni presenti nel file
        super(gioco.atlas.findRegion("mostro_movimento", 0));

        this.mondo = mondo;

        daEliminare = false;
        eliminato = false;
       

        direzione = DESTRA;

        vite = 2;
        punti = 50;

        statoTimer = .0f;
        
        //limiti della texture del mostro
        setBounds(0, 0, 34, 48);
        
        //l'animazione è un insieme di texture che scorrono per 
        //dare l'impressione del movimento
        Array<TextureRegion> fotogrammi = new Array<TextureRegion>();

        //le texture per il mostro sono di nome mostro_movimento 
        //e sono 5, dall'indice 0 al 7
        for (int i = 0; i < 8; i++)
        {
            fotogrammi.add(new TextureRegion(gioco.atlas.findRegion("mostro_movimento", i)));
        }

        // il primo parametro indica la durata di una singola immagine
        //il secondo indica l'array di texture dal quale creare l'animazione
        movimento = new Animation(0.1f, fotogrammi);

        fotogrammi.clear();

        creaMostro(x);
        
        //la grandezza dello Sprite viene scalato in base ai PPM 
        this.setSize(32 / (WoodenSlug.PPM / 1.8f), 32 / (WoodenSlug.PPM / 1.8f));
    }

      public int getPunti()
    {
        return punti;
    }
       
    private void creaMostro(float x)
    {
        //BodyDef, permette la creazione degli oggetti Body, contenuti nel mondo di gioco
        //il Body contiente tutte le proprietà fisiche: velocità, posizione, accelerazione etc..
        BodyDef bDef = new BodyDef();
        
        bDef.position.set(x / WoodenSlug.PPM, 350 / WoodenSlug.PPM);
        
        //il Mostro è un corpo dinamico, ha collisioni e si puo muovere
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2Corpo = mondo.createBody(bDef);
        
        //FixtureDef, permette la creazione della Fixture, contenitore che contiene il body
        FixtureDef fDef = new FixtureDef();
        
        //CircleShape, specifica la forma da far assumere al body
        CircleShape shape = new CircleShape();   
        shape.setRadius(20 / WoodenSlug.PPM);
        
        fDef.shape = shape;

        //il Mostro avrà collisioni con tutti gli altri corpi di indice positivo
        //ma non avrà collisioni con altri cani o nemici
        fDef.filter.groupIndex = -8;
        
        b2Corpo.createFixture(fDef);   
        b2Corpo.setGravityScale(0.5f);
        
        //setUserData(), permette di riconoscere a quale classe appartiene il corpo creato
        b2Corpo.setUserData(this);
    }
    
    public void attacco(Protagonista protagonista)
    {
        //se il protagonista è compreso nel raggio d'azione del Mostro
        //il Mostro infliggerà danno al protagonista
        
        if(protagonista.b2Corpo.getPosition().x - b2Corpo.getPosition().x >= -.4f
            && protagonista.b2Corpo.getPosition().x - b2Corpo.getPosition().x <= .4f
            && protagonista.b2Corpo.getPosition().y - b2Corpo.getPosition().y >= -.2f
            && protagonista.b2Corpo.getPosition().y - b2Corpo.getPosition().y <= .2f)
            
            if(!protagonista.isColpito())
                protagonista.colpito();
        
    }
    
    public void movimento(float xProtagonista)
    {
        //spostamento in base alla posizione lungo l'asse delle  X del protagonista
        
        if(xProtagonista < b2Corpo.getPosition().x)
            spostaSinistra();
        else
             spostaDestra();
                
    }
    
   public void spostaSinistra()
    {  
        //viene applicata una forza lineare al corpo del Mostro
        
        if(b2Corpo.getLinearVelocity().x >= -VELOCITA_MAX)
            b2Corpo.applyLinearImpulse(new Vector2(-.5f, 0), b2Corpo.getWorldCenter(), true);
    }
    
    public void spostaDestra()
    {
       //viene applicata una forza lineare al corpo del Mostro

       if(b2Corpo.getLinearVelocity().x <= VELOCITA_MAX)
            b2Corpo.applyLinearImpulse(new Vector2(.5f, 0), b2Corpo.getWorldCenter(), true);
    }

    public boolean isEliminato()
    {
        return eliminato;
    }

    public void setEliminato(boolean eliminato)
    {
        this.eliminato = eliminato;
    }
    
    
    
    public boolean isDaEliminare()
    {
        return daEliminare;
    }

    public void setDaEliminare(boolean daEliminare)
    {
        this.daEliminare = daEliminare;
    }
        
    
    public void update (float delta, float xProtagonista, float yProtagonista, Protagonista protagonista)
    {
        //se la sua Region ( texture nell'atlas) e specchiata lungo l'asse delle X
        
        if(isFlipX())
            //la sua posizione viene modificata per poter stare all'interno del corpo fisico
            setPosition((b2Corpo.getPosition().x - getWidth() / 2)  , (b2Corpo.getPosition().y - getHeight() / 2) );
        
       
        else
            //la sua posizione viene modificata per poter stare all'interno del corpo fisico
            setPosition((b2Corpo.getPosition().x - getWidth() / 2)  , (b2Corpo.getPosition().y - getHeight() / 2));
        
        if(vite <= 0)
            daEliminare = true;  
        
        if(eliminato)
            dispose();
        
        //aggiornamento della direzione del Mostro
        if(b2Corpo.getLinearVelocity().x > 0)
            direzione = DESTRA;
        else if (b2Corpo.getLinearVelocity().x < 0)
            direzione  = SINISTRA;
        
        movimento(xProtagonista);
        attacco(protagonista);
        
        //setRegion, set della Region(texture nel TextureAtlas)
        //in base al frame dell'animazione
        setRegion(getFrame(delta));
        
    }

    private TextureRegion getFrame(float delta)
    {
        TextureRegion regione = null;
        
        
        //  la Region avra come texture il frame in cui lo stato cambia
        //  o continua a mantere lo stesso stato
        //  permettendo di continuare l'animazione
        switch(direzione)
        {
            case SINISTRA:
               
                regione = (TextureRegion) movimento.getKeyFrame(statoTimer, true);
                break;
            case DESTRA:
                regione = (TextureRegion) movimento.getKeyFrame(statoTimer, true);
                break;
        }
        
        //se la direzione è sinsitra e la regione (texture)
        //non è specchiata, allora verrà specchiata
        if(direzione == SINISTRA && !regione.isFlipX())
            regione.flip(true, false);
        else if(direzione == DESTRA && regione.isFlipX())
            regione.flip(true, false);
        
        //lo stato del timer sarà uguale a statoTimer += delta
        //se la condizione (direzione == direzionePrecedente) è vera 
        //altrimenti verrà impostato a 0 siccome ci si trova in una nuova direzione
        statoTimer = direzione == direzionePrecedente ? statoTimer += delta : 0;
        direzionePrecedente = direzione;
        
        return regione;
    }
    
    public int getVite()
    {
        return vite;
    }

    public void setVite(int vite)
    {
        this.vite = vite;
    }

    public int getDirezione()
    {
        return direzione;
    }

    public void setDirezione(int direzione)
    {
        this.direzione = direzione;
    }
    
    public void dispose()
    {
        
    }

}
