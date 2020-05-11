package droid.game.butterknife.compiler;

import com.squareup.javapoet.CodeBlock;

final class FieldDrawableBinding implements ResourceBinding {
    private final Id id;
    private final String name;
    private final Id tintAttributeId;

    FieldDrawableBinding(Id id, String name, Id tintAttributeId) {
        this.id = id;
        this.name = name;
        this.tintAttributeId = tintAttributeId;
    }

    @Override public Id id() {
        return id;
    }

    @Override public boolean requiresResources(int sdk) {
        return false;
    }

    @Override public CodeBlock render(int sdk) {
        return CodeBlock.of("target.$L = ctx.getResources().getDrawable((int)Utils.id(ctx,\"$L\"))", name, id.code);
    }
}
