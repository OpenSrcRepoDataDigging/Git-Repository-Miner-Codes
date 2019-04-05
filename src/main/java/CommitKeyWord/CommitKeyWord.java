package CommitKeyWord;

import Contri.Contributor;
import com.csvreader.CsvWriter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.nio.charset.Charset;
import java.util.Collection;

public class CommitKeyWord {
    private Git git;
    public CommitKeyWord(Git input_git){
        git = input_git;
    }


    public void CalculateKeyWord(String filePath) throws Exception{
        Repository repository = git.getRepository();

        //写入文件准备
        CsvWriter csvWriter = new CsvWriter(filePath, ',', Charset.forName("UTF-8"));
        // 写表头
        String[] headers = {"AuthorName","KeyWord"};
        csvWriter.writeRecord(headers);

        // get a list of all known heads, tags, remotes, ...
        Collection<Ref> allRefs = repository.getAllRefs().values();

        // a RevWalk allows to walk over commits based on some filtering that is defined
        try (RevWalk revWalk = new RevWalk( repository )) {
            for( Ref ref : allRefs ) {
                revWalk.markStart( revWalk.parseCommit( ref.getObjectId() ));
            }
            System.out.println("Walking all commits starting with " + allRefs.size() + " refs: " + allRefs);
            int count = 0;
            for( RevCommit commit : revWalk ) {
                PersonIdent contributor = commit.getAuthorIdent();
                String Message = commit.getShortMessage();
                System.out.println("Commit: " + commit.getFullMessage());

                //begin to write
                csvWriter.write(contributor.getName());
                ParseKeyWord(csvWriter, Message);
                csvWriter.write(Message);
                csvWriter.endRecord();
                count++;
            }
            csvWriter.close();
            System.out.println("Had look in" + count + " commits");
        }
    }

    public void ParseKeyWord(CsvWriter csvWriter, String message)throws Exception{
        String[] sentence = message.split("\\s+");
        //分成四类
        int add = 0;//增加属性
        int del = 0;//去掉depreciation
        int mod = 0;//代码重构
        int fix = 0;//Fix Bugs
        for(String word : sentence)
        {
            if(word.contains("add") || word.contains("Add") || word.contains("Creat")){
                add++;
            }
            else if(word.contains("Delet") || word.contains("delet")){
                del++;
            }
            else if(word.contains("Remove") || word.contains("Updat") || word.contains("Refactor")){
                mod++;
            }
            else if(word.contains("Fix") || word.contains("FIX") || word.contains("Chang"))
                fix++;
        }
        csvWriter.write("Add" + Integer.toString(add));
        csvWriter.write("Del" + Integer.toString(del));
        csvWriter.write("Mod" + Integer.toString(mod));
        csvWriter.write("Fix" + Integer.toString(fix));
    }
}
