package fastLibrary;

import android.graphics.Bitmap;

/**
 * Created by root on 3/14/2016.
 */
public class ImageItem {
    private Bitmap image;
    private String title;
    private String file_name ;

    public ImageItem(Bitmap image, String title,String file_name) {
        super();
        this.image = image;
        this.title = title;
        this.file_name = file_name ;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getFileName () {return file_name; }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
