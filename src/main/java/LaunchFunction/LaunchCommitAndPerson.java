package LaunchFunction;

import CommitKeyWord.CommitAndPerson;
import Contri.LOCAndLastestCommit;
import Repository.GitRepository;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class LaunchCommitAndPerson {
    private static final Logger LOG = Logger.getLogger(LaunchLOC.class);
    static {
        BasicConfigurator.configure();
    }

    public static void Launch(GitRepository repo, String csvpath) throws Exception{
        LOG.debug("Ready to calculate");
        CommitAndPerson LOC = new CommitAndPerson(repo.getGit(),0);
        LOG.debug("Ready to save");
        LOC.SaveByDay(csvpath + "CommitKindPerPerson.csv");
    }
}
