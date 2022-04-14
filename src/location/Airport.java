package location;

import exception.otherClientException.IllegalInputException;

public class Airport extends CommonLocation{

	//constructor
	public Airport(String nameOfLocation, double latitude, double longitude) throws IllegalInputException{
		super(nameOfLocation, true, latitude, longitude);
	}
	
}
