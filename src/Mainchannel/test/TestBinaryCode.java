//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.test;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class TestBinaryCode {
    public TestBinaryCode() {
    }

    public static void main(String[] args) {
        String BinCode = "11111";
        BinCode = "01111";
        System.out.println("Integer of " + BinCode + " = " + Integer.parseInt(BinCode, 2));
    }

    public static String GenerateErrorXML(String actionCodeStr, String desc_str) {
        Element root = new Element("root");
        Document doc = new Document(root);
        Element actionCode = new Element("ACTIONCODE");
        actionCode.setText(actionCodeStr);
        root.addContent(actionCode);
        Element desc = new Element("DESC");
        desc.setText(desc_str);
        root.addContent(desc);
        XMLOutputter out = new XMLOutputter();
        String xmlstr = out.outputString(doc);
        return xmlstr;
    }
}
