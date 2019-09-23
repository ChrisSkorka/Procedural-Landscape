package info.chris.skorka;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class File {

    public static String readFileToString(String path){

        StringBuilder stringBuilder = new StringBuilder();

        InputStream inputStream = File.class.getResourceAsStream(path);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        try {

            String line;
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
