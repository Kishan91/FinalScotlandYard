import java.util.ArrayList;


public class Detective extends Player {

	public Detective()
	{
		for(int i = 0; i < 8; i++)
		{
			bus.add(GameState.TicketType.Bus);
		}
		for(int i = 0; i < 10; i++)
		{
			taxi.add(GameState.TicketType.Taxi);
		}
		
		for(int i = 0; i < 4; i++) tube.add(GameState.TicketType.DoubleMove);
		
		
	}
	
	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPosition() {
		// TODO Auto-generated method stub
		return 0;
	}
	/*
	public ArrayList<Card> getCards()
	{
		return null;
	}
	
	private void setCards()
	{
		
	}
	*/

}
