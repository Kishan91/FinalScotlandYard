import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

/**
 * TO DO LIST: Saturday
 //get double move to work
 //set visibility of mr x on map 
 //BUG - win game - MR X ROUND NUMBER NOT UPDATING
 //make new gui icons etc and use them
 //implement load and save game



 TO DO LIST: Sunday
 //Make code dry, 30 lines per method, 80 characters per line, improve variable and function names if need be
 //Make a list of all references if used + UNDERSTAND all of code before submission
 //animation between nodes - http://zetcode.com/tutorials/javagamestutorial/animation/ < -- LOOK AT THIS - EXTRA
 //other extensions if bothered

 DONE: Friday
 //custom number of detectives -- DONE!
 //Mr X tickets - get from pile -- DONE
 //what to do when players run out of tickets -- DONE
 //custom interface stuff only works if there is a -i argument when running
 //scaling font of current and next player labels
 //Shows current round
 //movePlayer works - 14 is hardcoded - still works cba
 //win state if detectives and mr x overlap
 //what happens if mr x can only move to 3 places with taxi tickets BUT he has no taxi tickets left
 //SORT SCALING PROPERLY - WORKS FOR 1280 x 800 and upwards
 //sorted out newgame bug + dialog box cancel bug
 //make sure detectives don't overlap each other
 //secret move works

 NOT SOLVABLE
 //Error changing number of detectives at start
 *
 IMAGES NEEDED:
 //HAS to be somewhat similar to current layout -- pain to redo all of setLocation completely for every component
 //image for new button
 //image for load button
 //image for save button
 //way of representing current player label
 //way of representing next player label
 //image for mr x tab
 //image for detective tab
 //image for mr x move log tab
 //image for tabbed panes
 //image for whole background
 * 
 * 
 */

/**
 * Class that will hold the state of the game. This is the class that will need
 * to implement the interfaces that we have provided you with
 */
public class GameState implements MapVisualisable, Initialisable,
		PlayerVisualisable, Visualisable, Controllable, customVisualisable {

	// Stores list of Detective players
	private ArrayList<Detective> listDetectives;
	// Stores list of Mr X players
	private ArrayList<MrX> listMrX;
	// Array of Detective start positions
	private final Integer[] DstartPos = new Integer[] { 13, 26, 29, 34, 50, 53,
			91, 94, 103, 112, 117, 132, 138, 141, 155, 174, 197, 198 };
	// Array of Mr X start positions
	private final Integer[] MrXstartPos = new Integer[] { 51, 146, 45, 132,
			106, 78, 127, 71, 172, 170, 166, 35, 104 };
	// String array that stores each line in the pos.txt file except the first
	// line
	private String[] posTxtStrings;
	// List of Mr X ID's
	private ArrayList<Integer> mrXIdList;
	// List of Detective ID's
	private ArrayList<Integer> detectiveIdList;
	// current player ID
	private int currentPlayerID;
	// current game turn
	private int currentTurn = 1;
	private int winningPlayer = -1;
	// graph file
	private Graph graph;
	private List<Integer> visibleTurns = new ArrayList<Integer>(Arrays.asList(3, 8, 13,18));

	/**
	 * Variable that will hold the filename for the map
	 */
	private String mapFilename = "map.jpg";

	/**
	 * Concrete implementation of the MapVisualisable getMapFilename function
	 * 
	 * @return The map filename
	 */
	public String getMapFilename() {
		return mapFilename;
	}

	// initialise game
	@Override
	public Boolean initialiseGame(Integer numberOfDetectives) {
		// tries to read the graph.txt file
		Reader read = new Reader();
		try {
			read.read("graph.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		graph = read.graph();
		// ID initialised as -1 initially
		int ID = -1;
		// List of detectives, List of Mr X players, Mr X ID list and Detective
		// ID list initialised
		listDetectives = new ArrayList<Detective>();
		listMrX = new ArrayList<MrX>();
		mrXIdList = new ArrayList<Integer>();
		detectiveIdList = new ArrayList<Integer>();
		// Whenever an ID is assigned to a detective or Mr X, it is stored in
		// this idxArray
		ArrayList<Integer> idArray = new ArrayList<Integer>(numberOfDetectives);

		// makes a MrX object
		makeMrX(idArray);
		// makes a Detective object, for the number of detectives
		for (int i = 1; i <= numberOfDetectives; i++) {
			makeDetective(ID, idArray, i);

		}

		// splits pos.txt file
		try {
			splitLines();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// currentPlayerID is Mr X
		currentPlayerID = mrXIdList.get(0);

		return null;
	}

	// makes Mr X object
	private void makeMrX(ArrayList<Integer> idArray) {
		MrX mrX = new MrX();
		// sets player type to Mr X
		mrX.setPlayerType(Player.playerType.MrX);
		// makes a random ID for Mr X out of the Mr X array
		int newID = new Random().nextInt(MrXstartPos.length);
		// Assigns Mr X's position to this new ID
		mrX.setPosition(MrXstartPos[newID]);
		// gets the places Mr X can move to from his current position and stores
		// it within the player
		mrX.setNodeNeighbours(graph.edges(mrX.getPosition().toString()));
		// assigns Mr X's ID to 0
		mrX.setID(0);
		// adds Mr X's ID to the idArray
		idArray.add(mrX.getID());
		// adds Mr X to the list of Mr X objects
		listMrX.add(mrX);
		// adds Mr X's ID to list of Mr X ID's
		mrXIdList.add(0);
		// stores the station type for the node Mr X is currently on
		setStationType(mrX);
	}

	// makes Detective object
	private void makeDetective(int ID, ArrayList<Integer> idArray, int i) {
		// sets ID initially to -1
		ID = -1;
		Detective temp = null;

		// until there is no ID collisions between Mr X and the Detective
		while (ID == -1) {
			// gets a new ID for the detective object
			ID = new Random().nextInt(DstartPos.length);
			// if the ID array already contains this ID, there is a collision --
			// sets the ID to -1 in this case and loop happens again
			if (!idArray.contains(DstartPos[ID])) {
				temp = new Detective();
				temp.setPlayerType(Player.playerType.Detective);
				temp.setPosition(DstartPos[ID]);
				temp.setNodeNeighbours(graph.edges(temp.getPosition()
						.toString()));
				idArray.add(DstartPos[ID]);
				setStationType(temp);
			} else
				ID = -1;
		}
		// assigns the detective this calculated ID
		temp.setID(i);
		// adds Detective to the list of Detective objects
		listDetectives.add(temp);
		// adds Detective ID to list of Detective ID's
		detectiveIdList.add(i);
	}

	// splits pos.txt file into strings where each string is a line in pos.txt -
	// here you can access node i by accessing index i in the array
	private void splitLines() throws IOException {
		// reads the pos.txt file and stores the whole file in a string
		String fileContents = readFileAsString("pos.txt");
		// gets rid of the first line of the pos.txt file
		fileContents = fileContents.substring(5);
		// stores every line of the pos.txt file as a string, which is stored in
		// a string array -- if you access index 2, you get the details for Node
		// 2
		posTxtStrings = fileContents.split("\n");
	}

	// reads text file and stores the whole thing in a string - COPIED AND
	// PASTED - HAVE TO FIND ALTERNATIVE OR UNDERSTAND
	private String readFileAsString(String filePath) throws IOException {
		StringBuffer fileData = new StringBuffer();
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
		}
		reader.close();
		return fileData.toString();
	}

	// gets x coordinate for a given node
	@Override
	public Integer getLocationX(Integer nodeId) {
		// takes the required string in the posTxtStrings array - contains the
		// ID, X coordinate and Y coordinate for the node separated by spaces
		// split this string at spaces to separate the values
		String[] coordinates = posTxtStrings[nodeId].split("\\s+");
		// return the 1st item as an integer - X coordinate
		return Integer.parseInt(coordinates[1]);
	}

	// gets y coordinate for a given node
	@Override
	public Integer getLocationY(Integer nodeId) {
		// takes the required string in the posTxtStrings array - contains the
		// ID, X coordinate and Y coordinate for the node separated by spaces
		// split this string at spaces to separate the values
		String[] coordinates = posTxtStrings[nodeId].split("\\s+");
		// return the 2nd item as an integer - Y coordinate
		return Integer.parseInt(coordinates[2]);
	}

	// gets list of Detective ID's
	@Override
	public List<Integer> getDetectiveIdList() {
		return detectiveIdList;
	}

	// gets list of Mr X ID's
	@Override
	public List<Integer> getMrXIdList() {
		return mrXIdList;
	}

	// given a playerID - returns the node they are currently on
	@Override
	public Integer getNodeId(Integer playerId) {
		Integer Position = null;
		// loops through detectives and there is an ID match - returns the
		// detective position
		for (Detective a : listDetectives) {
			if (a.getID().equals(playerId)) {
				Position = a.getPosition();
			}
		}
		// loops through Mr X's and if there is an ID match - returns the Mr X
		// position
		for (MrX a : listMrX) {
			if (a.getID().equals(playerId)) {
				Position = a.getPosition();
			}
		}
		return Position;
	}

	// gets the number of tickets of a certain type, a certain player has given
	// their playerID and ticket type
	@Override
	public Integer getNumberOfTickets(TicketType type, Integer playerId) {
		// stores final answer - set initially to -1
		int size = -1;
		// goes through all the Mr X objects
		for (MrX a : listMrX) {
			// if there is an ID match
			if (a.getID().equals(playerId)) {
				// if ticket match - store the size of the arraylist for this
				// ticket
				if (type.equals(TicketType.Bus))
					size = a.bus.size();
				else if (type.equals(TicketType.Taxi))
					size = a.taxi.size();
				else if (type.equals(TicketType.Underground))
					size = a.tube.size();
				else if (type.equals(TicketType.DoubleMove))
					size = a.Sdouble.size();
				else if (type.equals(TicketType.SecretMove))
					size = a.Ssecret.size();
			}
		}
		// goes through all the Detective objects
		for (Detective a : listDetectives) {
			// if there is an ID match
			if (a.getID().equals(playerId)) {
				// if ticket match - store the size of the arraylist for this
				// ticket
				if (type.equals(TicketType.Bus))
					size = a.bus.size();
				else if (type.equals(TicketType.Taxi))
					size = a.taxi.size();
				else if (type.equals(TicketType.Underground))
					size = a.tube.size();

			}
		}
		// return final answer
		return size;
	}

	// given a player - sets the station type for the node they are currently on
	private void setStationType(Player player) {
		//
		List<TicketType> ticketTypes = new ArrayList<TicketType>();
		// gets all the possible edges the player can transverse given their
		// current node and loops through them
		for (Edge a : player.getNodeNeighbours()) {
			// adds each edge type to the ticketTypes list
			if (a.type().equals(Edge.EdgeType.Bus))
				ticketTypes.add(TicketType.Bus);
			else if (a.type().equals(Edge.EdgeType.Taxi))
				ticketTypes.add(TicketType.Taxi);
			else if (a.type().equals(Edge.EdgeType.Underground))
				ticketTypes.add(TicketType.Underground);
		}

		/*
		 * After adding all the ticketTypes for each edge, if the array contains
		 * a bus and underground combination - it is a taxi/bus/underground
		 * station If the array contains a bus type, it is a taxi/bus station
		 * Otherwise it is just a taxi station Sets the player station type to
		 * this station type
		 */
		if (ticketTypes.contains(TicketType.Bus)
				&& ticketTypes.contains(TicketType.Underground))
			player.setStationType(Player.currentStationType.BusTaxiUnderground);
		else if (ticketTypes.contains(TicketType.Bus))
			player.setStationType(Player.currentStationType.BusTaxi);
		else
			player.setStationType(Player.currentStationType.Taxi);
	}

	// given a playerID - gets the required player and gets the station type,
	// given the node they are currently on - as a String
	public String getStationType(Integer playerId) {
		// stores final result
		String result = null;
		// gets player from playerID
		Player player = getPlayerFromId(playerId);
		// gets player station type and stores result as a string
		if (player.getStationType() == Player.currentStationType.Taxi)
			result = "Taxi";
		else if (player.getStationType() == Player.currentStationType.BusTaxi)
			result = "Bus and Taxi";
		else if (player.getStationType() == Player.currentStationType.BusTaxiUnderground)
			result = "Bus, Taxi and Underground";
		// returns final result
		return result;
	}

	// given a playerID - gets the required player, gets the possible edges they
	// can transverse, and returns all the connected nodes from the current
	// player node
	public List<String> getNodeNeighbours(Integer playerId) {
		Player player = getPlayerFromId(playerId);
		List<String> nodeNeighbours = new ArrayList<String>();
		for (Edge a : player.getNodeNeighbours()) {
			String connectedNode = a.connectedTo(player.getPosition()
					.toString());
			nodeNeighbours.add(connectedNode);
		}
		return nodeNeighbours;
	}

	// gets next player to move
	public Integer getNextPlayerToMove() {
		Integer nextPlayer = 0; // set intially to 0
		// size of Mr X ID list + Detective ID list (BUT because .size() gives
		// the answer starting at 1, we have to deduct 1
		int numberOfPlayers = mrXIdList.size() + detectiveIdList.size() - 1;
		// if the currentPlayerID is the last detective, the next player is set
		// to be Mr X, which in this case is 0
		if (currentPlayerID == numberOfPlayers)
			nextPlayer = mrXIdList.get(0);
		// else increment currentPlayerID
		else
			nextPlayer = currentPlayerID + 1;
		return nextPlayer;
	}

	// gets player given a player ID
	private Player getPlayerFromId(Integer playerId) {
		Player player = null;
		for (MrX a : listMrX) {
			// if there an ID match
			if (a.getID().equals(playerId)) {
				player = (Player) a;
			}
		}
		for (Detective a : listDetectives) {
			// if there is an ID match
			if (a.getID().equals(playerId)) {
				player = (Player) a;
			}
		}
		// returns a player type
		return player;
	}

	// given a playerID, targetNodeID and ticketType, the player is moved
	@Override
	public Boolean movePlayer(Integer playerId, Integer targetNodeId,
			TicketType ticketType) {
		// check initially to false
		boolean check = false;
		// gets the currentNode for the given player -- given his ID
		Integer currentNode = getNodeId(playerId);
		// gets the player given the player ID
		Player currentPlayer = getPlayerFromId(playerId);
		// goes through all the edges the player can possibly transverse
		for (Edge a : currentPlayer.getNodeNeighbours()) {
			// returns the connectedNode - as part of the edge given the current
			// node
			String connectedNode = a.connectedTo(currentNode.toString());
			// if the connected node matches the target node - we have to then
			// check if the edge type and the chosen ticket type match
			if (connectedNode.equals(targetNodeId.toString())) {
				// if they do match - EDGE EXISTS - we have to check if it's
				// possible given the tickets the player has
				if (checkEdge(ticketType, a.type())) {
					// if the player is a detective - we typecast the player as
					// a Detective
					if (currentPlayer.getPlayerType() == Player.playerType.Detective) {
						Detective player = (Detective) currentPlayer;
						// set check initially to true
						check = true;
						MrX mrXPlayer = (MrX) getPlayerFromId(0);
						// if there is a match in this "if clause" i.e. if the
						// move is valid given the player's tickets, the tickets
						// are updated and check remains true
						check = checkValidDetectiveTickets(check, player,
								mrXPlayer, ticketType);
						// if check is true afterwards -- the player has the
						// number of tickets available
						check = checkPositionNotOccupied(check, targetNodeId);
						if (check == true) {
							setNextPlayer((Player) player, targetNodeId);
						} else {
							if ((getStationType(playerId).equals("Taxi") && player.taxi
									.size() == 0)
									|| (getStationType(playerId).equals(
											"Bus and Taxi")
											&& player.bus.size() == 0 && player.taxi
											.size() == 0)) {
								noPlacesToMoveTo(player);
								return true;
								//move is not possible so return false
							} else return false;
							// move is not possible - so return false
						}
						// if the player is a Mr X player - we typecast the
						// player as a Mr X object
					} else {
						// same kind of stuff as before
						MrX player = (MrX) currentPlayer;
						check = true;
						check = checkValidMrXTickets(check, player, ticketType);
						if (check == true) {
							setNextPlayer((Player) player, targetNodeId);
						} else {
							return false;
						}
					}
				}
			}
		}
		// returns final answer
		return check;
	}

	private boolean checkPositionNotOccupied(boolean check, Integer targetNodeId) {
		for (Detective detective : listDetectives) {
			if (detective.getPosition().equals(targetNodeId)) {
				check = false;
			}
		}
		return check;
	}

	private void noPlacesToMoveTo(Detective player) {
		JOptionPane.showMessageDialog(null, "No possible places to move to",
				"Player move", JOptionPane.ERROR_MESSAGE);
		currentPlayerID = getNextPlayerToMove();
		currentTurn++;
	}

	private boolean checkValidMrXTickets(boolean check, MrX player,
			TicketType ticketType) {
		if (ticketType == TicketType.Bus && player.bus.size() > 0)
			player.bus.remove(player.bus.size() - 1);
		else if (ticketType == TicketType.Taxi && player.taxi.size() > 0)
			player.taxi.remove(player.taxi.size() - 1);
		else if (ticketType == TicketType.Underground && player.tube.size() > 0)
			player.tube.remove(player.tube.size() - 1);
		else if (ticketType == TicketType.SecretMove
				&& player.Ssecret.size() > 0)
			player.Ssecret.remove(player.Ssecret.size() - 1);
		else if (ticketType == TicketType.DoubleMove
				&& player.Sdouble.size() > 0)
			player.Sdouble.remove(player.Sdouble.size() - 1);
		else
			check = false;
		player.used.add(ticketType);
		return check;
	}

	private boolean checkValidDetectiveTickets(boolean check, Detective player,
			MrX mrXPlayer, TicketType ticketType) {
		if (ticketType == TicketType.Bus && player.bus.size() > 0) {
			player.bus.remove(player.bus.size() - 1);
			mrXPlayer.bus.add(ticketType);
		} else if (ticketType == TicketType.Taxi && player.taxi.size() > 0) {
			player.taxi.remove(player.taxi.size() - 1);
			mrXPlayer.taxi.add(ticketType);
		} else if (ticketType == TicketType.Underground
				&& player.tube.size() > 0) {
			player.tube.remove(player.tube.size() - 1);
			mrXPlayer.tube.add(ticketType);
		}
		// otherwise check is set to false
		else
			check = false;
		player.used.add(ticketType);
		return check;
	}

	private void setNextPlayer(Player player, Integer targetNodeId) {
		player.setPosition(targetNodeId);
		currentPlayerID = getNextPlayerToMove();
		currentTurn++;
		player.setNodeNeighbours(graph.edges(targetNodeId.toString()));
		setStationType(player);
	}

	// checks if edge type and ticket type match
	private boolean checkEdge(TicketType type1, Edge.EdgeType type2) {
		// check set initially to false
		boolean check = false;
		// if ticket type and edge are both bus - return true
		if (type1 == TicketType.Bus && type2 == Edge.EdgeType.Bus) {
			check = true;
			// same as before but with taxi
		} else if (type1 == TicketType.Taxi && type2 == Edge.EdgeType.Taxi) {
			check = true;
			// same as before but with underground
		} else if (type1 == TicketType.Underground
				&& type2 == Edge.EdgeType.Underground) {
			check = true;
		} else if (type1 == TicketType.SecretMove)
			check = true;
		// otherwise false is returned
		return check;
	}

	// gets the nearest node given the mouse click x,y location
	@Override
	public Integer getNodeIdFromLocation(Integer xPosition, Integer yPosition) {
		// stores final answer - intially set to -1
		int newNode = -1;
		// gets current node the current player is on
		String currentNode = getNodeId(currentPlayerID).toString();
		// gets all the edges the player can possibly transverse
		List<Edge> nodeNeighbours = graph.edges(currentNode);
		// if no possible moves to go to, return -2
		if (nodeNeighbours.size() == 0) return -2;
		for (Edge a : nodeNeighbours) {
			// gets the connected node given the current node for each edge
			String connectedNode = a.connectedTo(currentNode);
			// gets the x and y coordinate of the edge
			Integer nodeXCoordinate = getLocationX(Integer
					.valueOf(connectedNode));
			Integer nodeYCoordinate = getLocationY(Integer
					.valueOf(connectedNode));
			// if the mouse click is within region of a certain node that can be
			// travelled to
			if (withinNodeRegion(xPosition, yPosition, nodeXCoordinate,
					nodeYCoordinate)) {
				// return this certain node
				newNode = Integer.parseInt(connectedNode);
			}
		}
		return newNode;
	}

	// checks if given a mouse click, if it's x and y coordinates are close enough to the node's x and y coordinates, then that the move is valid
	private boolean withinNodeRegion(Integer xPosition, Integer yPosition,
			Integer nodeXCoordinate, Integer nodeYCoordinate) {
		boolean check = false;
		int distance = (int) Math.sqrt((Math.pow((nodeXCoordinate - xPosition),
				2)) + Math.pow((nodeYCoordinate - yPosition), 2));
		if (distance < 14)
			check = true;
		else
			check = false;
		return check;
	}

	@Override
	public Integer getWinningPlayerId() {
		return winningPlayer;
	}

	public Boolean isGameOver() {
		boolean gameOver = false;
		int totalNumber = listMrX.size() + listDetectives.size();
		int roundNumber = (int) Math.ceil((double) currentTurn / totalNumber);
		Test.printf(roundNumber);
		for (MrX player : listMrX) {
			for (Detective d : listDetectives) {
				if (d.getPosition().equals(player.getPosition())) {
					winningPlayer = d.getID();
					gameOver = true;
				}
			}
		}
		if (roundNumber == 23) {
			winningPlayer = mrXIdList.get(0);
			gameOver = true;
		}
		return gameOver;
	}

	// IMPLEMENT LATER
	@Override
	public Boolean saveGame(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	// IMPLEMENT LATER
	@Override
	public Boolean loadGame(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TicketType> getMoveList(Integer playerId) {
		Player player = getPlayerFromId(playerId);
		return player.used;
	}

	// checks if a player is visible, given -- USE FIX LATER
	@Override
	public Boolean isVisible(Integer playerId) {
		boolean visible = false;

		// Test.printf("ROUND" + roundNumber);
		/*
		 * if (detectiveIdList.contains(playerId)) visible = true; else {
		 * for(MrX a : listMrX) {
		 * 
		 * //if current player is a detective && it is a visible turn - display
		 * Mr X
		 * 
		 * 
		 * if(a.getID().equals(playerId) && currentPlayerID != 0 &&
		 * visibleTurns.contains(roundNumber)) { a.setVisibile(true); visible =
		 * true;z } else{ a.setVisibile(false); visible = false; } } }
		 */
		return visible;
	}
}