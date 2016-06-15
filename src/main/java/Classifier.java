
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Java
 */
public class Classifier {

    @SerializedName("class")
    @Expose
    private String _class;

    /**
     *
     * @return The _class
     */
    public String getClass_() {
        return _class;
    }

    /**
     *
     * @param _class The class
     */
    public void setClass_(String _class) {
        this._class = _class;
    }

}
