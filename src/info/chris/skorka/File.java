package info.chris.skorka;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class File {

    /**
     * read the contents of a recourse file
     * @param path path to file in resources of this program
     * @return contents of the file as a String
     */
    public static String readFileToString(String path){

        // string builder
        StringBuilder stringBuilder = new StringBuilder();

        // file reading
        InputStream inputStream = File.class.getResourceAsStream(path);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        try {

            String line;

            // read a line at a time and add it to the string builder
            while((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line).append("\n");
            }

        } catch (IOException e){
            System.err.println("IOException when reading '" + path + "'");
            return null;
        }

        return stringBuilder.toString();
    }

}
