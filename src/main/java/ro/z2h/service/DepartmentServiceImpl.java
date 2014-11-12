package ro.z2h.service;

import ro.z2h.dao.DepartmentDao;
import ro.z2h.domain.Department;
import ro.z2h.utils.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dumitru on 12.11.2014.
 */
public class DepartmentServiceImpl implements DepartmentService {

    private final static String username = "ZTH_07";
    private final static String password = "passw0rd";

    @Override
    public List<Department> findAllDepartments() {

        DepartmentDao departmentDao = new DepartmentDao();
        ArrayList<Department> arrayList = new ArrayList<Department>();
        Connection con = DatabaseManager.getConnection(username, password);
        try {
            arrayList = departmentDao.getAllDepartments(con);
            return arrayList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return new ArrayList<Department>();
    }
}
