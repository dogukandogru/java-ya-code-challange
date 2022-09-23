class Country {
    private final String name;
    private final double minLatitude;
    private final double minLongitude;
    private final double maxLatitude;
    private final double maxLongitude;

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", minLatitude=" + minLatitude +
                ", minLongitude=" + minLongitude +
                ", maxLatitude=" + maxLatitude +
                ", maxLongitude=" + maxLongitude +
                '}';
    }

    public double getMinLatitude() {
        return minLatitude;
    }

    public double getMinLongitude() {
        return minLongitude;
    }

    public String getName() {
        return name;
    }

    public double getMaxLatitude() {
        return maxLatitude;
    }

    public Country(String name, double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) {
        this.name = name;
        this.minLatitude = minLatitude;
        this.minLongitude = minLongitude;
        this.maxLatitude = maxLatitude;
        this.maxLongitude = maxLongitude;
    }

    public double getMaxLongitude() {
        return maxLongitude;
    }

}
