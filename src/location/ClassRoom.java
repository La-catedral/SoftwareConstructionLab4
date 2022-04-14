package location;

import exception.otherClientException.IllegalInputException;

public class ClassRoom extends CommonLocation{

	//constructor
	public ClassRoom(String nameOfLocation, double latitude, double longitude) throws IllegalInputException{
		super(nameOfLocation, false, latitude, longitude);
	}
	
}
