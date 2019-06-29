package model.board;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Class for managing the player board
 */
public class BoardGenerator {

    private List<GenerationSquare> genPoints = new ArrayList<>();
    private Map<Integer,Square> allSquares = new HashMap<>();
    private Map<Color, ArrayList<Square>> squaresInRoom = new EnumMap<>(Color.class);
    private Map<Integer, String> availableMaps = new HashMap<>();
    private Board board;


    /**
     * Constructor for loading boards
     * @param board the current playing board
     */
    public BoardGenerator(Board board){
        super();
        this.board = board;
        loadMaps();
    }

    /**
     * Opens the file with all the boards
     * @return A NodeList with all the boards
     * @throws ParserConfigurationException when there is an error in parsing
     * @throws SAXException when there is an error in parsing
     * @throws IOException when there is an error in opening the file
     */
    private NodeList openMapFile ()throws ParserConfigurationException, SAXException, IOException {

        InputStream mapInput = BoardGenerator.class.getResourceAsStream("/map.xml");
        //String path = "."+ File.separatorChar + "src" + File.separatorChar+
        //        "main" + File.separatorChar + "resources" + File.separatorChar + "map.xml";
        //File inputFile = new File(path);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new InputSource(mapInput));
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
                Node loadedBoard = boards.item(i);

                availableMaps.put(Integer.valueOf(((Element) loadedBoard).getAttribute("n")),
                        ((Element) loadedBoard).getAttribute("description"));
            }

        }
        catch (Exception e){
            Logger logger = Logger.getLogger(BoardGenerator.class.getName());
            logger.log(Level.WARNING, "Map not loaded", e);
        }
    }

    public Map<Integer, String> getMaps(){
        return availableMaps;
    }


    /**
     * Builds a new map given a chosen one
     * @param mapNumber Number of the map to choose
     * @return a map that has been generated to be used
     */
    public GameBoard createMap(int mapNumber){
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

                squaresInRoom.computeIfAbsent(color, k -> new ArrayList<>());

                if(((Element) square).getElementsByTagName("type").item(0).getTextContent().equals("generationSquare")){
                    GenerationSquare genSquare = new GenerationSquare(id,color,x, y, board.getWeaponDeck().getCard(3));
                    genPoints.add( genSquare);
                    allSquares.put(id, genSquare);

                    squaresInRoom.get(color).add(genSquare);
                }
                else if(((Element) square).getElementsByTagName("type").item(0).getTextContent().equals("ammoSquare")) {
                    AmmoSquare ammoSquare = new AmmoSquare(id, color, x, y, board.getAmmoDeck().getCard() );
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
                    allSquares.get(id).addNeighbourSquare(toAdd);
                }


            }
        }
        catch (Exception e){
            Logger logger = Logger.getLogger(BoardGenerator.class.getName());
            logger.log(Level.WARNING, "Problem in loading map", e);
        }

        return new GameBoard(genPoints, allSquares, squaresInRoom, mapNumber, availableMaps.get(mapNumber));
    }



}
