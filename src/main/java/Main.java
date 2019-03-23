import LOC.CalculateLOC;
import Repository.GitRepository;
import Repository.GitRepositoryFactory;
import org.eclipse.jgit.api.Git;

public class Main {

    public static void main(String args[]) {
        String GitAddress = "https://github.com/vegetable-father-son/Inverview-Preparation.git";

        try {
            GitRepository repo = new GitRepositoryFactory().cloneRepositoryFrom(GitAddress); //git clone到临时目录
//            GitRepository repo = new GitRepositoryFactory().cloneRepositoryFromTo(GitAddress,"E:\\大三下\\创新项目\\GitMiner_Codes\\Test"); //git clone到目标目录
//            GitRepository repo = new GitRepositoryFactory().openLocalRepositoryFrom("F:\\GitMiner_Codes"); //获得本地项目的引用
            Git git = repo.getGit();
            if (git == null){
                System.out.println("Git is null");
                return;
            }
            System.out.println("Ready to calculate");
            CalculateLOC calculateLOC = new CalculateLOC();
            calculateLOC.WorkOutLOC(git);

            System.out.println("Ready to cancel");
            repo.deleteRepository();
        }catch (Exception e){
            e.printStackTrace();
        }

        /*String gitFilePath = "D:\\课程学习\\大三上\\javasub\\java-2018f-homework";
        File root = new File(gitFilePath);
        Git git = null;
        GitRepository GitRepository;
        try{
            git = Git.open(root);
            GitRepository = git.getRepository();
            LOC.CalculateLOC calculateLOC = new LOC.CalculateLOC();
            calculateLOC.WorkOutLOC(git);
            //return GitRepository;
        }catch (Exception e){
            e.printStackTrace();
        }*/
        //开始计算LOC




        //getRepository.CloseJGitRepository();
        //GitRepository GitRepository = GetResponsibilty.openReposity();
    }
}
