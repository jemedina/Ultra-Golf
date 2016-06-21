package com.uaa.ultragolf.Levels;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.uaa.ultragolf.Global.*;
import com.uaa.ultragolf.Global.Point;

import java.awt.*;

public class LevelLoader {
    private World world;
    private Body pelotaBody , holeBody, watterBody;
    private int numLevel;
    private TiledMap map;
    private Point holeLocation;
    private boolean restore;
    private Point restoreLocation;
    private OrthogonalTiledMapRenderer mapRenderer;
    public LevelLoader(World world, Body pelotaBody, Body holeBody, int numLevel,TiledMap map) {
        this.world = world;
        this.pelotaBody = pelotaBody;
        this.holeBody = holeBody;
        this.numLevel = numLevel;
        this.map = map;
        restore = false;
    }
    public void setPelotaRestore(Point loc) {
        restore = true;
        restoreLocation = loc;
    }
    public OrthogonalTiledMapRenderer loadMapRenderer() {
        return new OrthogonalTiledMapRenderer(map,1/Constantes.PPM);
    }
    public Dimension getMapDimension() {
        Dimension d = new Dimension();
        Texture t = new Texture("levels/Fondo" + numLevel + ".png");
        d.setSize(t.getWidth(),t.getHeight());
        t.dispose();
        return d;
    }
    public Body getPelotaBody() {
        return pelotaBody;
    }
    public Body getHoleBody() {
        return holeBody;
    }
    public Body getWatterBody() { return watterBody; }
    public void loadLevel() {
        //Cargar el mapa
        map = new TmxMapLoader().load("levels/level" + numLevel + ".tmx");
        //Cargar objetos
        MapObjects mapObjects = map.getLayers().get("objetos").getObjects();
        for(MapObject object : mapObjects) {
            //Verificar si el objeto es la pelota o el hoyo
            /*Objeto pelota*/
            if(object.getProperties().get("pelota") != null) {
                EllipseMapObject ellipseMapObject = (EllipseMapObject)object;
                float x = ellipseMapObject.getEllipse().x/ Constantes.PPM;
                float y = ellipseMapObject.getEllipse().y/ Constantes.PPM;
                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyDef.BodyType.DynamicBody;
                if(restore)
                    bodyDef.position.set(restoreLocation.x,restoreLocation.y);
                else
                    bodyDef.position.set(x,y);
                bodyDef.fixedRotation = true;
                pelotaBody = world.createBody(bodyDef);
                FixtureDef fixtureDef = new FixtureDef();
                CircleShape shape = new CircleShape();
                shape.setRadius(0.3f);
                fixtureDef.shape = shape;
                fixtureDef.restitution = 0.4f;
                fixtureDef.density = 0.5f;
                fixtureDef.friction = 0.05f;
                pelotaBody.createFixture(fixtureDef);
            } else { /*Objetos del universo (normales)*/
                ChainShape shape = new ChainShape();
                float[] vertices = ((PolylineMapObject) object).getPolyline().getTransformedVertices();
                //float[] vertices = {10,20,30,30};
                for(int i = 0 ; i < vertices.length ; i++)
                    vertices[i] = vertices[i]/ Constantes.PPM;
                shape.createChain(vertices);
                //Construccion de dicho objeto
                BodyDef def = new BodyDef();
                def.type = BodyDef.BodyType.StaticBody;
                Body body = world.createBody(def);
                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = shape;
                fixtureDef.density = 1;
                body.createFixture(fixtureDef);
                if(object.getProperties().get("rozamiento")!=null){
                    fixtureDef.friction = Float.valueOf((String) object.getProperties().get("rozamiento"));
                    if(Float.valueOf((String) object.getProperties().get("rozamiento")) > 0.6f){
                        body.getFixtureList().first().setUserData("tierra");
                    } else{
                        body.getFixtureList().first().setUserData("common");
                    }
                }
                //Revisar si es el objeto hoyo
                if(object.getProperties().get("hoyo") != null){
                    holeLocation = new Point(vertices[0],vertices[1]);
                    holeBody = body;
                    holeBody.getFixtureList().first().setUserData("hole");
                }
                else if(object.getProperties().get("agua") != null){
                    watterBody = body;
                    watterBody.getFixtureList().first().setUserData("watter");
                }
                shape.dispose();
            }
        }
    }

    public Point getHoleLocation() {
        return holeLocation;
    }
}
