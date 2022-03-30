import java.sql.*;
import java.util.ArrayList;

public class MySQLOperations
{
    private String server;
    private String user;
    private String password;

    MySQLOperations(String server, String user, String password)
    {
        this.server = server;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection(MySQLOperations mySQLConnection)
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(mySQLConnection.server,mySQLConnection.user,mySQLConnection.password);
            System.out.println("Connected to " + mySQLConnection.server);
            con.setAutoCommit(false);
            return con;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        finally
        {
            return null;
        }
    }

    public void sqlInsert(Connection con, User record) throws SQLException
    {
        String sql = "INSERT INTO users(numero_secu,lastname,firstname,date_of_birth,phone_number,email," +
                "id_remboursement,code_soin,montant_remboursement, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = con.prepareStatement(sql);

        stmt.setString(1, record.getNumero_secu());
        stmt.setString(2, record.getLastname());
        stmt.setString(3, record.getFirstname());
        stmt.setDate(4, record.getDate_of_birth());
        stmt.setString(5, record.getPhone_number());
        stmt.setString(6, record.getEmail());
        stmt.setInt(7, record.getId_remboursement());
        stmt.setInt(8, record.getCode_soin());
        stmt.setString(9, record.getMontant_remboursement());
        stmt.setTimestamp(10, record.getTimestamp());

        stmt.addBatch();
        stmt.executeBatch();
    }

    public void sqlUpdate(Connection con, User record) throws SQLException
    {
        String sql = "UPDATE users SET  lastname = ?, firstname = ?, date_of_birth = ?, " +
                "phone_number = ?, email = ?, code_soin = ?, montant_remboursement = ?, timestamp = ? " +
                "WHERE id_remboursement = ?";
        PreparedStatement stmt = con.prepareStatement(sql);

        stmt.setString(1, record.getLastname());
        stmt.setString(2, record.getFirstname());
        stmt.setDate(3, record.getDate_of_birth());
        stmt.setString(4, record.getPhone_number());
        stmt.setString(5, record.getEmail());
        stmt.setInt(6, record.getCode_soin());
        stmt.setString(7, record.getMontant_remboursement());
        stmt.setTimestamp(8, record.getTimestamp());
        stmt.setInt(9, record.getId_remboursement());

        stmt.addBatch();
        stmt.executeBatch();
    }

    public void sqlCheckIfExist(Connection con, ArrayList<User> users, String filename, int countError) throws SQLException
    {
        String sql = "SELECT COUNT(*) FROM users WHERE id_remboursement = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        int countInsert = 0;
        int countUpdate = 0;
        for (User record : users)
        {
            stmt.setInt(1, record.getId_remboursement());
            ResultSet result = stmt.executeQuery();

            while(result.next())
            {
                if(result.getInt(1) == 0)
                {
                    //insert
                    sqlInsert(con, record);
                    countInsert++;
                }
                else
                {
                    //update
                    sqlUpdate(con, record);
                    countUpdate++;
                }
            }
        }
        System.out.println("Fichier " + filename + " : " + countInsert + " INSERT, " + countUpdate + " UPDATE en base de données, " + countError + " erreurs.");
        con.commit();
    }

    public void stopConnection(Connection con)
    {
        try
        {
            con.close();
            System.out.println("Fin de la connexion avec la base de données MySQL.");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
