package Contri;

/*
*@author MirageLyu
*
 */

import org.eclipse.jgit.revwalk.RevCommit;

public interface FileContributor {
    public void insertCommitToFileMap(RevCommit commit);
}
