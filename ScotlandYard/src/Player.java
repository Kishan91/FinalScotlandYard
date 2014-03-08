import java.util.ArrayList;


public abstract class Player {
	ArrayList<GameState.TicketType> bus = new ArrayList<GameState.TicketType>();
	ArrayList<GameState.TicketType> taxi = new ArrayList<GameState.TicketType>();
	ArrayList<GameState.TicketType> tube = new ArrayList<GameState.TicketType>();
	ArrayList<GameState.TicketType> Sdouble = new ArrayList<GameState.TicketType>();
	ArrayList<GameState.TicketType> Ssecret = new ArrayList<GameState.TicketType>();
	public Integer Position;
	public Integer ID;
	
	//for part 4
	public void move()
	{
	}
	
	public void setPosition(int p)
	{
		Position = p;
	}
	
	public Integer getPosition()
	{
		return Position;
	}
	
	public abstract ArrayList<ArrayList<GameState.TicketType>> getTransport();

}
