package model.board;

import model.card.*;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


import java.io.IOException;
import java.util.*;


public class Board {

    private AmmoDeck ammoDeck = new AmmoDeck();
    private PowerDeck powerDeck = new PowerDeck();
    private WeaponDeck weaponDeck = new WeaponDeck();

    private BoardMap map = new BoardMap();
    private SkullBoard skullBoard = new SkullBoard();

    public BoardMap getMap(){
        return map;
    }

    public AmmoDeck getAmmoDeck() {
        return ammoDeck;
    }
    public PowerDeck getPowerDeck(){
        return powerDeck;
    }
    public WeaponDeck getWeaponDeck() {
        return weaponDeck;
    }

    public class BoardMap {

        List<GenerationSquare> genPoints = new ArrayList<>();
        Map<Integer,Square> allSquares = new HashMap<>();
        Map<Integer, String> availableMaps = new HashMap<>();

        private NodeList openMapFile ()throws ParserConfigurationException, SAXException, IOException {

            File inputFile = new File("./src/main/resources/map.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            return doc.getElementsByTagName("board");


        }

        public void loadMaps(){

            try{
                NodeList boards  = openMapFile();

                for( int i = 0; i < boards.getLength(); i++){
                    Node board = boards.item(i);

                    availableMaps.put(Integer.valueOf(((Element) board).getAttribute("n")),
                            ((Element) board).getAttribute("description"));
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        public int chooseMap(){
            availableMaps.forEach((k,v)-> System.out.println("Map number " + k + " " + v));
            System.out.println("Select map: ");
            Scanner reader = new Scanner(System.in);
            return reader.nextInt();

        }

        public void createMap(int mapNumber){

            try {


                NodeList boards  = openMapFile();

                NodeList squares = ((Element) boards.item(mapNumber)).getElementsByTagName("square");

                for(int i = 0; i < squares.getLength(); i++){
                    Node square = squares.item(i);

                    int id = Integer.parseInt(((Element) square).getAttribute("n"));
                    Color color = Color.valueOf(((Element) square).getElementsByTagName("color").item(0).getTextContent());
                    String[] xy = ((Element) square).getElementsByTagName("xy").item(0).getTextContent().split("-");
                    int x = Integer.parseInt(xy[0]);
                    int y = Integer.parseInt(xy[1]);

                    if(((Element) square).getElementsByTagName("type").item(0).getTextContent().equals("generationSquare")){
                        GenerationSquare genSquare = new GenerationSquare(id,color,x, y,(List<Weapon>)(List<?>) weaponDeck.getCard(3));
                        genPoints.add( genSquare);
                        allSquares.put(id, genSquare);
                    }
                    else if(((Element) square).getElementsByTagName("type").item(0).getTextContent().equals("ammoSquare")) {
                        AmmoSquare ammoSquare = new AmmoSquare(id, color, x, y,(AmmoCard) ammoDeck.getCard() );
                        allSquares.put(id, ammoSquare);
                    }
                }

                for (int i = 0; i < squares.getLength(); i++){
                    Node square = squares.item(i);

                    int id = Integer.parseInt(((Element) square).getAttribute("n"));
                    Node connections = ((Element) square).getElementsByTagName("connection").item(0);
                    NodeList connectionIds = ((Element) connections).getElementsByTagName("id");
                    for (int j = 0; j < connectionIds.getLength(); j++){
                        Node connectionId = connectionIds.item(j);
                        int connId = Integer.parseInt(connectionId.getTextContent());
                        Square toAdd = allSquares.get(connId);
                        allSquares.get(id).addNextSquare(toAdd);
                    }


                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        public Square getGenerationPoint(Color color){
            for(Square gen: genPoints){
                if(color.equals(gen.getColor())){
                    return gen;
                }
            }
            return null;
        }
        public Square getSquare(int id){
            return allSquares.get(id);
        }
    }



}
