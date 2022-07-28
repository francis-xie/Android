
package com.basic.face.widget.imageview.edit;

/**
 * 画板状态变化监听
 */
interface BrushViewChangeListener {
    void onViewAdd(BrushDrawingView brushDrawingView);

    void onViewRemoved(BrushDrawingView brushDrawingView);

    void onStartDrawing();

    void onStopDrawing();
}
