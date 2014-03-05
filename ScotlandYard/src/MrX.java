


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
	
	
	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isVisible()
	{
		return visible;
		
	}
	/*public ArrayList<Card> getUsedTransport()
	{
		return null;
	}
	
	public void specialMove()
	{
		
	}
	
	public ArrayList<Card> getSpecial()
	{
		return null;
	}
	
	private void setSpecial()
	{
		
	}
	*/
	
}
