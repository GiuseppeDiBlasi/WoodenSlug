/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package livelli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import personaggi.Bottone;
import com.mygdx.game.GameStateManager;
import com.mygdx.game.WoodenSlug;

/**
 *
 * @author Giuseppe
 */
public class Opzioni extends State
{
    Texture sfondo; 
    
    Texture effetti;
    Texture musica;
    
    Texture valoreEffetti;
    Texture valoreMusica;
    
    Bottone indietroAttivo;
    Bottone indietroNonAttivo;
    
    Bottone piuEffetti;
    Bottone menoEffetti;
    
    Bottone piuMusica;
    Bottone menoMusica;
    
    Vector2 coordinateClick;
    
    //il valore dell'audio e in float
    //ma per semplificare la scelta all'utente
    //verranno convertiti in int solo per comunicazione con l'utente
    private int vEffetti;  
    private int vMusica;
    
    //delay per i bottoni piu e meno
    private float tempoTrascorso;
    
    //il valore massimo del volume e 10 in int, 1 in float
    private final int MAX_EFFETTI = 10;
    private final int MAX_MUSICA = 10;
    
    private Viewport cameraPort;
    
    
    
    public Opzioni(GameStateManager gsm)
    {
        super(gsm);
        sfondo = new Texture("sfondo.png");
        effetti = new Texture("bottoni/numeri/effetti.png");
        musica = new Texture("bottoni/numeri/musica.png");
        
        valoreMusica = new Texture("bottoni/numeri/zero.png");
        valoreEffetti = new Texture("bottoni/numeri/zero.png");
        
        mouse.x = 0;
        mouse.y = 0;
        mouse.z = 0;
        
        tempoTrascorso = .2f;
        
        coordinateClick = new Vector2(0,0);
        
        //conversione dei valori dell'audio da float ad int
        //es: int x =  0.1 * 10 ---> 1;
        vMusica = (int) ((WoodenSlug.volumeMusica) * 10);
        vEffetti = (int) ((WoodenSlug.volumeEffetti) * 10);
        
        camera = new OrthographicCamera();
        cameraPort = new ScreenViewport(camera);
        
        creaBottoneIndietro();
        
        creaBottonePiuEffetti();
        creaBottoneMenoEffetti();
        
        creaBottonePiuMusica();
        creaBottoneMenoMusica();
    }
    
    
    //scelta della texture per il valore
    //della musica in base al valore in int
    //della variabile vMusica
    private void valoreMusica()
    {
        switch(vMusica)
        {
            case 0:
                valoreMusica = new Texture("bottoni/numeri/zero.png");
                break;
            case 1:
                valoreMusica = new Texture("bottoni/numeri/uno.png");
                break;
            case 2:
                valoreMusica = new Texture("bottoni/numeri/due.png");
                break;
            case 3:
                valoreMusica = new Texture ("bottoni/numeri/tre.png");
                break;
            case 4:
                valoreMusica = new Texture ("bottoni/numeri/quattro.png");
                break;
            case 5:
                valoreMusica = new Texture("bottoni/numeri/cinque.png");
                break;
                
            case 6:
                valoreMusica = new Texture("bottoni/numeri/sei.png");
                break;
            case 7:
                valoreMusica = new Texture("bottoni/numeri/sette.png");
                break;
            case 8:
                valoreMusica = new Texture("bottoni/numeri/otto.png");
                break;
            case 9:
                valoreMusica = new Texture("bottoni/numeri/nove.png");
                break;
            case 10:
                valoreMusica = new Texture("bottoni/numeri/dieci.png");
                break;
            
        }
    }
    //scelta della texture per il valore
    //degli effetti in base al valore in int
    //della variabile vEffetti
    private void valoreEffetti()
    {
         switch(vEffetti)
        {
            case 0:
                valoreEffetti = new Texture("bottoni/numeri/zero.png");
                break;
            case 1:
                valoreEffetti = new Texture("bottoni/numeri/uno.png");
                break;
            case 2:
                valoreEffetti = new Texture("bottoni/numeri/due.png");
                break;
            case 3:
                valoreEffetti = new Texture ("bottoni/numeri/tre.png");
                break;
            case 4:
                valoreEffetti = new Texture ("bottoni/numeri/quattro.png");
                break;
            case 5:
                valoreEffetti = new Texture("bottoni/numeri/cinque.png");
                break;
                
            case 6:
                valoreEffetti = new Texture("bottoni/numeri/sei.png");
                break;
            case 7:
                valoreEffetti = new Texture("bottoni/numeri/sette.png");
                break;
            case 8:
                valoreEffetti = new Texture("bottoni/numeri/otto.png");
                break;
            case 9:
                valoreEffetti = new Texture("bottoni/numeri/nove.png");
                break;
            case 10:
                valoreEffetti = new Texture("bottoni/numeri/dieci.png");
                break;
            
        }
    }
    
    //metodo di per il salvataggio delle opzioni audio
    //scritta su file
    //verrà chiamato ogni volta che il tasto indietro è premuto
    private void salvataggioOpzioni()
    {
        FileHandle file = Gdx.files.local("bottoni/opzioni/volume_musica.txt");      
        float x = ( (float) vMusica) / 10;
       
        file.writeString("" + x, false);
        
        file = Gdx.files.local("bottoni/opzioni/volume_effetti.txt");  
        x = ( (float) vEffetti) / 10;
        
        System.out.println(x);
        
        file.writeString("" + x, false);
        
        //assegnazione dei nuovi valori alla musica 
        //e agli effetti, convertendoli da int a float
        //es: volumeMusica = 5 / 10 -----> 0.5f
        WoodenSlug.volumeMusica = ( (float) vMusica ) / 10;
        WoodenSlug.volumeEffetti = ( (float) vEffetti)  / 10;
     
    }

    private void creaBottoneIndietro()
    {
         indietroNonAttivo = new Bottone(10, 80 ,"bottoni/indietro/indietro.png");
        indietroAttivo = new Bottone(indietroNonAttivo, "bottoni/indietro/indietro_cliccato.png");
        
        indietroNonAttivo.setAttivo(true);
        indietroAttivo.setAttivo(false);
    }
    
    private void creaBottonePiuEffetti()
    {
        piuEffetti = new Bottone(0, 0, "bottoni/numeri/piu.png");
        piuEffetti.setPosizioneX( (WoodenSlug.LUNGHEZZA / 2) - (piuEffetti.getImmagineBottone().getWidth() / 2) + 450 );
        piuEffetti.setPosizioneY( (WoodenSlug.ALTEZZA / 2) - (piuEffetti.getImmagineBottone().getHeight()/ 2)  + 200 );
    }
    
    private void creaBottoneMenoEffetti()
    {
        menoEffetti = new Bottone(0, 0, "bottoni/numeri/meno.png");
        menoEffetti.setPosizioneX(piuEffetti.getPosizioneX() - 1200);
        menoEffetti.setPosizioneY(piuEffetti.getPosizioneY() + 30);
    }
    
    private void creaBottonePiuMusica()
    {
        piuMusica = new Bottone(0, 0, "bottoni/numeri/piu.png");
        piuMusica.setPosizioneX(piuEffetti.getPosizioneX());
        piuMusica.setPosizioneY(piuEffetti.getPosizioneY() - 300);
        
    }
    
    private void creaBottoneMenoMusica()
    {
        menoMusica = new Bottone(0, 0, "bottoni/numeri/meno.png");
        menoMusica.setPosizioneX(menoEffetti.getPosizioneX());
        menoMusica.setPosizioneY(piuMusica.getPosizioneY() + 30);
    }
    
    private void inputBottoneIndietro()
    {
       // se le coordinate del mouse sono comprese
        //tra il puntodi partenza X e Y della texture
        //piu la lunghezza della texture
        //allora il cursore e sopra al mouse
        if(mouse.x >= indietroNonAttivo.getPosizioneX()
               && mouse.x <= (indietroNonAttivo.getDimensioneX() + indietroNonAttivo.getPosizioneX())
               && mouse.y >= WoodenSlug.ALTEZZA - indietroNonAttivo.getPosizioneY() -  indietroNonAttivo.getImmagineBottone().getHeight() 
               && mouse.y <= (indietroNonAttivo.getDimensioneY() + WoodenSlug.ALTEZZA - indietroNonAttivo.getPosizioneY() -  indietroNonAttivo.getImmagineBottone().getHeight()))
    
        {
            indietroNonAttivo.setAttivo(false);
            indietroAttivo.setAttivo(true);
            
            //cambio dello stato tramite il metodo pop()
            //non serve creare un nuovo menu siccome ne esiste già uno
            if(coordinateClick.x == mouse.x && coordinateClick.y == mouse.y)
            {
                coordinateClick.x = 0;
                coordinateClick.y = 0;
               
                salvataggioOpzioni();
                
                gsm.pop();
            }
            
        }
        
        else
        {
            indietroNonAttivo.setAttivo(true);
            indietroAttivo.setAttivo(false);
        }
    }
    
    
    private boolean inputPiuEffetti()   
    {
        // se le coordinate del mouse sono comprese
        //tra il puntodi partenza X e Y della texture
        //piu la lunghezza della texture
        //allora il cursore e sopra al mouse
        if (mouse.x >= piuEffetti.getPosizioneX() 
            && mouse.x <= (piuEffetti.getDimensioneX() + piuEffetti.getPosizioneX())  
            && mouse.y >= WoodenSlug.ALTEZZA - piuEffetti.getPosizioneY() -  piuEffetti.getImmagineBottone().getHeight()
            && mouse.y <= piuEffetti.getDimensioneY() + WoodenSlug.ALTEZZA - piuEffetti.getPosizioneY() -  piuEffetti.getImmagineBottone().getHeight())
            
            // se il bottone è cliccato restituirà true
            if(coordinateClick.x == mouse.x && coordinateClick.y == mouse.y)
                return true;
                
            
        return false;

    }
    
    private boolean inputMenoEffetti()
    {
        // se le coordinate del mouse sono comprese
        //tra il puntodi partenza X e Y della texture
        //piu la lunghezza della texture
        //allora il cursore e sopra al mouse
        if (mouse.x >= menoEffetti.getPosizioneX() 
            && mouse.x <= (menoEffetti.getDimensioneX() + menoEffetti.getPosizioneX())  
            && mouse.y >= WoodenSlug.ALTEZZA - menoEffetti.getPosizioneY() -  menoEffetti.getImmagineBottone().getHeight()
            && mouse.y <= menoEffetti.getDimensioneY() + WoodenSlug.ALTEZZA - menoEffetti.getPosizioneY() -  menoEffetti.getImmagineBottone().getHeight())
             
            // se il bottone è cliccato restituirà true
            if(coordinateClick.x == mouse.x && coordinateClick.y == mouse.y)
                return true;
                
        return false;   
    }
    
    
    private boolean inputPiuMusica()
    {
        // se le coordinate del mouse sono comprese
        //tra il puntodi partenza X e Y della texture
        //piu la lunghezza della texture
        //allora il cursore e sopra al mouse
        if (mouse.x >= piuMusica.getPosizioneX() 
            && mouse.x <= (piuMusica.getDimensioneX() + piuMusica.getPosizioneX())  
            && mouse.y >= WoodenSlug.ALTEZZA - piuMusica.getPosizioneY() -  piuMusica.getImmagineBottone().getHeight()
            && mouse.y <= piuMusica.getDimensioneY() + WoodenSlug.ALTEZZA - piuMusica.getPosizioneY() -  piuMusica.getImmagineBottone().getHeight())
            
            // se il bottone è cliccato restituirà true
            if(coordinateClick.x == mouse.x && coordinateClick.y == mouse.y)
                return true;
                
            
        return false;
    }
    
    private boolean inputMenoMusica()
    {
        // se le coordinate del mouse sono comprese
        //tra il puntodi partenza X e Y della texture
        //piu la lunghezza della texture
        //allora il cursore e sopra al mouse
         if (mouse.x >= menoMusica.getPosizioneX() 
            && mouse.x <= (menoMusica.getDimensioneX() + menoMusica.getPosizioneX())  
            && mouse.y >= WoodenSlug.ALTEZZA - menoMusica.getPosizioneY() -  menoMusica.getImmagineBottone().getHeight()
            && mouse.y <= menoMusica.getDimensioneY() + WoodenSlug.ALTEZZA - menoMusica.getPosizioneY() -  menoMusica.getImmagineBottone().getHeight())
            
             // se il bottone è cliccato restituirà true
            if(coordinateClick.x == mouse.x && coordinateClick.y == mouse.y)
                return true;
                
            
        return false;
    }
    
    //in questo caso handleInput ha un parametro float delta
    //utilizzato per il delay per i bottoni piu e meno quando vengono premuto
    protected void handleInput(float delta)
    {
        //se il mouse viene clickato
        //vengono aggiornati i valori delle coordinate del click
        if(Gdx.input.isTouched(Buttons.LEFT))
        {
            coordinateClick.x = Gdx.input.getX();
            coordinateClick.y = Gdx.input.getY();
        }
        
        inputBottoneIndietro();
        
        azionePiuEffetti(delta);
        azioneMenoEffetti(delta);    
        
        azionePiuMusica(delta);
        azioneMenoMusica(delta);
        
    }

    //metodo per il controllo del bottone piu effetti
    private void azionePiuEffetti(float delta)
    {
        if(inputPiuEffetti())
        {
            //se il tasto è premuto si aspettano 0.200 secondi prima di incrementare il valore
            //e si impedisce l'input ad altri bottoni
              tempoTrascorso -= delta;           
                if(tempoTrascorso <= 0)
               
                    if(vEffetti < MAX_EFFETTI)
                    {
                        coordinateClick.x = 0;
                        coordinateClick.y = 0;
                        vEffetti++;
                        tempoTrascorso = .2f;
                         
                    }
        }
    }
    
    //metodo per il controllo del bottone meno effetti
    private void azioneMenoEffetti(float delta)
    {
        if(inputMenoEffetti())
        {
            tempoTrascorso -= delta;
            
            //se il tasto è premuto si aspettano 0.200 secondi prima di incrementare il valore
            //e si impedisce l'input ad altri bottoni    
            if(tempoTrascorso <= 0)
            {
                
                
                if(vEffetti > 0)
                {
                    coordinateClick.x = 0;
                    coordinateClick.y = 0;
                    vEffetti--;
                    
                    if(vEffetti < 0)
                        vEffetti = 0;
                    
                    tempoTrascorso = .2f;
                }
            }
        }
    }
    
    //metodo per il controllo del bottone piu musica
    private void azionePiuMusica(float delta)
    {
        if(inputPiuMusica())
        {
            tempoTrascorso -= delta;
            
            //se il tasto è premuto si aspettano 0.200 secondi prima di incrementare il valore
            //e si impedisce l'input ad altri bottoni
            if(tempoTrascorso <= 0)
            {
                if(vMusica < MAX_MUSICA)
                {
                    coordinateClick.x = 0;
                    coordinateClick.y = 0;
                    vMusica++;
                    tempoTrascorso = .2f;
                }
            }
        }
    }
    
    //metodo per il controllo del bottone meno musica
    private void azioneMenoMusica(float delta)
    {
        if(inputMenoMusica())
        {
            tempoTrascorso -= delta;
            
            
                
            if(tempoTrascorso <= 0)
            {              
                if(vEffetti > 0)
                {
                    coordinateClick.x = 0;
                    coordinateClick.y = 0;
                    vMusica--;
                    
                    if(vMusica < 0)
                        vMusica = 0;
                    
                    tempoTrascorso = .2f;
                }
            }
        }
    }
    
    @Override
    public void update(float delta)
    {
        handleInput(delta);
        
        valoreMusica();
        valoreEffetti();
        
        camera.update();
        cameraPort.update(WoodenSlug.LUNGHEZZA, WoodenSlug.ALTEZZA, true);
                
        
        mouse.x = Gdx.input.getX();
        mouse.y = Gdx.input.getY();
    }

    @Override
    public void render(SpriteBatch sb)
    {
       sb.begin();
        
       sb.draw(sfondo, 0, 0);        
       
       disegnaBottoneIndietro(sb);
       disegnaBloccoEffetti(sb);
       disegnaBloccoMusica(sb);
       
       sb.end();
    }
    
    private void disegnaBloccoMusica(SpriteBatch sb)
    {
        
       sb.draw(piuMusica.getImmagineBottone(), piuMusica.getPosizioneX(), piuMusica.getPosizioneY());
       sb.draw(menoMusica.getImmagineBottone(), menoMusica.getPosizioneX(), menoMusica.getPosizioneY());
       sb.draw(musica, (WoodenSlug.LUNGHEZZA / 2) - (musica.getWidth() / 2) - 90, ((WoodenSlug.ALTEZZA / 2) - (musica.getHeight() / 2) + 60));
       sb.draw(valoreMusica, (WoodenSlug.LUNGHEZZA / 2) - (musica.getWidth() / 2), ((WoodenSlug.ALTEZZA / 2) - (musica.getHeight() / 2) - 70));
       
    }
    
    private void disegnaBloccoEffetti(SpriteBatch sb)
    {
        sb.draw(piuEffetti.getImmagineBottone(), piuEffetti.getPosizioneX(), piuEffetti.getPosizioneY());
        sb.draw(effetti, (WoodenSlug.LUNGHEZZA / 2) - (effetti.getWidth() / 2) - 90, ((WoodenSlug.ALTEZZA / 2) - (effetti.getHeight() / 2) + 300));
        sb.draw(valoreEffetti, (WoodenSlug.LUNGHEZZA / 2) - (effetti.getWidth() / 2), ((WoodenSlug.ALTEZZA / 2) - (effetti.getHeight() / 2) + 200));
        sb.draw(menoEffetti.getImmagineBottone(), menoEffetti.getPosizioneX(), menoEffetti.getPosizioneY());
    }
            
    private void disegnaBottoneIndietro(SpriteBatch sb)
    {
         if(indietroNonAttivo.isAttivo())
            sb.draw(indietroNonAttivo.getImmagineBottone(), indietroNonAttivo.getPosizioneX(), indietroNonAttivo.getPosizioneY());
        
        if(indietroAttivo.isAttivo())
            sb.draw(indietroAttivo.getImmagineBottone(), indietroAttivo.getPosizioneX(), indietroAttivo.getPosizioneY());
    }
    
    @Override
    public void dispose()
    {
        sfondo.dispose();
    }

    @Override
    protected void handleInput()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
