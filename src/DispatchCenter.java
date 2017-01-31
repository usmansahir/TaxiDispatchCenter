
import java.util.ArrayList;
import java.util.HashMap;

public class DispatchCenter {
	/**
	 * Hash map of all taxis
	 */
	private HashMap<Integer, Taxi> taxis;

	/**
	 * Hash map. Contains an array list of all taxis in current area.
	 */
	private HashMap<String, ArrayList<Taxi>> areas;

	private String[] area = { "North", "South", "East", "West", "Downtown",
			"Airport" };

	private int[][] time = {{10,40,20,20,20,40},
							{40,10,20,20,20,40},
							{20,20,10,40,20,20},
							{20,20,40,10,20,60},
							{20,20,20,20,10,40},
							{40,40,20,60,40,10}};
	/**
	 * Constructor of the class. Generates taxis with random unique plate
	 * numbers
	 *
	 * @param n
	 *            Number of taxis to generate
	 */
	public DispatchCenter(int n) {
		// Initialize areas HashMap
		areas = new HashMap<String, ArrayList<Taxi>>();
		taxis = new HashMap<Integer, Taxi>();
		for (int i = 0; i < area.length; i++) {
			areas.put(area[i], new ArrayList<Taxi>());
		}
		// Generating n taxis
		int j = 0;
		for (int i = 0; i < n; i++) {
			int plate = 100 + (int) (900 * Math.random());
			if (checkTaxi(plate)) {
				Taxi taxi = new Taxi(plate);
				addTaxi(taxi, area[j]);
			}
			j++;
			if (j == area.length) {
				j = 0;
			}
		}
	}

	/**
	 * Returns a taxi due to it`s plate number
	 *
	 * @param plate
	 * @return
	 */
	public Taxi getTaxi(int plate) {
		return taxis.get(plate);
	}

	/**
	 * Returns a taxi list due to area
	 *
	 * @param area
	 * @return
	 */
	public ArrayList<Taxi> getTaxis(String area) {
		return areas.get(area);
	}

	/**
	 * Returns a name of area
	 *
	 * @param i
	 * @return
	 */
	public String getArea(int i) {
		return area[i];
	}

	/**
	 * Check if there is taxi with such plate number.
	 *
	 * @param plate
	 *            Number to check
	 * @return True if such plate number does not exists.
	 */
	public boolean checkTaxi(int plate) {
		if (taxis.get(plate) == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Check if there is such area.
	 *
	 * @param a
	 *            Area to check
	 * @return Number of area if such area exists or -1 if not.
	 */
	public int checkArea(String a) {
		for (int i = 0; i < area.length; i++) {
			if (a.equals(area[i])) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Adds a taxi to taxis and areas HashMaps
	 *
	 * @param taxi
	 *            Taxi to add
	 * @param area
	 *            Area where is a taxi.
	 */
	public void addTaxi(Taxi taxi, String area) {
		if (checkArea(area) != -1) {
			// add taxi to taxis HashMap
			taxis.put(taxi.getPlateNumber(), taxi);

			// add taxi to areas HashMap
			ArrayList<Taxi> temp = areas.get(area);
			temp.add(taxi);
			areas.put(area, temp);
		} else {
			System.out.println("Can`t add a taxi. There is no such area!");
		}
	}

	public void deleteTaxi(Taxi taxi, String area) {
		ArrayList<Taxi> tax = areas.get(area);
		tax.remove(taxi);
		areas.put(area, tax);
	}

	/**
	 * Method that takes an area as a parameter and returns an ArrayList of all
	 * Taxi objects in that area that are available.
	 *
	 * @param s
	 *            Area where to seek
	 * @return List of available taxis
	 */
	public ArrayList<Taxi> availableTaxisInArea(String s) {
		ArrayList<Taxi> allTaxis = areas.get(s);
		ArrayList<Taxi> freeTaxis = new ArrayList<Taxi>();
		for (int i = 0; i < allTaxis.size(); i++) {
			Taxi t = allTaxis.get(i);
			if (t.getAvailable()) {
				freeTaxis.add(t);
			}
		}
		if (freeTaxis.size() == 0) {
			freeTaxis = null;
		}
		return freeTaxis;
	}

	/**
	 * Method that returns an ArrayList of all Taxi objects in all areas that are unavailable.
	 * @return List of Busy taxis
	 */
	public ArrayList<Taxi> getBusyTaxis() {
		ArrayList<Taxi> busyTaxis = new ArrayList<Taxi>();
		for (int j = 0; j < area.length; j++) {
			ArrayList<Taxi> allTaxis = areas.get(area[j]);
			for (int i = 0; i < allTaxis.size(); i++) {
				Taxi t = allTaxis.get(i);
				if (!t.getAvailable()) {
					busyTaxis.add(t);
				}
			}
		}
		if (busyTaxis.size() == 0) {
			busyTaxis = null;
		}
		return busyTaxis;
	}

	public Taxi sendTaxiForRequest(ClientRequest c) {
		Taxi taxi = null;
		ArrayList<Taxi> tax = availableTaxisInArea(c.getPickupLocation());
		if (tax == null) {
			for (int i = 0; i < area.length; i++) {
				if (i != checkArea(c.getPickupLocation())) {
					tax = availableTaxisInArea(getArea(i));
					if (tax != null) {
						taxi = tax.get(0);
						deleteTaxi(taxi, getArea(i));
						taxi.setDeparture(c.getPickupLocation());
						taxi.setDestination(c.getDropoffLocation());
						int t1 = time[i][checkArea(c.getDropoffLocation())];
						int t2 = time[checkArea(c.getPickupLocation())][i];
						taxi.setEstimatedTimeToDest(t1 + t2);
						taxi.setAvailable(false);
						addTaxi(taxi, taxi.getDestination());
						return taxi;
					}
				}
			}
		} else {
			taxi = tax.remove(0);
			deleteTaxi(taxi, c.getPickupLocation());
			taxi.setDeparture(c.getPickupLocation());
			taxi.setDestination(c.getDropoffLocation());
			int t = time[checkArea(c.getPickupLocation())][checkArea(c.getDropoffLocation())];
			taxi.setEstimatedTimeToDest(t);
			taxi.setAvailable(false);
			addTaxi(taxi, taxi.getDestination());
			return taxi;
		}

		return taxi;
	}
}
