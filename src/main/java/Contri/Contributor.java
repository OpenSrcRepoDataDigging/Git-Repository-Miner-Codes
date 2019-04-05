package Contri;

import filecontributesupport.CodeFile;
import filecontributesupport.LOC;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *describe:
 *记录一位贡献者信息：
 * AuthorName 名字 LOC_Add_All添加的LOC，LOC_Delete_All删除的LOC
 * 开发者commit的信息
 *@author 欧阳
 *@date ${2019}/${3}/${32}
 */

public class Contributor implements FileContributor {
    private String AuthorName;
    private int LOC_Add_All;
    private int LOC_Delete_All;
    private List<CommitMessages> commitList = new ArrayList<>();    //记录该开发者的Commit信息
    private HashMap<String,CommitMessages> commitMap = new HashMap<String, CommitMessages>(); //每次Commit和Commit信息的HashMap

    //MirageLyu: Every contributor has a file list(using hashmap instead), each element is the LOC on this file by this contributor.
    private HashMap<String, LOC> locfilemap = new HashMap<>();

    public Contributor(String authorName) {
        AuthorName = authorName;
        LOC_Add_All = 0;
        LOC_Delete_All = 0;
    }

    @Override
    public String getName() {
        return this.AuthorName;
    }

    @Override
    public int getAllLOC() {
        return LOC_Add_All + LOC_Delete_All;
    }

    @Override
    public HashMap<String, LOC> getMap() {
        return locfilemap;
    }

    @Override
    public void insertFileMap(String cf, LOC loc) {
        //TODO: walk through this commit to find: All changed files and LOC, insert to locfilemap
        if(locfilemap.get(cf) != null){
            locfilemap.get(cf).addAddition(loc.getAddition()).addDeletion(loc.getDeletion());
        }else{
            locfilemap.put(cf, loc);
        }
    }

    @Override
    public FileContributor changeFilePathNameInMap(String oldpath, String newpath) {
        //TODO: update the name of mode to new path name
        if(!oldpath.equals(newpath)){
            locfilemap.put(newpath, locfilemap.get(oldpath));
            locfilemap.remove(oldpath);
        }
        return this;
    }

    @Override
    public boolean isInsideMap(String filename) {
        //TODO: whether the mode is inside the file-contributor map
        return locfilemap.get(filename) != null;
    }

    public void insertRevCommit(RevCommit commit){
        CommitMessages messages = new CommitMessages(commit);
        commitList.add(messages);
        commitMap.put(commit.getName(),messages);
    }

    public void setLOC_Add(String commitName,int LOC_Add){
        commitMap.get(commitName).setLOC_Add(LOC_Add);
        LOC_Add_All += LOC_Add;
    }

    public void setLOC_Delete(String commitName,int LOC_Delete){
        commitMap.get(commitName).setLOC_Add(LOC_Delete);
        LOC_Delete_All += LOC_Delete;
    }

    public int getLOC_Add_All() {
        return LOC_Add_All;
    }

    public int getLOC_Delete_All() {
        return LOC_Delete_All;
    }

    public void dispalyContributorMessages(){
        System.out.println("Contributor Name: "+ this.AuthorName);
        System.out.println("\033[32;1m" + "LOC++:" + LOC_Add_All + "\033[0m");
        System.out.println("\033[31;1m" + "LOC--:" + LOC_Delete_All + "\033[0m");

        System.out.println("FileList:  ");
        for(Map.Entry<String, LOC> entry : locfilemap.entrySet()){
            System.out.println(entry.getKey() + ":  " + entry.getValue().getAddition() + "/" + entry.getValue().getDeletion());
        }

        //commitList.forEach(CommitMessages::displayMessages);
        System.out.println("---------------End-----------------------------");
    }

}
