package Repository;

public class GitRepositoryFactory {

    //从远程服务器Git Clone，位置放在默认的缓存
    public static GitRepository cloneRepositoryFrom(String remoteURL){
        return new GitRepository(remoteURL,0);
    }

    public static GitRepository cloneRepositoryFromTo(String remoteURL, String localPath){
        return new GitRepository(remoteURL,localPath);
    }

    public static GitRepository openLocalRepositoryFrom(String localPath){
        return new GitRepository(localPath,1);
    }

}
