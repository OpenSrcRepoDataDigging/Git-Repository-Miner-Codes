package Contri;

import IOutils.MatrixDisplayDelegate;
import IOutils.MatrixSaveDelegate;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContributorMap implements FileContributorMatrix{
    private HashMap<String,Contributor> maps = new HashMap<String, Contributor>();

    /*  MirageLyu: File-Contributor-LOC Matrix */
    private List<FileContributor> fileContributors = new ArrayList<>();
    /*  MirageLyu: Delegate for save the Matrix */
    private MatrixSaveDelegate matrixSaveDelegate = new MatrixSaveDelegate();

    public ContributorMap(Git git) {
        initMaps(git);

        /* Initialize properties of the Save Delegate: 1.Contributor List; 2.Save FilePath */
        matrixSaveDelegate.setFileContributors(fileContributors);
        matrixSaveDelegate.setFilepath("D:\\JGIT_Output\\" + git.toString() + ".csv");
    }

    @Override
    public void compressMatrix(int height, int width) {
        //TODO: Compress the matrix to smaller size, on most lines files and most LOC contributors.

    }

    @Override
    public void insertFileContributor(FileContributor fc) {
        fileContributors.add(fc);
    }

    private void initMaps(Git git){
        RevWalk walk = new RevWalk(git.getRepository());
        Iterable<RevCommit> commits = null;
        try {
            commits = git.log().call();
            for(RevCommit commit:commits){
                String name = commit.getAuthorIdent().getName();
                if(getContributor(name)!=null){
                    getContributor(name).insertRevCommit(commit);
                }else{
                    Contributor author = new Contributor(name);

                    //MirageLyu: insert the reference of author to filecontributor list.
                    insertFileContributor(author);


                    author.insertRevCommit(commit);
                    this.bindContributor(name,author);
                }
            }

        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    public void bindContributor(String authorName, Contributor contributor){
        if(!maps.containsKey(authorName))
            maps.put(authorName,contributor);
    }

    public Contributor getContributor(String authorName){
        return maps.getOrDefault(authorName, null);
    }
    public HashMap<String, Contributor> getMaps() {
        return maps;
    }
    public MatrixSaveDelegate getMatrixSaveDelegate() {
        return matrixSaveDelegate;
    }
    public List<FileContributor> getFileContributors() {
        return fileContributors;
    }
}
