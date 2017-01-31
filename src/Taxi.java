
public class Taxi {
	private int plateNumber;
	private boolean available;
	private String destination;
	private String departure;
	private int estimatedTimeToDest;

	public int getPlateNumber() {
		return plateNumber;
	}

	public boolean getAvailable() {
		return available;
	}

	public String getDestination() {
		return destination;
	}

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public int getEstimatedTimeToDestination() {
		return estimatedTimeToDest;
	}

	public void setAvailable(boolean avail) {
		available = avail;
	}

	public void setDestination(String d) {
		destination = d;
	}

	public void setEstimatedTimeToDest(int t) {
		estimatedTimeToDest = t;
	}

	public void decreaseEstimatedTimeToDest() {
		estimatedTimeToDest--;
	}

	public Taxi(int plate) {
		plateNumber = plate;
		available = true;
		destination = "";
		departure = "";
		estimatedTimeToDest = 0;
	}

	public String toString() {
		if (available)
			return plateNumber + " (available)";
		return plateNumber + "(" + estimatedTimeToDest + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			Taxi t = (Taxi) obj;
			if (this.plateNumber == t.getPlateNumber()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}