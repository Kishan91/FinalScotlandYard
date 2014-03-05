import java.util.ArrayList;


public abstract class Player {
	Colour colour;
	
	ArrayList<GameState.TicketType> bus = new ArrayList<GameState.TicketType>();
	ArrayList<GameState.TicketType> taxi = new ArrayList<GameState.TicketType>();
	ArrayList<GameState.TicketType> tube = new ArrayList<GameState.TicketType>();
	ArrayList<GameState.TicketType> Sdouble = new ArrayList<GameState.TicketType>();
	ArrayList<GameState.TicketType> Ssecret = new ArrayList<GameState.TicketType>();
	private int Position;
	
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
	
	public String getTransport()
	{
		return null;
		
	}
}
