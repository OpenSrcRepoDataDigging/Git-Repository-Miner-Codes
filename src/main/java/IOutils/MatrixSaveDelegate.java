package IOutils;

/*
@author MirageLyu
*
 */

import Contri.FileContributor;

import java.util.List;

public class MatrixSaveDelegate implements Save {
    private List<FileContributor> fileContributors;


    public void setFileContributors(List<FileContributor> fileContributors) {
        this.fileContributors = fileContributors;
    }
    public List<FileContributor> getFileContributors() {
        return fileContributors;
    }

    public void save(){
        //TODO: Save the FileContributorMatrix to file in proper format.
    }
}
