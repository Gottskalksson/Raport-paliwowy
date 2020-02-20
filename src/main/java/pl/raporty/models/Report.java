package pl.raporty.models;

public class Report {
    private int id;
    private int carId;
    private String data;
    private double fuelLitters;
    private double pricePerLitter;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getFuelLitters() {
        return fuelLitters;
    }

    public void setFuelLitters(double fuelLitters) {
        this.fuelLitters = fuelLitters;
    }

    public double getPricePerLitter() {
        return pricePerLitter;
    }

    public void setPricePerLitter(double pricePerLitter) {
        this.pricePerLitter = pricePerLitter;
    }
}
