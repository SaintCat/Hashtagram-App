package org.vaadin.backend;

import com.google.gson.Gson;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

import org.apache.commons.io.FileUtils;

import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyVision;
import com.ibm.watson.developer_cloud.alchemy.v1.model.ImageKeyword;
import com.ibm.watson.developer_cloud.alchemy.v1.model.ImageKeywords;
import com.ibm.watson.developer_cloud.service.NotFoundException;
import com.ibm.watson.developer_cloud.visual_recognition.v2.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v2.model.VisualClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v2.model.VisualClassifier;
import facedetection.Face;
import facedetection.FaceDetection;
import facedetection.FaceLocation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.persistence.Query;
import org.vaadin.backend.domain.Tag;
import org.vaadin.backend.domain.TagUser;

@Stateless
public class TaggedUserService {

    @PersistenceContext(unitName = "user2-pu")
    private EntityManager entityManager;
    private static String EMOTIONS_CLASSIFIER_ID = "emotions_171438814";
    
    public Collection<Tag> getAllTags() {
        System.out.println("getAllTags() called");
        Query query = entityManager.createQuery("SELECT e FROM Tag e");
        return (Collection<Tag>) query.getResultList();
    }

    public void save(TagUser entity, List<String> tags) throws IOException {
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(entity.getPhoto()));
        //100 200
        int scaledH = 140;
        int originH = img.getHeight();
        double scaleFactor = (double) originH / scaledH;//0.7
        int scaledW = (int) (img.getWidth() / scaleFactor);
        BufferedImage resized = createResizedCopy(img, scaledW, scaledH, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resized, "jpg", baos);
        entity.setPhoto(entity.getPhoto());
        System.out.println("setTags() end");
        entity.setTags(tags.stream().map((String s) -> new Tag(s)).collect(Collectors.toList()));
        if (entity.getId() > 0) {
            entityManager.merge(entity);
        } else {
            List<Tag> tNew = new ArrayList<>();
            for (Tag tag : entity.getTags()) {
                Tag t;
                if ((t = entityManager.find(Tag.class, tag.getName())) != null) {
                    tNew.add(t);
                } else {
                    tNew.add(tag);
                }
            }
            entity.setTags(tNew);
            entityManager.persist(entity);
        }
    }

    public TagUser saveOrPersist(TagUser entity) {
        setTags(entity);

        if (entity.getId() > 0) {
            entityManager.merge(entity);
        } else {
            List<Tag> tNew = new ArrayList<>();
            for (Tag tag : entity.getTags()) {
                Tag t;
                if ((t = entityManager.find(Tag.class, tag.getName())) != null) {
                    tNew.add(t);
                } else {
                    tNew.add(tag);
                }
            }
            entity.setTags(tNew);
            entityManager.persist(entity);
        }
        return entity;
    }

    public void deleteEntity(TagUser entity) {
        if (entity.getId() > 0) {
            // reattach to remove

            entity = entityManager.merge(entity);
            entityManager.remove(entity);
        }
    }

    public void deleteEntity(Tag entity) {
        entity = entityManager.merge(entity);
        entityManager.remove(entity);
    }

    public List<TagUser> findAll() {
        System.out.println("findAll");
        CriteriaQuery<TagUser> cq = entityManager.getCriteriaBuilder().createQuery(TagUser.class);
        cq.select(cq.from(TagUser.class));
        List<TagUser> res = entityManager.createQuery(cq).getResultList();
        System.out.println(res.toString());
        return res;
    }

    public List<String> getTags(TagUser user) {
        try {
            user.setTags(new ArrayList<Tag>());
            System.out.println("Set start tags");
            String finalRes = "";
            AlchemyVision service = new AlchemyVision();
            service.setApiKey("3dfe9ebb2395a63ac70ac79e17bac0f3914129a8");

            File temp = File.createTempFile("temp-file-name", ".tmp");

            FileUtils.writeByteArrayToFile(temp, user.getPhoto());
            String res = executeCmdCommand(generateCommand(user.getPhoto));
            FaceDetection s = new Gson().fromJson(res, FaceDetection.class);
            System.out.println(s.getBluemixImages().get(0).getFaces());
             List<String> faceEmotions;
            for (Face face : s.getBluemixImages().get(0).getFaces()) {
                FaceLocation faceLoc = face.getFaceLocation();
                BufferedImage newIm = in.getSubimage(faceLoc.getLeft(), faceLoc.getTop(), faceLoc.getWidth(), faceLoc.getHeight());
                File outputfile = new File(facesDir + File.separator + image.getName());
                ImageIO.write(newIm, "jpg", outputfile);
                faceEmotions = classifyPhoto(newIm, "default", EMOTIONS_CLASSIFIER_ID);
            }
            return faceEmotions;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    BufferedImage createResizedCopy(Image originalImage, int scaledWidth, int scaledHeight, boolean preserveAlpha) {
        System.out.println("resizing...");
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledBI;
    }

}
