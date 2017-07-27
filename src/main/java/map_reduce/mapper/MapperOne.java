package map_reduce.mapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.DoubleAccumulator;

/**
 * Created by ayush.shukla on 7/27/2017.
 */
public class MapperOne extends Mapper<LongWritable, Text, Text, Text> {
    Gson gson = new Gson();
    private List<String> inputAsList;

    public void setup(Mapper.Context context) throws IOException, InterruptedException {
        Configuration conf = context.getConfiguration();
        System.out.println(conf);
        String inputAsListDeserialized = conf.get("inputAsListSerialized");
        inputAsList = gson.fromJson(inputAsListDeserialized, new TypeToken<List<String>>() {
        }.getType());
    }

    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        try {
            //converting json input to map
            Map obj = (Map) gson.fromJson(value.toString(), Object.class);
            if (obj != null) {
                if (obj.get("shopperid") != null) {
                    String shopperId = (String) obj.get("shopperid");

                    for (String id : inputAsList) {
                        if (shopperId.equals(id)) {
                            context.write(new Text(shopperId), new Text(shopperId));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
