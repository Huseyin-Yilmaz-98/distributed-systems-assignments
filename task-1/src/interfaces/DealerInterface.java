package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;


public interface DealerInterface extends Remote {
    /**
     * Searches the database for the car with the given serial number.
     * @param serialNumber Serial number to search for
     * @return the car object with the given serial number or null if not found
     */
    Car findCarBySerialNumber(String serialNumber) throws RemoteException;

    /**
     * Searches the database for the car objects with the given brand name.
     * @param brand Car brand to search for
     * @return a list of car objects with the given brand name, an empty list will be returned if no matches are found
     */
    List<Car> findCarsByBrand(String brand) throws RemoteException;

    /**
     * Creates a new car object with the given properties and adds the created object to the database
     * @param serialNumber Car's serial number
     * @param brand Car's brand
     * @param model Car's model name
     * @param color Car's color
     * @param year Car's manufacture year
     * @param price Car's price
     * @param weight Car's weight
     */
    void createCar(String serialNumber, String brand, String model, String color, int year, float price, float weight) throws RemoteException;

    /**
     * Searches the database for the receipt with the given id
     * @param id Receipt id to search for
     * @return Receipt object with the given id or null if not found
     */
    Receipt findReceiptById(String id) throws RemoteException;

    /**
     * Searches the database for the receipt objects with the given vendor name.
     * @param vendor Vendor name to search for
     * @return a list of receipts with the given vendor name, an empty list will be returned if no matches are found
     */
    List<Receipt> getReceiptsByVendor(String vendor) throws RemoteException;

    /**
     * Creates a new receipt object with the given properties and adds the created object to the database
     * @param ID Receipt id
     * @param vendor Vendor on receipt
     * @param carSerialNumber Serial number of the car that has been sold
     * @param date Purchase date
     */
    void createReceipt(String ID, String vendor, String carSerialNumber, Date date) throws RemoteException;
}
