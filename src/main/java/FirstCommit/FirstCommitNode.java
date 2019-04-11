package FirstCommit;

import java.util.Date;

public class FirstCommitNode {
    String author;
    Date date;

    public FirstCommitNode(String author, Date date) {
        this.author = author;
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public Date getDate() {
        return date;
    }
}
