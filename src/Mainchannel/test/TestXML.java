//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.test;

import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class TestXML {
    public TestXML() {
    }

    public static void main(String[] args) {
        String xmlstr = "";
        Element root = new Element("root");
        Document doc = new Document(root);
        doc.getContent().add(0, new Comment(" Generated: 1385/12/28 "));
        Element actionCode = new Element("ACTIONCODE");
        actionCode.setText("1002");
        root.addContent(actionCode);
        XMLOutputter out = new XMLOutputter();
        xmlstr = out.outputString(doc);
        System.out.println("out = " + xmlstr);
    }
}
