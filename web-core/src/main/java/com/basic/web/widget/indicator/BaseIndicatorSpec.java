
package com.basic.web.widget.indicator;

public interface BaseIndicatorSpec {
    /**
     * indicator
     */
    void show();

    void hide();

    void reset();

    void setProgress(int newProgress);

}
