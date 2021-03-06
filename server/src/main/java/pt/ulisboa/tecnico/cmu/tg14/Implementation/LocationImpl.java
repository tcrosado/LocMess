package pt.ulisboa.tecnico.cmu.tg14.Implementation;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import pt.ulisboa.tecnico.cmu.tg14.Mapper.CoordinatesMapper;
import pt.ulisboa.tecnico.cmu.tg14.Mapper.LocationMapper;
import pt.ulisboa.tecnico.cmu.tg14.Mapper.MessageMapper;
import pt.ulisboa.tecnico.cmu.tg14.Mapper.UserMapper;
import pt.ulisboa.tecnico.cmu.tg14.Model.Coordinates;
import pt.ulisboa.tecnico.cmu.tg14.Model.Location;
import pt.ulisboa.tecnico.cmu.tg14.Model.Message;
import pt.ulisboa.tecnico.cmu.tg14.Model.User;
import pt.ulisboa.tecnico.cmu.tg14.dao.LocationDao;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by trosado on 20/03/17.
 */
public class LocationImpl implements LocationDao {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;

    @Override
    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);

    }



    @Override
    public void create(String name, String ssid, String ble) {

        String SQL = "insert into Location (name, ssid, ble) values (?, ?, ?)";

        jdbcTemplateObject.update( SQL, name, ssid.toLowerCase(), ble.toLowerCase());
        return;
    }

    @Override
    public Location getLocationBySSID(String ssid) {
        String SQL = "Select * from Location where ssid=?";

        List<Location> locList = jdbcTemplateObject.query(SQL,new Object[]{ssid.toLowerCase()},new LocationMapper());
        if(locList.isEmpty())
            return null;
        else
            return locList.get(0);
    }

    @Override
    public List<Location> getLocationBySSID(List<String> ssids){
        if(ssids != null)
            return ssids.stream().map(this::getLocationBySSID).collect(Collectors.toList());
        else
            return new ArrayList<>();
    }


    @Override
    public Location getLocationByBle(String ble) {
        String SQL = "Select * from Location where ble=?";
            List<Location> loc = jdbcTemplateObject.query(SQL,new Object[]{ble.toLowerCase()},new LocationMapper());
        if(loc.isEmpty())
            return null;
        else
            return loc.get(0);
    }
    @Override
    public List<Location> getLocationByBle(List<String> bles) {
        if(bles != null)
            return bles.stream().map(this::getLocationByBle).collect(Collectors.toList());
        else
            return new ArrayList<>();
    }

    @Override
    public List<Location> getLocationByCoord(float lat, float lon) {
        String CoordSQL = "SELECT * ,POW(69.1 * (lat - ?), 2) + POW(69.1 * (? - lon) * COS(lat / 57.3), 2) AS distance FROM Coordinates " +
                "HAVING distance < POW(radius,2) ORDER BY distance;";
        List<Coordinates> coords = jdbcTemplateObject.query(CoordSQL,new Object[]{lat,lon},new CoordinatesMapper());


        System.out.println("getLocationByCoord: found locations by coord size -> "+coords.size());
        List<Location> loclist = new ArrayList<>();
        List<Location> result = new ArrayList<>();
        for(Coordinates coord: coords){
            String SQL = "Select * from Location where name=?";
            System.out.println("getLocationByCoord: found location name -> "+coord.getName());
            loclist = jdbcTemplateObject.query(SQL,new Object[]{coord.getName()},new LocationMapper());
            for(Location l : loclist){
                result.add(l);
            }
        }
        return result;
    }

    @Override
    public List<Location> getLocationList() {

        String CoordSQL = "SELECT * FROM Location ORDER BY name ASC;";
        return jdbcTemplateObject.query(CoordSQL,new LocationMapper());
   }


    @Override
    public void delete(String name) {

        System.out.println("deleting location -> "+name);
        String SQL = "delete from Location where name = ?";
        jdbcTemplateObject.update(SQL, name);
    }


}


