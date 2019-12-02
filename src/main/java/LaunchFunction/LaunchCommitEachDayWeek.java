package LaunchFunction;

import CommitKeyWord.CommitPersonDay;
import Repository.GitRepository;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class LaunchCommitEachDayWeek {
    private static final Logger LOG = Logger.getLogger(LaunchCommitLife.class);
    static {
        BasicConfigurator.configure();
    }
    public static void Launch(GitRepository repo, String csvpath) throws Exception{
        LOG.debug("Ready to Commit day and week");
        CommitPersonDay commitPerson= new CommitPersonDay(repo.getGit(), 0);
        commitPerson.SaveByDay(csvpath + "commitday.csv");
        commitPerson.SaveByWeek(csvpath + "commitweek.csv");
        LOG.debug("Ready to save");
    }
}
