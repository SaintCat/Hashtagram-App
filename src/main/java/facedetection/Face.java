/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facedetection;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Java
 */
public class Face {

    @SerializedName("age")
    @Expose
    private Age age;
    @SerializedName("face_location")
    @Expose
    private FaceLocation faceLocation;
    @SerializedName("gender")
    @Expose
    private Gender gender;

    /**
     *
     * @return The age
     */
    public Age getAge() {
        return age;
    }

    /**
     *
     * @param age The age
     */
    public void setAge(Age age) {
        this.age = age;
    }

    /**
     *
     * @return The faceLocation
     */
    public FaceLocation getFaceLocation() {
        return faceLocation;
    }

    /**
     *
     * @param faceLocation The face_location
     */
    public void setFaceLocation(FaceLocation faceLocation) {
        this.faceLocation = faceLocation;
    }

    /**
     *
     * @return The gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     *
     * @param gender The gender
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
