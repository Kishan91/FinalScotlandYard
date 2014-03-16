import java.io.Serializable;

public class Detective extends Player implements Serializable {

	public Detective()
	{
		//assigns 8 bus tickets for the detective
		for(int i = 0; i < 8; i++)
		{
			bus.add(GameState.TicketType.Bus);
		}
		//assigns 10 taxi tickets for the detective
		for(int i = 0; i < 10; i++)
		{
			taxi.add(GameState.TicketType.Taxi);
		}
		//assigns 4 double move tickets for the detective
		for(int i = 0; i < 4; i++) tube.add(GameState.TicketType.Underground);
	}
}
