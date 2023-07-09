package data;

import conf.Config;
import data.ttt.Projects;
import data.ttt.Users;

public class BaseData {

    public Users users;
    public Projects projects;
    //public Trackers trackers;

    public BaseData(Config config){
        users = new Users(config);
        projects = new Projects(config);
        //trackers = new Trackers(config);
    }


}
