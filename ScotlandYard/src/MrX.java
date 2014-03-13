import java.util.ArrayList;

public class MrX extends Player {
	//stores visibility of Mr X
	private boolean visible = false;
	
	public MrX()
	{
		//assigns 3 bus tickets and 3 tube tickets for Mr X
		for(int i = 0; i < 3; i++)
		{
			bus.add(GameState.TicketType.Bus); 
			tube.add(GameState.TicketType.Underground);
		}
		//assigns 4 taxi tickets and 4 secret tickets for Mr X
		for(int i = 0; i < 4; i++)
		{
			taxi.add(GameState.TicketType.Taxi);
			Ssecret.add(GameState.TicketType.SecretMove);
		}
		//assigns 2 double move tickets for Mr X
		for(int i = 0; i < 2; i++) Sdouble.add(GameState.TicketType.DoubleMove);
	}
	
	//sets visiblity of Mr X
	public void setVisibile(boolean iVisible)
	{
		visible = iVisible;
	}
	
	//gets visiblity of Mr X
	public boolean isVisible()
	{
		return visible;
	}
}
