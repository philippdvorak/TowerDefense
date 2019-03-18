package com.github.dddisch.towerdefense.utils.imageloader;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Provides a centralized place for safely loading images. This ImageLoader implementation is completely thread-safe. It features caching and lazy loading of Images.
 */
public class ImageLoader {

    private static Map<String, File> files = new ConcurrentHashMap<>();
    private static Map<String, Image> imageCache = new ConcurrentHashMap<>();

    static {
        File imageDirectory = new File("./res/img");
        addFiles(getFiles(imageDirectory), imageDirectory);
    }

    private static void addFiles(Stream<File> files, File base){
        files.forEach(
                f -> {
                    String name = base.toPath().relativize(f.toPath()).toString().replaceFirst("[.][^.]+$", "").replaceAll("[ /\\\\]", "::");
                    System.out.print("Registering " + f.getAbsolutePath() + " as " + name);

                    if(f.getName().replaceFirst("[.][^.]+$", "").equals(f.getParentFile().getName())){
                        String shorthand = base.toPath().relativize(f.toPath().getParent()).toString().replaceFirst("[.][^.]+$", "").replaceAll("[ /\\\\]", "::") + "::";
                        System.out.print(" and " + shorthand);
                        ImageLoader.files.put(shorthand, f);
                    }
                    ImageLoader.files.put(name, f);
                    System.out.println();
                }
        );
    }

    private static Stream<File> getFolderContent(File dir)
    {
        return Arrays.stream(Objects.requireNonNull(dir.listFiles()));
    }

    private static Stream<File> getFiles(File dir){
        return Stream.concat(
                getFolderContent(dir)
                        .filter(File::isDirectory)
                        .flatMap(ImageLoader::getFiles),
                getFolderContent(dir)
                        .filter(f -> !f.isDirectory())
                        .filter(f -> f.getName().contains(".jpg") || f.getName().contains(".png"))
        );
    }


    public static Image loadImage(String name){
        try {
            if(!files.containsKey(name))
            {
                throw new ImageIdentifierNotExisting();
            } else {
                if(!imageCache.containsKey(name))
                {
                    imageCache.put(name, new Image(new FileInputStream(files.get(name))));
                }
                return imageCache.get(name);
            }
        } catch (FileNotFoundException e) {
            throw new MissingImageResource("The image resource " + files.get(name) + " doesn't exist or access to it is prohibited", e);
        }
    }

    public static ImageView loadImageView(String name){
        return new ImageView(loadImage(name));
    }

}
