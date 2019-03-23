package Contri;

import org.eclipse.jgit.revwalk.RevCommit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Contributor {
    private String AuthorName;
    private int LOC_Add_All;
    private int LOC_Delete_All;
    private List<CommitMessages> commitList = new ArrayList<>();    //记录该开发者的Commit信息
    private HashMap<String,CommitMessages> commitMap = new HashMap<String, CommitMessages>(); //每次Commit和Commit信息的HashMap

    Contributor(String authorName) {
        AuthorName = authorName;
        LOC_Add_All = 0;
        LOC_Delete_All = 0;
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
        commitList.forEach(c -> {
            c.displayMessages();
        });
        System.out.println("---------------End-----------------------------");
    }

}
