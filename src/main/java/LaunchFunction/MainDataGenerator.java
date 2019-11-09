package LaunchFunction;

import Repository.GitRepository;
import Repository.GitRepositoryFactory;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.Git;

import java.io.*;
import java.sql.*;
import java.util.Date;

/*

    定义一个数据库表：RepoStatus
    四个属性： RepoName(str), RepoUrl(str), RepoLocalPath(str 相对路径), CloneTime(timestamp)

    所有克隆的Repo集中管理，目录结构如下：
    ~/.gitminer/{number, 0/repo,csv, 1/repo,csv, 2/repo,csv, 3/repo,csv}
    使用一个文件(number)记录当前计数，每个数字是一个目录，该目录下存储着Repo。

    考虑过该数据库是否可以被前端记录仓库列表的数据库合并。建议不合并。

 */

public class MainDataGenerator {

    /*public static void main(String[] args){

        MainDataGenerator md = new MainDataGenerator();
        try{
            md.init();
        } catch (Exception e){
            e.printStackTrace();
        }

        String result = md.nextRepoLocalPathString();
        System.out.println(result);

        try{
            md.checkExistRepoStatus("https://github.com/MirageLyu/Compiler.git");
        } catch (SQLException e){
            e.printStackTrace();
        }

        String GitRemoteAddress = "https://github.com/MirageLyu/Compiler.git";
        String localpath = md.nextRepoLocalPathString();
        String reponame = md.parseRepoNameFromUrl(GitRemoteAddress);
        Timestamp timestamp = new Timestamp(new Date().getTime());
        try {
            md.insertToRepoStatus(reponame, GitRemoteAddress, localpath, timestamp);
        } catch (SQLException e){
            e.printStackTrace();
        }

        try{
            md.checkExistRepoStatus("https://github.com/MirageLyu/Compiler.git");
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new MainDataGenerator().generateNew("https://github.com/MirageLyu/test.git");
    }
    */
    public static void main(String[] args) {
        new MainDataGenerator().generateNew("https://github.com/MirageLyu/test.git");
    }

    // 初始化log4j
    private static final Logger LOG = Logger.getLogger(MainDataGenerator.class);
    static {
        BasicConfigurator.configure();
    }

    // 与repo.db数据库的连接
    private Connection connection = null;

    // init() 方法初始化了.gitminer目录，并初始化了与数据库的连接。
    private void init() throws ClassNotFoundException, SQLException, IOException{

        // 初始化程序目录"~/.gitminer/"
        String pathname = "/home/"+System.getProperty("user.name")+"/.gitminer/";
        //String pathname = "/" + System.getProperty("user.name")+"/.gitminer/";
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

        // 初始化数据库连接(sqlite)
        // load driver
        Class.forName("org.sqlite.JDBC");
        String localdb = pathname + "repo.db";
        if (!new File(localdb).exists()){
            // 新建数据库文件时建表
            connection = DriverManager.getConnection("jdbc:sqlite:" + localdb);
            String sql = "CREATE TABLE REPOSTATUS\n" +
                    "(REPONAME             VARCHAR(255)  NOT NULL,\n" +
                    " REPOURL VARCHAR(255) PRIMARY KEY  NOT NULL,\n" +
                    " REPOLOCALPATH        VARCHAR(255)  NOT NULL,\n" +
                    " CLONETIME              TIMESTAMP  NOT NULL)";
            try {
                PreparedStatement ptmt = connection.prepareStatement(sql);
                ptmt.execute();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        else {
            connection = DriverManager.getConnection("jdbc:sqlite:" + localdb);
        }
        LOG.debug("Database repostatus create/connected successfully.");

    }

    // 断开connection的连接
    private void closeDBConnection(){

        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    // 根据number文件存储的值，初始化下一仓库存储的路径，并返回该目录对应的字符串。
    private String nextRepoLocalPathString(){

        /* 初始化一个新的仓库目录，通过查看~/.gitminer/number中的内容，返回当前仓库即将存放路径的"绝对路径"。
            初始化：根据number中的内容建立一个新目录，并建立repo和csv两个子目录。
            返回值为"/home/username/.gitminer/reponumber/"
         */

        // 读number文件
        String rootpathname = "/home/" + System.getProperty("user.name") + "/.gitminer/";
        File numberfile = new File(rootpathname + "number");
        InputStream is = null;
        Reader isr = null;
        int number = 0;
        try{
            is = new FileInputStream(numberfile);
            isr = new InputStreamReader(is);
            number = isr.read() - '0';
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try{
                if (null != is){
                    is.close();
                }
                if (null != isr){
                    isr.close();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        // 初始化新的仓库目录，建立repo/和csv/两个子目录。
        String result_str = rootpathname + number + "/";
        File result_path = new File(result_str);
        if (!result_path.mkdir()){
            LOG.error("Repo dir: " + number + " creates failed.");
        }
        String result_repo_str = result_str + "repo/";
        String result_csv_str = result_str + "csv/";
        File result_repo_path = new File(result_repo_str), result_csv_path = new File(result_csv_str);
        if (!result_csv_path.mkdir() || !result_repo_path.mkdir()){
            LOG.error("Repo dir: " + number + " sub dirs create failed.");
        }

        // number变量自增1，并写回到number文件中。
        number++;
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try{
            fos = new FileOutputStream(numberfile);
            osw = new OutputStreamWriter(fos);
            osw.write(number + '0');
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

        // 返回值为"/home/username/.gitminer/reponumber/"
        return result_str;
    }

    // 检查当前加入的仓库是否之前加入过，通过jdbc查RepoStatus表实现。
    private boolean checkExistRepoStatus(String GitRemoteAddress) throws SQLException{

        // 检查当前加入的仓库是否之前加入过，通过jdbc查RepoStatus表实现。
        String sql = "SELECT * FROM REPOSTATUS WHERE REPOURL=?";
        PreparedStatement ptmt = connection.prepareStatement(sql);
        ptmt.setString(1, GitRemoteAddress);
        ResultSet rs = ptmt.executeQuery();

        boolean flag = rs.next();

        if (flag){
            LOG.debug(GitRemoteAddress + " has been added before.");
        }
        else{
            LOG.debug(GitRemoteAddress + " hasn't been added before.");
        }
        return flag;

    }

    // 从url链接中解析出仓库的名称
    private String parseRepoNameFromUrl(String GitRemoteAddress){

        // 从url链接中解析出仓库的名称，提取 “.git”前 “/”后的字符串即可。
        String[] strs1 = GitRemoteAddress.split("\\.git");
        String[] strs2 = strs1[strs1.length-1].split("/");
        return strs2[strs2.length-1];

    }

    // 使用jdbc将参数中的数据加入到数据库中。
    private void insertToRepoStatus(String RepoName, String RepoUrl, String RepoLocalPath, Timestamp timestamp) throws SQLException{

        // 使用jdbc将参数中的数据加入到数据库中。
        String sql = "INSERT INTO REPOSTATUS(REPONAME, REPOURL, REPOLOCALPATH, CLONETIME)" +
                " VALUES(?,?,?,?)";
        PreparedStatement ptmt = connection.prepareStatement(sql);
        ptmt.setString(1, RepoName);
        ptmt.setString(2, RepoUrl);
        ptmt.setString(3, RepoLocalPath);
        ptmt.setTimestamp(4, timestamp);

        ptmt.execute();

    }

    /*
        返回值返回给前端，告知前端后端运行状态：
        0： 该仓库之前未添加过，url解析正常，仓库克隆正常，数据计算正常。
        1： 该仓库之前添加过。不予计算。
        more....
     */
    public int generateNew (String GitRemoteAddress) {

        // 0. 初始化
        try{
            init();
        } catch (Exception e){
            e.printStackTrace();
        }

        // 1. 检查当前url对应仓库是否曾经添加过；
        try {
            if (checkExistRepoStatus(GitRemoteAddress)) {
                // This repo has been added before. Return value 1 to front-end.
                return 1;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        // 2. 从url中解析出Repo的名称、url。并从getLocalPathString()方法中获取本地克隆目录。并获取当前时间戳。
        String localpath = nextRepoLocalPathString();
        String reponame = parseRepoNameFromUrl(GitRemoteAddress);
        Timestamp timestamp = new Timestamp(new Date().getTime());
        try {
            insertToRepoStatus(reponame, GitRemoteAddress, localpath, timestamp);
        } catch (SQLException e){
            e.printStackTrace();
        }

        // 3. 克隆仓库
        GitRepository repo = null;
        // Clone and init "repo"
        try {
            // 将仓库克隆到其目录的repo子目录下。
            repo = GitRepositoryFactory.cloneRepositoryFromTo(GitRemoteAddress, localpath + "repo/");
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

        // 4. 计算出所有csv文件 generate csv files. Insert Launch codes below.
        // TODO: 底层代码有问题，ContributorMap未正确初始化
        String csvpath = localpath + "csv/";
        try {
            LaunchCommitKeyWord.Launch(repo, csvpath);
            LaunchCommitLife.Launch(repo, csvpath);
            LaunchLOC.Launch(repo, csvpath);
        } catch (Exception e){
            e.printStackTrace();
        }

        // TODO: 5.将计算出的csv文件再读入，转换后写入数据库
        // 传入： csv文件路径，以及数据库的连接connection

        // 6. 断开数据库连接
        closeDBConnection();
        return 0;
    }

}
