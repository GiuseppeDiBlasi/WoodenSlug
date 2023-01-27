/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package personaggi;

import com.badlogic.gdx.graphics.Texture;


/**
 *
 * @author diblasi.giuseppe
 */
public class Bottone { // classe bottone utilizzata nei livelli per i menu( menu principale e opzioni)
    
    private Texture immagineBottone; // texture del bottone
    private int posizioneX; 
    private int posizioneY;
    private int dimensioneX;
    private int dimensioneY;
    private boolean attivo;// variabile bool per sapere se il cursore e sopra al bottone,
                           //e di conseguenza cambiare la texture del bottone
    
    
    public Bottone() // costruttore
    {
        attivo = false;
        posizioneX = 0;
        posizioneY = 0;
        dimensioneX = 0;
        dimensioneY = 0;
        immagineBottone = new Texture("");
    }
    
    // costruttore di copia date una posizione x , y e il path
    // della texture del bottone
    
    public Bottone(int posX, int posY,  String immagine) 
    {
        attivo = false;
        posizioneX = posX;
        posizioneY = posY;        
        immagineBottone = new Texture(immagine);
        dimensioneX = immagineBottone.getWidth();
        dimensioneY = immagineBottone.getHeight();
  
    }

    //costruttore di copia per il bottone
    public Bottone(Bottone x, String immagine)
    {
        attivo = x.attivo;
        posizioneX = x.posizioneX;
        posizioneY = x.posizioneY;
        dimensioneX = x.dimensioneX;
        dimensioneY = x.dimensioneY;
        immagineBottone = new Texture(immagine);
    }


    public Texture getImmagineBottone() {
        return immagineBottone;
    }

    public boolean isAttivo()
    {
        return attivo;
    }

    public void setAttivo(boolean attivo)
    {
        this.attivo = attivo;
    }
    
    
    public int getDimensioneX() {
        return dimensioneX;
    }

    public int getDimensioneY() {
        return dimensioneY;
    }

    public void setDimensioneX(int dimensioneX) {
        this.dimensioneX = dimensioneX;
    }

    public void setDimensioneY(int dimensioneY) {
        this.dimensioneY = dimensioneY;
    }
    
    
    public void dispose() // metodo per cancellare le aree di memoria non piu utilizzate
    {
        immagineBottone.dispose();

    }
           

    public void setImmagineBottone(Texture immagineBottone) {
        this.immagineBottone = immagineBottone;
    }

    public int getPosizioneX()
    {
        return posizioneX;
    }

    public int getPosizioneY()
    {
        return posizioneY;
    }

    public void setPosizioneX(int posizioneX)
    {
        this.posizioneX = posizioneX;
    }

    public void setPosizioneY(int posizioneY)
    {
        this.posizioneY = posizioneY;
    }
          
}
