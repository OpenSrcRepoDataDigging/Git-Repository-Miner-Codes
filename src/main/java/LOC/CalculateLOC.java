package LOC;

import Contri.ContributorMap;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.*;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.patch.HunkHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CalculateLOC {
    private Git git;
    ContributorMap contributorMap; //记录作者和记录作者贡献信息映射的Map，前期写在这里，后期得换个地方整合
    public CalculateLOC(){

    }
    /**
     * 传入git信息，打印所有的LOC
     * @param g
     * Git 参数
     * @return 
     * @throws Exception
     */
    public void WorkOutLOC(Git g) throws Exception{
        if(g == null)
            return;
        git = g;

        contributorMap = new ContributorMap(git); //记录作者和记录作者贡献信息映射的Map
        List<RevCommit> commitList = new ArrayList<>();
        Iterable<RevCommit> commits = git.log().call();
        for(RevCommit commit:commits){
            commitList.add(commit);
        }
        for(int i = 0; i < commitList.size() - 1; i++)
        {
            System.out.println("\033[31;1m" + "=======================This is the " + i + "th Commit======================="+"\033[0m");
            ShowLOC(commitList.get(i), commitList.get(i + 1));
        }
        if(commitList.size() == 2)
            ShowLOC(commitList.get(0), commitList.get(1));

        contributorMap.getMaps().forEach((author,commitMessage)->{
            commitMessage.dispalyContributorMessages();
        });
        LOC_Charts charts = new LOC_Charts(contributorMap);
        charts.drawChartPanel();
    }


    /**
     * 比较两个commit，计算获得两次commit代码的增减，填入用户对应的map中
     * @param revCommit, commit
     * revCommit 上一次的commit， commit新的commit
     * @return
     * @throws Exception
     */

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
            System.out.println("\033[32;1m" + "++" + addSize + "\033[0m");
            System.out.println("\033[31;1m" + "--" + subSize + "\033[0m");
            contributorMap.getContributor(revCommit.getAuthorIdent().getName()).setLOC_Add(revCommit.getName(),addSize);
            contributorMap.getContributor(revCommit.getAuthorIdent().getName()).setLOC_Delete(revCommit.getName(),subSize);
            System.out.println("----------------end-----------------");
            out.reset();
        }
    }
    /**
     * 解析Commit的信息得到Tree指针
     * @param revCommit
     * revCommit commit指针
     * @return null
     * @throws Exception
     */
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
