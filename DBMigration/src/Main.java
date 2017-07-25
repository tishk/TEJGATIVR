import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by root on 4/19/16.
 */
public class Main {

    static int threadCount=2;
    public static String recordCount="1";

    public  static void main(String parameters[]) throws Exception {
        if (parameters.length == 2) {
            threadCount = Integer.valueOf(parameters[0]);
            recordCount=parameters[1];
        } else {
            threadCount = 2;
        }
        new Properties_DBMigration();
        readCurrencyAccounts();
    }
    public static void createMasterUpDateProccess() throws SQLException, InterruptedException {
        for (int i=0;i<=threadCount;i++){
            new TransferMaster();
        }
    }
    public static void createPrivilegeUpDateProccess() throws SQLException, InterruptedException {
        for (int i=0;i<=threadCount;i++){
            new TransferPrivilageRegistration();
        }
    }
    public static void createGhabzProccess() throws SQLException, InterruptedException {
        for (int i=0;i<=threadCount;i++){
            new Ghabz(threadCount);
            Thread.sleep(777);
        }
    }
    public static  String getMsgSequence() {

        return String.valueOf(System.nanoTime()).substring(0,6);//144525438988

    }
    public static void  readCurrencyAccounts() throws FileNotFoundException, SQLException, InterruptedException {
        Scanner scanner=new Scanner(new File("/gateway/ARZ"));
        ArrayList<String> arrayList=new ArrayList<String>();
        while (scanner.hasNext()){
            arrayList.add(scanner.next().substring(0,10));
        }
        scanner.close();
        new Currency(arrayList);

    }

    }
