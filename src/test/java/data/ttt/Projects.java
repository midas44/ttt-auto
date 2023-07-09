package data.ttt;

import conf.Config;
import utils.PostgreSql;

public class Projects {

    Config config;
    PostgreSql db;

    public Projects(Config config) {
        this.config = config;
        db = new PostgreSql(config);
        getData();
    }


    public void getData(){

    }
}


