package IOutils;

/*
@author MirageLyu
*
 */

import Contri.FileContributor;
import com.csvreader.CsvWriter;

import java.nio.charset.Charset;
import java.util.List;

public class MatrixSaveDelegate implements Save {
    private List<FileContributor> fileContributors;

    String filepath;

    public void setFilepath(String filepath){
        this.filepath = filepath;
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
        CsvWriter csvWriter = new CsvWriter(filepath, ',', Charset.forName("UTF-8"));

    }
}
