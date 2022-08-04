package com.basic.router.compiler.util;


import com.basic.router.enums.TypeKind;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static com.basic.router.compiler.util.Consts.BOOLEAN;
import static com.basic.router.compiler.util.Consts.BYTE;
import static com.basic.router.compiler.util.Consts.DOUBEL;
import static com.basic.router.compiler.util.Consts.FLOAT;
import static com.basic.router.compiler.util.Consts.INTEGER;
import static com.basic.router.compiler.util.Consts.LONG;
import static com.basic.router.compiler.util.Consts.PARCELABLE;
import static com.basic.router.compiler.util.Consts.SHORT;
import static com.basic.router.compiler.util.Consts.STRING;

/**
 * 获取真实的java类型工具
 */
public class TypeUtils {

    private Types types;
    private Elements elements;
    private TypeMirror parcelableType;    //PARCELABLE类型

    public TypeUtils(Types types, Elements elements) {
        this.types = types;
        this.elements = elements;

        parcelableType = this.elements.getTypeElement(PARCELABLE).asType();
    }

    /**
     * 计算出真实的java类型工具
     *
     * @param element Raw type
     * @return Type class of java
     */
    public int typeExchange(Element element) {
        TypeMirror typeMirror = element.asType();

        // Primitive
        if (typeMirror.getKind().isPrimitive()) {
            return element.asType().getKind().ordinal();
        }

        switch (typeMirror.toString()) {
            case BYTE:
                return TypeKind.BYTE.ordinal();
            case SHORT:
                return TypeKind.SHORT.ordinal();
            case INTEGER:
                return TypeKind.INT.ordinal();
            case LONG:
                return TypeKind.LONG.ordinal();
            case FLOAT:
                return TypeKind.FLOAT.ordinal();
            case DOUBEL:
                return TypeKind.DOUBLE.ordinal();
            case BOOLEAN:
                return TypeKind.BOOLEAN.ordinal();
            case STRING:
                return TypeKind.STRING.ordinal();
            default:    // Other side, maybe the PARCELABLE or OBJECT.
                if (types.isSubtype(typeMirror, parcelableType)) {  // PARCELABLE
                    return TypeKind.PARCELABLE.ordinal();
                } else {    // For others
                    return TypeKind.OBJECT.ordinal();
                }
        }
    }
}
