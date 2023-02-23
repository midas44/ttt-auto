package conf;

import utils.FileMan;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {

    //global parameters
    public String browserName;
    public String browserPosition;
    public String browserSize;
    public String projectId;
    public Integer globalTimeout;
    public String emailLogin;
    public String emailPassword;

    //project parameters
    public String appName;
    public String appUrl;
    public String stand;

    //stand parameters
    public String dbHost;
    public String dbPort;
    public String initialDatabase;
    public String dbUsername;
    public String dbPassword;

    //runtime vars
    public Integer testNumber ;
    public Integer screenNumber;
    public String appVersion;
    public String baseUrl;

    public String resourceDir;
    public String resultsDir;

    public String dataDir;
    public String browserInfo;

    public FileMan fileMan;

    public Config(){

        //init
        fileMan = new FileMan();
        Properties globalProps = new Properties();
        Properties projectProps = new Properties();
        Properties standProps = new Properties();

        testNumber = 0;
        screenNumber = 0;
        appVersion = "";
        browserInfo = "";

        //load global props from file
        resourceDir = Paths.get("src","test","java", "conf").toFile().getAbsolutePath();
        fileMan.loadProps(globalProps, resourceDir, "global");

        //get global props
        browserName = globalProps.getProperty("browserName");
        browserPosition = globalProps.getProperty("browserPosition");
        browserSize = globalProps.getProperty("browserSize");
        projectId = globalProps.getProperty("projectId");
        globalTimeout = Integer.valueOf(globalProps.getProperty("globalTimeout"));
        emailLogin = globalProps.getProperty("emailLogin");
        emailPassword = globalProps.getProperty("emailPassword");

        //define data dir
        dataDir = Paths.get("src","test","java", "data", projectId).toFile().getAbsolutePath();

        //load project props from file
        String projectDir = Paths.get(resourceDir, projectId).toFile().getAbsolutePath();
        fileMan.loadProps(projectProps, projectDir, projectId);

        //get project props
        appName = projectProps.getProperty("appName");
        stand = projectProps.getProperty("stand");
        appUrl = projectProps.getProperty("appUrl");
        baseUrl = appUrl.replace("***", stand);

        //load stand props from file
        String standDir = Paths.get(projectDir, "stands").toFile().getAbsolutePath();
        fileMan.loadProps(standProps, standDir, stand);

        //get stand props
        dbHost = standProps.getProperty("dbHost");
        dbPort = standProps.getProperty("dbPort");
        initialDatabase = standProps.getProperty("initialDatabase");
        dbUsername = standProps.getProperty("dbUsername");
        dbPassword = standProps.getProperty("dbPassword");

        //define report dir
        resultsDir = Paths.get("allure-results").toFile().getAbsolutePath();
    }

    public void cleanResultsDir(){
        //do before all
        //can be optional
        fileMan.cleanDir(resultsDir);
    }

    public void createReportProps(){
        Properties reportProps = new Properties();
        reportProps.setProperty("App:", appName);
        reportProps.setProperty("Version:", appVersion);
        reportProps.setProperty("Browser:", browserInfo);
        reportProps.setProperty("Stand:", stand);
        reportProps.setProperty("URL:", baseUrl);
        fileMan.saveProps(reportProps, resultsDir,"environment", "These are test environment properties");
    }
}
