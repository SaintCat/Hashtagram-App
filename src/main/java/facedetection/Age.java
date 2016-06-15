/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facedetection;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Age {

    @SerializedName("max")
    @Expose
    private Integer max;
    @SerializedName("min")
    @Expose
    private Integer min;
    @SerializedName("score")
    @Expose
    private Double score;

    /**
     *
     * @return The max
     */
    public Integer getMax() {
        return max;
    }

    /**
     *
     * @param max The max
     */
    public void setMax(Integer max) {
        this.max = max;
    }

    /**
     *
     * @return The min
     */
    public Integer getMin() {
        return min;
    }

    /**
     *
     * @param min The min
     */
    public void setMin(Integer min) {
        this.min = min;
    }

    /**
     *
     * @return The score
     */
    public Double getScore() {
        return score;
    }

    /**
     *
     * @param score The score
     */
    public void setScore(Double score) {
        this.score = score;
    }

}
