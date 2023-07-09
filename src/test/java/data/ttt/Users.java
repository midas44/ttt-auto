package data.ttt;

import conf.Config;
import utils.PostgreSql;

import java.util.ArrayList;
import java.util.List;

public class Users {
    Config config;
    PostgreSql db;
    public List<String> allActiveUsers;
    public List<String> testAccounts;
    public List<String> testAccountsWithRoles = new ArrayList<>();
    public List<User> testUsers = new ArrayList<>();
    public List<User> nonManagers = new ArrayList<>();
    public List<User> employeesManagers = new ArrayList<>();
    public List<User> csManagers = new ArrayList<>();
    public List<User> techLeads = new ArrayList<>();

    public Users(Config config) {
        this.config = config;
        db = new PostgreSql(config);
        getData();
    }

    public void getData(){
        clearData();
        getAllActiveUsers();
        loadTestAccounts();
        getUsers();
    }

    private void clearData(){
        if (allActiveUsers != null) allActiveUsers.clear();
        if (testAccounts != null) testAccounts.clear();
        if (testAccountsWithRoles != null) testAccountsWithRoles.clear();
        if (testUsers != null) testUsers.clear();
        if (nonManagers != null) nonManagers.clear();
        if (employeesManagers != null) employeesManagers.clear();
        if (csManagers != null) csManagers.clear();
        if (techLeads != null) techLeads.clear();
    }

    private void getAllActiveUsers(){
        allActiveUsers =  db.getDbColumn_String("SELECT \"login\" FROM ttt_backend.employee WHERE enabled IS TRUE ORDER BY \"login\"");
    }

    private void loadTestAccounts(){
        testAccounts = config.fileMan.loadStringListFromTextFile(config.dataDir,"accounts");
    }

    private void getUsers(){
        for (final String login : testAccounts) {
            User user = new User(login, db);
            testUsers.add(user);
        }

        for (final User user : testUsers) {

            if(user.isEmployeesManager){
                employeesManagers.add(user);
            }

            if(user.isCsManager){
                csManagers.add(user);
            }

            if(!(user.isEmployeesManager | user.isCsManager | user.roleProjectManager | user.roleDepartmentManager |
                    user.roleViewAll | user.roleAdmin | user.roleOfficeDirector |user.roleChiefAccountant) & user.roleEmployee){
                nonManagers.add(user);
            }

            if(user.roleTechLead){
                //only techleads of test users
                for (final User user1 : testUsers) {
                    if (user1.techLead == user.id) {
                        techLeads.add(user);
                        break;
                    }
                }
            }

            testAccountsWithRoles.add(user.login + ": " + user.userRoles);
        }
    }

    public User getUserbyId(int id){
        for (final User user : testUsers) {
            if(user.id == id){
                return user;
            }
        }
        return null;
    }

    public static class User {
        PostgreSql db;

        //ttt_backend.employee
        public String login;
        public String name;
        public String russianFirstName;
        public String russianLastName;
        public String latinFirstName;
        public String latinLastName;
        public int id;
        public int companyStaffId;
        public int seniorManager;
        public boolean isCsManager;
        public boolean isEmployeesManager;
        public int techLead;

        //ttt_backend.employee_global_roles
        public boolean roleViewAll;
        public boolean roleTechLead;
        public boolean roleChiefAccountant;
        public boolean roleDepartmentManager;
        public boolean roleAccountant;
        public boolean roleAdmin;
        public boolean roleEmployee;
        public boolean roleContractor;
        public boolean roleProjectManager;
        public boolean roleOfficeDirector;

        //info
        public String userRoles;

        public User(String login, PostgreSql db) {
            this.login = login;
            this.db = db;
            getEmployeeFields();
            getRolesFields();
        }

        private void getEmployeeFields() {
            id = db.getDbField_int(getSqlQueryForEmployeeField("id"));
            companyStaffId = db.getDbField_int(getSqlQueryForEmployeeField("company_staff_id"));
            name = db.getDbField_String(getSqlQueryForEmployeeField("name"));
            russianFirstName = db.getDbField_String(getSqlQueryForEmployeeField("russian_first_name"));
            russianLastName = db.getDbField_String(getSqlQueryForEmployeeField("russian_last_name"));
            latinFirstName = db.getDbField_String(getSqlQueryForEmployeeField("latin_first_name"));
            latinLastName = db.getDbField_String(getSqlQueryForEmployeeField("latin_last_name"));
            seniorManager = db.getDbField_int(getSqlQueryForEmployeeField("senior_manager"));
            techLead = db.getDbField_int(getSqlQueryForEmployeeField("tech_lead"));
            isEmployeesManager = db.getDbField_boolean(getSqlQueryForEmployeeField("is_employees_manager"));
            isCsManager = false; //temp!!!
            //isCsManager = db.getDbField_boolean(getSqlQueryForEmployeeField("is_cs_manager")); //TODO enable when field is ready on stage
        }

        private String getSqlQueryForEmployeeField(String field) {
            return "SELECT \"" + field + "\" FROM ttt_backend.employee WHERE login = " + "'" + login + "'";
        }

        private void getRolesFields(){
            roleViewAll = db.getDbField_boolean(getSqlQueryForRolesValueCheck("ROLE_VIEW_ALL"));
            roleTechLead = db.getDbField_boolean(getSqlQueryForRolesValueCheck("ROLE_TECH_LEAD"));
            roleChiefAccountant = db.getDbField_boolean(getSqlQueryForRolesValueCheck("ROLE_CHIEF_ACCOUNTANT"));
            roleDepartmentManager = db.getDbField_boolean(getSqlQueryForRolesValueCheck("ROLE_DEPARTMENT_MANAGER"));
            roleAccountant = db.getDbField_boolean(getSqlQueryForRolesValueCheck("ROLE_ACCOUNTANT"));
            roleAdmin = db.getDbField_boolean(getSqlQueryForRolesValueCheck("ROLE_ADMIN"));
            roleEmployee=  db.getDbField_boolean(getSqlQueryForRolesValueCheck("ROLE_EMPLOYEE"));
            roleContractor = db.getDbField_boolean(getSqlQueryForRolesValueCheck("ROLE_CONTRACTOR"));
            roleProjectManager = db.getDbField_boolean(getSqlQueryForRolesValueCheck("ROLE_PROJECT_MANAGER"));
            roleOfficeDirector = db.getDbField_boolean(getSqlQueryForRolesValueCheck("ROLE_OFFICE_DIRECTOR"));

            userRoles = "";
            if (roleViewAll){userRoles += " ROLE_VIEW_ALL";}
            if (roleTechLead){userRoles += " ROLE_TECH_LEAD";}
            if (roleChiefAccountant){userRoles += " ROLE_CHIEF_ACCOUNTANT";}
            if (roleDepartmentManager){userRoles +=" ROLE_DEPARTMENT_MANAGER";}
            if (roleAccountant){userRoles += " ROLE_ACCOUNTANT";}
            if (roleAdmin){userRoles += " ROLE_ADMIN";}
            if (roleEmployee){userRoles += " ROLE_EMPLOYEE";}
            if (roleContractor){userRoles += " ROLE_CONTRACTOR";}
            if (roleProjectManager){userRoles += " ROLE_PROJECT_MANAGER";}
            if (roleOfficeDirector){userRoles += " ROLE_OFFICE_DIRECTOR";}
        }

        private String getSqlQueryForRolesValueCheck(String value) {
            return "SELECT CAST(CASE WHEN EXISTS (SELECT * FROM ttt_backend.employee_global_roles WHERE role_name='"+ value +
                    "' AND employee=" + id + ") THEN TRUE ELSE FALSE END AS bool)";
        }
    }
}

