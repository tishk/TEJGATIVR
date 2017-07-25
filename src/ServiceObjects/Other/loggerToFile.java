package ServiceObjects.Other;

import utils.PersianDateTime;
import utils.PropertiesUtils;
import utils.SMS.TelBank;
import utils.strUtils;

import javax.rmi.CORBA.Util;
import java.io.*;
import java.net.Socket;

public class loggerToFile {
    private static final loggerToFile inst = new loggerToFile();

    private PersianDateTime PDT = new PersianDateTime();
    strUtils strutils = new strUtils();

    private loggerToFile() {
        super();
    }
    public synchronized void logInfo(String content) {
        try {

            File FileOfSettings = new File(PropertiesUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath());


            String Path = FileOfSettings.toString() + "/Log/" + PDT.getShamsiDateForFileName() + "-Info.log";
            File file = new File(Path);
            if (!file.exists()) {
                file.createNewFile();
            }
            logCorrectedStringForInfo(file, content);
            printToScreen(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public synchronized void logInfoTelBank(String content) {
        try {

            File FileOfSettings = new File(PropertiesUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            printToScreen(FileOfSettings.toString());
            String Path = FileOfSettings.toString() + "/Log/" + PDT.getShamsiDateForFileName() + "-Info.log";
            File file = new File(Path);
            if (!file.exists()) {
                file.createNewFile();
            }
            logCorrectedStringForInfo(file, content);
            printToScreen(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public synchronized void logError(String className, String lineNumber, String content) {
        try {

            File FileOfSettings = new File(PropertiesUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String Path1=FileOfSettings.toString();
            printToScreen(FileOfSettings.toString());
            int i="Gateway.jar".length();
            String Path =Path1.substring(0,Path1.length()-i);
            Path = Path + "log/" + PDT.getShamsiDateForFileName() + "-Errors.log";
            System.out.println("logPath:"+Path);
            File file = new File(Path);
            if (!file.exists()) {
                file.createNewFile();
            }
            logCorrectedStringForErrors(file, content, className, lineNumber);
            printToScreen(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static loggerToFile getInstance() {
        return inst;
    }


    public synchronized String printToScreen(String S) {
        S = PDT.getShamsiDateForFileName() + " " + PDT.GetNowTimeWithSeparator() + " ==> " + S;

        int i;
        for (i = 0; i < S.length(); ++i) {
            System.out.print("_");
        }

        System.out.println("_");

        for (i = 0; i < S.length() + 1; ++i) {
            System.out.print(" ");
        }

        System.out.println("|");
        System.out.println(S + " |");

        for (i = 0; i < S.length(); ++i) {
            System.out.print("_");
        }

        System.out.println("_|");
        return S;
    }
    public static final void  printMessage(String S,boolean isForLogToFile,boolean isError) throws IOException {
        Socket socket = new Socket(PropertiesUtils.getThisHostForSaba(),1300 );
        PrintWriter out =null;
        try {
             out = new PrintWriter(socket.getOutputStream(), true);
            out.print(S);
            out.flush();
            Thread.sleep(100);
            out=null;
            socket.close();
        } catch (IOException ioe) {
            out=null;
            socket.close();
        } catch (InterruptedException e) {
            out=null;
            socket.close();
        }
        if (isForLogToFile ){
            if (isError) loggerToFile.getInstance().logError(loggerToFile.getClassName(),loggerToFile.getLineNumber(),S);
            else loggerToFile.getInstance().logInfo(S);
        }
    }

    private void logCorrectedStringForInfo(File file, String content) {
        content = PDT.getShamsiDateForFileName() + " " + PDT.GetNowTimeWithSeparator() + " ==> " + content;
        String temp = "";
        int i;
        try {
            FileWriter fw = new FileWriter(file, true); //the true will append the new data
            for (i = 0; i < content.length(); ++i) fw.write("_");
            fw.write("_\n");
            for (i = 0; i < content.length() + 1; ++i) fw.write(" ");
            fw.write("|\n");
            fw.write(content + " |\n");
            for (i = 0; i < content.length(); ++i) fw.write("_");
            fw.write("_|\n");
            fw.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void logCorrectedStringForErrors(File file, String content, String ClassName, String LineNumber) {
        int maxLen = 0;
        int i = 0, j = 0;
        String MainString = "";
        String tempString = "An error has occurred with the following specifications:";
        String DateString = "Date : " + PDT.getShamsiDateForFileName();
        String TimeString = "Time : " + PDT.GetNowTimeWithSeparator();
        String classname = "ClassName : " + ClassName;
        String linenNO = "LineNumber in ClassName : " + LineNumber;
        String description = "Description  : " + content;
        String tempDescription = "";
        content = content.trim();
        int pointer = 0;
        if (description.length() > 60) {
            for (i = 0; i <= description.length(); i++) {
                if ((i % 60) == 0) {
                    tempDescription = tempDescription + strutils.leftString(description, 57) + " |\r";
                    pointer++;
                    description = description.substring(60 * pointer);
                    if (description.length() < 60) {
                        tempDescription = tempDescription + description;
                        for (j = 0; j < 57 - description.length(); j++) tempDescription = tempDescription + " ";
                        tempDescription = tempDescription + " |\r";
                        break;
                    }

                }
            }
        } else tempDescription = description;
        description = tempDescription;
        maxLen = tempString.length();
        for (i = 0; i < maxLen + 1; ++i) MainString = MainString + "_";
        MainString = MainString + "_\r";
        MainString = MainString + tempString;
        for (i = 0; i < maxLen - tempString.length(); ++i) MainString = MainString + " ";
        MainString = MainString + "  |\r";
        for (i = 0; i < maxLen + 1; ++i) MainString = MainString + " ";
        MainString = MainString + " |\n";
        MainString = MainString + DateString;
        for (i = 0; i < maxLen + 1 - DateString.length(); ++i) MainString = MainString + " ";
        MainString = MainString + " |\n";
        MainString = MainString + TimeString;
        for (i = 0; i < maxLen + 1 - TimeString.length(); ++i) MainString = MainString + " ";
        MainString = MainString + " |\n";
        MainString = MainString + classname;
        for (i = 0; i < maxLen + 1 - classname.length(); ++i) MainString = MainString + " ";
        MainString = MainString + " |\n";
        MainString = MainString + linenNO;
        for (i = 0; i < maxLen + 1 - linenNO.length(); ++i) MainString = MainString + " ";
        MainString = MainString + " |\n";
        MainString = MainString + description;
        for (i = 0; i < maxLen + 1; ++i) MainString = MainString + " ";
        MainString = MainString + " |\n";
        for (i = 0; i < maxLen; ++i) MainString = MainString + "_";
        MainString = MainString + "__|\n";
        try {
            FileWriter fw = new FileWriter(file, true);
            fw.write(MainString);
            fw.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
         /* maxLen=0;
          i=0;
          j=0;
          MainString="";
          tempString="";
          DateString="";
          TimeString="";
          classname="";
          linenNO="";
          description="";
          tempDescription="";*/
    }

    public static final String getClassName() {
        return String.valueOf(Thread.currentThread().getStackTrace()[2].getClassName());
    }

    public static final String getLineNumber() {
        return String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber());
    }


   /*\
   sample using:
               LoggerToFile.getInstance().logError(LoggerToFile.getClassName,LoggerToFile.getLineNumber,"error for log");
               LoggerToFile.getInstance().logInfo("info for log");
   */
}