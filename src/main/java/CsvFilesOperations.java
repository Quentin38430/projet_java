import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class CsvFilesOperations
{
    public void readFiles(Connection con, MySQLOperations mySQLOperations, File folder_in, String INPUT_PATH, String OUTPUT_PATH, String ERROR_PATH)
    {
        try
        {
            for(File file : folder_in.listFiles())
            {
                String filename = file.getName();

                if((!file.isDirectory()) && (FilenameUtils.getExtension(filename).equals("csv")))
                {
                    String filenameSubstring = filename.substring(6, 20);
                    DateFormat formatDateTime = new SimpleDateFormat("yyyyMMddHHmmss");
                    java.util.Date date = formatDateTime.parse(filenameSubstring);
                    long time = date.getTime();
                    Timestamp timestamp = new Timestamp(time);

                    BufferedReader lineReader = new BufferedReader(new FileReader(INPUT_PATH + "/" + filename));
                    CSVParser records = CSVParser.parse(lineReader, CSVFormat.EXCEL);

                    ArrayList<User> users = new ArrayList<>();
                    int countError = 0;
                    for (CSVRecord record : records)
                    {
                        if(checkData(record))
                        {
                            User user = new User();
                            user.setNumero_secu(record.get(0));
                            user.setLastname(record.get(1));
                            user.setFirstname(record.get(2));
                            user.setDate_of_birth(Date.valueOf(record.get(3)));
                            user.setPhone_number(record.get(4));
                            user.setEmail(record.get(5));
                            user.setId_remboursement(parseInt(record.get(6)));
                            user.setCode_soin(parseInt(record.get(7)));
                            user.setMontant_remboursement(record.get(8));
                            user.setTimestamp(timestamp);
                            users.add(user);
                        }
                        else
                        {
                            File fileError = new File(ERROR_PATH + "/error_" + filename);
                            if(fileError.exists())
                            {
                                writeErrorFile(ERROR_PATH, filename, record, true);
                            }
                            else
                            {
                                writeErrorFile(ERROR_PATH, filename, record, false);
                            }
                            countError++;
                        }
                    }
                    mySQLOperations.sqlCheckIfExist(con, users, filename, countError);
                    moveFile(INPUT_PATH, OUTPUT_PATH, filename);
                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    public void writeErrorFile(String ERROR_PATH, String filename, CSVRecord record, boolean fileExist)
    {
        try
        {
            filename = "error_" + filename;
            if(fileExist)
            {
                // Insertion d'une ligne en erreur dans un fichier CSV déjà existant
                BufferedWriter csvFile = new BufferedWriter(new FileWriter(ERROR_PATH + "/" + filename, true));
                addLine(record, csvFile);
            }
            else
            {
                // Création du nouveau fichier CSV contenant la première ligne en erreur
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(ERROR_PATH + "/" + filename));
                addLine(record, writer);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void addLine(CSVRecord record, BufferedWriter csvFile) throws IOException
    {
        CSVPrinter csvPrinter = new CSVPrinter(csvFile, CSVFormat.DEFAULT);

        csvPrinter.printRecord(record.get(0), record.get(1), record.get(2), record.get(3), record.get(4),
                record.get(5), record.get(6), record.get(7), record.get(8));

        csvPrinter.flush();
    }

    public boolean checkData(CSVRecord record)
    {
        if(record.get(0).equals(null) || record.get(0) == "" || record.get(0).length() != 13)
            return false;
        else if(record.get(1).equals(null) || record.get(1) == "" || record.get(1).length() > 50)
            return false;
        else if(record.get(2).equals(null) || record.get(2) == "" || record.get(2).length() > 50)
            return false;
        else if(record.get(3).equals(null) || record.get(3) == "" || !checkDateFormat(record.get(3)))
            return false;
        else if(record.get(4).equals(null) || record.get(4) == "" || record.get(4).length() != 10)
            return false;
        else if(record.get(5).equals(null) || record.get(5) == "" || record.get(5).length() > 255)
            return false;
        else if(record.get(6).equals(null) || record.get(6) == "" || parseInt(record.get(6)) < 1)
            return false;
        else if(record.get(7).equals(null) || record.get(7) == "" || parseInt(record.get(7)) < 1 || parseInt(record.get(7)) > 10)
            return false;
        else if(record.get(8).equals(null) || record.get(8) == "" || record.get(8).length() < 3 || record.get(8).length() > 5)
            return false;
        else
            return true;
    }

    public boolean checkDateFormat(String dateFormat)
    {
        SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd");
        formatDateTime.setLenient(false);
        try
        {
            java.util.Date date = formatDateTime.parse(dateFormat);
            return true;
        }
        catch (ParseException e)
        {
            return false;
        }
    }

    public void moveFile(String INPUT_PATH, String OUTPUT_PATH, String filename)
    {
        try
        {
            Path tmp = Files.move(Paths.get(INPUT_PATH + "/" + filename), Paths.get(OUTPUT_PATH + "/" + filename));
            if(tmp != null)
            {
                System.out.println("Fichier déplacé avec succès !");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
