package Contri;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContributorMap implements FileContributorMatrix{
    private HashMap<String,Contributor> maps = new HashMap<String, Contributor>();

    /*  File-Contributor-LOC Matrix */
    private List<FileContributor> fileContributors = new ArrayList<>();

    public ContributorMap(Git git) {
        initMaps(git);
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
}
