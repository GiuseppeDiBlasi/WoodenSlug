package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import livelli.FinePartita;
import livelli.Menu;



public class WoodenSlug extends ApplicationAdapter
{
    //lunghezza della finestra
    public static final int LUNGHEZZA = 1900;
    // altezza della finestra
    public static final int ALTEZZA = 905;
    //numero di pixel contenuti in metro
    public static final float PPM = 100f; 
    
    //variabili static per il volume
    public static float volumeMusica = 1f;
    public static float volumeEffetti = 1f;
    
    public static boolean gameOver;
    public static boolean vittoria;
    
    //gestore degli stati
    GameStateManager gsm;

    //SpriteBatch, "foglio" sul quale verranno disegante tutte le texture e gli sprite
    //è static perché conviene avere  un solo SpriteBatch siccome è piuttosto pesante sulla memoria
    public static SpriteBatch batch;
   
    
    @Override
    public void create() // crazione della finestra
    {
        gsm = new GameStateManager();
        batch = new SpriteBatch();
        
        gameOver = false;
        vittoria = false;
        
        // colore di sfondo
        Gdx.gl.glClearColor(0, 0, 0, 0); 
        
        //il primo stato del gioco sarà il menu principale
        gsm.push(new Menu(gsm) ) ; 
    }

    // metodo render, viene chiamato ogni frame e disegna le texture, e sprite
    @Override
    public void render() 
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(batch);
    }
    
    
    //metodo dispose, libera la memoria occupata
    @Override
    public void dispose()
    {
        batch.dispose();

    }
    
}