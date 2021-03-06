package ks3.pmf.view.swing;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JPanel;

import ks3.pmf.boundary.ImageItem;
import ks3.pmf.boundary.ImagePanel;
import ks3.pmf.data.ImageFile;

public class SwingImagePanel implements ImagePanel<Component, Image> {

    private class ResizeListener extends ComponentAdapter {
        @Override
        public void componentResized(ComponentEvent ev) {
            if (panel.getComponentCount() > 0) {
                updateColumnCount();
            }
        }
    }

    private final JPanel panel;
    private final Component component;
    private final List<SwingImageItem> imageList = new ArrayList<>();

    private boolean needToSyncImages = false;

    public SwingImagePanel() {
        this(new JPanel(new GridLayout(0, 2, 5, 5)));
    }

    protected SwingImagePanel(JPanel panel) {
        this.panel = panel;
        panel.setDoubleBuffered(true);
        component = SwingUtils.wrapInScrollPane(panel, new ResizeListener());
    }

    @Override
    public Component getComponent() {
        return component;
    }

    @Override
    public void addAllImages(List<ImageFile<Image>> imageFiles) {
        needToSyncImages = true;
        imageList.addAll(imageFiles.stream().map(SwingImageItem::new).collect(Collectors.toList()));
    }

    @Override
    public List<ImageItem<Component>> getImageList() {
        return Collections.unmodifiableList(imageList);
    }

    @Override
    public void refreshImageDisplay() {
        if (needToSyncImages) {
            prepareImagesForDisplay();
        }

        panel.revalidate();
        panel.repaint();
    }

    private void prepareImagesForDisplay() {
        panel.removeAll();
        imageList.stream().forEachOrdered(image -> panel.add(image.getComponent()));
        needToSyncImages = false;
    }

    protected void updateColumnCount() {
        int newColCount = calculateOptimalColumnCount(component.getWidth() - 35);
        GridLayout imgLayout = (GridLayout) panel.getLayout();
        if (imgLayout.getColumns() != newColCount) {
            imgLayout.setColumns(newColCount);
            refreshImageDisplay();
        }
    }

    protected int calculateOptimalColumnCount(int newWidth) {
        int itemWidth = panel.getComponent(0).getWidth();
        int colCount = Math.floorDiv(newWidth, itemWidth);
        return (colCount < 1) ? 1 : colCount;
    }

    protected JPanel getPanel() {
        return panel;
    }

}
