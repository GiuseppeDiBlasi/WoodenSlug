/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package livelli;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.GameStateManager;
import com.mygdx.game.WoodenSlug;

/**
 *
 * @author Giuseppe
 */
public class FinePartita extends State
{

    Texture sfondo;
    Texture gameOver;
    Texture vittoria;
    
    float tempoTrascorso;
    
    Viewport cameraPort;
    
    public FinePartita(GameStateManager gsm, boolean gameOver)
    {
        super(gsm);
        
        sfondo = new Texture("sfondo.png");
        
        tempoTrascorso = .0f;
        
        if(gameOver)
            this.gameOver = new Texture("game_over.png");
        else
            vittoria = new Texture("vittoria.png");
        
        camera = new OrthographicCamera();
        
        //ScreenVieport, tenderà ad estendere tutte le texture sulla finestra
        cameraPort = new ScreenViewport(camera);
    }

    @Override
    protected void handleInput()
    {
       
    }

    @Override
    public void update(float delta)
    {
        tempoTrascorso += delta;
        
        if(tempoTrascorso >= 5f)
            gsm.pop();
    }

    @Override
    public void render(SpriteBatch sb)
    {
        sb.begin();
        
        sb.draw(sfondo, 0, 0);
        if(gameOver != null)
            sb.draw(gameOver, (WoodenSlug.LUNGHEZZA / 2) - (gameOver.getWidth() / 2),
                    (WoodenSlug.ALTEZZA / 2 ) - (gameOver.getHeight() / 2) );
        
        if(vittoria != null)
            sb.draw(vittoria, (WoodenSlug.LUNGHEZZA / 2) - (vittoria.getWidth() / 2),
                    (WoodenSlug.ALTEZZA / 2 ) - (vittoria.getHeight() / 2));
        
        sb.end();
    }

    @Override
    public void dispose()
    {
        if(gameOver != null)
            gameOver.dispose();
        
        if(vittoria != null)
            vittoria.dispose();
    }
    
}
