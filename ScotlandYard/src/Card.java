
public class Card {

	NumberBusCards bus;
	NumberTaxiCards taxi;
	NumberUndergroundCards underground;
	public Card(NumberBusCards bus1, NumberTaxiCards taxi1, NumberUndergroundCards underground1)
	{
		bus.Amount = bus1.Amount;
		taxi.Amount = taxi1.Amount;
		underground.Amount = underground1.Amount;
	}
	
}
