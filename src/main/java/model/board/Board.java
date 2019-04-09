package model.board;

import model.card.AmmoDeck;
import model.card.PowerDeck;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


import java.util.List;
import java.util.Scanner;


public class Board {

    private AmmoDeck ammoDeck = new AmmoDeck();
    private PowerDeck powerDeck = new PowerDeck();

    public BoardMap map = new BoardMap();
    private SkullBoard skullBoard = new SkullBoard();

    public Board(){

    }


    public class BoardMap {

        List<GenerationSquare> genPoints;

        BoardMap(){
            loadMaps();
        }

        private void createMap(){
            //load from file
            //choose map

        }
        private void loadMaps(){

            try{

                File inputFile = new File("./src/main/resources/map.xml");

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(inputFile);
                doc.getDocumentElement().normalize();

                System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
                NodeList boards  = doc.getElementsByTagName("board");
                //NodeList boards = doc.getChildNodes();
                System.out.println("----------------------------");

                for( int i = 0; i < boards.getLength(); i++){
                    Node board = boards.item(i);
                    System.out.println("\nCurrent Element :" + board.getNodeName());
                    System.out.println("board number : "+((Element) board).getAttribute("n"));
                    System.out.println("board number : "+((Element) board).getAttribute("description"));


                }

                System.out.println("Choose a map:");
                Scanner reader = new Scanner(System.in);
                int mapNumber = 0; //reader.nextLine();

                System.out.println("your number: " + mapNumber);

                NodeList squares = ((Element) boards.item(mapNumber)).getElementsByTagName("square");

                for(int i = 0; i < squares.getLength(); i++){
                    Node square = squares.item(i);
                    System.out.println("\nCurrent Element :" + square.getNodeName());


                    System.out.println("board number : "+((Element) square).getAttribute("n"));
                    System.out.println("square color : "+((Element) square).getElementsByTagName("color").item(0).getTextContent());
                    System.out.println("square color : "+((Element) square).getElementsByTagName("type").item(0).getTextContent());

                }


                /*
                for(int i = 0; i < boards.getLength(); i++){
                    Node nNode = boards.item(i);
                    //System.out.println("\nCurrent Element :" + nNode.getNodeName());

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        if(eElement.getAttribute("n").equals(""+mapNumber) ){
                            System.out.println("printing");



                        }
                        break;


                    }


                }

*/


            }
            catch (Exception e){
                e.printStackTrace();
            }
        }


        public Square giveGenerationPoint(Color color){
            for(Square gen: genPoints){
                if(color.equals(gen.getColor())){
                    return gen;
                }
            }
            return null;
        }
    }



}
