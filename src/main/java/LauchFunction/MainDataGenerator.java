package LauchFunction;

import Repository.GitRepository;
import Repository.GitRepositoryFactory;
import org.eclipse.jgit.api.Git;

public class MainDataGenerator {

    private String getLocalPathString(){
        return "Local String";
    }

    public void generate (String GitAddress, Boolean isRemoteURL){

        GitRepository repo = null;

        if (isRemoteURL){

            // TODO: Clone and init "repo"

            try{
                repo = new GitRepositoryFactory().cloneRepositoryFromTo(GitAddress, getLocalPathString());
            } catch(Exception e){
                e.printStackTrace();
            }

        }
        else{

            // TODO: get reference of local repo to init "repo"

            repo = new GitRepositoryFactory().openLocalRepositoryFrom(GitAddress);

        }

        try {
            assert repo != null;
            Git git = repo.getGit();
            if (git == null){
                throw new NullPointerException();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        // TODO: generate csv files. Insert Launch codes below.


    }

}
