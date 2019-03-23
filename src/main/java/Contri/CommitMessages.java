package Contri;

import org.eclipse.jgit.revwalk.RevCommit;


public class CommitMessages {
    private RevCommit commit;
    private int LOC_Add;
    private int LOC_Delete;

    public CommitMessages(RevCommit commit) {
        this.commit = commit;
        LOC_Add = 0;
        LOC_Delete = 0;
    }

    public void setLOC_Add(int LOC_Add) {
        this.LOC_Add = LOC_Add;
    }

    public void setLOC_Delete(int LOC_Delete) {
        this.LOC_Delete = LOC_Delete;
    }

    public void displayMessages(){
        System.out.println("commit " + commit.getName());
        System.out.printf("Author: %s <%s>\n", commit.getAuthorIdent().getName(), commit.getAuthorIdent().getEmailAddress());
        System.out.println("Date: " + commit.getAuthorIdent().getWhen());
        System.out.println("\t" + commit.getShortMessage());
        System.out.println("\033[32;1m" + "++" + LOC_Add + "\033[0m");
        System.out.println("\033[31;1m" + "--" + LOC_Delete + "\033[0m");
        System.out.println();
    }
}
