package LaunchFunction;


import Contri.SaveLOC2csv;
import LOC.CalculateLOC;
import Repository.GitRepository;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

//启动计算LOC
public class LaunchLOC {
    private static final Logger LOG = Logger.getLogger(LaunchLOC.class);
    static {
        BasicConfigurator.configure();
    }

    public static void Launch(GitRepository repo, String csvpath) throws Exception{
        LOG.debug("Ready to calculate");
        CalculateLOC calculateLOC = new CalculateLOC(repo);
        calculateLOC.WorkOutLOC();

        LOG.debug("Ready to save");
        SaveLOC2csv.Save2csv(calculateLOC.getContributorMap(), csvpath + "loc.csv");

        calculateLOC.getContributorMap().getMatrixSaveDelegate().save(csvpath + "fcm.csv");
    }
}
