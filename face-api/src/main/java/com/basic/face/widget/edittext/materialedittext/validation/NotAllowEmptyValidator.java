
package com.basic.face.widget.edittext.materialedittext.validation;

import androidx.annotation.NonNull;

/**
 * 非空检验
 *

 * @since 2019/5/14 10:27
 */
public class NotAllowEmptyValidator extends METValidator {

    public NotAllowEmptyValidator(@NonNull String errorMessage) {
        super(errorMessage);
    }

    @Override
    public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
        return !isEmpty;
    }
}
