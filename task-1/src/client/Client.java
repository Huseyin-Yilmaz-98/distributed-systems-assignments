package client;

import interfaces.Car;
import interfaces.DealerInterface;
import interfaces.Receipt;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Client {
    final static int port = 4444;
    final DealerInterface server;

    /**
     * Constructor function for the class Client.
     * Looks for the server on the given port and connects if the registry is found.
     */
    public Client() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(port);
        server = (DealerInterface) registry.lookup("Dealer");
    }

    /**
     * Instantiates a client object
     * and keeps asking the user for an action until the program is terminated.
     */
    public static void main(String[] args) throws NotBoundException, RemoteException {
        String selection = "";
        Client client = new Client();
        while (!selection.equals("0")) {
            System.out.println("1. New car: \n" +
                    "2. New receipt\n" +
                    "3. Print car by serial number\n" +
                    "4. Print cars by brand\n" +
                    "5. Print receipt by id\n" +
                    "6. Print receipts by buyer\n" +
                    "0. Exit");

            selection = scanString();
            switch (selection) {
                case "0":
                    System.out.println("Exiting...");
                    break;
                case "1":
                    client.handleCreateCar();
                    break;
                case "2":
                    client.handleCreateReceipt();
                    break;
                case "3":
                    client.handlePrintCarBySerialNumber();
                    break;
                case "4":
                    client.handlePrintCarsByBrand();
                    break;
                case "5":
                    client.handlePrintReceiptById();
                    break;
                case "6":
                    client.handlePrintReceiptsByVendor();
                    break;
                default:
                    System.out.println("Invalid selection!");
            }
            System.out.println();
        }
    }

    /**
     * Reads a non-empty string from the scanner
     *
     * @return Input string.
     */
    public static String scanString() {
        Scanner scanner = new Scanner(System.in);
        String text;
        while (true) {
            text = scanner.nextLine();
            if (text.length() == 0) {
                System.out.println("Input cannot be empty: ");
            } else {
                return text;
            }
        }
    }

    /**
     * Reads a valid floating number from the scanner
     *
     * @return Input floating number.
     */
    public static float scanFloat() {
        String text;
        while (true) {
            text = scanString();
            try {
                return Float.parseFloat(text);
            } catch (NumberFormatException e) {
                System.out.println("Input must be a number: ");
            }
        }
    }

    /**
     * Reads a date string from the scanner in the dd/MM/yyyy format and converts it into a date object
     *
     * @return Input date object.
     */
    public static Date scanDate() {
        String pattern;
        while (true) {
            pattern = scanString();
            try {
                return new SimpleDateFormat("dd/MM/yyyy").parse(pattern);
            } catch (ParseException e) {
                System.out.println("Date must be in the day/month/year format (Ex: 12/01/2015): ");
            }
        }
    }

    /**
     * Asks the user for new car information
     * and calls the function that registers the new car on the server.
     */
    public void handleCreateCar() throws RemoteException {
        System.out.println("Serial number: ");
        String serialNumber = scanString();
        System.out.println("Brand: ");
        String brand = scanString();
        System.out.println("Model: ");
        String model = scanString();
        System.out.println("Color: ");
        String color = scanString();
        System.out.println("Year: ");
        int year = (int) scanFloat();
        System.out.println("Weight");
        float weight = scanFloat();
        System.out.println("Price");
        float price = scanFloat();
        server.createCar(serialNumber, brand, model, color, year, price, weight);
        System.out.println("Registration successful...\n");
    }

    /**
     * Asks the user for new receipt information
     * and calls the function that registers the new receipt on the server.
     */
    public void handleCreateReceipt() throws RemoteException {
        System.out.println("Receipt ID: ");
        String ReceiptID = scanString();

        System.out.println("Vendor: ");
        String vendor = scanString();

        System.out.println("Car Serial Number: ");
        String serialNumber = scanString();

        System.out.println("Receipt date in the format day/month/year: ");
        Date receiptDate = scanDate();

        server.createReceipt(ReceiptID, vendor, serialNumber, receiptDate);
    }

    /**
     * Asks the user for a serial number and prints the car object with the given number.
     */
    public void handlePrintCarBySerialNumber() throws RemoteException {
        System.out.println("Serial number: ");
        String serialNumber = scanString();
        Car car = server.findCarBySerialNumber(serialNumber);
        if (car == null) {
            System.out.println("No car found with this serial number!");
        } else {
            System.out.println(car);
        }
    }

    /**
     * Asks the user for a brand name and prints the car objects with the given brand name.
     */
    public void handlePrintCarsByBrand() throws RemoteException {
        System.out.println("Brand: ");
        String brand = scanString();
        List<Car> carListByBrand = server.findCarsByBrand(brand);

        if (carListByBrand == null || carListByBrand.size() == 0) {
            System.out.println("No car list found with this brand!");
        } else {
            for (Car car : carListByBrand) {
                System.out.println(car);
            }
        }
    }

    /**
     * Asks the user for an id number and prints the receipt object with the given id
     */
    public void handlePrintReceiptById() throws RemoteException {
        System.out.println("Receipt ID: ");
        String receiptID = scanString();
        Receipt receipt = server.findReceiptById(receiptID);
        if (receipt == null) {
            System.out.println("No receipt found with this ID!");
        } else {
            System.out.println(receipt);
        }
    }

    /**
     * Asks the user for a vendor name and prints the receipt objects with the given vendor name.
     */
    public void handlePrintReceiptsByVendor() throws RemoteException {
        System.out.println("Buyer name: ");
        String buyerName = scanString();
        List<Receipt> receiptList = server.getReceiptsByVendor(buyerName);
        if (receiptList == null || receiptList.size() == 0) {
            System.out.println("No receipt found with this buyer name!");
        } else {
            for (Receipt receipt : receiptList) {
                System.out.println(receipt);
            }
        }
    }
}
