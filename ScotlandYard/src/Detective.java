import java.util.ArrayList;


public class Detective extends Player {

	public Detective()
	{
		NumberBusCards bus = new NumberBusCards();
		NumberTaxiCards taxi = new NumberTaxiCards();
		NumberUndergroundCards underground = new NumberUndergroundCards();
		bus.Amount = 8;
		taxi.Amount = 10;
		underground.Amount = 4;
		
		
		
		
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
	
	public ArrayList<Card> getCards()
	{
		return null;
	}
	
	private void setCards()
	{
		
	}

}
