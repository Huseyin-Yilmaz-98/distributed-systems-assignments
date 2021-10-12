package server;

import interfaces.Car;
import interfaces.DealerInterface;
import interfaces.Receipt;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Dealer implements DealerInterface {
    final String carsFilePath = "cars.dat";
    final String receiptsFilePath = "receipts.dat";
    final static int port = 4444;

    /**
     * Registers an RMI server and instantiates and binds a Dealer object.
     */
    public static void main(String[] args) {
        try {
            Dealer dealer = new Dealer();
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind("Dealer", UnicastRemoteObject.exportObject(dealer, port));

            System.err.println("Server ready");

            dealer.checkFiles();

            System.out.println("Cars in the file: ");
            List<Car> cars = dealer.findAllCars();
            for (Car car : cars) {
                System.out.println(car);
            }

            System.out.println("\nReceipts in the file: ");
            List<Receipt> receipts = dealer.findAllReceipts();
            for (Receipt receipt : receipts) {
                System.out.println(receipt);
            }
        } catch (Exception e) {
            System.err.println("Server exception: " + e.getMessage());
        }
    }

    /**
     * Checks whether the cars and receipts files exist in the
     * current working directory. If the files don't exist,
     * creates the files and populates them with the default values.
     */
    private void checkFiles() throws RemoteException, ParseException {
        File carsFile = new File(carsFilePath);
        File receiptsFile = new File(receiptsFilePath);
        if (!carsFile.exists()) {
            try {
                if (carsFile.createNewFile()) {
                    System.out.println("Cars file created.");
                    createCar("4512360", "Hyundai", "Venue", "Blue", 2021, 19935, 1184);
                    createCar("4568989", "Hyundai", "Accent", "Red", 2020, 16270, 1356);
                }
            } catch (IOException e) {
                System.out.println("Failed to create cars file, exiting. Cause: " + e);
                System.exit(1);
            }
        }

        if (!receiptsFile.exists()) {
            Random rand = new Random();

            // The starting point of random dates
            Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2000");

            // The ending point of random dates (Current time)
            Date currentDate = new Date();

            createReceipt("1", "Carz", Integer.toString(1000 + rand.nextInt(8999)), new Date(ThreadLocalRandom.current().nextLong(startDate.getTime(), currentDate.getTime())));
            createReceipt("2", "Carz", Integer.toString(1000 + rand.nextInt(8999)), new Date(ThreadLocalRandom.current().nextLong(startDate.getTime(), currentDate.getTime())));
        }
    }

    @Override
    public Car findCarBySerialNumber(String serialNumber) throws RemoteException {
        List<Car> cars = findAllCars();
        for (Car car : cars) {
            if (car.getSerialNumber().equals(serialNumber))
                return car;
        }
        return null;
    }

    @Override
    public List<Car> findCarsByBrand(String brand) throws RemoteException {
        List<Car> cars = findAllCars();

        return cars.stream().filter(car -> car.getBrand().toLowerCase(Locale.ROOT).equals(brand.toLowerCase())).collect(Collectors.toList());
    }

    /**
     * Reads the cars file and creates a list of all the cars read from the file
     * @return A list of all the cars in the database
     */
    private List<Car> findAllCars() {
        List<Car> cars = new ArrayList<>();
        try {
            FileInputStream f = new FileInputStream(carsFilePath);
            ObjectInputStream o = new ObjectInputStream(f);
            Object car;
            while ((car = o.readObject()) != null) {
                cars.add((Car) car);
            }
            o.close();
            f.close();
        } catch (EOFException ignored) {
        } catch (FileNotFoundException e) {
            System.out.println("Cars file not found!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading from file: " + e);
        }
        return cars;
    }

    @Override
    public void createCar(String serialNumber, String brand, String model, String color, int year, float price, float weight) throws RemoteException {
        List<Car> cars = findAllCars();
        cars.add(new Car(serialNumber, brand, model, color, year, price, weight));
        try {
            FileOutputStream f = new FileOutputStream(carsFilePath);
            ObjectOutputStream o = new ObjectOutputStream(f);
            for (Car car : cars) {
                o.writeObject(car);
            }
            o.close();
            f.close();
        } catch (FileNotFoundException e) {
            System.out.println("Cars file not found!");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e);
        }
    }

    @Override
    public Receipt findReceiptById(String id) throws RemoteException {
        List<Receipt> receipts = findAllReceipts();
        for (Receipt receipt : receipts) {
            if (receipt.getID().equals(id))
                return receipt;
        }
        return null;
    }

    /**
     * Reads the receipts file and creates a list of all the receipts read from the file
     * @return A list of all the receipts in the database
     */
    private List<Receipt> findAllReceipts() {
        List<Receipt> receipts = new ArrayList<>();
        try {
            FileInputStream f = new FileInputStream(receiptsFilePath);
            ObjectInputStream o = new ObjectInputStream(f);
            Object receipt;
            while ((receipt = o.readObject()) != null) {
                receipts.add((Receipt) receipt);
            }
            o.close();
            f.close();
        } catch (EOFException ignored) {
        } catch (FileNotFoundException e) {
            System.out.println("Receipts file not found!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading from file: " + e);
        }
        return receipts;
    }

    @Override
    public List<Receipt> getReceiptsByVendor(String vendor) throws RemoteException {
        List<Receipt> receipts = findAllReceipts();

        return receipts.stream().filter(r -> r.getVendor().toLowerCase(Locale.ROOT).equals(vendor.toLowerCase(Locale.ROOT))).collect(Collectors.toList());
    }

    @Override
    public void createReceipt(String ID, String vendor, String carSerialNumber, Date date) throws RemoteException {
        List<Receipt> receipts = findAllReceipts();
        receipts.add(new Receipt(ID, vendor, carSerialNumber, date));
        try {
            FileOutputStream f = new FileOutputStream(receiptsFilePath);
            ObjectOutputStream o = new ObjectOutputStream(f);
            for (Receipt receipt : receipts) {
                o.writeObject(receipt);
            }
            o.close();
            f.close();
        } catch (FileNotFoundException e) {
            System.out.println("Receipts file not found!");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e);
        }
    }
}
