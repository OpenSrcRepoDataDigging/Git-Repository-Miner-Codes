package Contri;

import filecontributesupport.CodeFile;
import filecontributesupport.LOC;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private HashMap<CodeFile, LOC> locfilemap = new HashMap<>();

    public Contributor(String authorName) {
        AuthorName = authorName;
        LOC_Add_All = 0;
        LOC_Delete_All = 0;
    }

    @Override
    public void insertCommitToFileMap(RevCommit commit) {
        //TODO: walk through this commit to find: All changed files and LOC, insert to locfilemap

    }

    public void insertRevCommit(RevCommit commit){
        CommitMessages messages = new CommitMessages(commit);
        commitList.add(messages);
        commitMap.put(commit.getName(),messages);

        insertCommitToFileMap(commit);
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
        commitList.forEach(c -> {
            c.displayMessages();
        });
        System.out.println("---------------End-----------------------------");
    }

}
