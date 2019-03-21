package Repository;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

public class GetRepository {
    private File root;
    public Git openJGitRepository(String name) throws IOException, GitAPIException{
        //String gitFilePath = "D:\\课程学习\\大三上\\javasub\\java-2018f-homework";
        root = CloneRemoteRepository.CloneToLocal(name);
        Git git;
        Repository repository = null;
        try{
            git = Git.open(root);
            repository = git.getRepository();
            return git;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public void CloseJGitRepository() throws IOException, GitAPIException {
        CloneRemoteRepository.DeleteLocalRepository(root);
    }

    public static Repository openJGitCookbookRepository() throws IOException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .build();
        return repository;
    }

    public static Repository createNewRepository() throws IOException {
        // prepare a new folder
        File localPath = File.createTempFile("TestGitRepository", "");
        localPath.delete();

        // create the directory
        Repository repository = FileRepositoryBuilder.create(new File(localPath, ".git"));
        repository.create();

        return repository;
    }
}