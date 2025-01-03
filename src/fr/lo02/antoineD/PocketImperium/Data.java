package fr.lo02.antoineD.PocketImperium;

import fr.lo02.antoineD.PocketImperium.Exception.EmptyPropertiesException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Data {
    protected Properties prop = new Properties();

    public Data(String path){
        try (InputStream input = new FileInputStream(path)) {
            this.prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<List<Integer>> getData(String key) {
        String dataString = prop.getProperty(key);
        List<List<Integer>> data = new ArrayList<>();

        if (dataString == null){
            throw new EmptyPropertiesException("This property \""+key+"\" is empty");
        }

        String[] rows = dataString.split(";");
        for (String row : rows) {
            List<Integer> rowList = new ArrayList<>();
            String[] values = row.split(",");
            for (String value : values) {
                rowList.add(Integer.parseInt(value));
            }
            data.add(rowList);
        }
        return data;
    }
}
