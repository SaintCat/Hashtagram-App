/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facedetection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Java
 */
public class FaceDetection {

    @SerializedName("images")
    @Expose
    private List<BluemixImage> images = new ArrayList<BluemixImage>();
    @SerializedName("images_processed")
    @Expose
    private Integer imagesProcessed;

    /**
     *
     * @return The images
     */
    public List<BluemixImage> getBluemixImages() {
        return images;
    }

    /**
     *
     * @param images The images
     */
    public void setBluemixImages(List<BluemixImage> images) {
        this.images = images;
    }

    /**
     *
     * @return The imagesProcessed
     */
    public Integer getBluemixImagesProcessed() {
        return imagesProcessed;
    }

    /**
     *
     * @param imagesProcessed The images_processed
     */
    public void setBluemixImagesProcessed(Integer imagesProcessed) {
        this.imagesProcessed = imagesProcessed;
    }
}
