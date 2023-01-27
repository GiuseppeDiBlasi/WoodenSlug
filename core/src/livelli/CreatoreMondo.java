/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package livelli;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.WoodenSlug;

/**
 *
 * @author Giuseppe
 */
public class CreatoreMondo //creatore del mondo nel motore fisico Box2D
{
    //dato il riferimento al gioco, al mondo, la mappa, e qual è l'indice del terreno
    //crea il mondo fisico di gioco
    public CreatoreMondo(Gioco gioco, World mondo, TiledMap mappa, int indiceTerreno)
    {
        //BodyDef, permette la creazione degli oggetti Body, contenuti nel mondo di gioco
        //il Body contiente tutte le proprietà fisiche: velocità, posizione, accelerazione etc...  
        BodyDef bDef = new BodyDef();   
        
        //PolygonShape, specifica la forma da far assumere al body
        PolygonShape shape = new PolygonShape();
        
        //FixtureDef, permette la creazione della Fixture, contenitore che contiene il body
        FixtureDef fDef = new FixtureDef();
        Body body;

        //ciclo for per gli oggetti di mappa in un determinato indice
        for (MapObject object : mappa.getLayers().get(indiceTerreno).getObjects().getByType(RectangleMapObject.class))
        {
            
            Rectangle rec = ((RectangleMapObject) object).getRectangle();

            //dichiarazione del tipo di Body da creare: static body sarà un corpo statico con collisioni
            bDef.type = BodyDef.BodyType.StaticBody;
            
            //si setta la posizione del corpo in base alle coordinate del rettangolo
            //dividendo per 2 la dimensione ottenuta, in modo da creare il rettangolo correttamente
            //tutto verrà scalato per i PPM(Pixel Per metro)
            bDef.position.set((rec.getX() + rec.getWidth() / 2) / WoodenSlug.PPM, (rec.getY() + rec.getHeight() / 2) / WoodenSlug.PPM);

            //il mondo crea il corpo fisico in base al BodyDef
            body = mondo.createBody(bDef);

            //la forma geometrica del corpo sarà una scatola
            shape.setAsBox((rec.getWidth() / 2) / WoodenSlug.PPM, (rec.getHeight() / 2) / WoodenSlug.PPM);
            fDef.shape = shape;
            
            //indice di filtro, l'indice di filtro indica con quali corpi avrà collisioni,
            //se di indice positivo, avrà collisioni con tutti gli altri indici.
            //se di indice negativi, non avrà collisioni solamente con altri corpi di indice negativo
            fDef.filter.groupIndex = 2;
            body.createFixture(fDef);
            
            //setUserData(), permette di riconoscere a quale classe appartiene il corpo creato
            body.setUserData(gioco);
        }
            
    }
}
