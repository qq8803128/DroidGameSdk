package droid.game.butterknife.compiler;

import com.squareup.javapoet.CodeBlock;

public class FieldLayoutBinding implements ResourceBinding {
    private final Id id;
    private final String name;

    FieldLayoutBinding(Id id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override public Id id() {
        return id;
    }

    @Override public boolean requiresResources(int sdk) {
        return false;
    }

    @Override public CodeBlock render(int sdk) {
        return CodeBlock.of("target.$L = (int)Utils.id(ctx,\"$L\")", name, id.code);
    }
}
