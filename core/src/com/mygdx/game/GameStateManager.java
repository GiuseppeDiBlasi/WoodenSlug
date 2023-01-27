/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import livelli.State;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Stack;

/**
 *
 * @author Giuseppe
 */


// classe che gestisce gli stati del gioco
public class GameStateManager 
{
    //stack che contiene tutti gli stati
    // <State> indica che lo Stack puo contenere solo dati di tipo State
    // e che verrà controllato a tempo di compilazione
    
    private Stack<State> states;
    private boolean pop;
    
    public GameStateManager()
    {
        states = new Stack<State>();
        pop = false;
    }

    public boolean isPop()
    {
        return pop;
    }

    public void setPop(boolean pop)
    {
        this.pop = pop;
    }
    
    public void push(State state)
    {
        states.push(state);
    }
    
    public void pop()
    {
        // quando si esegue il pop di uno stato
        //si libera anche l'aerea di memoria che occupava
        states.pop().dispose(); 
    }
    
    public void setState(State state)
    {
        //simile al push()
        states.pop();
        states.push(state);
    }
    
    public void update(float delta)
    {
        //metodo update per l'ultimo stato entrato nella pila
        states.peek().update(delta);
    }
    
    public void render(SpriteBatch sb)
    {
        //metodo render per l'utlimo stato entranto nella pila
        states.peek().render(sb);
    }
    
    
}
