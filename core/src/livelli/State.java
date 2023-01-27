/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package livelli;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.GameStateManager;

/**
 *
 * @author Giuseppe
 */

public abstract class  State
{
    protected OrthographicCamera camera; // punto di vista del giocatore
    protected Vector3 mouse; //vettore che contiene le 3 coordinate per il mouse: x, y e z che non verrà utilizzata
    protected GameStateManager gsm; //gestore degli stati del gioco
    

    
    protected State(GameStateManager gsm)
    {
        this.gsm = gsm;
        camera = new OrthographicCamera();
        mouse = new Vector3(0,0,0); // creazione del mouse con coordinate di base
        
       
    }
    
    // metodo che servirà a gestire gli input dell'utente
    protected abstract void handleInput(); 
    
    //metodo che viene chiamato ogni frame e aggiorna la finestra; delta è quanto tempo trascorre tra una chiamata all'altra
    public abstract  void update(float delta); 
    
    // metodo utilizzato per disegnare le texture sulla finestra; sb è lo SpriteBatch, ovvero il "foglio sul  quale vengono disegnate le texture
    public abstract void render(SpriteBatch sb);
    
    //metodo per liberare la memoria occupata da oggetti che non sono piu utilizzati per esempio le texture
    public abstract void dispose();

} 
