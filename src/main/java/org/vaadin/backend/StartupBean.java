/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.backend;

import com.ibm.watson.developer_cloud.service.NotFoundException;
import com.ibm.watson.developer_cloud.visual_recognition.v2.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v2.model.VisualClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v2.model.VisualClassifier;
import java.io.File;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Startup;
import javax.inject.Singleton;

/**
 *
 * @author Java
 */
@Singleton
@Startup
public class StartupBean {

    @PostConstruct
    private void startup() {
        System.out.println("Startup");
        VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2015_12_02);
//        service.setApiKey("3dfe9ebb2395a63ac70ac79e17bac0f3914129a8");
        service.setApiKey("02a6297b759128a71e59e1a97c682826398584b4");
//        service.setUsernameAndPassword("chernyshev", "02a6297b759128a71e59e1a97c682826398584b4");
        File positiveImages = new File("src/test/resources/visual_recognition/positive.zip");
        File negativeImages = new File("src/test/resources/visual_recognition/negative.zip");
        File image = new File("src/test/resources/visual_recognition/car.png");

        VisualClassifier newClass = service.createClassifier("foo", positiveImages, negativeImages);
        try {
            newClass = service.getClassifier(newClass.getId());
            VisualClassification result = service.classify(image, newClass);
            System.out.println(result);
        } finally {
            // FIXME: deleteClassifier is returning 404 but the classifier is deleted
            try {
                service.deleteClassifier(newClass.getId());
            } catch (NotFoundException e) {
            }
        }
    }

}
