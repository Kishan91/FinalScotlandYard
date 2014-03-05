import java.util.ArrayList;


public abstract class Player {
	ArrayList<GameState.TicketType> bus = new ArrayList<GameState.TicketType>();
	ArrayList<GameState.TicketType> taxi = new ArrayList<GameState.TicketType>();
	ArrayList<GameState.TicketType> tube = new ArrayList<GameState.TicketType>();
	ArrayList<GameState.TicketType> Sdouble = new ArrayList<GameState.TicketType>();
	ArrayList<GameState.TicketType> Ssecret = new ArrayList<GameState.TicketType>();
	private int Position;
	
	//for part 4
	public void move()
	{
	}
	
	public void setPosition(int p)
	{
		Position = p;
	}
	
	public  int getPosition()
	{
		return Position;
	}
	
	public abstract ArrayList<ArrayList<GameState.TicketType>> getTransport();

}
