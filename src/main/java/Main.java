import Contri.SaveLOC2csv;
import LOC.CalculateLOC;
import Repository.GitRepository;
import Repository.GitRepositoryFactory;
import org.eclipse.jgit.api.Git;

import javax.swing.plaf.synth.SynthEditorPaneUI;

public class Main {

    public static void main(String args[]) {
        String GitAddress = "https://github.com/MirageLyu/alluxio.git";
//        String GitAddress = "https://github.com/OpenSrcRepoDataDigging/DailyLog.git";

        try {
            //GitRepository repo = new GitRepositoryFactory().cloneRepositoryFrom(GitAddress); //git clone到临时目录
            //GitRepository repo = new GitRepositoryFactory().cloneRepositoryFromTo(GitAddress,"E:\\GitMiner_Codes\\Test"); //git clone到目标目录
            GitRepository repo = new GitRepositoryFactory().openLocalRepositoryFrom("E:\\GitMiner_Codes\\Test"); //获得本地项目的引用
            Git git = repo.getGit();
            if (git == null){
                System.out.println("Git is null");
                return;
            }
            System.out.println("Ready to calculate");
            CalculateLOC calculateLOC = new CalculateLOC();
            calculateLOC.WorkOutLOC(git);

            System.out.println("Ready to save");
            //SaveLOC2csv.Save2csv(calculateLOC.getContributorMap(), "D:/");

            System.out.println("Ready to cancel");
            //TODO: 测试时为了速度，使用本地项目，但是记得如果用临时目录，要删除
            //repo.deleteRepository();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
