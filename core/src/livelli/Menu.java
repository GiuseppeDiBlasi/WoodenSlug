/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package livelli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.audio.Music;
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
 * @author diblasi.giuseppe
 */
public class Menu extends State // la classe menu eredita dalla classe astratta State
{

    //bottone gioca e i sue due stati: attivo e non attivo; attivo quando il mouse ci passa sopra
    //non attivo quando il mouse non sta sopra al bottone
    Bottone bottoneGiocaNonAttivo;
    Bottone bottoneGiocaAttivo;

    //bottone opzioni e i sue due stati: attivo e non attivo; attivo quando il mouse ci passa sopra
    //non attivo quando il mouse non sta sopra al bottone
    Bottone bottoneOpzioniNonAttivo;
    Bottone bottoneOpzioniAttivo;

    //bottone esci e i sue due stati: attivo e non attivo; attivo quando il mouse ci passa sopra
    //non attivo quando il mouse non sta sopra al bottone
    Bottone bottoneEsciAttivo;
    Bottone bottoneEsciNonAttivo;

    Bottone bottoneCreditiAttivo;
    Bottone bottoneCreditiNonAttivo;

    //sfondo della finestra
    Texture sfondo;
    Texture titolo;
    
    //Vector2, contiene 2 tipi di coordinate
    //una per l'asse X e Y.
    Vector2 coordinateClick;

    //Music, è per i file di grosse dimensioni, quindi tracce musicali
    Music musica;

    private Viewport cameraPort;

    // il costruttore ottiene come parametro il gestore degli stati che verrà passato a ogni nuovo stato al quale si accede
    public Menu(GameStateManager gsm)
    {
        super(gsm); // costruttore della superclasse
        caricaOpzioni(); //caricamento delle opzioni (volume musica ed volue effetti) da file 

        sfondo = new Texture("sfondo.png"); // creazione dello sfondo
        titolo = new Texture("titolo.png");
        
        mouse.x = 0;
        mouse.y = 0;
        mouse.z = 0;

        coordinateClick = new Vector2(0, 0);

        camera = new OrthographicCamera();

        //ScreenVieport, tenderà ad estendere tutte le texture sulla finestra
        cameraPort = new ScreenViewport(camera);

        creaBottoneGioca();
        creaBottoneOpzioni();
        creaBottoneEsci();

        //creazione del file musicale, Gdx.files.internal; la voce internal è di sola lettura
        musica = Gdx.audio.newMusic(Gdx.files.internal("musica/musica_menu.mp3"));
        musica.play();
        musica.setLooping(true);

    }

    //metodo per il caricamento delle opzioni audio,
    //verra chiamato a ogni avvio del gioco
    private void caricaOpzioni()
    {
        //creazione del file handle per la lettura da file
        FileHandle file = Gdx.files.internal("bottoni/opzioni/volume_musica.txt");
        String text = file.readString();

        //se la stringa non è vuota allora vuol dire che è stato salvato quaclosa 
        if (!text.isEmpty())
        //conversione della stringa in float
        {
            WoodenSlug.volumeMusica = Float.parseFloat(text);
        }

        file = Gdx.files.internal("bottoni/opzioni/volume_effetti.txt");
        text = file.readString();

        if (!text.isEmpty())
        {
            WoodenSlug.volumeEffetti = Float.parseFloat(text);
        }

    }

    private void creaBottoneGioca()
    {
        bottoneGiocaNonAttivo = new Bottone(0, 0, "bottoni/gioca/gioca.png");
        bottoneGiocaNonAttivo.setPosizioneX(820);
        bottoneGiocaNonAttivo.setPosizioneY(530);
        bottoneGiocaAttivo = new Bottone(bottoneGiocaNonAttivo, "bottoni/gioca/gioca_cliccato.png");

        bottoneGiocaNonAttivo.setAttivo(true); //la texture che verrà mostrata del bottone Gioca è quella non attiva inizialmente
        bottoneGiocaAttivo.setAttivo(false);
    }

    private void creaBottoneOpzioni()
    {
        bottoneOpzioniNonAttivo = new Bottone(0, 0, "bottoni/opzioni/opzioni.png");
        bottoneOpzioniNonAttivo.setPosizioneX((WoodenSlug.LUNGHEZZA / 2) - (bottoneOpzioniNonAttivo.getImmagineBottone().getWidth() / 2));
        bottoneOpzioniNonAttivo.setPosizioneY(350);

        bottoneOpzioniAttivo = new Bottone(bottoneOpzioniNonAttivo, "bottoni/opzioni/opzioni_cliccato.png");

        bottoneOpzioniNonAttivo.setAttivo(true);
        bottoneOpzioniAttivo.setAttivo(false);
    }

    private void creaBottoneEsci()
    {
        bottoneEsciNonAttivo = new Bottone(0, 0, "bottoni/esci/esci.png");
        bottoneEsciNonAttivo.setPosizioneX((WoodenSlug.LUNGHEZZA / 2) - (bottoneEsciNonAttivo.getImmagineBottone().getWidth() / 2));
        bottoneEsciNonAttivo.setPosizioneY(bottoneEsciNonAttivo.getPosizioneY() + 130);

        bottoneEsciAttivo = new Bottone(bottoneEsciNonAttivo, "bottoni/esci/esci_cliccato.png");

        bottoneEsciNonAttivo.setAttivo(true);
        bottoneEsciAttivo.setAttivo(false);
    }

    private void inputBottoneGioca()
    {
        // se le coordinate del mouse sono comprese
        //tra il puntodi partenza X e Y della texture
        //piu la lunghezza della texture
        //allora il cursore e sopra al mouse
        if (mouse.x >= bottoneGiocaNonAttivo.getPosizioneX()
                && mouse.x <= (bottoneGiocaNonAttivo.getDimensioneX() + bottoneGiocaNonAttivo.getPosizioneX())
                && mouse.y >= WoodenSlug.ALTEZZA - bottoneGiocaNonAttivo.getPosizioneY() - bottoneGiocaNonAttivo.getImmagineBottone().getHeight()
                && mouse.y <= (bottoneGiocaNonAttivo.getDimensioneY() + WoodenSlug.ALTEZZA - bottoneGiocaNonAttivo.getPosizioneY() - bottoneGiocaNonAttivo.getImmagineBottone().getHeight()))
        {
            // se il cursore è sopra al bottone
            //allora viene cambiata la texture
            bottoneGiocaNonAttivo.setAttivo(false);
            bottoneGiocaAttivo.setAttivo(true);

            // se le coordinate del mouse sono all'interno del bottone
            // è il tasto sinsitro viene premuto partirà il gioco
            if (coordinateClick.x == mouse.x && coordinateClick.y == mouse.y)
            {
                musica.stop();

                //le coordinate del click vengono spostate cosi da evitare
                //che il bottone sia continuamente premuto
                coordinateClick.x = 0;
                coordinateClick.y = 0;

                gsm.push(new Gioco(gsm));
            }

        } else
        {
            bottoneGiocaNonAttivo.setAttivo(true);
            bottoneGiocaAttivo.setAttivo(false);
        }
    }

    private void inputBottoneOpzioni()
    {
        // se le coordinate del mouse sono comprese
        //tra il puntodi partenza X e Y della texture
        //piu la lunghezza della texture
        //allora il cursore e sopra al mouse
        if (mouse.x >= bottoneOpzioniNonAttivo.getPosizioneX()
                && mouse.x <= (bottoneOpzioniNonAttivo.getDimensioneX() + bottoneOpzioniNonAttivo.getPosizioneX())
                && mouse.y >= WoodenSlug.ALTEZZA - bottoneOpzioniNonAttivo.getPosizioneY() - bottoneOpzioniNonAttivo.getImmagineBottone().getHeight()
                && mouse.y <= bottoneOpzioniNonAttivo.getDimensioneY() + WoodenSlug.ALTEZZA - bottoneOpzioniNonAttivo.getPosizioneY() - bottoneOpzioniNonAttivo.getImmagineBottone().getHeight())

        {
            bottoneOpzioniNonAttivo.setAttivo(false);
            bottoneOpzioniAttivo.setAttivo(true);

            //cambio dello stato da Menu ad Opzioni
            if (coordinateClick.x == mouse.x && coordinateClick.y == mouse.y)
            {
                coordinateClick.x = 0;
                coordinateClick.y = 0;

                gsm.push(new Opzioni(gsm));
            }

        } else
        {
            bottoneOpzioniNonAttivo.setAttivo(true);
            bottoneOpzioniAttivo.setAttivo(false);
        }

    }

    private void inputBottoneEsci()
    {
        // se le coordinate del mouse sono comprese
        //tra il puntodi partenza X e Y della texture
        //piu la lunghezza della texture
        //allora il cursore e sopra al mouse
        if (mouse.x >= bottoneEsciNonAttivo.getPosizioneX()
                && mouse.x <= (bottoneEsciNonAttivo.getDimensioneX() + bottoneEsciNonAttivo.getPosizioneX())
                && mouse.y >= WoodenSlug.ALTEZZA - bottoneEsciNonAttivo.getPosizioneY() - bottoneEsciNonAttivo.getImmagineBottone().getHeight()
                && mouse.y <= bottoneEsciNonAttivo.getDimensioneY() + WoodenSlug.ALTEZZA - bottoneEsciNonAttivo.getPosizioneY() - bottoneEsciNonAttivo.getImmagineBottone().getHeight())

        {
            bottoneEsciNonAttivo.setAttivo(false);
            bottoneEsciAttivo.setAttivo(true);

            //il gioco viene chiuso
            if (Gdx.input.isButtonPressed(Buttons.LEFT))
            {
                System.exit(0);
            }

        } else
        {
            bottoneEsciNonAttivo.setAttivo(true);
            bottoneEsciAttivo.setAttivo(false);
        }
    }

    // metodo per gestire gli input (tastiera mouse) dell'utente
    //chiamato all'interno del metodo update,
    //quindi gli input vengono controllati ad ogni frame
    @Override
    protected void handleInput()
    {
        // se il mouse e premuto
        //si ottengono le coordinate del mouse 
        //nel momento in cui avviene il click
        if (Gdx.input.isTouched(Buttons.LEFT))
        {
            coordinateClick.x = Gdx.input.getX();
            coordinateClick.y = Gdx.input.getY();
        }

        inputBottoneGioca();
        inputBottoneOpzioni();
        inputBottoneEsci();

    }

    //chiamato ogni frame
    //permette l'aggiornamento del gioco
    //delta è la differenza tra la chiamata
    //precedente del metodo update con l'ultima
    @Override
    public void update(float delta)
    {

        camera.update();
        cameraPort.update(WoodenSlug.LUNGHEZZA, WoodenSlug.ALTEZZA);

        //musica.setVolume(), nel metodo update così 
        //se si cambia il volume della musica non ci sarà
        //bisogno di dover riavviare il gioco
        musica.play();
        musica.setVolume(WoodenSlug.volumeMusica);

        handleInput();

        if (WoodenSlug.gameOver)
        {
            gsm.push(new FinePartita(gsm, true));
            WoodenSlug.gameOver = false;
            
        }
        
        if (WoodenSlug.vittoria)
        {
            gsm.push(new FinePartita(gsm, false));
            WoodenSlug.vittoria = false;
        }

        //le coordinate del mouse verranno aggiornate ad ogni frame
        mouse.x = Gdx.input.getX();
        mouse.y = Gdx.input.getY();

        camera.setToOrtho(false, WoodenSlug.LUNGHEZZA, WoodenSlug.ALTEZZA);
    }

    // il punto (0,0) è in basso a destra
    //chiamato ad ogni frame, e disegna il frame
    @Override
    public void render(SpriteBatch sb)
    {
        //imposta il tipo di proiezione in base alla telecamera
        sb.setProjectionMatrix(cameraPort.getCamera().combined);

        //per disegnare occorre sempre il metodo begin
        //dello SpriteBatch, altrimenti verrà generata un eccezzione
        
        if(!WoodenSlug.gameOver && !WoodenSlug.vittoria)
        {
            sb.begin();

            sb.draw(sfondo, 0, 0);
            sb.draw(titolo, (WoodenSlug.LUNGHEZZA / 2) - (titolo.getWidth() / 2), (WoodenSlug.ALTEZZA / 2) - (titolo.getHeight() / 2) + 300);
            
            disegnaBottoneGioca(sb);
            disegnaBottoneOpzioni(sb);
            disegnaBottoneEsci(sb);

            //quando si termina di disegnare occorre sempre il metodo end
            //dello SpriteBatch, altrimenti verrà generata un eccezzione
            sb.end();
        
        }
    }

    private void disegnaBottoneGioca(SpriteBatch sb)
    {
        //viene disegnato solo il bottone attivo
        if (bottoneGiocaNonAttivo.isAttivo())
        {
            sb.draw(bottoneGiocaNonAttivo.getImmagineBottone(), bottoneGiocaNonAttivo.getPosizioneX(),
                    bottoneGiocaNonAttivo.getPosizioneY());
        }

        if (bottoneGiocaAttivo.isAttivo())
        {
            sb.draw(bottoneGiocaAttivo.getImmagineBottone(), bottoneGiocaNonAttivo.getPosizioneX(),
                    bottoneGiocaNonAttivo.getPosizioneY());
        }
    }

    private void disegnaBottoneOpzioni(SpriteBatch sb)
    {
        //viene disegnato solo il bottone attivo
        if (bottoneOpzioniNonAttivo.isAttivo())
        {
            sb.draw(bottoneOpzioniNonAttivo.getImmagineBottone(),
                    bottoneOpzioniNonAttivo.getPosizioneX(),
                    bottoneOpzioniNonAttivo.getPosizioneY());
        }

        if (bottoneOpzioniAttivo.isAttivo())
        {
            sb.draw(bottoneOpzioniAttivo.getImmagineBottone(), bottoneOpzioniAttivo.getPosizioneX(),
                    bottoneOpzioniAttivo.getPosizioneY());
        }
    }

    private void disegnaBottoneEsci(SpriteBatch sb)
    {
        //viene disegnato solo il bottone attivo
        if (bottoneEsciNonAttivo.isAttivo())
        {
            sb.draw(bottoneEsciNonAttivo.getImmagineBottone(),
                    bottoneEsciNonAttivo.getPosizioneX(), bottoneEsciNonAttivo.getPosizioneY());
        }

        if (bottoneEsciAttivo.isAttivo())
        {
            sb.draw(bottoneEsciAttivo.getImmagineBottone(),
                    bottoneEsciAttivo.getPosizioneX(), bottoneEsciAttivo.getPosizioneY());
        }
    }

    //metodo per liberare la memoria occupata
    @Override
    public void dispose()
    {
        sfondo.dispose();

        bottoneGiocaNonAttivo.dispose();
        bottoneGiocaAttivo.dispose();

        bottoneEsciNonAttivo.dispose();
        bottoneEsciAttivo.dispose();

        bottoneOpzioniNonAttivo.dispose();
        bottoneOpzioniAttivo.dispose();

        musica.dispose();
    }

}
