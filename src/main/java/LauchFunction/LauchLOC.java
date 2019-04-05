package LauchFunction;


import LOC.CalculateLOC;
import Repository.GitRepository;

//启动计算LOC
public class LauchLOC {
    public LauchLOC(){
    }

    public static void Lauch(GitRepository repo) throws Exception{
        System.out.println("Ready to calculate");
        CalculateLOC calculateLOC = new CalculateLOC(repo);
        calculateLOC.WorkOutLOC();

        System.out.println("Ready to save");
        //SaveLOC2csv.Save2csv(calculateLOC.getContributorMap(), "D:/");
    }
}
