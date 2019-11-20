package LaunchFunction.DataConvert.model;

import exception.exceptions.TupleValueTypeNotMatchAttributeException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBTable {

    private static final Logger LOG = Logger.getLogger(DBTable.class);
    static {
        BasicConfigurator.configure();
    }

    public DBTable(String dbTableName, List<DBTuple> dbTuples, List<DBAttribute> dbAttributes){
        this.dbTuples = dbTuples;
        this.dbTableName = dbTableName;
        this.dbAttributes = dbAttributes;
    }

    private String dbTableName;

    private List<DBTuple> dbTuples = new ArrayList<DBTuple>();

    private List<DBAttribute> dbAttributes = new ArrayList<DBAttribute>();



    public void setDbAttributes(List<DBAttribute> dbAttributes) {
        this.dbAttributes = dbAttributes;
    }

    public void setDbTuples(List<DBTuple> dbTuples) {
        this.dbTuples = dbTuples;
    }

    public List<DBAttribute> getDbAttributes() {
        return dbAttributes;
    }

    public List<DBTuple> getDbTuples() {
        return dbTuples;
    }

    public void setDbTableName(String dbTableName) {
        this.dbTableName = dbTableName;
    }

    public String getDbTableName() {
        return dbTableName;
    }

    // To check whether Tuple values' type matches Attributes' type.
    public boolean checkType(DBTuple dbTuple) throws TupleValueTypeNotMatchAttributeException {

        return true;
    }

    private void createTable(Connection conn) throws Exception{

        StringBuilder sql = new StringBuilder("CREATE TABLE '" + dbTableName + "'\n(");
        for (int i = 0; i<dbAttributes.size(); i++){
            DBAttribute dba = dbAttributes.get(i);
            sql.append("'").append(dba.getAttr_name()).append("'").append(" ").append(dba.getSQLTypeString()).append(" ").append(dba.getSQLNotNullString());
            if (i == dbAttributes.size()-1){
                sql.append(")");
            }
            else{
                sql.append(",\n");
            }
        }

        LOG.debug("Generate sql string: \n" + sql);

        try {
            PreparedStatement ptmt = conn.prepareStatement(sql.toString());
            ptmt.execute();
            ptmt.close();
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    private void insertTuples (Connection conn) throws Exception{
        StringBuilder sql = new StringBuilder("INSERT INTO " + dbTableName + "\n(");
        for (int i = 0; i<dbAttributes.size(); i++){
            DBAttribute dba = dbAttributes.get(i);
            sql.append("'").append(dba.getAttr_name()).append("'");
            if (i == dbAttributes.size()-1){
                sql.append(") ");
            }
            else{
                sql.append(", ");
            }
        }
        sql.append("VALUES(");
        for (int i = 0; i<dbAttributes.size(); i++){
            sql.append("?");
            if (i == dbAttributes.size()-1){
                sql.append(")");
            }
            else{
                sql.append(",");
            }
        }

        LOG.debug("Insert Statement: " + sql.toString());

        PreparedStatement ptmt = conn.prepareStatement(sql.toString());
        for (DBTuple dbt : dbTuples){
            dbt.prepareSQLStatement(ptmt);
            ptmt.executeUpdate();
        }

        ptmt.close();
    }

    private void deleteTable(Connection conn) throws Exception {
        String sql = "DELETE FROM " + dbTableName;
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.execute();
        ptmt.close();
    }

    public void insertToSQL(Connection conn, boolean Update){

        try {
            if (Update){
                deleteTable(conn);
                insertTuples(conn);
            }
            else {
                createTable(conn);
                insertTuples(conn);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
