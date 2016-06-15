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
public class Gender {

    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("score")
    @Expose
    private Double score;

    /**
     *
     * @return The gender
     */
    public String getGender() {
        return gender;
    }

    /**
     *
     * @param gender The gender
     */
    public void setGender(String gender) {
        this.gender = gender;
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
