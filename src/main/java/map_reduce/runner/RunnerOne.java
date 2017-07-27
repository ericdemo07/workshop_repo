package map_reduce.runner;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import map_reduce.mapper.MapperOne;
import map_reduce.util.CommonUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Created by ayush.shukla on 7/27/2017.
 */
public class RunnerOne extends Configured implements Tool {
    static Map<String, String> map = new HashMap();

    public static void main(String... args) throws Exception {

        //creating new array of size 3
        args = new String[3];
        try {
            //input dir
            args[0] = CommonUtil.getResourcePath() + "input" + File.separator + "inputfile1";
            //output dir
            args[1] = CommonUtil.getResourcePath() + "output";
            //input csv
            args[2] = CommonUtil.getResourcePath() + "inputcsv" + File.separator + "input-url.csv";
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }

        String inputAsListSerialized = null;
        //here we will be pushing inputcsv parameters to a string
        try {
            Stream<String> stream = Files.lines(Paths.get(args[2]));
            List<String> list = stream.filter(string -> !string.isEmpty()).map(string -> string.trim()).distinct().collect(Collectors.toList());
            Gson gson = new Gson();
            inputAsListSerialized = gson.toJson(list);
            if (inputAsListSerialized == null || inputAsListSerialized.length() < 3) {
                throw new Exception("inputcsv can not be empty");
            }
            System.out.println(inputAsListSerialized);
        } catch (IOException e) {
            System.out.println("Exception occoured while trying to read csv");
            e.printStackTrace();
        }

        map = new HashMap<>();                                  //config map
        map.put("inputAsListSerialized", inputAsListSerialized);

        ToolRunner.run(new RunnerOne(), args);
    }

    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("ayush","shukla");
        System.out.println("Starting Map Reduce");
        System.out.println("map [" + map);
        System.out.println("conf [" + conf.size());
        map.forEach((k,v)->conf.set(k,v));
        System.out.println("conf1 [" + conf);
        Job job = new Job(conf);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        //just to make sure output folder is clean
        FileSystem.get(job.getConfiguration()).delete(new Path(args[1]), true);
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        job.setJobName("workshop_bigdata");
        job.setJarByClass(RunnerOne.class);
        job.setMapperClass(MapperOne.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

       // boolean success = job.waitForCompletion(true);
        //return success ? 0 : 1;
        return 0;
    }
}
