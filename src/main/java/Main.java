import LOC.CalculateLOC;
import LOC.GetRepository;
import org.eclipse.jgit.api.Git;

public class Main {

    public static void main(String args[]) {
        String GitAddress = "https://github.com/vegetable-father-son/Inverview-Preparation.git";
        try {
            GetRepository getRepository = new GetRepository();
            Git git = getRepository.openJGitRepository(GitAddress);
            if (git == null)
                return;
            System.out.println("Ready to calculate");
            CalculateLOC calculateLOC = new CalculateLOC();
            calculateLOC.WorkOutLOC(git);

            System.out.println("Ready to cnceal");
            getRepository.CloseJGitRepository();
        }catch (Exception e){
            e.printStackTrace();
        }

        /*String gitFilePath = "D:\\课程学习\\大三上\\javasub\\java-2018f-homework";
        File root = new File(gitFilePath);
        Git git = null;
        Repository repository;
        try{
            git = Git.open(root);
            repository = git.getRepository();
            LOC.CalculateLOC calculateLOC = new LOC.CalculateLOC();
            calculateLOC.WorkOutLOC(git);
            //return repository;
        }catch (Exception e){
            e.printStackTrace();
        }*/
        //开始计算LOC




        //getRepository.CloseJGitRepository();
        //Repository repository = GetResponsibilty.openReposity();
    }
}
