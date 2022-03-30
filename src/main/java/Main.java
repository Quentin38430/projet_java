import java.io.File;
import java.sql.*;

public class Main
{
    public static void main(String[] args)
    {
        // Chemins menant aux différents répertoires
        String INPUT_PATH  = "../ProjetFinal_v2/inputFiles";
        String OUTPUT_PATH = "../ProjetFinal_v2/outputFiles";
        String ERROR_PATH  = "../ProjetFinal_v2/outputFiles/errors";

        File folder_in = new File(INPUT_PATH);

        // Connexion à la base de données MySQL
        MySQLOperations mySQLOperations = new MySQLOperations("jdbc:mysql://localhost:8889/java_project", "root","root");
        Connection con = mySQLOperations.getConnection(mySQLOperations);

        // Lecture des fichiers dans le répertoire donné
        CsvFilesOperations csvFiles = new CsvFilesOperations();
        csvFiles.readFiles(con, mySQLOperations, folder_in, INPUT_PATH, OUTPUT_PATH, ERROR_PATH);

        // Arrêt de la connexion avec la base de données MySQL
        mySQLOperations.stopConnection(con);
    }
}
