package Repository;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


public class GitRepository {

    private Git git;
    private String localPath;
    private String remoteURL;
    private File localFilePath;

    //type == 0，则从远程服务器Git Clone，位置放在默认的缓存
    //type == 1，则直接打开本地路径上的Git 仓库
    public GitRepository(String URL, int type) {
        switch (type){
            case 0:{
                this.remoteURL = URL;
                try {
                    createTempFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                gitClone();
                break;
            }
            case 1:{
                openRepository(URL);
                break;
            }
        }
    }

    //从远程服务器Git Clone，位置放在指定位置
    public GitRepository(String remoteURL, String localPath) {
        this.localPath = localPath;
        this.localFilePath = new File(localPath);
        this.remoteURL = remoteURL;
        gitClone();
    }

    //从github上clone代码
    public void gitClone(){
        //设置远程服务器上的用户名和密码（若clone的是自己的private的仓库则需要此操作）
        UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider =new
                UsernamePasswordCredentialsProvider("username","password");

        //clone代码库命令
        CloneCommand cloneCommand = Git.cloneRepository();

        //得到clone到本地的git组织
        try {
            System.out.println("Cloning from " + remoteURL + " to " + localPath);
            git= cloneCommand.setURI(remoteURL) //设置远程URI
                    .setBranch("master") //设置clone下来的分支
                    .setDirectory(localFilePath) //设置下载存放路径
                    .setCredentialsProvider(usernamePasswordCredentialsProvider) //设置权限验证
                    .call();
            System.out.println("Having GitRepository: " + git.getRepository().getDirectory());

        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    //创建临时缓存文件存放Git Clone的仓库
    public void createTempFile() throws IOException {
        localFilePath = File.createTempFile("TestGitRepository", "");
        localPath = localFilePath.getPath();
        if(!localFilePath.delete()) {
            throw new IOException("Could not delete temporary file " + localFilePath);
        }
    }

    public void openRepository(String URL){
        localPath = URL;
        localFilePath = new File(localPath);
        try {
            git = Git.open(localFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Git getGit() {
        return git;
    }

    public void deleteRepository(){
        // clean up here to not keep using more and more disk-space for these samples
        try {
            git.close();
//            repo.close();
            FileUtils.deleteDirectory(localFilePath);
            System.out.println("Repository has deleted completely");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, GitAPIException {
        // 设置文件路径
        File repoDir = new File(""+".git");
        // 用FileRepositoryBuilder打开一个仓库
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        // 获得对于一个仓库的引用
        try (Repository repository = builder.setGitDir(repoDir)
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .build()) {
            System.out.println("Having GitRepository: " + repository.getDirectory());

            // the Ref holds an ObjectId for any type of object (tree, commit, blob, tree)
            // 得到Ref后，可以做很多事情
            Ref head = repository.exactRef("refs/heads/master");
            System.out.println("Ref of refs/heads/master: " + head);

            //通过RevWalk获得git log的模拟(commit, Author, Date, Message,etc.)
            try (RevWalk revWalk = new RevWalk(repository)) {
                RevCommit lastCommit = revWalk.parseCommit(head.getObjectId());
                revWalk.markStart(lastCommit);
                revWalk.forEach(c -> {
                    System.out.println("commit " + c.getName());
                    System.out.printf("Author: %s <%s>\n", c.getAuthorIdent().getName(), c.getAuthorIdent().getEmailAddress());
                    System.out.println("Date: " + LocalDateTime.ofEpochSecond(c.getCommitTime(), 0, ZoneOffset.UTC));
                    System.out.println("\t" + c.getShortMessage() + "\n");
                });
            }

        }
    }

}
