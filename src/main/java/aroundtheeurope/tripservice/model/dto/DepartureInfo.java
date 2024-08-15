    package aroundtheeurope.tripservice.model.dto;
    import aroundtheeurope.tripservice.model.entity.FlightInfoEntity;
    import com.fasterxml.jackson.annotation.JsonProperty;

    import java.time.LocalDateTime;

    /**
     * Model class representing departure information.
     */
    public class DepartureInfo {

        @JsonProperty("flightNumber")
        private String flightNumber;
        @JsonProperty("departureAt")
        private LocalDateTime departureAt;
        @JsonProperty("originAirportName")
        private String originAirportName;
        @JsonProperty("originAirportCode")
        private String originAirportCode;
        @JsonProperty("originCountryCode")
        private String originCountryCode;
        @JsonProperty("destinationAirportName")
        private String destinationAirportName;
        @JsonProperty("destinationAirportCode")
        private String destinationAirportCode;
        @JsonProperty("destinationCountryCode")
        private String destinationCountryCode;
        @JsonProperty("price")
        private double price;
        @JsonProperty("currencyCode")
        private String currencyCode;

        /**
         * Constructor for DepartureInfo.
         *
         * @param flightNumber          the flight number
         * @param departureAt           the departure date and time
         * @param originAirportName     the name of the origin airport
         * @param originAirportCode     the IATA code of the origin airport
         * @param originCountryCode     the ISO country code of the origin country
         * @param destinationAirportName the name of the destination airport
         * @param destinationAirportCode the IATA code of the destination airport
         * @param destinationCountryCode the ISO country code of the destination country
         * @param price                 the price of the flight
         * @param currencyCode          the ISO currency code
         */
        public DepartureInfo (
                                String flightNumber,
                                LocalDateTime departureAt,
                                String originAirportName,
                                String originAirportCode,
                                String originCountryCode,
                                String destinationAirportName,
                                String destinationAirportCode,
                                String destinationCountryCode,
                                double price,
                                String currencyCode
        ) {
            this.flightNumber = flightNumber;
            this.departureAt = departureAt;
            this.originAirportName = originAirportName;
            this.originAirportCode = originAirportCode;
            this.originCountryCode = originCountryCode;
            this.destinationAirportName = destinationAirportName;
            this.destinationAirportCode = destinationAirportCode;
            this.destinationCountryCode = destinationCountryCode;
            this.price = price;
            this.currencyCode = currencyCode;
        }

        public static DepartureInfo constructFromEntity(FlightInfoEntity entity){
            return new DepartureInfo(
                    entity.getFlightNumber(),
                    entity.getDepartureAt(),
                    entity.getOriginAirportName(),
                    entity.getOriginAirportCode(),
                    entity.getOriginCountryCode(),
                    entity.getDestinationAirportName(),
                    entity.getDestinationAirportCode(),
                    entity.getDestinationCountryCode(),
                    entity.getPrice(),
                    entity.getCurrencyCode()
            );
        }

        // Getters and setters

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getDestinationCountryCode() {
            return destinationCountryCode;
        }

        public void setDestinationCountryCode(String destinationCountryCode) {
            this.destinationCountryCode = destinationCountryCode;
        }

        public String getOriginCountryCode() {
            return originCountryCode;
        }

        public void setOriginCountryCode(String originCountryCode) {
            this.originCountryCode = originCountryCode;
        }

        public String getFlightNumber() {
            return flightNumber;
        }

        public void setFlightNumber(String flightNumber) {
            this.flightNumber = flightNumber;
        }

        public LocalDateTime getDepartureAt() {
            return departureAt;
        }

        public void setDepartureAt(LocalDateTime departureAt) {
            this.departureAt = departureAt;
        }

        public String getOriginAirportName() {
            return originAirportName;
        }

        public void setOriginAirportName(String originAirportName) {
            this.originAirportName = originAirportName;
        }

        public String getOriginAirportCode() {
            return originAirportCode;
        }

        public void setOriginAirportCode(String originAirportCode) {
            this.originAirportCode = originAirportCode;
        }

        public String getDestinationAirportName() {
            return destinationAirportName;
        }

        public void setDestinationAirportName(String destinationAirportName) {
            this.destinationAirportName = destinationAirportName;
        }

        public String getDestinationAirportCode() {
            return destinationAirportCode;
        }

        public void setDestinationAirportCode(String destinationAirportCode) {
            this.destinationAirportCode = destinationAirportCode;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return "From " + originAirportCode + " to " + destinationAirportCode + " at " + departureAt + " for " + price;
        }
    }
