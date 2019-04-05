package Contri;

import org.eclipse.jgit.revwalk.RevCommit;

import java.util.Date;

/*
 * @Author: Young
 * @Description: 该类记录一条Commit记录的各种信息
 * */
public class CommitMessages {
    private RevCommit commit;
    //Commit的固有属性
    private Date CommitTime;
    private String CommitMsg;
    //需要比较才有的数据
    private int LOC_Add;
    private int LOC_Delete;

    public CommitMessages(RevCommit commit) {
        this.commit = commit;
        this.CommitTime = commit.getAuthorIdent().getWhen();
        this.CommitMsg = commit.getFullMessage();
        this.LOC_Add = 0;
        this.LOC_Delete = 0;
    }

    public void setLOC_Add(int LOC_Add) {
        this.LOC_Add = LOC_Add;
    }

    public void setLOC_Delete(int LOC_Delete) {
        this.LOC_Delete = LOC_Delete;
    }

    public Date getCommitTime() {
        return CommitTime;
    }

    public String getCommitMsg() {
        return CommitMsg;
    }

    public void displayMessages(){
        System.out.println("commit " + commit.getName());
        System.out.printf("Author: %s <%s>\n", commit.getAuthorIdent().getName(), commit.getAuthorIdent().getEmailAddress());
        System.out.println("Date: " + commit.getAuthorIdent().getWhen());
        System.out.println("\t" + commit.getFullMessage());
        System.out.println("\033[32;1m" + "++" + LOC_Add + "\033[0m");
        System.out.println("\033[31;1m" + "--" + LOC_Delete + "\033[0m");
        System.out.println();
    }


}
