package skamila.weather.api.forecast;

import java.util.List;

public class FavoriteCities {

    private List<City> cities;

    public void addCity(City city){
        cities.add(city);
    }

    public void deleteCity(City city){
        cities.remove(city);
    }

    public List<City> getCities(){
        return cities;
    }

}
