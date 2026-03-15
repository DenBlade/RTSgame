package com.example.rtsgame.map;

import com.example.rtsgame.Config;
import com.example.rtsgame.ResourceType;
import com.example.rtsgame.map.tiles.ResourceTile;
import com.example.rtsgame.map.tiles.Tile;
import com.example.rtsgame.map.tiles.TileFactory;
import com.example.rtsgame.map.tiles.buildings.BuildingTile;
import com.example.rtsgame.map.tiles.buildings.BuildingType;
import com.example.rtsgame.map.tiles.buildings.CastleTile;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MapManager {
    int mapWidth;
    int mapHeight;
    Canvas canvas;
    GraphicsContext gc;
    int[] tilesetIds;
    Image[] tilesetImages;
    Tile[][] tilesData;
    BuildingPrefab castlePrefab;

    public MapManager(String fileName) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream is = getClass().getResourceAsStream("/map/"+fileName);
        Document doc = builder.parse(is);
        Element map = doc.getDocumentElement();

        mapWidth = Integer.parseInt(map.getAttribute("width"));
        mapHeight = Integer.parseInt(map.getAttribute("height"));
        tilesData = new Tile[mapWidth][mapHeight];
        canvas = new Canvas(mapWidth * Config.TILE_WIDTH, mapHeight * Config.TILE_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false);

        NodeList layers = doc.getElementsByTagName("layer");
        NodeList tilesets = doc.getElementsByTagName("tileset");
        tilesetImages = new Image[tilesets.getLength()];
        tilesetIds = new int[tilesets.getLength()];

        castlePrefab = loadBuildingFromTMX("castleBuilding.tmx");

        for(int i = 0; i < tilesets.getLength(); i++) { // getting used tilesets
            Element tilesetItem = (Element) tilesets.item(i);
            String tilesetFileName = tilesetItem.getAttribute("source");
            tilesetFileName = tilesetFileName.split("\\.")[0];
            tilesetImages[i] = new Image(getClass().getResourceAsStream("/map/"+tilesetFileName+".png"));
            tilesetIds[i] = Integer.parseInt(tilesetItem.getAttribute("firstgid"));
        }

        for(int layerId = 0; layerId < layers.getLength(); layerId++) { // drawing layers
            Element layer = (Element) layers.item(layerId);
            Element data = (Element) layer.getElementsByTagName("data").item(0);
            String[] tiles = data.getTextContent().trim().split(",");
            TileFactory tileFactory;
            switch (layer.getAttribute("name")){
                case "bottom":
                    tileFactory = (x,y) -> new Tile(x, y, true);
                    break;
                case "collisions":
                    tileFactory = (x,y) -> new Tile(x, y, false);
                    break;
                case "gold_mines":
                    tileFactory = (x,y) -> new ResourceTile(x, y, ResourceType.GOLD, 1);
                    break;
                    default:
                        tileFactory = (x,y) -> new Tile(x, y, true);
                        break;

            }
            this.processLayer(tiles, tileFactory);
        }
//        //drawing PlayerCastle
//        gc.drawImage(castlePrefab.getImage(), Config.PLAYER_CASTLE_POS[0]*Config.TILE_WIDTH, Config.PLAYER_CASTLE_POS[1]*Config.TILE_HEIGHT);
//        //storing it in memory
//        CastleTile.incrementBuildingsCount();
//        final int playerCastleId = CastleTile.getBuildingsCount();
//        TileFactory playerCastleFactory = (x, y) -> new CastleTile(playerCastleId, x, y, false);
//        setTilesData(Config.PLAYER_CASTLE_POS[0], Config.PLAYER_CASTLE_POS[1], castlePrefab.getWidth(), castlePrefab.getHeight(), playerCastleFactory);
        placeCastle(false, Config.PLAYER_CASTLE_POS[0], Config.PLAYER_CASTLE_POS[1]);

//        //drawing EnemyCastle
//        gc.drawImage(castlePrefab.getImage(), Config.ENEMY_CASTLE_POS[0]*Config.TILE_WIDTH, Config.ENEMY_CASTLE_POS[1]*Config.TILE_HEIGHT);
//        //storing it in memory
//        CastleTile.incrementBuildingsCount();
//        final int enemyCastleId = CastleTile.getBuildingsCount();
//        TileFactory enemyCastleFactory = (x, y) -> new CastleTile(enemyCastleId, x, y, true);
//        setTilesData(Config.ENEMY_CASTLE_POS[0], Config.ENEMY_CASTLE_POS[1], castlePrefab.getWidth(), castlePrefab.getHeight(), enemyCastleFactory);
        placeCastle(true, Config.ENEMY_CASTLE_POS[0], Config.ENEMY_CASTLE_POS[1]);

    }


    private void processLayer(String[] tiles, TileFactory tileFactory){ //drawing main map layer
        Image tileset = null;
        for (int y = 0; y < mapHeight; y++) { // drawing tiles
            for (int x = 0; x < mapWidth; x++) {
                int index = y * mapWidth + x;
                int tileId = Integer.parseInt(tiles[index].trim());

                if (tileId == 0) continue; // empty tile
                for(int tilesetId = tilesetIds.length-1; tilesetId>=0; tilesetId--){ // checking to which tileset tile belongs
                    if(tileId>=tilesetIds[tilesetId]){
                        tileId = tileId-tilesetIds[tilesetId]+1;
                        tileset = tilesetImages[tilesetId];
                        break;
                    }
                }
                setTilesData(x,y,tileFactory);
                this.drawTile(gc, tileId, tileset, x, y);
            }
        }
    }
    private void setTilesData(int x, int y, TileFactory tileFactory){
        tilesData[x][y] = tileFactory.createTile(x, y);
    }
    private void setTilesData(int x, int y, int width, int height, TileFactory tileFactory){
        for(int i = x; i < x + width; i++) {
            for(int j = y; j < y + height; j++) {
                tilesData[i][j] = tileFactory.createTile(i, j);
            }
        }
    }
    private void drawLayer(GraphicsContext gc, Image tileset, int mapWidth, int mapHeight, String[] tiles){ //drawing layer to graphicsContext
        for (int y = 0; y < mapHeight; y++) { // drawing tiles
            for (int x = 0; x < mapWidth; x++) {
                int index = y * mapWidth + x;
                int tileId = Integer.parseInt(tiles[index].trim());

                if (tileId == 0) continue; // empty tile
                this.drawTile(gc, tileId, tileset, x, y);
            }
        }
    }

    private void drawTile(GraphicsContext gc, int tileId, Image tileset, int x, int y){
        int tilesetColumns = (int)(tileset.getWidth() / Config.TILE_WIDTH);
        int tileIndex = tileId - 1;
        int sx = (tileIndex % tilesetColumns) * Config.TILE_WIDTH; // tile coordinates on tileset
        int sy = (tileIndex / tilesetColumns) * Config.TILE_HEIGHT;
        gc.drawImage(
                tileset,
                sx, sy, Config.TILE_WIDTH, Config.TILE_HEIGHT,
                x * Config.TILE_WIDTH, y * Config.TILE_HEIGHT,
                Config.TILE_WIDTH, Config.TILE_HEIGHT
        );
    }
    private BuildingPrefab loadBuildingFromTMX(String fileName) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream is = getClass().getResourceAsStream("/map/"+fileName);
        Document doc = builder.parse(is);
        Element map = doc.getDocumentElement();

        int mapWidth = Integer.parseInt(map.getAttribute("width"));
        int mapHeight = Integer.parseInt(map.getAttribute("height"));
        Canvas canvas = new Canvas(mapWidth * Config.TILE_WIDTH, mapHeight * Config.TILE_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false);

        Element layer = (Element) doc.getElementsByTagName("layer").item(0);
        Element data = (Element) layer.getElementsByTagName("data").item(0);
        String[] tiles = data.getTextContent().trim().split(",");
        Element tileset = (Element) doc.getElementsByTagName("tileset").item(0);
        String tilesetFileName = tileset.getAttribute("source").split("\\.")[0];
        Image tilesetImage = new Image(getClass().getResourceAsStream("/map/"+tilesetFileName+".png"));

        drawLayer(gc, tilesetImage, mapWidth, mapHeight, tiles);
        WritableImage buildingImage = new WritableImage(mapWidth * Config.TILE_WIDTH, mapHeight * Config.TILE_HEIGHT);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        canvas.snapshot(params, buildingImage);
        return new BuildingPrefab(buildingImage, mapWidth, mapHeight);
    }
    public void placeCastle(boolean ownByAI, int tileX, int tileY){
        //drawing PlayerCastle
        gc.drawImage(castlePrefab.getImage(), tileX*Config.TILE_WIDTH, tileY*Config.TILE_HEIGHT);
        //storing it in memory
        CastleTile.incrementBuildingsCount();
        final int playerCastleId = CastleTile.getBuildingsCount();
        TileFactory playerCastleFactory = (x, y) -> new CastleTile(playerCastleId, x, y, ownByAI);
        setTilesData(tileX, tileY, castlePrefab.getWidth(), castlePrefab.getHeight(), playerCastleFactory);
    }
    public BuildingPrefab getBuildingPrefab(BuildingType buildingType){
        switch(buildingType){
            case CASTLE -> {
                return castlePrefab;
            }
        }
        return null;
    }

    public Canvas getCanvas(){
        return canvas;
    }
    public void debugWriteTilesData(){
        for(int i = 0; i<tilesData.length; i++){
            for (int j = 0; j<tilesData[i].length; j++){
                System.out.print((tilesData[i][j] instanceof BuildingTile)+" ");
            }
            System.out.println();
        }
    }
    public Tile getTile(int[] coords){
        return tilesData[coords[0]][coords[1]];
    }
    public Tile getTile(int x, int y){
        return tilesData[x][y];
    }
    public Tile getTileAt(double x, double y){
        return getTile(convertToTileCoordinates(new double[]{x, y}));
    }
    public boolean isTileTraversable(int x, int y){
        return tilesData[x][y].isTraversable();
    }
    public Tile[][] getTilesData(){
        return tilesData;
    }
    public static double[] convertToWorldCoordinates(int[] coords){
        return new double[]{coords[0]*Config.TILE_WIDTH,coords[1]*Config.TILE_HEIGHT};
    }
    public static int[] convertToTileCoordinates(double[] coords){
        return new int[]{(int)coords[0]/Config.TILE_WIDTH,(int)coords[1]/Config.TILE_HEIGHT};
    }
    public boolean isTraversableAt(double x, double y){
        int[] coords = convertToTileCoordinates(new double[]{x,y});
        return isTileTraversable(coords[0],coords[1]);
    }
    public List<Tile> getAdjacentTiles(int x, int y){
        List<Tile> list = new ArrayList<>();
        if(tilesData[x-1][y].isTraversable()) list.add(tilesData[x-1][y]);
        if(tilesData[x][y-1].isTraversable()) list.add(tilesData[x][y-1]);
        if(tilesData[x+1][y].isTraversable()) list.add(tilesData[x+1][y]);
        if(tilesData[x][y+1].isTraversable()) list.add(tilesData[x][y+1]);
        return list;
    }
    public boolean isInBounds(int x, int y){
        return (x >= 0 && x < mapWidth && y >= 0 && y < mapHeight);
    }
}
