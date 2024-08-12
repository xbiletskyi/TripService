CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE trip_requests (
                               id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                               budget NUMERIC,
                               departure_at TIMESTAMP,
                               destination VARCHAR(255),
                               excluded_airports TEXT,
                               max_stay INT,
                               min_stay INT,
                               origin VARCHAR(255),
                               return_before TIMESTAMP,
                               schengen_only BOOLEAN,
                               time_limit_seconds INT,
                               user_id INT REFERENCES users(id)
);

CREATE TABLE departure_info (
                                id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                flight_number VARCHAR(255),
                                departure_at TIMESTAMP,
                                origin_airport_name VARCHAR(255),
                                origin_airport_code VARCHAR(10),
                                origin_country_code VARCHAR(10),
                                destination_airport_name VARCHAR(255),
                                destination_airport_code VARCHAR(10),
                                destination_country_code VARCHAR(10),
                                price NUMERIC,
                                currency_code VARCHAR(10)
);

CREATE TABLE found_trips (
                             id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                             total_price NUMERIC,
                             total_flights INT,
                             unique_cities INT,
                             unique_countries INT,
                             departure_at TIMESTAMP,
                             arrival_at TIMESTAMP,
                             user_id UUID REFERENCES users(id),
                             request_id UUID REFERENCES trip_requests(id)
);

CREATE TABLE found_trip_schedule (
                                     found_trip_id UUID REFERENCES found_trips(id) ON DELETE CASCADE,
                                     departure_info_id UUID REFERENCES departure_info(id) ON DELETE CASCADE,
                                     PRIMARY KEY (found_trip_id, departure_info_id)
);

