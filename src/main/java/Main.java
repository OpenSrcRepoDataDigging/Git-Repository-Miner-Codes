import Contri.SaveLOC2csv;
import LOC.CalculateLOC;
import CommitKeyWord.CommitKeyWord;
import LauchFunction.*;
import LauchFunction.LauchCommitKeyWord;
import Repository.GitRepository;
import Repository.GitRepositoryFactory;
import org.eclipse.jgit.api.Git;

import javax.swing.plaf.synth.SynthEditorPaneUI;

public class Main {

    public static void main(String args[]) {
        String GitAddress = "https://github.com/Microsoft/microsoft.github.io.git";
//        String GitAddress = "https://github.com/OpenSrcRepoDataDigging/DailyLog.git";


        try {
//            GitRepository repo = new GitRepositoryFactory().cloneRepositoryFrom(GitAddress); //git clone到临时目录
//            GitRepository repo = new GitRepositoryFactory().cloneRepositoryFromTo(GitAddress,"E:\\GitMiner_Codes\\Test"); //git clone到目标目录
            GitRepository repo = new GitRepositoryFactory().openLocalRepositoryFrom("E:\\GitMiner_Codes\\Test"); //获得本地项目的引用
            Git git = repo.getGit();
            if (git == null){
                System.out.println("Git is null");
                return;
            }
            /*@Depreciated
            建议使用LauchFuntion中的包启动
             */
            System.out.println("Ready to calculate");
            CalculateLOC calculateLOC = new CalculateLOC(repo);
            calculateLOC.WorkOutLOC();

            calculateLOC.getContributorMap().compressMatrix(20,50).getMatrixSaveDelegate().save();

            //System.out.println("Ready to save");
            //SaveLOC2csv.Save2csv(calculateLOC.getContributorMap(), "D:/");

            //System.out.println("Ready to cancel");

            System.out.println("Ready to cancel");

            //LauchCommitKeyWord.Lauch(git);

            //TODO: 测试时为了速度，使用本地项目，但是记得如果用临时目录，要删除
            //repo.deleteRepository();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
