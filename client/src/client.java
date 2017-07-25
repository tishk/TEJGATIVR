import Mainchannel.Exceptions.InvalidParameterException;
import Mainchannel.Exceptions.ResponseParsingException;
import Mainchannel.sender.SenderException;
import ServiceObjects.Account.*;
import ServiceObjects.Other.BillInfoByTelNumber;
import ServiceObjects.Pan.BalanceForCard;
import ServiceObjects.Pan.BillPayByBillIDPan;
import ServiceObjects.Pin.AuthenticatePin1;
import ServiceObjects.Pin.AuthenticatePin2;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;
import gui.ava.html.image.generator.HtmlImageGenerator;
import org.apache.commons.io.FileUtils;
import org.docx4j.org.xhtmlrenderer.pdf.ITextRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;


public class client {
    private static Bank b = null;
    private static String errorCodeIfExist = "";

    public  static void main(String parameters[]) throws Exception {

        startApp();


    }

    private static void testMonitoring() throws IOException, ServerNotActiveException {
        new Properties_Client();
         String input = null;
         ObjectFromRequest objectFromRequest=null;
         ResponseFromObject responseFromObject=null;
        input="530708527031957;2120043317470910000000091498529690700-01-02-0C-2B-E3"+"w";
        objectFromRequest=new ObjectFromRequest(input);
        responseFromObject=new ResponseFromObject(objectFromRequest.getCreatedObject(),input);
        System.out.println(responseFromObject);

    }

    private static void testClientRequest() throws IOException, ServerNotActiveException {
        InternalFollowUp  internalFollowUp=new InternalFollowUp();
        internalFollowUp.setSourceAccount("3435531082");
        internalFollowUp.setFollowUpCode("145930");
        internalFollowUp.setIsInternalFollowCode(true);
        internalFollowUp.setIsPanPayment(false);
        internalFollowUp.setCallUniqID("1234567891234");

        internalFollowUp=(InternalFollowUp) submitRequestToGateway(internalFollowUp);
        System.out.println(internalFollowUp.getActionCode());
    }
    private static void testClientRequest_() throws IOException, ServerNotActiveException {
        FundTransfer fundTransfer=new FundTransfer();
        fundTransfer.setSourceAccount("3435531082");
        fundTransfer.setDestinationAccount("3512512541");
        fundTransfer.setTransactionAmount("1");
        fundTransfer.setCallUniqID("1234567891234");
        fundTransfer.setIsFundTransfer(true);
        fundTransfer=(FundTransfer) submitRequestToGateway(fundTransfer);
        System.out.println(fundTransfer.getActionCode());
    }
    private static void testClientPanBillPayment() throws IOException, ServerNotActiveException {
        BillPayByBillIDPan billPayByIDPan=new BillPayByBillIDPan();
        billPayByIDPan.setPan("5859831022332901");
        billPayByIDPan.setPin("34611");
        billPayByIDPan.setBillID("9413325304124");
        billPayByIDPan.setPaymentID("21160192");
        billPayByIDPan.setAmount("210000");
        billPayByIDPan.setCallUniqID("1239874569874");
        billPayByIDPan= (BillPayByBillIDPan) submitRequestToGateway(billPayByIDPan);
        String actionCode=billPayByIDPan.getActionCode();

        System.out.println(billPayByIDPan.getActionCode());
    }
    private static void testClientBillPaymentAccount() throws IOException, ServerNotActiveException {
        BillPayByIDAccount billPayByIDAccount=new BillPayByIDAccount();
        billPayByIDAccount.setSourceAccount("3435531082");
        billPayByIDAccount.setBillID("9413325304124");
        billPayByIDAccount.setPaymentID("21160192");
        billPayByIDAccount.setAmount("210000");
        billPayByIDAccount.setCallUniqID("1239874569874");
        billPayByIDAccount= (BillPayByIDAccount) submitRequestToGateway(billPayByIDAccount);
        String actionCode=billPayByIDAccount.getActionCode();

        System.out.println(billPayByIDAccount.getTraceCode());

    }
    private static void testTelSwitch() throws IOException, ServerNotActiveException {

        BillInfoByTelNumber billInfoByTelNumber=new BillInfoByTelNumber();
        billInfoByTelNumber.setPan("3435531082");
        billInfoByTelNumber.setIsMobile(false);
        billInfoByTelNumber.setTelNo("09125016043");

        billInfoByTelNumber.setCallUniqID("7777777777777");
        billInfoByTelNumber= (BillInfoByTelNumber) submitRequestToGateway(billInfoByTelNumber);

        System.out.println(billInfoByTelNumber.getActionCode());
        System.out.println(billInfoByTelNumber.getBillID());

    }
    private static void testPin1() throws IOException, ServerNotActiveException {

        AuthenticatePin1 authenticatePin1=new AuthenticatePin1();
        authenticatePin1.setAccountNumber("3435531082");
        authenticatePin1.setPin("9221");
        authenticatePin1.setCallerID("09379707473");
        authenticatePin1.setDoChangePin(false);
        authenticatePin1.setCallUniqID("7777777777777");
        authenticatePin1=(AuthenticatePin1)submitRequestToGateway(authenticatePin1);



    }
    private static void testPin2() throws IOException, ServerNotActiveException {

        AuthenticatePin2 authenticatePin2=new AuthenticatePin2();
        authenticatePin2.setAccountNumber("3435531082");
        authenticatePin2.setPin("13601365");
        authenticatePin2.setCallerID("09379707473");
//        authenticatePin2.setDoChangePin(false);
        authenticatePin2.setCallUniqID("7777777777777");
        authenticatePin2=(AuthenticatePin2)submitRequestToGateway(authenticatePin2);
        System.out.println(authenticatePin2.getActionCode());


    }
    private static void testPin2_Change() throws IOException, ServerNotActiveException {


        AuthenticatePin2 authenticatePin2=new AuthenticatePin2();
        authenticatePin2.setAccountNumber("3435531082");
        authenticatePin2.setPin("13651360");
        authenticatePin2.setPin_New("13601365");
        authenticatePin2.setCallerID("09379707473");
        authenticatePin2.setDoChangePin(true);
        authenticatePin2.setCallUniqID("7777777777777");
        authenticatePin2=(AuthenticatePin2)submitRequestToGateway(authenticatePin2);
        System.out.println(authenticatePin2.getActionCode());


    }
    private static void testPin1_ChangePass() throws IOException, ServerNotActiveException, InterruptedException {



/*
        AuthenticatePin1 authenticatePin1=new AuthenticatePin1();
        authenticatePin1.setAccountNumber("3435531082");
        authenticatePin1.setPin_New("1111");
        authenticatePin1.setPin("9221");
        authenticatePin1.setCallerID("09379707473");
        authenticatePin1.setDoChangePin(true);
        authenticatePin1.setCallUniqID("7777777777774");
        authenticatePin1=(AuthenticatePin1)submitRequestToGateway(authenticatePin1);*/
//        Thread.sleep(2000);
        AuthenticatePin1 authenticatePin1=new AuthenticatePin1();

//        Executor executor=new ThreadPoolExecutor()
        authenticatePin1=new AuthenticatePin1();
        authenticatePin1.setAccountNumber("3435531082");
//        authenticatePin1.setPin_New("1111");
        authenticatePin1.setPin("1111");
        authenticatePin1.setCallerID("09379707473");
//        authenticatePin1.setDoChangePin(true);
        authenticatePin1.setCallUniqID("7777777777772");
        authenticatePin1=(AuthenticatePin1)submitRequestToGateway(authenticatePin1);

        Thread.sleep(2000);
//        Executor executor=new ThreadPoolExecutor()
        authenticatePin1=new AuthenticatePin1();
        authenticatePin1.setAccountNumber("3435531082");
        authenticatePin1.setPin_New("1111");
//        authenticatePin1.setPin("9221");
        authenticatePin1.setCallerID("09379707473");
//        authenticatePin1.setDoChangePin(true);
        authenticatePin1.setCallUniqID("7777777777773");
        authenticatePin1=(AuthenticatePin1)submitRequestToGateway(authenticatePin1);


    }

    public  static void startApp() throws IOException, ServerNotActiveException {
        new Properties_Client();
        new Listener();
        /*String req="3260026133889450000000021888969900700-0B-CD-AF-0F-8CT030015597198300000000000080000000000000000000000000000000000";
        /*String req="3260026133889450000000021888969900700-0B-CD-AF-0F-8CT030351251254100000000000080000000000000000000000000000000000";
        ObjectFromRequest objectFromRequest=new ObjectFromRequest(req);
        ResponseFromObject responseFromObject=new ResponseFromObject(objectFromRequest.getCreatedObject(),req);
        String res=responseFromObject.getResponse();*/
    }
    public  static String   getTodayDate(){

        Date Time=new Date();
        SimpleDateFormat DateFormat =
                //new SimpleDateFormat ("hh:mm:ss a");
                new SimpleDateFormat ("yyyy.MM.dd");
        String Now=DateFormat.format(Time);
        return Now;

    }
    private static boolean testServerConnected(String parameters[]) throws IOException, ResponseParsingException, SenderException, InvalidParameterException, SQLException, ServerNotActiveException {
        if (connectedToServer(parameters[0], parameters[1])) {
            if (parameters[2].equals("004"))
                createStatementFile(parameters, b.submitRequest(parameters));
            else if (parameters[2].equals("777")) {
                balanceForCard();
            }
            System.out.print(b.submitRequest(parameters));
        }
        b = null;
        System.out.println(errorCodeIfExist);
        return true;
    }

    public  static void  test() throws IOException, ServerNotActiveException {
        new Properties_Client();
        String req="";

        req="3260026133889450000000021888969900700-0B-CD-AF-0F-8CT030015597198300000000000080000000000000000000000000000000000";

        ObjectFromRequest objectFromRequest=new ObjectFromRequest(req);
        System.out.println(new ResponseFromObject(objectFromRequest.getCreatedObject(), req).getResponse());;
    }


    private static boolean connectedToServer(String IP,String Port) {
        try {

            b = (Bank) Naming.lookup("rmi://" + IP + ":"+Port+"/Gateway");
            return true;
        } catch (NotBoundException var2) {
            errorCodeIfExist = "$6501*Description : " + var2.getMessage()+"*$";
            return false;
        } catch (MalformedURLException var3) {
            errorCodeIfExist = "$6502*Description : " + var3.getMessage()+"*$";
            return false;
        } catch (RemoteException var4) {
            errorCodeIfExist = "$6504*Description : " + var4.getMessage()+"*$";
            return false;
        } catch (Exception var5) {
            errorCodeIfExist = "$6505*Description : " + var5.getMessage()+"*$";
            return false;
        }
    }
    private static void    createStatementFile(String params[],String resultFromServer) throws IOException {
        FileUtils.writeStringToFile(new File(params[3]+"-"+params[4]+"-"+params[5]),resultFromServer);
    }

    public static void balanceForCard() throws IOException, RemoteException, SQLException, ServerNotActiveException {
        BalanceForCard balanceForCard=new BalanceForCard();
        balanceForCard.setPan("5859831000001247");
        balanceForCard.setPin("259100");
        printToScreen("in test balance ");
        balanceForCard=(BalanceForCard)submitRequestToGateway(balanceForCard);
        printToScreen(balanceForCard.getActionCode());
        printToScreen(balanceForCard.getResultFromServer().getActualBalance());
    }
    public static void accinfo() throws IOException, RemoteException, SQLException, ServerNotActiveException {
        AccountInformation accountInformation=new AccountInformation();
        accountInformation.setAccountNumber("38323822");
        accountInformation=(AccountInformation)submitRequestToGateway(accountInformation);
        printToScreen(accountInformation.getActionCode());
        printToScreen(accountInformation.getResultFromChannel().getAccountcurrency());
    }

    public static void balanceForAccount() throws IOException, RemoteException, SQLException, ServerNotActiveException {
        BalanceForAccount balanceForAccount=new BalanceForAccount();
        balanceForAccount.setAccountNumber("38323822");
        balanceForAccount=(BalanceForAccount) submitRequestToGateway(balanceForAccount);
        printToScreen(balanceForAccount.getActionCode());
        printToScreen(balanceForAccount.getResultFromChannel().getActualBalance());
    }
    public static void auth() throws IOException, ServerNotActiveException {
       AuthenticatePin2 authenticatePin2=new AuthenticatePin2();
        authenticatePin2.setAccountNumber("38323822");
        authenticatePin2.setPin("1818");
        authenticatePin2.setDoChangePin(false);
        authenticatePin2.setCallerID("88362552");
        authenticatePin2.setCallUniqID("123456789123456");
        authenticatePin2.setIsNotHaghighiAccount(true);
        authenticatePin2 = (AuthenticatePin2) submitRequestToGateway(authenticatePin2);
        System.out.println("client.auth"+authenticatePin2.getActionCode());
    }

    public static String printToScreen(String S) {
       // S = PDT.getShamsiDateForFileName() + " " + PDT.GetNowTimeWithSeparator() + " ==> " + S;

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
    public static Object submitRequestToGateway(Object object) throws IOException, ServerNotActiveException {
        Bank b=null;
        try {
            b=(Bank) Naming.lookup("rmi://10.39.41.113:1364/Gateway");
            object=b.submitRequest(object);
        } catch (NotBoundException e) {
            e.printStackTrace();
            printToScreen("1:" + e.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            printToScreen("2:" + e.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
            printToScreen("3:" + e.toString());
        } catch (InvalidParameterException e) {
            e.printStackTrace();
            printToScreen("4:" + e.toString());
        } catch (ResponseParsingException e) {
            e.printStackTrace();
            printToScreen("5:" + e.toString());
        } catch (SenderException e) {
            e.printStackTrace();
            printToScreen("6:" + e.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return object;
    }


    public static void loadhtml() throws IOException, DocumentException {
        String filePath="D:\\GatewayBackups\\Gateway_onlySourec_9504010938\\source\\Gateway\\out\\artifacts\\Telbank\\7.html";
        String content = readFile(filePath, StandardCharsets.UTF_8);
        File file = new File("D:\\GatewayBackups\\Gateway_onlySourec_9504010938\\source\\Gateway\\out\\artifacts\\Telbank\\77.pdf");

        OutputStream f = new FileOutputStream(file);
        Document document = new Document();
        PdfWriter.getInstance(document, f);
        document.open();
        HTMLWorker htmlWorker = new HTMLWorker(document);
        htmlWorker.parse(new StringReader(content));
        document.close();
        f.close();


    }
    public static String readFile(String path, Charset encoding)throws IOException{
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
    public static void convert() throws IOException {
        HtmlImageGenerator htmlImageGenerator=new HtmlImageGenerator();
        Dimension dimension=new Dimension();
        dimension.setSize(1024,768);
        htmlImageGenerator.setSize(dimension);
        String filePath="D:\\GatewayBackups\\Gateway_onlySourec_9504010938\\source\\Gateway\\out\\artifacts\\Telbank\\Fax2188737520-1465818168.45.html";
        String content = readFile(filePath, StandardCharsets.UTF_8);
        htmlImageGenerator.loadUrl("file:///D:/GatewayBackups/Gateway_onlySourec_9504010938/source/Gateway/out/artifacts/Telbank/Fax2188737520-1465818168.45.html");
        File file = new File("D:\\GatewayBackups\\Gateway_onlySourec_9504010938\\source\\Gateway\\out\\artifacts\\Telbank\\1111.png");
        htmlImageGenerator.saveAsImage(file);
    }
    public static void convert2() throws IOException, DocumentException {
        String filePath="D:\\GatewayBackups\\Gateway_onlySourec_9504010938\\source\\Gateway\\out\\artifacts\\Telbank\\Fax2188737520-1465818168.45.html";
        String html = readFile(filePath, StandardCharsets.UTF_8);

        JLabel label = new JLabel(html);
        label.setSize(1024, 768);

        BufferedImage image = new BufferedImage(
                label.getWidth(), label.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        {
            // paint the html to an image
            Graphics g = image.getGraphics();
            g.setColor(Color.BLACK);
            label.paint(g);
            g.dispose();
        }

        // get the byte array of the image (as jpeg)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        byte[] bytes = baos.toByteArray();
        ImageIO.write(image, "png", new File("test.png"));
    }
    public static void convert3() throws IOException {
        String filePath="D:\\GatewayBackups\\Gateway_onlySourec_9504010938\\source\\Gateway\\out\\artifacts\\Telbank\\7.html";
        String pdfFile="D:\\GatewayBackups\\Gateway_onlySourec_9504010938\\source\\Gateway\\out\\artifacts\\Telbank\\1111111.pdf";
        String html = readFile(filePath, StandardCharsets.UTF_8);
        File file = new File(filePath);
        final String xhtmlUrl = file.toURI().toURL().toString();
        final OutputStream reportPdfStream = new FileOutputStream(pdfFile);
        final ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(file);
        renderer.layout();
        try {
            renderer.createPDF(reportPdfStream);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        reportPdfStream.close();
    }
}
