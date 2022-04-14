package location;

import exception.otherClientException.IllegalInputException;

public class TrainStation extends CommonLocation{

	//constructor
		public TrainStation(String nameOfLocation, double latitude, double longitude) throws IllegalInputException{
			super(nameOfLocation, true, latitude, longitude);
		}
}
