package locationChange;

import exception.otherClientException.IllegalInputException;
import location.Location;

public interface LocationCouldChange {
//提供更改位置的方法
	/**
	 * change the location,request this location could not be same as the object's location
	 * @param thisLocation , the location to be changed to
	 * @throws IllegalInputException if thisLocation equals to former location
	 */
	public void changeSingleLocation(Location thisLocation) throws IllegalInputException;
}
