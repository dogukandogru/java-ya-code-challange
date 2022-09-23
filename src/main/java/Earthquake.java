class Earthquake {
    private final String country;
    private final String place;
    private final double magnitude;
    private final String date;
    private final String time;

    public Earthquake(String country, String place, double magnitude, String date, String time) {
        this.country = country;
        this.place = place;
        this.magnitude = magnitude;
        this.date = date;
        this.time = time;
    }

    public String getCountry() {
        return country;
    }

    public String getPlace() {
        return place;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

}
