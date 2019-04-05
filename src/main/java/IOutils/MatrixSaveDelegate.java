package IOutils;

/*
@author MirageLyu
*
 */

import Contri.FileContributor;
import com.csvreader.CsvWriter;
import filecontributesupport.LOC;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

public class MatrixSaveDelegate implements Save {
    private List<FileContributor> fileContributors;
    private List<String> filelist;
    private String filepath;

    public void setFilepath(String filepath){
        this.filepath = filepath;
    }

    public void setFilelist(List<String> filelist) {
        this.filelist = filelist;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFileContributors(List<FileContributor> fileContributors) {
        this.fileContributors = fileContributors;
    }
    public List<FileContributor> getFileContributors() {
        return fileContributors;
    }

    public void save(){
        //TODO: Save the FileContributorMatrix to file in proper format.
        System.out.println("--- MirageLyu: Start to save Contributor-File-LOC Matrix ---");

        CsvWriter csvWriter = new CsvWriter(filepath, ',', Charset.forName("GBK"));
        try{
            csvWriter.write("");
            for(String filename : filelist){
                csvWriter.write(filename);
            }
            csvWriter.endRecord();
        }catch (IOException e){
            e.printStackTrace();
        }
        try{
            for(FileContributor fc : fileContributors){
                csvWriter.write(fc.getName());
                System.out.println("Writing Contributor: " + fc.getName());
                HashMap<String, LOC> hm = fc.getMap();
                for(String filename : filelist){
                    if(hm.containsKey(filename)){
                        csvWriter.write(String.valueOf(hm.get(filename).getAddition() + hm.get(filename).getDeletion()));
                    }
                    else{
                        csvWriter.write(String.valueOf(0));
                    }
                }
                csvWriter.endRecord();
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        csvWriter.close();
        System.out.println("--- MirageLyu: Finish saving Contributor-File-LOC Matrix ---");
    }
}
