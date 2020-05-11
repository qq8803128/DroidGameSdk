package droid.game.x2c.compiler;

import com.google.auto.service.AutoService;
import droid.game.x2c.annotation.Xml;
import droid.game.x2c.compiler.xml.LayoutManager;

import java.util.Set;
import java.util.TreeSet;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * @author chengwei 2018/8/7
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes("droid.game.x2c.annotation.Xml")
public class XmlProcessor extends AbstractProcessor {

    private int mGroupId = 0;
    private LayoutManager mLayoutMgr;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        Log.init(processingEnvironment.getMessager());
        mLayoutMgr = LayoutManager.instance();
        mLayoutMgr.setFiler(processingEnvironment.getFiler());
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Xml.class);
        TreeSet<String> layouts = new TreeSet<>();
        for (Element element : elements) {
            Xml xml = element.getAnnotation(Xml.class);
            String[] names = xml.layouts();
            for (String name : names) {
                layouts.add(name.substring(name.lastIndexOf(".") + 1));
            }
        }

        for (String name : layouts) {
            if (mGroupId == 0 && mLayoutMgr.getLayoutId(name) != null) {
                mGroupId = (mLayoutMgr.getLayoutId(name) >> 24);
            }
            mLayoutMgr.setGroupId(mGroupId);
            mLayoutMgr.translate(name);
        }

        mLayoutMgr.printTranslate();
        return false;
    }


}
