package droid.game.gradle.builder;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static javax.lang.model.element.Modifier.*;

public final class FinalR3ClassBuilder {
    private static final String[] SUPPORTED_TYPES = {
            "anim", "array", "attr", "bool", "color", "dimen", "drawable", "id", "integer", "layout", "menu", "plurals",
            "string", "style", "styleable", "mipmap"
    };


    private FinalR3ClassBuilder() {
    }

    public static void brewJava(File rFile, File outputDir, String packageName, String className, boolean debug)
            throws Exception {
        CompilationUnit compilationUnit = JavaParser.parse(rFile);
        TypeDeclaration resourceClass = compilationUnit.getTypes().get(0);

        TypeSpec.Builder result =
                TypeSpec.classBuilder(className).addModifiers(PUBLIC).addModifiers(FINAL);

        for (Node node : resourceClass.getChildNodes()) {
            if (node instanceof ClassOrInterfaceDeclaration) {
                addResourceType(Arrays.asList(SUPPORTED_TYPES), result, (ClassOrInterfaceDeclaration) node, debug, packageName);
            }
        }

        JavaFile finalR = JavaFile.builder(packageName, result.build())
                .addFileComment("Generated code from Butter Knife gradle plugin. Do not modify!")
                .build();

        finalR.writeTo(outputDir);
    }

    private static void addResourceType(List<String> supportedTypes, TypeSpec.Builder result,
                                        ClassOrInterfaceDeclaration node, boolean debug, String packageName) {
        if (!supportedTypes.contains(node.getNameAsString())) {
            return;
        }

        String type = node.getNameAsString();
        TypeSpec.Builder resourceType = TypeSpec.classBuilder(type).addModifiers(PUBLIC, STATIC, FINAL);

        for (BodyDeclaration field : node.getMembers()) {
            if (field instanceof FieldDeclaration) {
                FieldDeclaration declaration = (FieldDeclaration) field;
                // Check that the field is an Int because styleable also contains Int arrays which can't be
                // used in annotations.
                if (isInt(declaration)) {
                    addResourceField(resourceType, declaration.getVariables().get(0), type, debug,packageName);
                }
            }
        }

        result.addType(resourceType.build());
    }

    private static boolean isInt(FieldDeclaration field) {
        Type type = field.getCommonType();
        return type instanceof PrimitiveType
                && ((PrimitiveType) type).getType() == PrimitiveType.Primitive.INT;
    }

    private static void addResourceField(TypeSpec.Builder resourceType, VariableDeclarator variable,
                                         String type, boolean debug, String packageName) {
        String fieldName = variable.getNameAsString();
        String fieldValue = variable.getInitializer().map(Node::toString).orElse(null);
        FieldSpec.Builder fieldSpecBuilder = FieldSpec.builder(String.class, fieldName)
                .addModifiers(PUBLIC, STATIC, FINAL)
                .initializer("\"R." + type + "." + fieldName + "\"");

        resourceType.addField(fieldSpecBuilder.build());
        if (debug) {
            FieldSpec.Builder jumpSpecBuilder = FieldSpec.builder(int.class, fieldName + "_jump")
                    .addModifiers(PUBLIC, STATIC, FINAL)
                    .initializer("R." + type + "." + fieldName);
            resourceType.addField(jumpSpecBuilder.build());
        }
    }


    private static String capitalize(String word) {
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }
}