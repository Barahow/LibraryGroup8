package sample;


import Model.Book;
import Model.Member;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class DbUtil {

    private Connection conn = null;
    private static Statement stmt = null;

    //Modify this string with your local database/schema name and username and password.
    private static String DB_URL;

    //Modify this SELECT statement to fit your table name and columns, if you want to get some specific columns.
    private static final String MY_SELECT_STATEMENT = "SELECT * FROM customer order by id asc ;";

    //Singleton pattern.
    private static DbUtil instance = null;

    public DbUtil() {
        Properties properties = new Properties();
        //this will point to the database url in my package
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("properties/dbInfo.properties")) {
            properties.load(in);
            DB_URL = properties.getProperty("dbUrl");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        setupMemberTable();
        connect();
        setuptable();

    }


    public static DbUtil getInstance() {
        if (instance == null) {
            instance = new DbUtil();
        }

        return instance;
    }

    private Connection connect() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL);
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }

    // this is method is to setup my book table to fetch
    public void setuptable() {
        String Table_Name = "book"; //we could return this...

        if (conn == null)
            conn = connect();

        if (conn != null) {

            Statement statement = null;
            // ResultSet resultSet = null;

            try {
                statement = conn.createStatement();
                DatabaseMetaData dbm = conn.getMetaData();
                ResultSet Tables = dbm.getTables(null, null, Table_Name, null);

                // Loop through all result rows and print them.
                if (Tables.next()) {
                    System.out.println(" Tables " + Table_Name + " already exist, ready to go ");

                } else {
                    stmt.execute("CREATE TABLE " + Table_Name + "("
                            + "  isbn int(11) primary key, \n"
                            + "   title varchar(45), \n"
                            + "  author varchar(45), \n"
                            + " bookcategory_typeid int(45), \n"
                            + " available boolean default true "
                            + ")");


                }
            } catch (SQLException ex) {


                // handle any errors
                System.out.println("SQLException: " + ex.getMessage() + "setupdatabase");
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            } finally {

            }
        }
        return;
    }

    private void cleanUp(Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException sqlEx) {
            } // ignore
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException sqlEx) {
            } // ignore
        }
    }

    //Release the DB connection
    private void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException sqlEx) {
            } // ignore
        }


    }

    //this method is for executing queries.
    public ResultSet ExecuteQuery(String query) {
        ResultSet results;
        try {
            stmt = conn.createStatement();
            results = stmt.executeQuery(query);


        } catch (SQLException e) {
            e.printStackTrace();
            return null;

        } finally {

        }

        return results;
    }

    // used for ex creating inserting data to the database or creating a table
    public boolean executeAction(String qu) {
        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {

        }

    }

    public boolean deleteBook(Book book) throws SQLException {

        try {


            String deleteStatement = "DELETE FROM book WHERE book_id = ?";
            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
            stmt.setString(1, String.valueOf(book.getISBN()));
            int inform = stmt.executeUpdate();
            // if the table is larger than 0 then return true otherwise return false
            if (inform == 1) {
                return true;
            }
            // if successful retunr true

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //otherwise return false
        return false;
    }

    public void setupMemberTable() {
        String Table_Name = "member"; //we could return this...

        if (conn == null)
            conn = connect();

        if (conn != null) {

            Statement statement = null;
            // ResultSet resultSet = null;

            try {
                statement = conn.createStatement();
                DatabaseMetaData dbm = conn.getMetaData();
                ResultSet Tables = dbm.getTables(null, null, Table_Name, null);

                // Loop through all result rows and print them.
                if (Tables.next()) {
                    System.out.println(" Tables " + Table_Name + " already exist, ready to go ");

                } else {
                    stmt.execute("CREATE TABLE " + Table_Name + "("
                            + "  SSN int(11) primary key, \n"
                            + "   name varchar(45), \n"
                            + "  address varchar(45), \n"
                            + " phone_number int(45), \n"
                            + "  email  varchar(45), \n"
                            + " membertype boolean default true "
                            + ")");


                }
            } catch (SQLException ex) {


                // handle any errors
                System.out.println("SQLException: " + ex.getMessage() + "setupdatabase");
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            } finally {

            }
        }
        return;
    }

    public boolean deletemember(Member member) throws SQLException {

        try {


            String deleteStatement = "DELETE FROM member WHERE SSN = ?";
            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
            stmt.setString(1, member.getSSN());
            int inform = stmt.executeUpdate();
            // if the table is larger than 0 then return true otherwise return false
            if (inform == 1) {
                return true;
            }
            // if successful retunr true

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }
//
//    public ArrayList<Book> executeQuery(String bookname) {
//        String st = "SELECT * FROM book;";
//        try {
//            ResultSet resultSet = stmt.executeQuery(st);
//            while (resultSet.next()) {
//                //System.out.println("Start");
//                if (resultSet.getString(2).contains(bookname) && resultSet.getBoolean(4)) {
//                    //  System.out.println(resultSet.getString(1) + ",   " + resultSet.getString(2));
//                    Book book = new Book(
//                            resultSet.getString(1),
//                            resultSet.getInt(2),
//                            resultSet.getString(3),
//                            resultSet.getInt(4),
//                            resultSet.getBoolean(5));
//                    book.add(book);
//                }
//                re
//            }
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
    public boolean SearchBook (Book book) throws SQLException {

        try {


            String deleteStatement = "SELECT FROM book WHERE title = ?";
            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
            stmt.setString(2, book.getTitle());
            int inform = stmt.executeUpdate();
            // if the table is larger than 0 then return true otherwise return false
            if (inform == 1) {
                return true;
            }
            // if successful retunr true

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }
}

