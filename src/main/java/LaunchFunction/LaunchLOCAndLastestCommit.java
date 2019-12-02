package LaunchFunction;

import Contri.LOCAndLastestCommit;
import Contri.SaveLOC2csv;
import LOC.CalculateLOC;
import Repository.GitRepository;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class LaunchLOCAndLastestCommit {
    private static final Logger LOG = Logger.getLogger(LaunchLOC.class);
    static {
        BasicConfigurator.configure();
    }

    public static void Launch(GitRepository repo, String csvpath) throws Exception{
        LOG.debug("Ready to calculate");
        LOCAndLastestCommit LOC = new LOCAndLastestCommit(repo);
        LOG.debug("Ready to save");
        LOC.SavePersonLOCAndLastestCommit(csvpath + "LOCandLastestCommit.csv");
    }
}
