import java.util.ArrayList;


public class MrX extends Player {

	
	public MrX()
	{
		NumberBusCards bus = new NumberBusCards();
		NumberTaxiCards taxi = new NumberTaxiCards();
		NumberUndergroundCards underground = new NumberUndergroundCards();
		NumberBlackCards black = new NumberBlackCards();
		NumberDoubleCards doublecard = new NumberDoubleCards();
		bus.Amount = 3;
		taxi.Amount = 4;
		underground.Amount = 3;
		black.Amount = 4;
		doublecard.Amount = 2;
		
		
		
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
	
	public boolean isVisible()
	{
		return false;
		
	}
	
	public ArrayList<Card> getUsedTransport()
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
	
	
	
}
