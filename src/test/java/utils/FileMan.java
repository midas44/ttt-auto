package utils;

import org.apache.commons.io.FileUtils;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FileMan {

    public void loadProps(Properties props, String dir, String name){
        String propsSource = Paths.get(dir, name+".properties").toFile().getAbsolutePath();
        try(FileReader fileReader = new FileReader(propsSource)){
            props.load(fileReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveProps(Properties props, String dir, String name, String comment){
        String propsTarget = Paths.get(dir, name+".properties").toFile().getAbsolutePath();
        try(FileWriter output = new FileWriter(propsTarget)){
            props.store(output, comment);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFile(String dir, String filename) {
        Path target = Paths.get(dir, filename+".txt");
        try {
            boolean result = Files.deleteIfExists(target);
            if (result) {
                System.out.println("File is deleted!");
            } else {
                System.out.println("Sorry, unable to delete the file.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void cleanDir(String dir){
        File directory = new File(dir);
        try {
            FileUtils.cleanDirectory(directory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveStringListToTextFile(List<String> list, String dir, String filename){
        String target = Paths.get(dir, filename+".txt").toFile().getAbsolutePath();
        FileWriter writer;
        try {
            writer = new FileWriter(target);
            for (final String line : list) {
                writer.write(line + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> loadStringListFromTextFile(String dir, String filename){
        String source = Paths.get(dir, filename+".txt").toFile().getAbsolutePath();
        ArrayList<String> list = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(
                    new FileReader(source))) {
                while (reader.ready()) {
                    list.add(reader.readLine());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        return list;
    }
}
