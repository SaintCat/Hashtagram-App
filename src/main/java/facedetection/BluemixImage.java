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
public class BluemixImage {

    @SerializedName("faces")
    @Expose
    private List<Face> faces = new ArrayList<Face>();
    @SerializedName("image")
    @Expose
    private String image;

    /**
     *
     * @return The faces
     */
    public List<Face> getFaces() {
        return faces;
    }

    /**
     *
     * @param faces The faces
     */
    public void setFaces(List<Face> faces) {
        this.faces = faces;
    }

    /**
     *
     * @return The image
     */
    public String getImage() {
        return image;
    }

    /**
     *
     * @param image The image
     */
    public void setImage(String image) {
        this.image = image;
    }
}
