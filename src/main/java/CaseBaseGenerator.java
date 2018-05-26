import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CaseBaseGenerator {

    private String _filePath;

    public CaseBaseGenerator() {}

    protected void generate() {
        String selectedFile = selectFile();
        List<String> fileContents = readCSV(selectedFile);


    }

    protected String selectFile() {
        // Shit is commented out because it takes way too long, use to get the Absolute path and then just change the return
        // Value shakkas bruv.
        /*
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "CSV files", "csv");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " +
                    chooser.getSelectedFile().getAbsolutePath());
            return chooser.getSelectedFile().getAbsolutePath();
        } else {
            return "No File Chosen";
        }*/
        return "/Users/samli/University/COMPSCI 760/Project Shit/MushroomKLECBR/data/small-test.csv";
    }

    protected List<String> readCSV(String selectedFile) {
        List<String> lines = new ArrayList<>();

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(selectedFile));
        } catch (FileNotFoundException e) {
            return lines;
        }

        String line;

        try {
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

}
