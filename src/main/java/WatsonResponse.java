
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Java
 */
public class WatsonResponse {

    @SerializedName("classifier_id")
    @Expose
    private String classifierId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("owner")
    @Expose
    private String owner;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("classes")
    @Expose
    private List<Classifier> classes = new ArrayList<Classifier>();

    /**
     *
     * @return The classifierId
     */
    public String getClassifierId() {
        return classifierId;
    }

    /**
     *
     * @param classifierId The classifier_id
     */
    public void setClassifierId(String classifierId) {
        this.classifierId = classifierId;
    }

    /**
     *
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return The owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     *
     * @param owner The owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     *
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return The created
     */
    public String getCreated() {
        return created;
    }

    /**
     *
     * @param created The created
     */
    public void setCreated(String created) {
        this.created = created;
    }

    /**
     *
     * @return The classes
     */
    public List<Classifier> getClasses() {
        return classes;
    }

    /**
     *
     * @param classes The classes
     */
    public void setClasses(List<Classifier> classes) {
        this.classes = classes;
    }

    @Override
    public String toString() {
        return "WatsonResponse{" + "classifierId=" + classifierId + ", name=" + name + ", owner=" + owner + ", status=" + status + ", created=" + created + ", classes=" + classes + '}';
    }
    
    
}
