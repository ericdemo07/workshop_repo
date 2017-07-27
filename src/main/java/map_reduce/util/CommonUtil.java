package map_reduce.util;

import java.io.File;

/**
 * Created by ayush.shukla on 7/27/2017.
 */
public class CommonUtil
{
    //nullifying OS path dependency, if any
    public static String getResourcePath() throws Exception {
        String root, path;
        String os = System.getProperty("os.name");
        if (os.toLowerCase().contains("win")) {
            File currDir = new File(".");
            path = currDir.getAbsolutePath();
            path = path.substring(0, path.length() - 1);
        } else if (os.toLowerCase().contains("mac")) {
            File currDir = new File(".");
            path = currDir.getAbsolutePath();
            path = path.substring(0, path.length() - 1);
        } else {
            throw new Exception("OS not determined");
        }
        root = path + "src" + File.separator + "main" + File.separator + "resources" + File.separator;
        return root;
    }
}
