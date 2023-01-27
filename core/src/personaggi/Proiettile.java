/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package personaggi;

import armi.Arma;
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
import static personaggi.Protagonista.DESTRA;
import static personaggi.Protagonista.SINISTRA;

/**
 *
 * @author Giuseppe
 */
public class Proiettile extends Sprite
{
    public static final float VELOCITA_MAX_PROIETTILE = 5f;
    
    public World mondo;
    public Body b2Corpo;
    
    private int direzione;
    private int danno;
    
    private boolean daEliminare;   
    
    private Animation movimento;
    
   
    //World, mondo in cui l'oggetto esiste, Gioco ( stato) nel quale esiste
    //float x è utilizzato per il posizionamento del proiettile
    //float y è utilizzato per l'altezza del proiettile
    public Proiettile(World mondo, Gioco gioco, float x, float y, int direzione, Arma arma)
    {
        //le texture sono tutte nel file personaggi.pack  
        //contenute nel TextureAtlas di Gioco
        //ogni texture ha una propria regione con nome e indice
        //se si apre il personaggi.pack con un editor di testo
        //si potrà leggere tutte le informazioni relative
        //a tutte le regioni presenti nel file
        super(gioco.atlas.findRegion("proiettile_movimento", 0));
        
        this.mondo = mondo;
        this.direzione = direzione; 
        danno = arma.getDanno();
        
        daEliminare = false;
                
        //l'animazione è un insieme di texture che scorrono per 
        //dare l'impressione del movimento
        Array<TextureRegion> fotogrammi = new Array<TextureRegion>();
        
         //le texture per il proiettile sono di nome proiettile_movimento 
        //e sono 5, dall'indice 0 al 2
        for(int i = 0; i < 2; i++)
            fotogrammi.add(new TextureRegion(gioco.atlas.findRegion("proiettile_movimento", i) ) );
        
         // il primo parametro indica la durata di una singola immagine
        //il secondo indica l'array di texture dal quale creare l'animazione
        movimento = new Animation(0.05f, fotogrammi);
        
        fotogrammi.clear();
        
        creaProiettile(x, y);
        
        this.setSize(16 / (WoodenSlug.PPM / 1.8f), 6 / (WoodenSlug.PPM / 1.8f));
    }

    public boolean isDaEliminare()
    {
        return daEliminare;
    }
    
    private void creaProiettile(float x, float y)
    {
        //BodyDef, permette la creazione degli oggetti Body, contenuti nel mondo di gioco
        //il Body contiente tutte le proprietà fisiche: velocità, posizione, accelerazione etc..
        BodyDef bDef = new BodyDef();
        bDef.position.set(x,y);
        
        //il proiettile è un corpo dinamico, ha collisioni e si puo muovere
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2Corpo = mondo.createBody(bDef);
        
        //FixtureDef, permette la creazione della Fixture, contenitore che contiene il body
        FixtureDef fDef = new FixtureDef();
        
        //CircleShape, specifica la forma da far assumere al body
        CircleShape shape = new CircleShape();   
        shape.setRadius(4 / WoodenSlug.PPM);
        
        fDef.shape = shape;
        
        //l'opzione setBullet(true), permette al motore fisico 
        //di controllore piu spesso il proiettile siccome è un oggetto
        //che si sposta molto velocemente
        b2Corpo.setBullet(true);
        
        b2Corpo.createFixture(fDef);
        
        /**
        * Il proiettile non ha un indice di filtro poiché
        * avrà collisioni con qualsiasi oggetto
        * presente nel mondo di gioco      
        */
        
        //setUserData(), permette di riconoscere a quale classe appartiene il corpo creato
        b2Corpo.setUserData(this);
       
        //la gravità del proiettile sarà 0, ovverro proseguirà
        //sempre in linea retta finché non incontra un ostacolo
        b2Corpo.setGravityScale(0);
                
    }

    public int getDanno()
    {
        return danno;
    }
    
    public void muoviDestra()
    {
        //viene applicata una forza lineare al corpo del proiettile
        //in base alla direzione del protagonista
        
        if(b2Corpo.getLinearVelocity().x <= VELOCITA_MAX_PROIETTILE)
            b2Corpo.applyLinearImpulse(new Vector2(5f, 0), b2Corpo.getWorldCenter(), true);
    }
    
    public void muoviSinistra()
    {
        //viene applicata una forza lineare al corpo del proiettile
        //in base alla direzione del protagonista
        
        if(b2Corpo.getLinearVelocity().x >= -VELOCITA_MAX_PROIETTILE)
            b2Corpo.applyLinearImpulse(new Vector2(-5f, 0), b2Corpo.getWorldCenter(), true);
    }
    
    public void movimento()
    {
        //movimento del proiettile in base alla direzione del protagonista
        
         switch(direzione)
        {
            case Protagonista.SINISTRA:
                muoviSinistra();
                break;
            case Protagonista.DESTRA:
                muoviDestra();
                break;
        }
    }
    
    public void impatto()
    {  
        daEliminare = true;
    }
    
    public void update(float delta)
    {
        movimento();
       
        //se la sua Region ( texture nell'atlas) e specchiata lungo l'asse delle X
        if(isFlipX())
            //la sua posizione viene modificata per poter stare all'interno del corpo fisico
            setPosition((b2Corpo.getPosition().x - getWidth() / 2) , (b2Corpo.getPosition().y - getHeight() / 2));
            
        else
           //la sua posizione viene modificata per poter stare all'interno del corpo fisico
            setPosition((b2Corpo.getPosition().x - getWidth() / 2) , (b2Corpo.getPosition().y - getHeight() / 2));
       
        //questi sono tutti i casi in cui il proiettile avrà una velocità troppo
        //bassa per poter muoversi (secondo una nostra convenzione)
        //dunque viene eliminato 
        //inizio:
        if(direzione == Protagonista.DESTRA && b2Corpo.getLinearVelocity().x < 0)
           daEliminare = true;
       
        if(direzione == Protagonista.SINISTRA && b2Corpo.getLinearVelocity().x > 0)
           daEliminare = true;
       
        if(b2Corpo.getLinearVelocity().y > 0 || b2Corpo.getLinearVelocity().y < 0
               || b2Corpo.getLinearVelocity().x == 0)
            daEliminare = true;
       
        //fine:
        
        //setRegion, set della Region(texture nel TextureAtlas)
        //in base al frame dell'animazione
        setRegion(getFrame(delta));
       
    }
    
    public void dispose()
    {
        mondo.dispose();       
    }        
   
    //usato durante la progettazione (test)
    public void colpo()
    {
        System.out.println("oggetto colpito");
    }

    private TextureRegion getFrame(float delta)
    {
        TextureRegion regione;
        
          
        //  la Region avra come texture il frame in cui lo stato cambia
        //  o continua a mantere lo stesso stato
        //  permettendo di continuare l'animazione
        switch(direzione)
        {
            case SINISTRA:
                regione = (TextureRegion) movimento.getKeyFrame(delta, true);
                
                break;
                
            case DESTRA:
                regione = (TextureRegion) movimento.getKeyFrame(delta, true);
                break;
                
            default:
                regione = (TextureRegion) movimento.getKeyFrame(delta, true);
                break;
        }
        
        //se la direzione è sinsitra e la regione (texture)
        //non è specchiata, allora verrà specchiata
        if(direzione  == SINISTRA && !regione.isFlipX())
            regione.flip(true, false);
        
        return regione;
    }
    
}
