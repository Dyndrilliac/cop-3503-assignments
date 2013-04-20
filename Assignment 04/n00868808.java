/*
	Author: Matthew Boyette
	Date:   3/10/2013
	
	My program allows any number of vehicle records to be processed.
*/

import java.io.*;
import java.text.*;
import java.util.*;
import javax.swing.*;

public class n00868808
{
	public static class Vehicle
	{
		private String ownersName = "";
		private String address    = "";
		private String phone      = "";
		private String email      = "";
		
		public static class VehicleEmailAscendingComparator implements Comparator<Vehicle>
		{
		    public int compare(Vehicle vehicle1, Vehicle vehicle2)
		    {
		    	return vehicle1.getEmail().compareTo(vehicle2.getEmail());
		    }
		}
		
		public static class VehicleEmailDescendingComparator implements Comparator<Vehicle>
		{
		    public int compare(Vehicle vehicle1, Vehicle vehicle2)
		    {
		    	return vehicle2.getEmail().compareTo(vehicle1.getEmail());
		    }
		}
		
		public static class VehicleEmailAscendingIgnoreCaseComparator implements Comparator<Vehicle>
		{
		    public int compare(Vehicle vehicle1, Vehicle vehicle2)
		    {
		    	return vehicle1.getEmail().compareToIgnoreCase(vehicle2.getEmail());
		    }
		}
		
		public static class VehicleEmailDescendingIgnoreCaseComparator implements Comparator<Vehicle>
		{
		    public int compare(Vehicle vehicle1, Vehicle vehicle2)
		    {
		    	return vehicle2.getEmail().compareToIgnoreCase(vehicle1.getEmail());
		    }
		}
		
		public Vehicle() {}
		
		public Vehicle(String ownersName, String address, String phone, String email)
		{
			this.setOwnersName(ownersName);
			this.setAddress(address);
			this.setPhone(phone);
			this.setEmail(email);
		}
		
		public Vehicle(Vehicle newVehicle)
		{
			this.setVehicle(newVehicle);
		}
		
		public boolean equals(Object obj)
		{
			if (obj == null) // If 'obj' is null, it cannot be equal to 'this' so return false.
			{
				return false;
			}
			
			if (this == obj) // If 'obj' is located at the same memory address as 'this', then 'obj' is 'this', and thus they are equal so return true.
			{
				return true;
			}
			
			// Check to see if 'this' is an object of the same class as 'obj'.
			if (this.getClass() != obj.getClass())
			{
				// If 'this' is not an object of the same class as 'obj', check to see if the two classes are assignment compatible.
				if (this.getClass().isInstance(obj))
				{
					// If the two classes are assignment compatible, use the hashCode methods to determine equality.
					if (this.hashCode() == obj.hashCode())
					{
						return true;
					}
				}
			}
			// If 'this' is an object of the same class as 'obj', use the hashCode methods to determine equality.
			else
			{
				if (this.hashCode() == obj.hashCode())
				{
					return true;
				}
			}
			
			// If the two classes are not the same or assignment compatible, return false. If hashCode method comparisons are false, return false.
			return false;
		}
		
		public String getAddress()
		{
			return this.address;
		}
		
		public String getEmail()
		{
			return this.email;
		}
		
		public String getOwnersName()
		{
			return this.ownersName;
		}
		
		public String getPhone()
		{
			return this.phone;
		}
		
		public int hashCode()
		{
			/*
				The hashCode method is a way to reduce all of an object's properties down to a single integer to assist in equality comparisons.
				Each property's individual hash code is joined by the XOR operator, which will generate a new hash code that will change even if
				only a single bit changes in any of its component parts.
			*/
			return (this.getOwnersName().hashCode() ^ this.getAddress().hashCode() ^ this.getPhone().hashCode() ^ this.getEmail().hashCode());
		}
		
		// This method takes a string and determines if it can be safely parsed as a boolean.
		// Return value of true indicates that the string is safe to parse, and false means that the string is not safe to parse.
		public static boolean isStringParsedAsBoolean(String s)
		{
			try
			{
				// parseBoolean throws an exception if the string can't be parsed.
				Boolean.parseBoolean(s);
			}
			catch (Exception exception)
			{
				// If we catch an exception, then we return false.
				return false;
			}

			// Base case; return true if the string was parsed without an exception being thrown.
			return true;
		}
		
		// This method takes a string and determines if it can be safely parsed as a float.
		// Return value of true indicates that the string is safe to parse, and false means that the string is not safe to parse.
		public static boolean isStringParsedAsFloat(String s)
		{
			try
			{
				// parseFloat throws an exception if the string can't be parsed.
				Float.parseFloat(s);
			}
			catch (Exception exception)
			{
				// If we catch an exception, then we return false.
				return false;
			}

			// Base case; return true if the string was parsed without an exception being thrown.
			return true;
		}
		
		// This method takes a string and determines if it can be safely parsed as an integer.
		// Return value of true indicates that the string is safe to parse, and false means that the string is not safe to parse.
		public static boolean isStringParsedAsInteger(String s)
		{
			try
			{
				// parseInt throws an exception if the string can't be parsed.
				Integer.parseInt(s);
			}
			catch (Exception exception)
			{
				// If we catch an exception, then we return false.
				return false;
			}

			// Base case; return true if the string was parsed without an exception being thrown.
			return true;
		}
		
		public void setAddress(String address)
		{
			this.address = address;
		}
		
		public void setEmail(String email)
		{
			this.email = email;
		}
		
		public void setOwnersName(String ownersName)
		{
			this.ownersName = ownersName;
		}
		
		public void setPhone(String phone)
		{
			this.phone = phone;
		}
		
		public void setVehicle(Vehicle newVehicle)
		{
			this.setAddress(newVehicle.getAddress());
			this.setEmail(newVehicle.getEmail());
			this.setOwnersName(newVehicle.getOwnersName());
			this.setPhone(newVehicle.getPhone());
		}
		
		public String toString()
		{
			return ("Record type: " + this.getClass().getSimpleName() + 
					"\nOwner's name: " + this.getOwnersName() + 
					"\nOwner's address: " + this.getAddress() + 
					"\nOwner's phone number: " + this.getPhone() + 
					"\nOwner's email address: " + this.getEmail() + "\n");
		}
	}
	
	public static class Bicycle extends Vehicle
	{
		private int numberOfSpeeds = 0;
		
		public Bicycle() {}
		
		public Bicycle(String ownersName, String address, String phone, String email, int numberOfSpeeds)
		{
			super(ownersName, address, phone, email);
			this.setNumberOfSpeeds(numberOfSpeeds);
		}
		
		public Bicycle(Bicycle newBicycle)
		{
			super(newBicycle);
			this.setNumberOfSpeeds(newBicycle.getNumberOfSpeeds());
		}

		public Integer getNumberOfSpeeds()
		{
			return this.numberOfSpeeds;
		}
		
		public int hashCode()
		{
			/*
				The hashCode method is a way to reduce all of an object's properties down to a single integer to assist in equality comparisons.
				Each property's individual hash code is joined by the XOR operator, which will generate a new hash code that will change even if
				only a single bit changes in any of its component parts.
			*/
			return (super.hashCode() ^ this.getNumberOfSpeeds().hashCode());
		}
		
		public void setBicycle(Bicycle newBicycle)
		{
			this.setVehicle(newBicycle);
			this.setNumberOfSpeeds(numberOfSpeeds);
		}

		public void setNumberOfSpeeds(int numberOfSpeeds)
		{
			this.numberOfSpeeds = numberOfSpeeds;
		}
		
		public String toString()
		{
			return (super.toString() + 
					"Number of speeds: " + this.getNumberOfSpeeds() + "\n");
		}
	}
	
	public static class Car extends Vehicle
	{
		private String  color         = "";
		private boolean isConvertible = false;
		
		public Car() {}
		
		public Car(String ownersName, String address, String phone, String email, boolean isConvertible, String color)
		{
			super(ownersName, address, phone, email);
			this.setConvertible(isConvertible);
			this.setColor(color);
		}
		
		public Car(Car newCar)
		{
			super(newCar);
			this.setConvertible(newCar.isConvertible());
			this.setColor(newCar.getColor());
		}
		
		public String getColor()
		{
			return this.color;
		}
		
		public int hashCode()
		{
			/*
				The hashCode method is a way to reduce all of an object's properties down to a single integer to assist in equality comparisons.
				Each property's individual hash code is joined by the XOR operator, which will generate a new hash code that will change even if
				only a single bit changes in any of its component parts.
			*/
			return (super.hashCode() ^ this.getColor().hashCode() ^ this.isConvertible().hashCode());
		}
		
		public Boolean isConvertible()
		{
			return this.isConvertible;
		}
		
		public void setCar(Car newCar)
		{
			this.setVehicle(newCar);
			this.setConvertible(newCar.isConvertible());
			this.setColor(newCar.getColor());
		}
		
		public void setColor(String color)
		{
			this.color = color;
		}
		
		public void setConvertible(boolean isConvertible)
		{
			this.isConvertible = isConvertible;
		}
		
		public String toString()
		{
			return (super.toString() + 
					"Color: " + this.getColor() + 
					"\nConvertible: " + this.isConvertible() + "\n");
		}
	}
	
	public static class Truck extends Vehicle
	{
		private float  numberOfTons = 0.0f;
		private float  costOfTruck  = 0.0f;
		private String datePurchased = "1/1/1970";
		
		public Truck() {}
		
		public Truck(String ownersName, String address, String phone, String email, float numberOfTons, float costOfTruck, String datePurchased)
		{
			super(ownersName, address, phone, email);
			this.setNumberOfTons(numberOfTons);
			this.setCostOfTruck(costOfTruck);
			this.setDatePurchased(datePurchased);
		}
		
		public Truck(Truck newTruck)
		{
			super(newTruck);
			this.setNumberOfTons(newTruck.getNumberOfTons());
			this.setCostOfTruck(newTruck.getCostOfTruck());
			this.setDatePurchased(newTruck.getDatePurchased());
		}
		
		public Float getCostOfTruck()
		{
			return costOfTruck;
		}
		
		public String getDatePurchased() 
		{
			return datePurchased;
		}
		
		public Float getNumberOfTons()
		{
			return numberOfTons;
		}
		
		public int hashCode()
		{
			/*
				The hashCode method is a way to reduce all of an object's properties down to a single integer to assist in equality comparisons.
				Each property's individual hash code is joined by the XOR operator, which will generate a new hash code that will change even if
				only a single bit changes in any of its component parts.
			*/
			return (super.hashCode() ^ this.getNumberOfTons().hashCode() ^ this.getCostOfTruck().hashCode() ^ this.getDatePurchased().hashCode());
		}
		
		public void setCostOfTruck(float costOfTruck)
		{
			this.costOfTruck = costOfTruck;
		}
		
		public void setDatePurchased(String datePurchased)
		{
			if (validateDateString(datePurchased))
			{
				this.datePurchased = datePurchased;
			}
			else
			{
				handleException(new Exception("The given date is invalid. The object has not been changed."));
			}
		}
		
		public void setNumberOfTons(float numberOfTons)
		{
			this.numberOfTons = numberOfTons;
		}
		
		public void setTruck(Truck newTruck)
		{
			this.setVehicle(newTruck);
			this.setNumberOfTons(newTruck.getNumberOfTons());
			this.setCostOfTruck(newTruck.getCostOfTruck());
			this.setDatePurchased(newTruck.getDatePurchased());
		}
		
		public String toString()
		{
			return (super.toString() + 
					"Number of tons: " + this.getNumberOfTons() + 
					"\nCost of truck: " + this.getCostOfTruck() + 
					"\nDate purchased: " + this.getDatePurchased() + "\n");
		}
		
		public static boolean validateDateString(String date)
		{
			if ((date == null) || date.isEmpty())
			{
				return false;
			}
			
			// Check for MM/DD/YYYY format and insure the correct number of months and days.
			if (date.matches("[0-9]+/[0-9]+/[0-9]+"))
			{
				int[]    intArray    = {0, 0, 0};
				String[] stringArray = date.split("/", 3);
				
				for (int i = 0; i < 3; i++)
				{
					intArray[i] = Integer.parseInt(stringArray[i]);
				}
				
				if (intArray[0] < 1)
				{
					handleException(new Exception("Month value must exceed the lower bound of 1."));
					return false;
				}
				
				if (intArray[0] > 12)
				{
					handleException(new Exception("Month value must not exceed the upper bound of 12."));
					return false;
				}
				
				int dayUpperBound, dayLowerBound = 1;
				
				switch (intArray[0])
				{
				
					case 2:
						
						dayUpperBound = 28;
						break;
						
					case 4:
					case 6:
					case 9:
					case 11:
						
						dayUpperBound = 30;
						break;
						
					default:
						
						dayUpperBound = 31;
						break;
				}
				
				if (intArray[1] < dayLowerBound)
				{
					handleException(new Exception("Day value must exceed the lower bound of " + dayLowerBound + "."));
					return false;
				}
				
				if (intArray[1] > dayUpperBound)
				{
					handleException(new Exception("Day value must not exceed the upper bound of " + dayUpperBound + "."));
					return false;
				}
				
				if (intArray[2] < 0)
				{
					handleException(new Exception("Year value must be greater than or equal to 0."));
					return false;
				}
				
				return true;
			}
			
			return false;
		}
	}
	
	public static class AmericanCar extends Car
	{
		private boolean isMadeInDetroit = false;
		private boolean isUnionShop     = false;
		
		public AmericanCar() {}
		
		public AmericanCar(String ownersName, String address, String phone, String email, boolean isConvertible, String color, boolean isMadeInDetroit, boolean isUnionShop)
		{
			super(ownersName, address, phone, email, isConvertible, color);
			this.setMadeInDetroit(isMadeInDetroit);
			this.setUnionShop(isUnionShop);
		}
		
		public AmericanCar(AmericanCar newAmericanCar)
		{
			super(newAmericanCar);
			this.setMadeInDetroit(newAmericanCar.isMadeInDetroit());
			this.setUnionShop(newAmericanCar.isUnionShop());
		}
		
		public int hashCode()
		{
			/*
				The hashCode method is a way to reduce all of an object's properties down to a single integer to assist in equality comparisons.
				Each property's individual hash code is joined by the XOR operator, which will generate a new hash code that will change even if
				only a single bit changes in any of its component parts.
			*/
			return (super.hashCode() ^ this.isMadeInDetroit().hashCode() ^ this.isUnionShop().hashCode());
		}
		
		public Boolean isMadeInDetroit()
		{
			return this.isMadeInDetroit;
		}

		public Boolean isUnionShop()
		{
			return this.isUnionShop;
		}
		
		public void setAmericanCar(AmericanCar newAmericanCar)
		{
			this.setCar(newAmericanCar);
			this.setMadeInDetroit(newAmericanCar.isMadeInDetroit());
			this.setUnionShop(newAmericanCar.isUnionShop());
		}

		public void setMadeInDetroit(boolean isMadeInDetroit)
		{
			this.isMadeInDetroit = isMadeInDetroit;
		}

		public void setUnionShop(boolean isUnionShop)
		{
			this.isUnionShop = isUnionShop;
		}

		public String toString()
		{
			return (super.toString() + 
					"Made in detroit: " + this.isMadeInDetroit() + 
					"\nUnion shop: " + this.isUnionShop() + "\n");
		}
	}
	
	public static class ForeignCar extends Car
	{
		private String countryOfManufacture = "";
		private float  importDuty           = 0.0f;
		
		public ForeignCar() {}
		
		public ForeignCar(String ownersName, String address, String phone, String email, boolean isConvertible, String color, String countryOfManufacture, float importDuty)
		{
			super(ownersName, address, phone, email, isConvertible, color);
			this.setCountryOfManufacture(countryOfManufacture);
			this.setImportDuty(importDuty);
		}
		
		public ForeignCar(ForeignCar newForeignCar)
		{
			super(newForeignCar);
			this.setCountryOfManufacture(newForeignCar.getCountryOfManufacture());
			this.setImportDuty(newForeignCar.getImportDuty());
		}
		
		public String getCountryOfManufacture()
		{
			return this.countryOfManufacture;
		}

		public Float getImportDuty()
		{
			return this.importDuty;
		}
		
		public int hashCode()
		{
			/*
				The hashCode method is a way to reduce all of an object's properties down to a single integer to assist in equality comparisons.
				Each property's individual hash code is joined by the XOR operator, which will generate a new hash code that will change even if
				only a single bit changes in any of its component parts.
			*/
			return (super.hashCode() ^ this.getCountryOfManufacture().hashCode() ^ this.getImportDuty().hashCode());
		}

		public void setCountryOfManufacture(String countryOfManufacture)
		{
			this.countryOfManufacture = countryOfManufacture;
		}
		
		public void setForeignCar(ForeignCar newForeignCar)
		{
			this.setCar(newForeignCar);
			this.setCountryOfManufacture(newForeignCar.getCountryOfManufacture());
			this.setImportDuty(newForeignCar.getImportDuty());
		}

		public void setImportDuty(float importDuty)
		{
			this.importDuty = importDuty;
		}

		public String toString()
		{
			return (super.toString() + 
					"Country of manufacture: " + this.getCountryOfManufacture() + 
					"\nImport duty: " + this.getImportDuty() + "\n");
		}
	}
	
	private static void displayMenu(List<Vehicle> vehicleList)
	{
		Scanner keyboard = new Scanner(System.in);
		boolean stopMenu = false;
		
		do
		{
			System.out.println("Welcome! Please choose an option from the menu below.\n");
			System.out.println("\t[1]: Print all of the vehicles.");
			System.out.println("\t[2]: Sort all of the vehicles by email addresses and print them.");
			System.out.println("\t[3]: Print the number of vehicles.");
			System.out.println("\t[4]: Sort the vehicles and print only the bicycles and trucks.");
			System.out.println("\t[5]: Print only the vehicles in area code 987.\n");
			System.out.println("*Note: You may enter the word \"stop\" (not case-sensitive) at any time to end the program.\n");
			System.out.print("Input: ");
			
			String input = keyboard.next();
			
			if (Vehicle.isStringParsedAsInteger(input))
			{
				System.out.println("");
				int choice = Integer.parseInt(input);

				switch (choice)
				{

				case 1:

					printAllVehicles(vehicleList);
					break;

				case 2:

					sortAndPrintAllVehicles(vehicleList);
					break;

				case 3:

					printNumberOfVehicles(vehicleList);
					break;

				case 4:

					printSortedBicyclesAndTrucks(vehicleList);
					break;

				case 5:

					printVehiclesInAreaCode(vehicleList, 987);
					break;

				default:

					handleException(new Exception("Unable to recognize the given command. Only the integers 1 through 5 are valid."));
					break;
				}
			}
			else
			{
				if (input.equalsIgnoreCase("stop"))
				{
					stopMenu = true;
				}
				else
				{
					handleException(new Exception("Unable to recognize the given input. Please enter an integer."));
				}
			}
		}
		while (stopMenu == false);
		
		keyboard.close();
	}
	
	private static String getDateTimeStamp()
	{
		/*
			This method was constructed after searching for simple custom date/time formatting.
			Its only downside is that the Date class is deprecated, and may become unavailable
			in the future. I am in the process of working on a better alternative.
		*/
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy hh:mm:ss a z");
		return dateFormat.format(new Date());
	}
	
	private static String getFilePath(boolean isOpen)
	{
		JFileChooser fileDialog = new JFileChooser();
		boolean      stopFlag   = false;
		String       filePath   = null;
		int          choice     = 0;
		
		do // Loop while stopFlag equals false, post-test.
		{
			if (isOpen)
			{
				choice = fileDialog.showOpenDialog(null);
			}
			else
			{
				choice = fileDialog.showSaveDialog(null);
			}
			
			switch (choice)
			{
			case JFileChooser.APPROVE_OPTION:
				
				try
				{
					filePath = fileDialog.getSelectedFile().getCanonicalPath();
					stopFlag = true;
				}
				catch (Exception exception)
				{
					filePath = null;
					stopFlag = false;
				}
				break;
				
			case JFileChooser.CANCEL_OPTION:
				
				filePath = null;
				stopFlag = true;
				break;
				
			default:
				
				filePath = null;
				stopFlag = false;
				break;
			}
		}
		while (stopFlag == false);
		
		return filePath;
	}
	
	private static void handleException(Exception exception)
	{
		/*
			Report error message, complete with some useful debug info.
			Source file is where the error chain ended, which could be null in the case of a function in the Java API.
			Cause file is where the error chain began, which is the bottom of the stack and where the bad method is likely to be.
		*/
		JOptionPane.showMessageDialog(null,
			exception.toString() + 
			"\n\nSource file: " + exception.getStackTrace()[0].getFileName() +
			"\nLine number: " + exception.getStackTrace()[0].getLineNumber() +
			"\n\nCause file: " + exception.getStackTrace()[exception.getStackTrace().length-1].getFileName() +
			"\nLine number: " + exception.getStackTrace()[exception.getStackTrace().length-1].getLineNumber() +
			"\n\nWhen: " + getDateTimeStamp(),
			"Unhandled Exception",
			JOptionPane.ERROR_MESSAGE);
		exception.printStackTrace();
	}
	
	public static void main(String[] args)
	{
		List<Vehicle> vehicleList = new ArrayList<Vehicle>();
		
		if (args.length > 0)
		{
			for (int i = 0; i < args.length; i++)
			{
				openFile(args[i], vehicleList);
			}
		}
		else
		{
			openFile(null, vehicleList);
		}
		
		displayMenu(vehicleList);
	}
	
	private static void openFile(String filePath, List<Vehicle> vehicleList)
	{
		if ((filePath == null) || filePath.isEmpty())
		{
			filePath = getFilePath(true);
		}
		
		if ((filePath == null) || filePath.isEmpty())
		{
			// User has canceled the file operation; abort!
			return;
		}
		
		Scanner inputStream = null; // Stream object for file input.
		
		try
		{
			// Initialize file stream. If the given path is invalid, an exception is thrown.
			inputStream = new Scanner(new File(filePath));
			parseFile(inputStream, vehicleList);
		}
		catch (Exception exception)
		{
			// Handle the exception by alerting the user of the error.
			handleException(exception);
		}
		finally
		{
			if (inputStream != null)
			{
				// Close the input stream.
				inputStream.close();
				inputStream = null;
			}
		}
	}
	
	private static void parseFile(Scanner inputStream, List<Vehicle> vehicleList)
	{
		while (inputStream.hasNextLine())
		{
			String ownersName, address, phone, email, numberOfSpeeds, isConvertible, color, numberOfTons, costOfTruck, datePurchased, isMadeInDetroit, isUnionShop, countryOfManufacture, importDuty;
			// Get the next line in the file.
			String line = inputStream.nextLine();
			
			switch (line.toLowerCase())
			{
			
				case "vehicle":
					
					ownersName = inputStream.nextLine();
					address    = inputStream.nextLine();
					phone      = inputStream.nextLine();
					email      = inputStream.nextLine();
					
					vehicleList.add(new Vehicle(ownersName, address, phone, email));
					break;
					
				case "bicycle":
					
					ownersName     = inputStream.nextLine();
					address        = inputStream.nextLine();
					phone          = inputStream.nextLine();
					email          = inputStream.nextLine();
					numberOfSpeeds = inputStream.nextLine();
					
					if (Vehicle.isStringParsedAsInteger(numberOfSpeeds))
					{
						vehicleList.add(new Bicycle(ownersName, address, phone, email, Integer.parseInt(numberOfSpeeds)));
					}
					else
					{
						handleException(new Exception("Failure parsing file: 'numberOfSpeeds' must be an integer."));
					}
					break;
					
				case "car":
					
					ownersName    = inputStream.nextLine();
					address       = inputStream.nextLine();
					phone         = inputStream.nextLine();
					email         = inputStream.nextLine();
					isConvertible = inputStream.nextLine();
					color         = inputStream.nextLine();
					
					if (Vehicle.isStringParsedAsBoolean(isConvertible))
					{
						vehicleList.add(new Car(ownersName, address, phone, email, Boolean.parseBoolean(isConvertible), color));
					}
					else
					{
						handleException(new Exception("Failure parsing file: 'isConvertible' must be a boolean."));
					}
					break;
					
				case "truck":
					
					ownersName    = inputStream.nextLine();
					address       = inputStream.nextLine();
					phone         = inputStream.nextLine();
					email         = inputStream.nextLine();
					numberOfTons  = inputStream.nextLine();
					costOfTruck   = inputStream.nextLine();
					datePurchased = inputStream.nextLine();
					
					if (Vehicle.isStringParsedAsFloat(numberOfTons) && Vehicle.isStringParsedAsFloat(costOfTruck))
					{
						vehicleList.add(new Truck(ownersName, address, phone, email, Float.parseFloat(numberOfTons), Float.parseFloat(costOfTruck), datePurchased));
					}
					else
					{
						if (!Vehicle.isStringParsedAsFloat(numberOfTons))
						{
							handleException(new Exception("Failure parsing file: 'numberOfTons' must be a float."));
						}
						else if (!Vehicle.isStringParsedAsFloat(costOfTruck))
						{
							handleException(new Exception("Failure parsing file: 'costOfTruck' must be a float."));
						}
						else
						{
							handleException(new Exception("Failure parsing file: unknown anomaly."));
						}
					}
					break;
					
				case "american car":
					
					ownersName      = inputStream.nextLine();
					address         = inputStream.nextLine();
					phone           = inputStream.nextLine();
					email           = inputStream.nextLine();
					isConvertible   = inputStream.nextLine();
					color           = inputStream.nextLine();
					isMadeInDetroit = inputStream.nextLine();
					isUnionShop     = inputStream.nextLine();
					
					if ((Vehicle.isStringParsedAsBoolean(isConvertible)) && (Vehicle.isStringParsedAsBoolean(isMadeInDetroit)) && (Vehicle.isStringParsedAsBoolean(isUnionShop)))
					{
						vehicleList.add(new AmericanCar(ownersName, address, phone, email, Boolean.parseBoolean(isConvertible), color, Boolean.parseBoolean(isMadeInDetroit), Boolean.parseBoolean(isUnionShop)));
					}
					else
					{
						if (!Vehicle.isStringParsedAsBoolean(isConvertible))
						{
							handleException(new Exception("Failure parsing file: 'isConvertible' must be a boolean."));
						}
						else if (!Vehicle.isStringParsedAsBoolean(isMadeInDetroit))
						{
							handleException(new Exception("Failure parsing file: 'isMadeInDetroit' must be a boolean."));
						}
						else if (!Vehicle.isStringParsedAsBoolean(isUnionShop))
						{
							handleException(new Exception("Failure parsing file: 'isUnionShop' must be a boolean."));
						}
						else
						{
							handleException(new Exception("Failure parsing file: unknown anomaly."));
						}
					}
					break;
					
				case "foreign car":
					
					ownersName           = inputStream.nextLine();
					address              = inputStream.nextLine();
					phone                = inputStream.nextLine();
					email                = inputStream.nextLine();
					isConvertible        = inputStream.nextLine();
					color                = inputStream.nextLine();
					countryOfManufacture = inputStream.nextLine();
					importDuty           = inputStream.nextLine();
					
					if (Vehicle.isStringParsedAsFloat(importDuty) && (Vehicle.isStringParsedAsBoolean(isConvertible)))
					{
						vehicleList.add(new ForeignCar(ownersName, address, phone, email, Boolean.parseBoolean(isConvertible), color, countryOfManufacture, Float.parseFloat(importDuty)));
					}
					else
					{
						if (!Vehicle.isStringParsedAsFloat(importDuty))
						{
							handleException(new Exception("Failure parsing file: 'importDuty' must be a float."));
						}
						else if (!Vehicle.isStringParsedAsBoolean(isConvertible))
						{
							handleException(new Exception("Failure parsing file: 'isConvertible' must be a boolean."));
						}
						else
						{
							handleException(new Exception("Failure parsing file: unknown anomaly."));
						}
					}
					break;
					
				default:
					
					if (!line.isEmpty())
					{
						handleException(new Exception("Failure parsing file: record type '" + line + "' is not recognized."));
					}
					break;
			}
		}
	}
	
	private static void printAllVehicles(List<Vehicle> vehicleList)
	{
		for (int i = 0; i < vehicleList.size(); i++)
		{
			System.out.println(vehicleList.get(i).toString());
		}
	}
	
	private static void sortAndPrintAllVehicles(List<Vehicle> vehicleList)
	{
		List<Vehicle> sortedList = new ArrayList<Vehicle>();
		sortedList.addAll(vehicleList);
		Collections.sort(sortedList, new Vehicle.VehicleEmailAscendingComparator());
		
		for (int i = 0; i < sortedList.size(); i++)
		{
			System.out.println(sortedList.get(i).toString());
		}
	}
	
	private static void printNumberOfVehicles(List<Vehicle> vehicleList)
	{
		System.out.println("Number of records: " + vehicleList.size() + "\n");
	}
	
	private static void printSortedBicyclesAndTrucks(List<Vehicle> vehicleList)
	{
		List<Vehicle> sortedList = new ArrayList<Vehicle>();
		sortedList.addAll(vehicleList);
		Collections.sort(sortedList, new Vehicle.VehicleEmailAscendingComparator());
		
		for (int i = 0; i < vehicleList.size(); i++)
		{
			if ((vehicleList.get(i) instanceof Bicycle) || (vehicleList.get(i) instanceof Truck))
			{
				System.out.println(vehicleList.get(i).toString());
			}
		}
	}
	
	private static void printVehiclesInAreaCode(List<Vehicle> vehicleList, int areaCode)
	{
		for (int i = 0; i < vehicleList.size(); i++)
		{
			if (vehicleList.get(i).getPhone().contains("(" + areaCode + ")"))
			{
				System.out.println(vehicleList.get(i).toString());
			}
		}
	}
}