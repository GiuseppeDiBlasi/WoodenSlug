/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import personaggi.GestoreGioco;
import personaggi.Protagonista;

/**
 *
 * @author Giuseppe
 */
public class Hud
{
    //Stage, dove tutti gli attori, in questo caso label sono contenuti
    public Stage stage;
    
    //Viewport, permette di adattare le immagini alla risoluzione dello schermo
    // in vari modi  che possono essere scelti dal costruttore
    private Viewport viewport;
    
    //variabili per l'HUD per il giocatore tempo trascorso, punteggio totalizzato e vite rimaste
    private float tempo;
    private int punteggio;
    private int vite;
    private int munizioni;
    private String arma;
    
    //label che verranno inserite nello stage
    Label viteLabel;
    Label scrittaVite;
    
    Label armaScelta;
    Label scrittaArma;
    
    Label scrittaTempo;
    Label tempoLabel;
    
    Label ondata;
    Label scrittaOndata;
    
    Label scrittaPunteggio;
    Label punteggioLabel; 

    Label scrittaMunizioni;
    Label munizioniLabel;
    
    public Hud(SpriteBatch sb)
    {
        tempo = 0f;
        punteggio = 0;
        munizioni = 0;

        viewport = new FitViewport(WoodenSlug.LUNGHEZZA, WoodenSlug.ALTEZZA, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        
        //table, conterrà tutte le label create 
        Table table = new Table();
        //table.top(), dice dove posizionare le label
        table.top(); 
        
        //lo sfondo della table sara del colore di sfondo
        table.setFillParent(true);
        
        //meotodo per la creazione degli oggetti di tipo label
        creaLabel();
        
        //aggiunta la label verra estesa lungo l'asse X, con un padTop(1), ovvero
        //la distanza dal punto piu alto dello schermo meno  il padTop()
        table.add(scrittaTempo).expandX().padTop(1);       
        table.add(armaScelta).expandX().padTop(1);
        table.add(scrittaVite).expandX().padTop(1);
        table.add(scrittaMunizioni).expandX().padTop(1);
        table.add(scrittaOndata).expandX().padTop(1);
        table.add(scrittaPunteggio).expandX().padTop(1);
        
        //table.row() crea una nuova riga per la tabella,andando a capo
        table.row();
        
        //padTop di 2
        table.add(tempoLabel).expandX().padTop(2);
        table.add(scrittaArma).expandX().padTop(2);
        table.add(viteLabel).expandX().padTop(2);
        table.add(munizioniLabel).expandX().padTop(2);
        table.add(ondata).expandX().padTop(2);
        table.add(punteggioLabel).expandX().padTop(2);
        
        //nello stage verra aggiunto l'attore table che contiene tutte le label
        stage.addActor(table);
        
    }
    
    private void creaLabel()
    {
        
        // le label avranno il font standard della libreria e saranno di colore rosso
        //alcune label hanno un format per le cifre decimali
        
        viteLabel = new Label(String.format("%01d", vite), new Label.LabelStyle(new BitmapFont(), Color.RED));
        scrittaVite = new Label("VITE:", new Label.LabelStyle(new BitmapFont(), Color.RED));
        
        tempoLabel = new Label(String.format("%03f", tempo), new Label.LabelStyle(new BitmapFont(), Color.RED));
        scrittaTempo = new Label("TEMPO: ", new Label.LabelStyle(new BitmapFont(), Color.RED));
        
        scrittaPunteggio = new Label("PUNTEGGIO:", new Label.LabelStyle(new BitmapFont(), Color.RED));
        punteggioLabel = new Label(String.format("%06d", punteggio), new Label.LabelStyle(new BitmapFont(), Color.RED)); 
       
        armaScelta = new Label("ARMA: ", new Label.LabelStyle(new BitmapFont(), Color.RED));
        scrittaArma = new Label(arma, new Label.LabelStyle(new BitmapFont(), Color.RED));
        
        ondata = new Label("", new Label.LabelStyle(new BitmapFont(), Color.RED));
        scrittaOndata = new Label("ONDATE COMPLETATE: ", new Label.LabelStyle(new BitmapFont(), Color.RED));
        
        scrittaMunizioni = new Label("MUNIZIONI: ", new Label.LabelStyle(new BitmapFont(), Color.RED));
        munizioniLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.RED));
        
    }
    
    //vite in base al volore dell'oggetto protagonista
    public void setVite(Protagonista protagonista)
    {
        vite = protagonista.getVite();
    }
    
    // punteggio in base al valore dell'oggetto protagonista
    public void setPunteggio(Protagonista protagonista) 
    {
        punteggio = protagonista.getPunteggio();
    }
    
    //il tempo sara una somma continua del delta
    public void setTempo(float delta)
    {
        tempo += delta;
    }
    
    public void setMunizioni(int munizioni)
    {
        this.munizioni = munizioni;
    }
    
    public void setArma(String arma)
    {
        this.arma = arma;
    }
    
    //metodo chiamato ogni frame 
    public void update(float delta, Protagonista protagonista)
    {
        setPunteggio(protagonista);
        setTempo(delta);
        setVite(protagonista);
        setArma(protagonista.getArma().getNome());
        
        if(!arma.equals("PISTOLA"))
        {
            setMunizioni(protagonista.getArma().getMunizioni() );
            munizioniLabel.setText(munizioni);
        }
        else
            munizioniLabel.setText("INF.");
        
        //aggiornamento dei testi delle label
        punteggioLabel.setText(punteggio);
        tempoLabel.setText((int) tempo);
        scrittaArma.setText(arma);
        viteLabel.setText(vite);
        ondata.setText(GestoreGioco.ondataAttuale);
    }

    public void dispose()
    {
        stage.dispose();
        
    }
    
}
