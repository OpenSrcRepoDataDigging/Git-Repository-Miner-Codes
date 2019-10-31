package LaunchFunction;

import Repository.GitRepository;
import Repository.GitRepositoryFactory;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.Git;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/*
    定义一个数据库：RepoStatus
    四个属性： RepoName(str), RepoUrl(str), RepoLocalPath(str 绝对路径), CloneTime(timestamp)

    所有克隆的Repo集中管理，目录结构如下：
    ~/.gitminer/{number, 0/repo,csv, 1/repo,csv, 2/repo,csv, 3/repo,csv}
    使用一个文件(number)记录当前计数，每个数字是一个目录，该目录下存储着Repo。

    考虑过该数据库是否可以被前端记录仓库列表的数据库合并。建议不合并。
 */

public class MainDataGenerator {

    private static final Logger LOG = Logger.getLogger(MainDataGenerator.class);
    static {
        BasicConfigurator.configure();
    }

    private Connection connection = null;

    public static void main(String[] args){
        try{
            new MainDataGenerator().init();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void init() throws ClassNotFoundException, SQLException, IOException{

        // TODO: 初始化程序目录"~/.gitminer/"
        String pathname = "/home/"+System.getProperty("user.name")+"/.gitminer/";
        File main_dir = new File(pathname);
        if (!main_dir.exists()){
            LOG.debug(".gitminer not exists, create first.");
            if(!main_dir.mkdir()){
                LOG.error(".gitminer dir create failed.");
            }
        }
        File number_file = new File(pathname + "number");
        if (!number_file.exists()){
            LOG.debug("number file not exists, create first, and init its value as 0.");
            if (!number_file.createNewFile()){
                LOG.error("number file create failed.");
            }
            FileOutputStream fos = null;
            OutputStreamWriter osw = null;
            try{
                fos = new FileOutputStream(number_file);
                osw = new OutputStreamWriter(fos);
                osw.write("0");
            } catch (IOException e){
                e.printStackTrace();
            } finally {
                if (null != osw){
                    try{
                        osw.close();
                    }  catch (IOException e){
                        e.printStackTrace();
                    }
                }
                if (null != fos){
                    try{
                        fos.close();
                    } catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }


        // TODO: 初始化数据库连接(sqlite)
        // load driver
        Class.forName("org.sqlite.JDBC");
        String localdb = pathname + "repo.db";
        connection = DriverManager.getConnection("jdbc:sqlite:" + localdb);
        LOG.debug("Database repostatus create/connected successfully.");

    }

    private void closeDBConnection(){

        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    private String getLocalPathString(){

        // TODO: 通过查看~/.gitminer/number中的内容，返回当前仓库即将存放路径的"绝对路径"。

        return "";
    }

    private boolean checkExistRepoStatus(String GitRemoteAddress){

        // TODO: 检查当前加入的仓库是否之前加入过，通过jdbc查RepoStatus表实现。

        return false;
    }

    private String parseRepoNameFromUrl(String GitRemoteAddress){

        // TODO: 从url链接中解析出仓库的名称，提取 “.git”前 “/”后的字符串即可。

        return "";
    }

    private void insertToRepoStatus(String RepoName, String RepoUrl, String RepoLocalPath, Timestamp timestamp){

        // TODO: 使用jdbc将参数中的数据加入到数据库中。

    }

    /*
        返回值返回给前端，告知前端后端运行状态：
        0： 该仓库之前未添加过，url解析正常，仓库克隆正常，数据计算正常。
        1： 该仓库之前添加过。不予计算。
        more....
     */
    public int generateNew (String GitRemoteAddress) {

        // 1. 检查当前url对应仓库是否曾经添加过；
        if (checkExistRepoStatus(GitRemoteAddress)){
            // This repo has been added before. Return value 1 to front-end.
            return 1;
        }

        // 2. 从url中解析出Repo的名称、url。并从getLocalPathString()方法中获取本地克隆目录。并获取当前时间戳。
        String localpath = getLocalPathString();
        String reponame = parseRepoNameFromUrl(GitRemoteAddress);
        Timestamp timestamp = new Timestamp(new Date().getTime());
        insertToRepoStatus(reponame, GitRemoteAddress,localpath, timestamp);

        // 3. 克隆仓库
        GitRepository repo = null;
        // TODO: Clone and init "repo"
        try {
            repo = GitRepositoryFactory.cloneRepositoryFromTo(GitRemoteAddress, localpath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            assert repo != null;
            Git git = repo.getGit();
            if (git == null) {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 4. 计算出所有csv文件

        // TODO: generate csv files. Insert Launch codes below.


        return 0;
    }

}
