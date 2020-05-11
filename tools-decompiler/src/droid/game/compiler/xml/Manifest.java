package droid.game.compiler.xml;

import droid.game.compiler.util.TextUtils;
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

public class Manifest extends Item {
    static String packageName;
    private String prefix;
    public Application application;

    public static Manifest load(String xml, boolean stream) {
        try {
            Document document = null;
            if (stream) {
                document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .parse(new ByteArrayInputStream(xml.getBytes()));
            } else {
                document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .parse(new File(xml));
            }
            return (Manifest) new Manifest().parse(document.getElementsByTagName("manifest").item(0));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Item parse(Node node) {
        Item item = super.parse(node);
        packageName = attr.get("package");
        return item;
    }

    public void save(String androidManifestXmlFile) {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            write(document, null);
            writeXmlToFile(androidManifestXmlFile, document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeXmlToFile(String androidManifestXmlFile, Document document) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            String parent = new File(androidManifestXmlFile).getParent();
            File pDir = new File(parent);
            if (!pDir.exists()) {
                pDir.mkdirs();
            }
            FileOutputStream os = new FileOutputStream(new File(androidManifestXmlFile));
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
            case "application":
                item = new Application().parse(node);
                application = (Application) item;
                break;
            default:
                item = super.createReadChildren(node);
                break;
        }
        return item;
    }

    @Override
    public String[] createWriteAttributes(String key, String value) {
        if (key.equals("package") && !TextUtils.isEmpty(prefix)){
            return super.createWriteAttributes(key,packageName + "." + prefix);
        }
        return super.createWriteAttributes(key, value);
    }

    @Override
    protected String getAttrFilter() {
        return "remove-android-manifest-attr.json";
    }

    public void setPackageNamePrefix(String prefix) {
        this.prefix = prefix;
    }
}
