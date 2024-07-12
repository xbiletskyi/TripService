package aroundtheeurope.findroute.Services;

import aroundtheeurope.findroute.Models.DepartureInfo;

import java.util.List;

public interface DepartureService {
    List<DepartureInfo> getDepartures(String airportCode,
                                      String date,
                                      boolean schengenOnly);
}
