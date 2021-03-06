package ks3.pmf.model.awt;

import java.awt.Image;
import java.io.File;

import ks3.pmf.data.ImageFile;
import ks3.pmf.model.ImageFileBuilder;
import ks3.pmf.model.ImageIconBuilder;

public class AwtImageFileBuilder implements ImageFileBuilder {
    
    public ImageIconBuilder<? extends Image> iconBuilder;

    public AwtImageFileBuilder(ImageIconBuilder<? extends Image> iconBuilder) {
        this.iconBuilder = iconBuilder;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public ImageFile build(File file, int targetWidth, int targetHeight) {
        return new AwtImageFile(iconBuilder.build(file, targetWidth, targetHeight), file.getName());
    }

}
