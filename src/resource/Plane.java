package resource;

import exception.otherClientException.IllegalInputException;

/**
 *immutable ,define the plane object
 */
public class Plane implements Resource {
//immutable 
	
	private final String ID;
	private final String type;
	private final int seats;
	private final double age;
	
	// Abstraction function:
    // 	AF(ID,type,seats,age) = the ID of the plane,what type is the plane,how many seats does the plane have,the age of the plane 
    // Representation invariant:
    //	the field must be non null,seats > 0 ,age >=0
    // Safety from rep exposure:
    //  all fields are private and final
	//	the Observer return an immutable

	// constructor
	/**
	 * construct a new Plane object
	 * @param ID , the ID of the plane 
	 * @param type , the type of the plane
	 * @param seats , the number of the seats 
	 * @param age ,the age of the plane
	 * @throws IllegalInputException if seats <=0 or age < 0
	 */
	public Plane(String ID, String type, int seats, double age) throws IllegalInputException{
		if(seats <=0 || age<0)
			throw new IllegalInputException("应满足座位数 > 0 ,年龄 >=0");
		this.ID = ID;
		this.type = type;
		this.seats = seats;
		this.age = age;
		checkRep();
	}

	// checkRep
	private void checkRep() {
		assert ID != null;
		assert type != null;
		assert seats > 0;
		assert age >= 0;
	}

	@Override
	public String getID() {
		return this.ID;
	}
	
	/**
	 * get the type of this plane  
	 * @return the type of this plane  
	 */
	public String getType()
	{
		return this.type;
	}
	
	/**
	 * get the number of the seats of the plane
	 * @return the number of the seats of the plane
	 */
	public int getSeats()
	{
		return this.seats;
	}

	/**
	 * get the age of this plane
	 * @return the age of this plane
	 */
	public double getAge()
	{
		return this.age;
	}
	
	@Override
	public String toString() {
		return "ID: " + ID + " type:" + type + " seats=" + seats + " age=" + age;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		long temp;
		temp = Double.doubleToLongBits(age);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + seats;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Plane other = (Plane) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		if (Double.doubleToLongBits(age) != Double.doubleToLongBits(other.age))
			return false;
		if (seats != other.seats)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	
	
	
	

}
