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

/**
 * Class for managing the cards and the boards
 */
public class Board {

    private AmmoDeck ammoDeck = new AmmoDeck();
    private PowerDeck powerDeck = new PowerDeck();
    private WeaponDeck weaponDeck = new WeaponDeck();

    private BoardMap map = new BoardMap();
    private SkullBoard skullBoard = new SkullBoard();

    /**
     * Gives the map of the game where the players are playing
     * @return Map of the game
     */
    public BoardMap getMap(){
        return map;
    }

    /**
     * Gives the current deck of ammo
     * @return The ammo deck
     */
    public AmmoDeck getAmmoDeck() {
        return ammoDeck;
    }

    /**
     * Gives the current deck of powerup cards
     * @return The powerup deck
     */
    public PowerDeck getPowerDeck(){
        return powerDeck;
    }

    /**
     * Gives the current deck of weapon cards
     * @return The weapon deck
     */
    public WeaponDeck getWeaponDeck() {
        return weaponDeck;
    }

    public void setAmmoDeck(AmmoDeck ammoDeck) {
        this.ammoDeck = ammoDeck;
    }

    public void setPowerDeck(PowerDeck powerDeck) {
        this.powerDeck = powerDeck;
    }

    public void setWeaponDeck(WeaponDeck weaponDeck) {
        this.weaponDeck = weaponDeck;
    }

    /**
     * Class for managing the player board
     */
    public class BoardMap {

        private List<GenerationSquare> genPoints = new ArrayList<>();
        private Map<Integer,Square> allSquares = new HashMap<>();
        private Map<Color, ArrayList<Square>> squaresInRoom = new HashMap<>();
        private Map<Integer, String> availableMaps = new HashMap<>();


        public BoardMap(){
            super();
            loadMaps();
        }

        /**
         * Opens the file with all the boards
         * @return A NodeList with all the boards
         * @throws ParserConfigurationException
         * @throws SAXException
         * @throws IOException
         */
        private NodeList openMapFile ()throws ParserConfigurationException, SAXException, IOException {

            String path = "."+ File.separatorChar + "src" + File.separatorChar+
            "main" + File.separatorChar + "resources" + File.separatorChar +"map.xml";
            File inputFile = new File(path);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            return doc.getElementsByTagName("board");


        }

        /**
         * Creates a list with all the available boards
         */
        private void loadMaps(){

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

        /**
         * Lets the user choose the board where to play
         * @return The chosen board
         */
        public int chooseMap(){
            availableMaps.forEach((k,v)-> System.out.println("Map number " + k + " " + v));
            System.out.println("Select map: ");
            Scanner reader = new Scanner(System.in);
            return reader.nextInt();

        }

        public Map<Integer, String> getMaps(){
            return availableMaps;
        }

        /**
         * Builds a new map given a chosen one
         * @param mapNumber Number of the map to choose
         */
        public void createMap(int mapNumber){
            //first parse all the squares in the room then connect them
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

                    if(squaresInRoom.get(color) == null){
                        squaresInRoom.put(color, new ArrayList<>());
                    }

                    if(((Element) square).getElementsByTagName("type").item(0).getTextContent().equals("generationSquare")){
                        GenerationSquare genSquare = new GenerationSquare(id,color,x, y,(List<Weapon>)(List<?>) weaponDeck.getCard(3));
                        genPoints.add( genSquare);
                        allSquares.put(id, genSquare);

                        squaresInRoom.get(color).add(genSquare);
                    }
                    else if(((Element) square).getElementsByTagName("type").item(0).getTextContent().equals("ammoSquare")) {
                        AmmoSquare ammoSquare = new AmmoSquare(id, color, x, y,(AmmoCard) ammoDeck.getCard() );
                        allSquares.put(id, ammoSquare);

                        squaresInRoom.get(color).add(ammoSquare);
                    }
                }

                //conncection of the rooms
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

        /**
         * Gives the generation point given a color
         * @param color Color of the generation point
         * @return Generation point
         */
        public Square getGenerationPoint(Color color){
            for(Square gen: genPoints){
                if(color.equals(gen.getColor())){
                    return gen;
                }
            }
            return null;
        }

        /**
         * Gives the square with the specified id
         * @param id Id of the square
         * @return The square with that id
         */
        public Square getSquare(int id){
            return allSquares.get(id);
        }

        public Map<Color, ArrayList<Square>> getSquaresInRoom(){
            return squaresInRoom;
        }

        public Square getSquare(int x,int y){
        Optional<Map.Entry<Integer, Square>>  temp =  allSquares.entrySet().stream().filter(a -> x == (a.getValue().getX())
            && y == (a.getValue().getY())).findAny();

        //no idea how to fix sonarlint warning
        return temp.isPresent() ? temp.get().getValue() : null;
        }


        /**
         * @return a list of all square in the Map
         */
        public Set<Square> allSquares(){
            return (Set<Square>) allSquares.values();
        }
    }


}
