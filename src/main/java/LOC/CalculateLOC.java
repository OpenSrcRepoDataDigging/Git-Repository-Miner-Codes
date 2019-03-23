package LOC;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.*;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.patch.HunkHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class CalculateLOC {
    private Git git;
    public CalculateLOC(){

    }

    public void WorkOutLOC(Git g) throws Exception{
        if(g == null)
            return;
        git = g;
        RevWalk walk = new RevWalk(git.getRepository());
        List<RevCommit> commitList = new ArrayList<>();
        //获取最近两次记录
        //Iterable<RevCommit> commits = git.log().setMaxCount(2).call();
        Iterable<RevCommit> commits = git.log().call();
        System.out.println("---------------------------------------------------------");
        for(RevCommit commit:commits){
            commitList.add(commit);
            System.out.println("commit " + commit.getName());
            System.out.printf("Author: %s <%s>\n", commit.getAuthorIdent().getName(), commit.getAuthorIdent().getEmailAddress());
            System.out.println("Date: " + commit.getAuthorIdent().getWhen());
            System.out.println("\t" + commit.getShortMessage());
            System.out.println("---------------------------------------------------------");
        }
//        for(int i = 0; i < commitList.size() - 1; i++)
//        {
//            System.out.println("This is the " + i);
//            ShowLOC(commitList.get(i), commitList.get(i + 1));
//        }
        //if(commitList.size() == 2)
            //ShowLOC(commitList.get(0), commitList.get(1));
    }

    private void ShowLOC(RevCommit revCommit, RevCommit commit) throws Exception{
        AbstractTreeIterator newTree = prepareTreeParser(revCommit);
        AbstractTreeIterator oldTree = prepareTreeParser(commit);
        List<DiffEntry> diff = git.diff().setOldTree(oldTree).setNewTree(newTree)
                .setShowNameAndStatusOnly(true).call();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //设置比较器为忽略空白字符对比（Ignores all whitespace）
        DiffFormatter diffFormatter = new DiffFormatter(out);
        //忽略空白字符的对比
        diffFormatter.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
        diffFormatter.setRepository(git.getRepository());
        System.out.println("------------------------------start-----------------------------");
        //每一个都是一个文件版本之间的变动差异
        for(DiffEntry diffEntry : diff){
            //打印具体内容
            diffFormatter.format(diffEntry);
            String diffText = out.toString("UTF-8");
            System.out.println(diffText);

            //获取差异的位置，从而统计差异行数
            FileHeader fileHeader = diffFormatter.toFileHeader(diffEntry);
            List<HunkHeader> hunkHeaders = (List<HunkHeader>) fileHeader.getHunks();
            int addSize = 0;
            int subSize = 0;
            for(HunkHeader hunkHeader:hunkHeaders){
                EditList editList = hunkHeader.toEditList();
                for(Edit edit : editList){
                    subSize += edit.getEndA() - edit.getBeginA();
                    addSize += edit.getEndB() - edit.getBeginB();
                }
            }
            System.out.println("addSize = " + addSize);
            System.out.println("subSize = " + subSize);
            System.out.print("----------------end-----------------");
            out.reset();
        }
    }

    private AbstractTreeIterator prepareTreeParser(RevCommit revCommit) {
        System.out.println("----------------prepare Tree Parse ----------------");
        System.out.println(revCommit.getId());
        try(RevWalk walk = new RevWalk(git.getRepository())){
            System.out.println(revCommit.getTree().getId());
            RevTree tree = walk.parseTree(revCommit.getTree().getId());

            CanonicalTreeParser oldTreeParser = new CanonicalTreeParser();
            try(ObjectReader oldReader = git.getRepository().newObjectReader()){
                oldTreeParser.reset(oldReader, tree.getId());
            }
            walk.dispose();
            return oldTreeParser;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
