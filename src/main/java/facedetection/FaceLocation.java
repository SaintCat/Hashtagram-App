/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facedetection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Java
 */
public class FaceLocation {

    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("left")
    @Expose
    private Integer left;
    @SerializedName("top")
    @Expose
    private Integer top;
    @SerializedName("width")
    @Expose
    private Integer width;

    /**
     *
     * @return The height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     *
     * @param height The height
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     *
     * @return The left
     */
    public Integer getLeft() {
        return left;
    }

    /**
     *
     * @param left The left
     */
    public void setLeft(Integer left) {
        this.left = left;
    }

    /**
     *
     * @return The top
     */
    public Integer getTop() {
        return top;
    }

    /**
     *
     * @param top The top
     */
    public void setTop(Integer top) {
        this.top = top;
    }

    /**
     *
     * @return The width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     *
     * @param width The width
     */
    public void setWidth(Integer width) {
        this.width = width;
    }
}
