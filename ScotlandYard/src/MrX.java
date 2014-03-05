import java.util.ArrayList;

public class MrX extends Player {

	private boolean visible = false;
	public MrX()
	{
		for(int i = 0; i < 3; i++)
		{
			bus.add(GameState.TicketType.Bus); 
			tube.add(GameState.TicketType.Underground);
		}
		for(int i = 0; i < 4; i++)
		{
			taxi.add(GameState.TicketType.Taxi);
			Ssecret.add(GameState.TicketType.SecretMove);
		}
		
		for(int i = 0; i < 2; i++) Sdouble.add(GameState.TicketType.DoubleMove);
	}
	
	// for part 4 ye
	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isVisible()
	{
		return visible;	
	}


	@Override
	public ArrayList<ArrayList<GameState.TicketType>> getTransport() {
		// TODO Auto-generated method stub
		ArrayList<ArrayList<GameState.TicketType>> tickets = new ArrayList<ArrayList<GameState.TicketType>>();
		tickets.add(bus);
		tickets.add(tube);
		tickets.add(taxi);
		tickets.add(Ssecret);
		tickets.add(Sdouble);
		return tickets;
	}
	
	
	/*public ArrayList<Card> getUsedTransport()
	{
		return null;
	}
	
	public void specialMove()
	{
		
	}
	
	*/
	
}
