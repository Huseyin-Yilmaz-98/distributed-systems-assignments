package interfaces;

import java.io.Serializable;
import java.util.Date;


public class Receipt implements Serializable {
    private static final long serialVersionUID = 638252221122791451L;
    final String ID, vendor, carSerialNumber;
    final Date date;

    public Receipt(String ID, String vendor, String carSerialNumber, Date date) {
        this.ID = ID;
        this.vendor = vendor;
        this.carSerialNumber = carSerialNumber;
        this.date = date;
    }

    public String getID() {
        return ID;
    }

    public String getVendor() {
        return vendor;
    }

    public String getCarSerialNumber() {
        return carSerialNumber;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "ID='" + ID + '\'' +
                ", vendor='" + vendor + '\'' +
                ", carSerialNumber='" + carSerialNumber + '\'' +
                ", date=" + date +
                '}';
    }
}
