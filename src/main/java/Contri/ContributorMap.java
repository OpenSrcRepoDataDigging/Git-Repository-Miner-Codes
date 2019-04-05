package Contri;

import IOutils.MatrixSaveDelegate;
import filecontributesupport.LOC;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/*
 * @Author: Young，MirageLyu
 * @Description: 该类主要负责建立贡献者String和贡献者的索引
 * */
public class ContributorMap implements FileContributorMatrix{
    private HashMap<String,Contributor> maps = new HashMap<String, Contributor>();

    /*  MirageLyu: File-Contributor-LOC Matrix */
    private List<FileContributor> fileContributors = new ArrayList<>();
    /*  MirageLyu: Delegate for save the Matrix */
    private MatrixSaveDelegate matrixSaveDelegate = new MatrixSaveDelegate();
    /* MirageLyu: All File Names List */
    private List<String> fileList = new ArrayList<>();
    /* MirageLyu: File-LOC(Changed lines) List <filename, LOC>. */
    private HashMap<String, LOC> file_LOCmap = new HashMap<>();

    public HashMap<String, LOC> getFile_LOCmap() {
        return file_LOCmap;
    }

    public ContributorMap(Git git) {
        initMaps(git);
        /* Initialize properties of the Save Delegate: 1.Contributor List; 2.Save FilePath */
        matrixSaveDelegate.setFileContributors(fileContributors);
        matrixSaveDelegate.setFilepath("D:\\1.csv");
        matrixSaveDelegate.setFilelist(fileList);
    }

    @Override
    public FileContributorMatrix compressMatrix(int height, int width) {
        /* height: the number of Contributors; width: the number of Files. */

        //TODO: Compress the matrix to smaller size, on most lines files and most LOC contributors.
        fileContributors.sort((o1, o2) -> o2.getAllLOC() - o1.getAllLOC());

        for(; height <fileContributors.size(); ){
            fileContributors.remove(fileContributors.size() - 1);
        }

        /* TODO: Compress FileList */
        fileList.sort((o1, o2) -> file_LOCmap.get(o2).getDeletion() + file_LOCmap.get(o2).getAddition() - file_LOCmap.get(o1).getAddition() - file_LOCmap.get(o2).getDeletion());

        for(; width < fileList.size(); ){
            fileList.remove(fileList.size() - 1);
        }

        return this;
    }

    @Override
    public void insertFileContributor(FileContributor fc) {
        fileContributors.add(fc);
    }

    @Override
    public void insertFileToFileList(String filename) {
        if(!fileList.contains(filename))
            fileList.add(filename);
    }

    @Override
    public FileContributorMatrix insertFileLOCPairToMap(String filename, LOC loc){
        if(file_LOCmap.containsKey(filename)){
            file_LOCmap.get(filename).addAddition(loc.getAddition()).addDeletion(loc.getDeletion());
        }
        else{
            file_LOCmap.put(filename, loc);
        }
        return this;
    }

    @Override
    public FileContributorMatrix renameFileNameInFileLOCMap(String oldpath, String newpath){
        if(file_LOCmap.containsKey(oldpath)){
            file_LOCmap.put(newpath, file_LOCmap.get(oldpath));
            file_LOCmap.remove(oldpath);
        }
        return this;
    }

    @Override
    public void removeFileFromFileList(String filename) {
        fileList.remove(filename);
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

    @Override
    public MatrixSaveDelegate getMatrixSaveDelegate() {
        return matrixSaveDelegate;
    }
    public List<FileContributor> getFileContributors() {
        return fileContributors;
    }
}
