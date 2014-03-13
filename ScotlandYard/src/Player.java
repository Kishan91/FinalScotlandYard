import java.util.ArrayList;
import java.util.List;


public abstract class Player {
	//way to distinguish which player is which
	enum playerType 
	{
		Detective, MrX
	}
	//what station the player is currently on
	enum currentStationType
	{
		Taxi, BusTaxi, BusTaxiUnderground
	}
	
	//Arraylists for tickets
	ArrayList<GameState.TicketType> bus = new ArrayList<GameState.TicketType>();
	ArrayList<GameState.TicketType> taxi = new ArrayList<GameState.TicketType>();
	ArrayList<GameState.TicketType> tube = new ArrayList<GameState.TicketType>();
	ArrayList<GameState.TicketType> used = new ArrayList<GameState.TicketType>();
	ArrayList<GameState.TicketType> Sdouble = new ArrayList<GameState.TicketType>();
	ArrayList<GameState.TicketType> Ssecret = new ArrayList<GameState.TicketType>();
	//stores position the player is on
	private Integer Position;
	//id of player
	private Integer ID;
	//type of player
	private playerType type;
	//stores all the edges the player can possibly transverse given his current position
	private List<Edge> nodeNeighbours;
	//given the type of station given the node the player is currently on
	private currentStationType stationType;
	
	//set and get for Position
	public void setPosition(int p)
	{
		Position = p;
	}
	public Integer getPosition()
	{
		return Position;
	}
	
	//set and get for ID
	public void setID(int i)
	{
		ID = i;
	}
	public Integer getID()
	{
		return ID;
	}
	
	//set and get for playerType
	public void setPlayerType(playerType itype){
		type = itype;
	}
	public playerType getPlayerType()
	{
		return type;
	}
	
	//set and get for nodeNeighbours
	public void setNodeNeighbours(List<Edge> inodeNeighbours)
	{
		nodeNeighbours = inodeNeighbours;
	}
	public List<Edge> getNodeNeighbours()
	{
		return nodeNeighbours;
	}
	
	//set and get for station type
	public void setStationType(currentStationType iStationType)
	{
		stationType = iStationType;
	}
	public currentStationType getStationType()
	{
		return stationType;
	}
}
