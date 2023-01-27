/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package personaggi;

import armi.Arma;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

import armi.Pistola;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Timer.Task;
import com.mygdx.game.WoodenSlug;
import livelli.Gioco;


/**
 *
 * @author Giuseppe
 */
public class Protagonista extends Sprite
{
    
    //stati nei quali si puo ritrovare il protagonista
    public enum Stato
    {
        SALTO, CORSA, FERMO, SPARO
    };

    
    public Stato statoAttuale;
    public Stato statoPrecedente;

    //World, mondo in cui il protagonista esiste
    public World mondo;
    public Body b2Corpo;

    public static final float VELOCITA_MAX = 4.5f;

    public static final float FORZA_SALTO = 10f;

    public static final int SINISTRA = 0;
    public static final int DESTRA = 1;
    public static final int PROTAGONISTA = 0;
    public static final int PROIETTILI_MAX = 100;

    //arma del protagonista
    private Array<Arma> armi;
    private Array<Proiettile> proiettili;

    private int vite;
    private int armaScelta;
    private int punteggio;
    private int direzione;

    private float delayProiettile;
    private float statoTimer;

    
    //il protagonista ha 3 animazioni in base allo stato in cui si trova
    private Animation protagonistaInattivo;
    private Animation protagonistaMovimento;
    private Animation protagonistaSalto;

    private boolean inSalto;
    private boolean daEliminare;
    private boolean colpito;
    private boolean eliminato;
    private boolean singolo;
    
    private Timer timer;

    private Gioco gioco;

    private Sound sparo;
    private Sound armaScarica;
    
   

    public Protagonista(World mondo, Gioco gioco)
    {       
        //le texture sono tutte nel file personaggi.pack  
        //contenute nel TextureAtlas di Gioco
        //ogni texture ha una propria regione con nome e indice
        //se si apre il personaggi.pack con un editor di testo
        //si potrà leggere tutte le informazioni relative
        //a tutte le regioni presenti nel file
        super(gioco.atlas.findRegion("protagonista_fermo", 0));
        this.mondo = mondo;
     
        punteggio = 0;

        //l'animazione è un insieme di texture che scorrono per 
        //dare l'impressione del movimento
        Array<TextureRegion> fotogrammi = new Array<TextureRegion>();

        
        //cicli for per l'impostazione delle animazioni
        //inizio:
        for (int i = 0; i < 4; i++)
        {
            fotogrammi.add(new TextureRegion(gioco.atlas.findRegion("protagonista_fermo", i)));
        }
        
        // il primo parametro indica la durata di una singola immagine
        //il secondo indica l'array di texture dal quale creare l'animazione
        protagonistaInattivo = new Animation(0.1f, fotogrammi);

        fotogrammi.clear();

        for (int i = 0; i < 3; i++)
        {
            fotogrammi.add(new TextureRegion(gioco.atlas.findRegion("protagonista_movimento", i)));
        }
        
        // il primo parametro indica la durata di una singola immagine
        //il secondo indica l'array di texture dal quale creare l'animazione
        protagonistaMovimento = new Animation(0.1f, fotogrammi);

        fotogrammi.clear();

        for (int i = 0; i < 1; i++)
        {
            fotogrammi.add(new TextureRegion(gioco.atlas.findRegion("protagonista_salto", -1)));
        }
        
        
        // il primo parametro indica la durata di una singola immagine
        //il secondo indica l'array di texture dal quale creare l'animazione
        protagonistaSalto = new Animation(0.1f, fotogrammi);

        fotogrammi.clear();
        //fine.
        
        statoAttuale = Stato.FERMO;
        statoPrecedente = Stato.FERMO;
        statoTimer = 0f;
        direzione = DESTRA;

        daEliminare = false;
        inSalto = false;
        colpito = false;
        eliminato = false;
        singolo = false;
        
        armi = new Array<Arma>();
        armi.add(new Pistola());
        
        armaScelta = 0;
        proiettili = new Array();

        delayProiettile = armi.get(armaScelta).getDelayColpi() / 60f;

        
        vite = 3;

        setBounds(0, 0, 34, 48);

        //limiti della texture del protagonista
        creaProtagonista();
        
        this.setSize(32 / (WoodenSlug.PPM / 1.8f), 32 / (WoodenSlug.PPM / 1.8f));
        timer = new Timer();

        this.gioco = gioco;
        
        sparo = Gdx.audio.newSound(Gdx.files.internal("suoni/sparo2.mp3"));
        armaScarica = Gdx.audio.newSound(Gdx.files.internal("suoni/vuoto.mp3"));
    }

    private void creaProtagonista()
    {
        //BodyDef, permette la creazione degli oggetti Body, contenuti nel mondo di gioco
        //il Body contiente tutte le proprietà fisiche: velocità, posizione, accelerazione etc..
        BodyDef bDef = new BodyDef();
        bDef.position.set(4f / WoodenSlug.PPM, 300f / WoodenSlug.PPM);
        
        //il protagonista è un corpo dinamico, ha collisioni e si puo muovere
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2Corpo = mondo.createBody(bDef);

        //FixtureDef, permette la creazione della Fixture, contenitore che contiene il body
        FixtureDef fDef = new FixtureDef();
        
        //CircleShape, specifica la forma da far assumere al body
        CircleShape shape = new CircleShape();
        shape.setRadius(18 / WoodenSlug.PPM);

        fDef.shape = shape;

        //il protagonista avrà collisioni con tutti gli oggetti presenti nel mondo
        //siccome ha un indice di filtro positivo
        fDef.filter.groupIndex = 2;

        b2Corpo.createFixture(fDef);       
        b2Corpo.setGravityScale(0.5f);
        
        //setUserData(), permette di riconoscere a quale classe appartiene il corpo creato
        b2Corpo.setUserData(this);

    }

    private void cambiaArmaDestra()
    {
        
        if(armaScelta < armi.size - 1)
            armaScelta++;
        
        else
            armaScelta = 0;
             
    }
    
    private void cambiaArmaSinistra()
    {
        if(armaScelta > 0)
            armaScelta--;
        else
            armaScelta = armi.size - 1;
    }

    public Array<Arma> getArmi()
    {
        return armi;
    }
    
    
    
    public Arma getArma()
    {
        return armi.get(armaScelta);
    }
    
    public Array<Proiettile> getProiettili()
    {
        return proiettili;
    }

    public boolean isEliminato()
    {
        return eliminato;
    }

    public void setEliminato(boolean eliminato)
    {
        this.eliminato = eliminato;
    }

    public boolean isColpito()
    {
        return colpito;
    }

    public void setColpito(boolean colpito)
    {
        this.colpito = colpito;
    }

    public boolean isDaEliminare()
    {
        return daEliminare;
    }

    public void setDaEliminare(boolean daEliminare)
    {
        this.daEliminare = daEliminare;
    }

    public int getArmaScelta()
    {
        return armaScelta;
    }

    public void setArmaScelta(int armaScelta)
    {
        this.armaScelta = armaScelta;
    }

    public int getPunteggio()
    {
        return punteggio;
    }

    public void setPunteggio(int punteggio)
    {
        this.punteggio = punteggio;
    }

    public int getDirezione()
    {
        return direzione;
    }

    public void setDirezione(int direzione)
    {
        this.direzione = direzione;
    }

    public boolean gameOver()
    {
        // se il protagonista è da eliminare
        //la partita termina
        
        return isDaEliminare();
    }

    public void colpito()
    {
        //se il protagonista viene colpito da un nemico
        //il protagonista avrà un secondo e mezzo di delay
        //prima di poter esser colpito nuovamente
        
        colpito = true;
        vite--;
        //System.out.println("colpito");
        timer.scheduleTask(new Task()
        {
            @Override
            public void run()
            {
                colpito = false;
                //System.out.println("posso essere colpito di nuovo");

            }
        },
                1.5f //    delay     
        );
    }

    public void sparo(float delta)
    {
        //se l'array per i proiettili ha raggiunto la grandezza massima
        //non verranno creati altri proiettili
        if (proiettili.size == PROIETTILI_MAX){  return; }

        delayProiettile -= delta;
        
        //se la condizione è verificata verrà creato
        //il proiettile dopo che il delay è terminato
        
        if(delayProiettile <= 0)
        {
            if(armi.get(armaScelta).getMunizioni() > 0 || armi.get(armaScelta).getClass() == Pistola.class)
                creaProiettile();
            else
                armaScarica.play(WoodenSlug.volumeEffetti);
            
            //reset del delay
            delayProiettile = armi.get(armaScelta).getDelayColpi() / 60f;
        }
    }

    private void creaProiettile()
    {
        
            switch (direzione)
            {
                case SINISTRA:        
                        proiettili.add(new Proiettile(mondo, gioco, b2Corpo.getPosition().x - .6f,
                                   b2Corpo.getPosition().y, direzione, armi.get(armaScelta) ) );
                        sparo.play(WoodenSlug.volumeEffetti);
                    break;

                case DESTRA:
                        proiettili.add(new Proiettile(mondo, gioco, b2Corpo.getPosition().x + .6f,
                                      b2Corpo.getPosition().y, direzione, armi.get(armaScelta) ) );
                        sparo.play(WoodenSlug.volumeEffetti);

            }
            
            if(armi.get(armaScelta).getClass() != Pistola.class )
                armi.get(armaScelta).setMunizioni(armi.get(armaScelta).getMunizioni() - 1);
    }
    
    public void salto()
    {
         //viene applicata una forza lineare al corpo del protagonista sull'asse Y
        
        b2Corpo.applyLinearImpulse(new Vector2(0, 3.5f), b2Corpo.getWorldCenter(), true);
        inSalto = true;
    }

    public void spostaSinistra()
    {
        //viene applicata una forza lineare al corpo del protagonista
        
        if (b2Corpo.getLinearVelocity().x >= -VELOCITA_MAX)
        {
            b2Corpo.applyLinearImpulse(new Vector2(-.5f, 0), b2Corpo.getWorldCenter(), true);
        }
    }

    public void spostaDestra()
    {
        //viene applicata una forza lineare al corpo del protagonista
        
        if (b2Corpo.getLinearVelocity().x <= VELOCITA_MAX)
        {
            b2Corpo.applyLinearImpulse(new Vector2(.5f, 0), b2Corpo.getWorldCenter(), true);
        }
    }
    
    public void fermo()
    {
        b2Corpo.setLinearVelocity(new Vector2(0, mondo.getGravity().y));
    }

    public void handleInput(float delta)
    {
        // se un qualsiasi tasto è premuto
        //si andrà a controllare quale tasto è stato premuto
        
        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY))
        {
            //freccia SU per il salto
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            {
                if (b2Corpo.getLinearVelocity().y == 0)
                {
                    salto();
                } else
                {
                    inSalto = false;
                }
            }

            // se freccia DESTRA e SINISTRA sono premuto allo stesso tempo
            //il protagonista si ferma
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            {
                fermo();
            }

            //freccia SINISTRA per spostamento laterale a sinistra
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            {
                direzione = SINISTRA;
                spostaSinistra();
            }

            //freccia DESTRA per spostamento laterale a destra
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            {
                direzione = DESTRA;
                spostaDestra();
            }
            
            if(Gdx.input.isKeyJustPressed(Input.Keys.E))
            {
                cambiaArmaDestra();
            }
            
            if(Gdx.input.isKeyJustPressed(Input.Keys.Q))
                cambiaArmaSinistra();

        }        
        else // se nessun tasto è premuto, il protagonista sta fermo
        {
            fermo();
        }

        //SPAZIO per lo sparo
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE))
           sparo(delta);
    
    }

    public void update(float delta)
    {
        if (vite == 0)
            daEliminare = true;
        

         //se la sua Region ( texture nell'atlas) e specchiata lungo l'asse delle X
        if (isFlipX())
        {
            //la sua posizione viene modificata per poter stare all'interno del corpo fisico
            setPosition((b2Corpo.getPosition().x - getWidth() / 2) - .1f, (b2Corpo.getPosition().y - getHeight() / 2) + .1f);
        } else
        {
            //la sua posizione viene modificata per poter stare all'interno del corpo fisico
            setPosition((b2Corpo.getPosition().x - getWidth() / 2) + .1f, (b2Corpo.getPosition().y - getHeight() / 2) + .1f);
        }
    
        //aggiornamento dell'array dei proiettili
        for (Proiettile x : proiettili)
        {
            x.update(delta);
        }

        // se è in caduto verrà applicata la sedicesima parte della gravità del mondo
        if (b2Corpo.getLinearVelocity().y < 0)
        {
            b2Corpo.applyLinearImpulse(new Vector2(mondo.getGravity().x, (mondo.getGravity().y) / 32f), b2Corpo.getWorldCenter(), true);
        }

        // se non è ne in salto ne in caduta allora inSalto e falso
        if (b2Corpo.getLinearVelocity().y == 0)
        {
            inSalto = false;
        }        
        
        //setRegion, set della Region(texture nel TextureAtlas)
        //in base al frame dell'animazione
        setRegion(getFrame(delta));

    }

    private TextureRegion getFrame(float delta)
    {
        
        statoAttuale = getStato();

        TextureRegion regione;

        //  la Region avra come texture il frame in cui lo stato cambia
        //  o continua a mantere lo stesso stato
        //  permettendo di continuare l'animazione
        switch (statoAttuale)
        {
            case SALTO:
                regione = (TextureRegion) protagonistaSalto.getKeyFrame(statoTimer);
                break;

            case CORSA:
                regione = (TextureRegion) protagonistaMovimento.getKeyFrame(statoTimer, true);
                break;
            default:
                regione = (TextureRegion) protagonistaInattivo.getKeyFrame(statoTimer, true);
                break;
        }

         //se la direzione è sinsitra e la regione (texture)
        //non è specchiata, allora verrà specchiata
        if (direzione == SINISTRA && !regione.isFlipX())
        {
            regione.flip(true, false);
        } 
        else if (direzione == DESTRA && regione.isFlipX())
        {
            regione.flip(true, false);
        }

        //lo stato del timer sarà uguale a statoTimer += delta
        //se la condizione (direzione == direzionePrecedente) è vera 
        //altrimenti verrà impostato a 0 siccome ci si trova in una nuova direzione

        statoTimer = statoAttuale == statoPrecedente ? statoTimer += delta : 0;
        statoPrecedente = statoAttuale;

        return regione;
    }

    //ricerca dello stato attuale del protagonista
    private Stato getStato()
    {
        if (b2Corpo.getLinearVelocity().x != 0 && inSalto)
        {
            return Stato.SALTO;
        } else if (b2Corpo.getLinearVelocity().x != 0)
        {
            return Stato.CORSA;
        } else if (b2Corpo.getLinearVelocity().y > 0)
        {
            return Stato.SALTO;
        } else
        {
            return Stato.FERMO;
        }
    }

    
    public void eliminaProiettili()
    {
        for (int i = 0; i < proiettili.size; i++)
        {
            if (proiettili.get(i).isDaEliminare())
            {
                mondo.destroyBody(proiettili.get(i).b2Corpo);         
                proiettili.removeIndex(i);
                
                //il proiettile viene rimosso dall'array
            }

        }
    }

    public int getVite()
    {
        return vite;
    }

    public void setVite(int vite)
    {
        this.vite = vite;
    }

    public void dispose()
    {
        mondo = null;
    }
}
