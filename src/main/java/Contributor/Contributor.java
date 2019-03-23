package Contributor;

import org.eclipse.jgit.revwalk.RevCommit;

import java.util.ArrayList;
import java.util.List;

public class Contributor {
    private String AuthorName;
    private int LOC_Add;
    private int LOC_Delete;
    private List<RevCommit> commitList = new ArrayList<>();

    public Contributor(String authorName) {
        AuthorName = authorName;
        LOC_Add = 0;
        LOC_Delete = 0;
        commitList = new ArrayList<>();
    }
}
