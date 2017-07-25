

import java.io.IOException;

/**
 * Created by Administrator on 9/30/2016.
 */
public class Main {

    public static void main(String arg[]) throws IOException, InterruptedException {
         //sendFax__();
    }

 /*   public static   void  sendFax__() throws IOException, InterruptedException {
        //http://www.hylafax.org/man/current/faxsetup.1m.html
        //sendfax -d 85569999  /ivr/Fax/FaxVega200.tiff

        //String faxFilePath="/ivr/Faxx.pdf";
        String faxFile="/ivr/Faxx.pdf";
        System.out.println("in send fax function system.out.print:fax File is:"+faxFile);
        String faxDestinationNO= "809";
        String faxUser="807";
        String faxPass="npc807";
        String document="";
        HylaFAXClient c = new HylaFAXClient();

        try {
            c.open("10.39.41.63");
            if(c.user("807")){
                c.pass("npc807");
            }
            c.noop();
            c.tzone("LOCAL");
            FileInputStream var36 = new FileInputStream(faxFile);
            document = c.putTemporary(var36);

            Job var37 = c.createJob();
            var37.setFromUser(faxUser);
            var37.setNotifyAddress(faxUser);
            var37.setKilltime("000259");
            var37.setMaximumDials(1);
            var37.setMaximumTries(1);
            var37.setPriority(127);
            var37.setDialstring(faxDestinationNO);
            var37.setVerticalResolution(196);

            var37.setPageDimension(Pagesize.LETTER);
            var37.setNotifyType("none");
            var37.setChopThreshold(3);
            var37.addDocument(document);
            c.submit(var37);
        } catch (Exception var34) {
            System.out.println(var34.getMessage());
        } finally {
            try {
                System.out.println("Jop ID:"+String.valueOf(c.job()));
                c.quit();
            } catch (Exception var33) {
                System.out.println(var33.getMessage());
            }

        }


    }*/
}
