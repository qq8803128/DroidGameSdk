package droid.game.compiler.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Public extends Item{
    public static Public load(String xml, boolean stream) {
        try {
            Document document = null;
            if (stream) {
                document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .parse(new ByteArrayInputStream(xml.getBytes()));
            } else {
                document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .parse(new File(xml));
            }
            return (Public) new Public().parse(document.getElementsByTagName("resources").item(0));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save(String xml) {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            write(document, null);
            writeXmlToFile(xml, document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeXmlToFile(String xml, Document document) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            String parent = new File(xml).getParent();
            File pDir = new File(parent);
            if (!pDir.exists()) {
                pDir.mkdirs();
            }
            FileOutputStream os = new FileOutputStream(new File(xml));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            DOMSource source = new DOMSource();
            source.setNode(document);
            StreamResult result = new StreamResult();
            result.setOutputStream(os);
            transformer.transform(source, result);
            os.flush();
            os.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public Item createReadChildren(Node node) {
        Item item = null;
        switch (node.getNodeName()) {
            case "public":
                PublicItem publicItem = (PublicItem)(new PublicItem().parse(node));
                item = publicItem;
                break;
            default:
                item = super.createReadChildren(node);
                break;
        }
        return item;
    }

    public static class PublicItem extends Item{
        public String publicName;


        @Override
        public Item parse(Node node) {
            publicName = node.getAttributes().getNamedItem("name").getNodeValue();
            return super.parse(node);
        }
    }
}
