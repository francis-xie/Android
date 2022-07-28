
package com.basic.web.widget.indicator;

/**
 
 * @since 1.0.0
 */
public interface BaseIndicatorSpec {
    /**
     * indicator
     */
    void show();

    void hide();

    void reset();

    void setProgress(int newProgress);

}
