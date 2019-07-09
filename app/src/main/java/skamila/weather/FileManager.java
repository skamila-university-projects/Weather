package skamila.weather;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.content.Context.MODE_PRIVATE;

public class FileManager {

    private Context context;
    private String fileName;

    public FileManager(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName + ".txt";
    }

    public void saveToFile(String text) {

        FileOutputStream fos;

        try {
            fos = context.openFileOutput(fileName, MODE_PRIVATE);
            fos.write(text.getBytes());
            System.out.println(context.getPackageResourcePath());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String loadFromFile() {

        FileInputStream fis;
        String text;

        try {
            fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();

            while ((text = br.readLine()) != null) {
                sb.append(text);
            }
            System.out.println(context.getPackageResourcePath());
            fis.close();
            isr.close();
            br.close();

            return sb.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";

    }

}
