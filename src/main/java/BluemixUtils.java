
import com.google.gson.Gson;
import facedetection.Face;
import facedetection.FaceDetection;
import facedetection.FaceLocation;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Java
 */
public class BluemixUtils {

    private static String TMP_DIR = "C:\\Users\\Java\\Desktop\\tmp";
    private static FilenameFilter filter = new GenericExtFilter("jpg");

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("It is worked");
        ClassifierUnit happy = new ClassifierUnit("happy", "D:\\datasets\\all\\happy_good_faces");
        ClassifierUnit angry = new ClassifierUnit("angry", "D:\\datasets\\all\\angry_good_faces");
        ClassifierUnit sad = new ClassifierUnit("sad", "D:\\datasets\\all\\sad_good_faces");
//        ClassifierUnit negative = new ClassifierUnit("negat", "C:\\Users\\Java\\Desktop\\Новая папка\\not");
        String classifierId = classify(happy, angry, sad);
        System.out.println(classifierId);
//        retriveFaces(happy);
    }

    public static void retriveFaces(ClassifierUnit unit) throws IOException {
        //curl -X POST -F "images_file=@prez.jpg" "https://gateway-a.watsonplatform.net/visual-recognition/api/v3/detect_faces?api_key={api-key}&version=2016-05-20"
        File facesDir = new File(unit.getFolderWithImages() + "_faces");
        facesDir.mkdir();
        for (File image : new File(unit.getFolderWithImages()).listFiles()) {
            try {
                String command = "D:/curl/curl -X POST -F \"images_file=@" + image.getAbsolutePath() + "\" \"https://gateway-a.watsonplatform.net/visual-recognition/api/v3/detect_faces?api_key={02a6297b759128a71e59e1a97c682826398584b4}&version=2016-05-20\"";
                String res = executeCmdCommand(command);
                FaceDetection s = new Gson().fromJson(res, FaceDetection.class);
                System.out.println(s.getBluemixImages().get(0).getFaces());
                BufferedImage in = ImageIO.read(image);
                for (Face face : s.getBluemixImages().get(0).getFaces()) {
                    FaceLocation faceLoc = face.getFaceLocation();
                    BufferedImage newIm = in.getSubimage(faceLoc.getLeft(), faceLoc.getTop(), faceLoc.getWidth(), faceLoc.getHeight());
                    File outputfile = new File(facesDir + File.separator + image.getName());
                    ImageIO.write(newIm, "jpg", outputfile);
                }
            } catch (Exception ex) {
                Logger.getLogger("KrilovUtils.class").fine(ex.getMessage());
            }
        }

    }

    public static String classify(ClassifierUnit... positive) throws IOException, InterruptedException {
        List<ClassifierUnit> all = new ArrayList<>(Arrays.asList(positive));
//        all.add(negative);
        int minSize = findMinSize(all);
        showPhotoCountInDirs(all);
        System.out.println("Min size = " + minSize);
        clearTempDir();
        copyPhotosInTempDir(all, minSize);
        createZipArchives(all);
        String command = generateCommandForClassify(all);
        String result = executeCmdCommand(command);
        System.out.println(result);
        WatsonResponse resonse = new Gson().fromJson(result, WatsonResponse.class);
        System.out.println(resonse.toString());
        String status = resonse.getStatus();
        do {
            TimeUnit.SECONDS.sleep(5);
            String statusCommand = generateClassifierStatusCommand(resonse.getClassifierId());
//        String statusCommand = generateClassifierStatusCommand("emotions_1479682895");

            status = executeCmdCommand(statusCommand);
            System.out.println("Execute status command, res = " + status);
            if (status.contains("ready") || status.contains("failed")) {
                System.out.println("Training completed");
                break;
            }
        } while (status.contains("training"));
        return resonse.getClassifierId();
    }

    private static int findMinSize(List<ClassifierUnit> all) {
//        return 270;
        return all.stream().map((ClassifierUnit s) -> s.getFolderWithImages())
                .map((String s) -> new File(s).listFiles(filter).length).min(Integer::compare).get();
    }

    private static void showPhotoCountInDirs(List<ClassifierUnit> all) {
        all.stream().map((ClassifierUnit s) -> s.getFolderWithImages())
                .forEach((s) -> System.out.print(s + " " + new File(s).listFiles(filter).length + "\n"));
    }

    private static void clearTempDir() throws IOException {
        if (!new File(TMP_DIR).exists()) {
            System.out.println("Create temp directory");
            new File(TMP_DIR).mkdir();
        }
        System.out.println("Clear tmp dir");
        FileUtils.cleanDirectory(new File(TMP_DIR));
    }

    private static void copyPhotosInTempDir(List<ClassifierUnit> all, int minSize) throws IOException {
        for (ClassifierUnit unit : all) {
            File dir = new File(TMP_DIR + File.separator + unit.getName());
            dir.mkdir();
            System.out.println("Copying files to " + dir.getAbsolutePath());
            File photosDir = new File(unit.getFolderWithImages());
            File[] photoes = photosDir.listFiles(filter);
            for (int i = 0; i < minSize; i++) {
                File source = photoes[i];
                System.out.println("Copying file " + photoes[i].getName());
                Files.copy(source.toPath(),
                        (new File(dir.getAbsolutePath() + File.separator + (i + 1)
                                + source.getName().substring(source.getName().indexOf(".")))).toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
            }

        }
    }

    private static void createZipArchives(List<ClassifierUnit> all) throws IOException {
        for (ClassifierUnit unit : all) {
            File dist;
            ZipUtils.zipFolder(new File(TMP_DIR + File.separator + unit.getName()),
                    dist = new File(TMP_DIR + File.separator + unit.getName() + ".zip"));
            System.out.println("Create zip archive " + dist.getAbsolutePath());
            zipArchivesFiles.add(dist.getAbsolutePath());
        }
    }
    private static List<String> zipArchivesFiles = new ArrayList<>();

    private static String generateCommandForClassify(List<ClassifierUnit> all) {
//        curl -X POST -F "angry_positive_examples=@tmp/angry.zip" -
        //F "happy_positive_examples=@tmp/happy.zip" -F "sad_positive_examples=@tmp/sad.zi
        //p" -F "negative_examples=@tmp/negat.zip" -F "name=emotions" "https://gateway-a.w
        //atsonplatform.net/visual-recognition/api/v3/classifiers?api_key={02a6297b759128a
        //71e59e1a97c682826398584b4}&version=2016-05-20"
        StringBuilder sb = new StringBuilder();
        sb.append("D:\\curl\\curl ");
        System.out.println("All size = " + all.size());
        for (int i = 0; i < all.size(); i++) {
            ClassifierUnit unit = all.get(i);
            sb.append("-F ").append(unit.getName()).append("_positive_examples=@").append(zipArchivesFiles.get(i)).append(" ");
        }
//        sb.append("-F negative_examples=@").append(zipArchivesFiles.get(zipArchivesFiles.size() - 1)).append(" ");
        sb.append("-F \"name=emotions\" \"https://gateway-a.watsonplatform.net/visual-recognition/api/v3/classifiers?api_key={02a6297b759128a71e59e1a97c682826398584b4}&version=2016-05-20\"");
        return sb.toString();
    }

    private static String generateClassifierStatusCommand(String id) {
//        curl -X GET "https://gateway-a.watsonplatform.net/visual-r
//ecognition/api/v3/classifiers/{emotions_1027142399}?api_key={02a6297b759128a71e5
//9e1a97c682826398584b4}&version=2016-05-20"
        return "D:\\curl\\curl -X GET \"https://gateway-a.watsonplatform.net/visual-recognition/api/v3/classifiers/"
                + "{" + id + "}"
                + "?api_key={02a6297b759128a71e59e1a97c682826398584b4}&version=2016-05-20\"";
    }

    private static String executeCmdCommand(String command) throws IOException {
        System.out.println("Execute " + command);
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", command);
//        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        StringBuilder result = new StringBuilder();
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) {
                break;
            }
            result.append(line);
            System.out.println(line);
        }
        return result.toString();

    }

    public static class ClassifierUnit {

        private String name;
        private String folderWithImages;

        public ClassifierUnit(String name, String folderWithImages) {
            this.name = name;
            this.folderWithImages = folderWithImages;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFolderWithImages() {
            return folderWithImages;
        }

        public void setFolderWithImages(String folderWithImages) {
            this.folderWithImages = folderWithImages;
        }

    }

    public static class GenericExtFilter implements FilenameFilter {

        private String ext;

        public GenericExtFilter(String ext) {
            this.ext = ext;
        }

        public boolean accept(File dir, String name) {
            return (name.endsWith(ext));
        }
    }
}
