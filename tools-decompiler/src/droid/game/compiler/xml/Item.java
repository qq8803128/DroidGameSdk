package droid.game.compiler.xml;

import droid.game.compiler.util.FileUtils;
import droid.game.compiler.util.TextUtils;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;

import java.util.*;

public class Item {
    public String name;
    public String androidName;
    public Map<String, String> attr = new HashMap<>();
    public List<Item> child = new ArrayList<>();

    private String dir;

    public Item parse(Node node) {
        name = node.getNodeName();

        try {
            androidName = node.getAttributes().getNamedItem("android:name").getNodeValue();
        } catch (Throwable e) {
            androidName = "";
        }

        parseAttributes(node.getAttributes());
        parseChildNodes(node.getChildNodes());

        return this;
    }

    public void parseAttributes(NamedNodeMap attributes) {
        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                String key = attributes.item(i).getNodeName();
                String val = attributes.item(i).getNodeValue();
                attr.put(key, val);
            }
        }
    }

    public void parseChildNodes(NodeList nodeList) {
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (isDefered(node)) {
                    continue;
                }

                Item c = createReadChildren(node);
                if (c != null) {
                    child.add(c);
                }
            }
        }
    }

    public Item createReadChildren(Node node) {
        return new Item().parse(node);
    }

    public boolean isDefered(Node node) {
        return node.getClass().toString().contains("DeferredTextImpl");
    }

    public void write(Document document, Element element) {
        //root
        String filter = getNameFilter();
        if (!TextUtils.isEmpty(filter)){
            List<String> filters = FileUtils.json(dir,filter,new TypeToken<List<String>>(){}.getType());
            if (filters != null && filters.size() > 0){
                for (String name : filters){
                    if (androidName.startsWith(name)){
                        return;
                    }
                }
            }
        }

        if (element == null) {
            Element node = document.createElement(name);
            document.appendChild(node);
            element = node;
        } else {
            Element node = document.createElement(name);
            element.appendChild(node);
            element = node;
        }

        for (String key : attr.keySet()) {
            String value = attr.get(key);

            filter = getAttrFilter();
            if (!TextUtils.isEmpty(filter)){
                List<String> filters = FileUtils.json(dir,filter,new TypeToken<List<String>>(){}.getType());
                if (filters != null && filters.size() > 0){
                    boolean next = false;
                    for (String name : filters){
                        if (key.equals(name)){
                            next = true;
                            break;
                        }
                    }
                    if (next){
                        continue;
                    }
                }
            }

            String[] writeAttr = createWriteAttributes(key, value);
            if (writeAttr != null && writeAttr.length == 2) {
                element.setAttribute(writeAttr[0], writeAttr[1]);
            }
        }

        for (Item children : child) {
            children.write(document, element);
        }
    }

    public String[] createWriteAttributes(String key, String value) {
        if (isEmpty(key) || isEmpty(value)) {
            return new String[]{};
        }
        return new String[]{key, value};
    }

    public boolean isEmpty(String text) {
        return text == null || text.length() == 0;
    }

    public String getPackageName(){
        return Manifest.packageName;
    }

    public Item setDir(String dir){
        this.dir = dir;
        for(Item c : child){
            c.setDir(dir);
        }
        return this;
    }

    public String getDir(){
        return dir;
    }

    protected String getAttrFilter(){
        return "";
    }

    protected String getNameFilter(){
        return "";
    }
}
