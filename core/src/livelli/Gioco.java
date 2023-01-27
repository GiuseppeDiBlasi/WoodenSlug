/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package livelli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.GameStateManager;
import com.mygdx.game.Hud;
import com.mygdx.game.WoodenSlug;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import personaggi.Ammo;
import personaggi.Cane;

import personaggi.GestoreGioco;
import personaggi.Medikit;
import personaggi.MitraPickUp;
import personaggi.Mostro;
import personaggi.Proiettile;
import personaggi.Protagonista;

/**
 *
 * @author Giuseppe
 */
public class Gioco extends State
{

    //personaggi all'interno del mondo di gioco
    Protagonista protagonista;
    Array<Mostro> mostri;
    Array<Cane> cani;
    Medikit medikit;
    Ammo munizioni;
    MitraPickUp mitra;
    
    public static final float GRAVITA = -10f;

    public static boolean generaMostri = false;

    //TextureAtlas, contiene le Region (regioni in cui si trovano le texture)
    public TextureAtlas atlas;

    //TmxMapLoader, caricatore della mappa basata su tile (caselle)
    //TiledMap, la mappa di gioco
    //OrthogonalTiledMapRenderer, renderer per la mappa, si occupa di disegnare la mappa
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    
    private Music musica;

    //l'HUD sarà all'interno dello stato di gioco
    private Hud hud;

    //mondo di gioco
    private World mondo;
    private Box2DDebugRenderer b2dr; //utilizzato nella fase di progettazione (solo test)

    private GestoreGioco gestoreGioco;
    private boolean pausa;
    
    // il costruttore ottiene come parametro il gestore degli stati che verrà passato a ogni nuovo stato al quale si accede
    public Gioco(GameStateManager gsm)
    {
        super(gsm);

        atlas = new TextureAtlas("personaggi.pack");

        mapLoader = new TmxMapLoader();

        //scelta casuale della mappa
        sceltaMappa();

        //posizionamento della telecamera per inquadrare il mondo di gioco
        //scalata per PPM
        camera.setToOrtho(false, WoodenSlug.LUNGHEZZA / (WoodenSlug.PPM * 2f), WoodenSlug.ALTEZZA / (WoodenSlug.PPM * 2f));
        b2dr = new Box2DDebugRenderer();

        protagonista = new Protagonista(mondo, this);
        mostri = new Array<Mostro>();
        cani = new Array<Cane>();
        hud = new Hud(WoodenSlug.batch);

        mondo.setContactListener(new WorldContactListener());

        gestoreGioco = new GestoreGioco(this);
        
        pausa = false;
      
        musica = Gdx.audio.newMusic(Gdx.files.internal("musica/musica_gioco.mp3"));
        musica.play();
        musica.setLooping(true);
        musica.setVolume(WoodenSlug.volumeMusica);
        
    }
    
    private void sceltaMappa()
    {
        int mappa = MathUtils.random(1, 2);

        switch (mappa)
        {
            case 1:              
                map = mapLoader.load("mappe/Mappa1/Mappa1.tmx");
                renderer = new OrthogonalTiledMapRenderer(map, 1 / WoodenSlug.PPM);
                mondo = new World(new Vector2(0, GRAVITA), true);
                new CreatoreMondo(this, mondo, map, 2);
                break;

            case 2:
                map = mapLoader.load("mappe/Mappa2/Mappa2.tmx");
                renderer = new OrthogonalTiledMapRenderer(map, 1 / WoodenSlug.PPM);
                mondo = new World(new Vector2(0, GRAVITA), true);
                new CreatoreMondo(this, mondo, map, 2);
                break;

        }

    }

    protected void handleInput(float delta)
    {
        protagonista.handleInput(delta);

        //P per la pausa, se si ripreme si ritorna allo stato di gioco
        if (Gdx.input.isKeyJustPressed(Keys.P))       
            pausa = !pausa;
        

        //se si preme ESC si tornerà al menù principale
        if (Gdx.input.isKeyPressed(Keys.ESCAPE))
        {
            salvataggioPunteggio();
            musica.dispose();
            gsm.pop();
        }

    }

    @Override
    public void update(float delta)
    {
        handleInput(delta);

        if (!pausa)
        {
            //aggiornamento del mondo fisico
            mondo.step(delta, 6, 2);

             if(gestoreGioco.isFineGioco())
             {
                WoodenSlug.vittoria = true;
                musica.dispose();
                salvataggioPunteggio();
                gsm.pop();
             }
            
            protagonista.update(delta);
            
            //se medikit e diverso da null
            //allora è un istanza di Medikit
            //quindi dovrà essere aggiornato ogni frame
            if(medikit != null)
            {
                medikit.update(delta);
                rimuoviMedikit();
            }
            
            //rimozione dei corpi da eliminare nel mondo fisico
            protagonista.eliminaProiettili();   
            
            if(mitra != null)
            {
                mitra.update(delta);
                rimuoviMitra();
            }
            
            if(munizioni != null)
            {
                munizioni.update(delta);
                rimuoviAmmo();
            }    
                  
            
            //rimozione dei corpi da eliminare nel mondo fisico
            rimuoviMostro();
            rimuoviCani();
            rimuoviProtagonista();

            //la posizione della telecamera verrà aggiornata per seguire
            //lungo l'asse X il protagonista
            camera.position.x = protagonista.b2Corpo.getPosition().x;

            //aggiornamento per ogni frame dei mostri
            for (Mostro mostro : mostri)
                mostro.update(delta, protagonista.b2Corpo.getPosition().x, protagonista.b2Corpo.getPosition().y, protagonista);
            
            //aggiornamento per ogni frame dei cani
            for (Cane cane : cani)           
                cane.update(delta, protagonista.b2Corpo.getPosition().x, protagonista.b2Corpo.getPosition().y, protagonista);
            

            hud.update(delta, protagonista);

            gestoreGioco.update(delta);
                
        }
    }

    public void salvataggioPunteggio()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date data = new Date();
        
        char x = '\n';
        FileHandle file = Gdx.files.local("punteggi/punteggi.txt");
        file.writeString("Punteggio : " + protagonista.getPunteggio() + " Data: "
                        + dateFormat.format(data)  + x , true);
        
    }
    
    public MitraPickUp getMitra()
    {
        return mitra;
    }

    public void setMitra(MitraPickUp mitra)
    {
        this.mitra = mitra;
    }
 
    public Ammo getMunizioni()
    {
        return munizioni;
    }

    public void setMunizioni(Ammo munizioni)
    {
        this.munizioni = munizioni;
    }
    
    public Medikit getMedikit()
    {
        return medikit;
    }

    public void setMedikit(Medikit medikit)
    {
        this.medikit = medikit;
    }

    
    
    public World getMondo()
    {
        return mondo;
    }

   
    

    
    private void rimuoviMedikit()
    {
        if(medikit.isDaEliminare() ) 
            
            if(!medikit.isEliminato())
            {
                
                mondo.destroyBody(medikit.b2Corpo);
                medikit.b2Corpo.setUserData(null);
                medikit.setEliminato(true);
                medikit = null;
            }
            
        
    }
    
    private void rimuoviMitra()
    {
        if(mitra.isDaEliminare())
            if(!mitra.isEliminato())
            {
                mondo.destroyBody(mitra.b2Corpo);
                mitra.b2Corpo.setUserData(null);
                mitra.setEliminato(true);
                mitra = null;
            }
    }
    
    private void rimuoviAmmo()
    {
        if(munizioni.isDaEliminare())
            if(!munizioni.isEliminato())
            {
                mondo.destroyBody(munizioni.b2Corpo);
                munizioni.b2Corpo.setUserData(null);
                munizioni.setEliminato(true);
                munizioni = null;
            }
    }
    
    private void rimuoviMostro()
    {
        for (int i = 0; i < mostri.size; i++)
        {
            if (mostri.get(i).isDaEliminare())
            {

                if (!mostri.get(i).isEliminato())
                {
                    protagonista.setPunteggio(protagonista.getPunteggio() + mostri.get(i).getPunti());
                    mondo.destroyBody(mostri.get(i).b2Corpo);
                    mostri.get(i).setEliminato(true);
                    mostri.removeIndex(i);
                }

            }

        }
    }

    private void rimuoviProtagonista()
    {
        if (protagonista.isDaEliminare())
        {
            mondo.destroyBody(protagonista.b2Corpo);
            protagonista.setEliminato(true);
            protagonista.dispose();
            
            WoodenSlug.gameOver = true;
            
            musica.dispose();
            salvataggioPunteggio();
            
            gsm.pop();
            
        }
    }

    public int getQuantitaMostri()
    {
        return mostri.size;
    }

    public Array<Mostro> getMostri()
    {
        return mostri;
    }

    public Array<Cane> getCani()
    {
        return cani;
    }

    @Override
    public void render(SpriteBatch sb)
    {

        //aggiornamento della telecamera ad ogni frame
        camera.update();
        
        //la vista del renderer della mappa sara impostata in base alla telecamera
        renderer.setView(camera);
        renderer.render();

        // b2dr.render(mondo, camera.combined);
        sb.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        sb.setProjectionMatrix(camera.combined);

        sb.begin();
             
        if(medikit != null)
            if(!medikit.isDaEliminare())
                medikit.draw(sb);
        
        if(mitra != null)
            if(!mitra.isDaEliminare())
                mitra.draw(sb);
        
        if(munizioni != null)
            if(!munizioni.isDaEliminare())
                munizioni.draw(sb);
        
        protagonista.draw(sb);
        
        disegnaPersonaggi(sb);

        sb.end();

    }

    private void disegnaPersonaggi(SpriteBatch sb)
    {
        Array<Proiettile> proiettili = protagonista.getProiettili();

        for (Proiettile proiettile : proiettili)
        {
            proiettile.draw(sb);
        }

        for (Mostro mostro : mostri)
        {
            mostro.draw(sb);
        }

        for (Cane cane : cani)
        {
            cane.draw(sb);
        }
    }
    
    @Override
    public void dispose()
    {
    }

    @Override
    protected void handleInput()
    {

    }

    private void rimuoviCani()
    {
        for (int i = 0; i < cani.size; i++)
        {
            if (cani.get(i).isDaEliminare())
            {

                if (!cani.get(i).isEliminato())
                {
                    protagonista.setPunteggio(protagonista.getPunteggio() + cani.get(i).getPunti());
                    mondo.destroyBody(cani.get(i).b2Corpo);
                    cani.get(i).setEliminato(true);
                    cani.removeIndex(i);
                }

            }

        }
    }

}
