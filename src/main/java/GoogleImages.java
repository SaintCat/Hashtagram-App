
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Java
 */
public class GoogleImages {

    public static void main(String[] args) throws InterruptedException {
        String searchTerm = "s woman";   // term to search for (use spaces to separate terms)
        int offset = 40;                      // we can only 20 results at a time - use this to offset and get more!
        String fileSize = "50mp";             // specify file size in mexapixels (S/M/L not figured out yet)
        String source = null;                 // string to save raw HTML source code

// format spaces in URL to avoid problems
        searchTerm = searchTerm.replaceAll(" ", "%20");

// get Google image search HTML source code; mostly built from PhyloWidget example:
// http://code.google.com/p/phylowidget/source/browse/trunk/PhyloWidget/src/org/phylowidget/render/images/ImageSearcher.java
        int offset2 = 0;
        Set urlsss = new HashSet<String>();
        while (offset2 < 600) {
            try {
                URL query = new URL("https://www.google.ru/search?start=" + offset2 + "&q=angry+woman&newwindow=1&client=opera&hs=bPE&source=lnms&tbm=isch&sa=X&ved=0ahUKEwiAgcKozIfNAhWoHJoKHSb_AUoQ_AUIBygB&biw=1517&bih=731&dpr=0.9#imgrc=G_1tH3YOPcc8KM%3A");
                HttpURLConnection urlc = (HttpURLConnection) query.openConnection();                                // start connection...
                urlc.setInstanceFollowRedirects(true);
                urlc.setRequestProperty("User-Agent", "");
                urlc.connect();
                BufferedReader in = new BufferedReader(new InputStreamReader(urlc.getInputStream()));               // stream in HTTP source to file
                StringBuffer response = new StringBuffer();
                char[] buffer = new char[1024];
                while (true) {
                    int charsRead = in.read(buffer);
                    if (charsRead == -1) {
                        break;
                    }
                    response.append(buffer, 0, charsRead);
                }
                in.close();                                                                                          // close input stream (also closes network connection)
                source = response.toString();
            } // any problems connecting? let us know
            catch (Exception e) {
                e.printStackTrace();
            }

// print full source code (for debugging)
// println(source);
// extract image URLs only, starting with 'imgurl'
            if (source != null) {
//                System.out.println(source);
                int c = StringUtils.countMatches(source, "http://www.vmir.su");
                System.out.println(c);
                int index = source.indexOf("src=");
                System.out.println(source.subSequence(index, index + 200));
                while (index >= 0) {
                    System.out.println(index);
                    index = source.indexOf("src=", index + 1);
                    if (index == -1) {
                        break;
                    }
                    String rr = source.substring(index, index + 200 > source.length() ? source.length() : index + 200);

                    if (rr.contains("\"")) {
                        rr = rr.substring(5, rr.indexOf("\"", 5));
                    }
                    System.out.println(rr);
                    urlsss.add(rr);
                }
            }
            offset2 += 20;
            Thread.sleep(1000);
            System.out.println("off set = " + offset2);
                    
        }

        System.out.println(urlsss);
        urlsss.forEach(new Consumer<String>() {

            public void accept(String s) {
                try {
                    saveImage(s, "C:\\Users\\Java\\Desktop\\ang\\" + UUID.randomUUID().toString() + ".jpg");
                } catch (IOException ex) {
                    Logger.getLogger(GoogleImages.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
//            String[][] m = matchAll(source, "img height=\"\\d+\" src=\"([^\"]+)\"");

        // older regex, no longer working but left for posterity
        // built partially from: http://www.mkyong.com/regular-expressions/how-to-validate-image-file-extension-with-regular-expression
        // String[][] m = matchAll(source, "imgurl=(.*?\\.(?i)(jpg|jpeg|png|gif|bmp|tif|tiff))");    // (?i) means case-insensitive
//            for (int i = 0; i < m.length; i++) {                                                          // iterate all results of the match
//                println(i + ":\t" + m[i][1]);                                                         // print (or store them)**
//            }
    }

    private static void parseJson(JSONObject json) throws JSONException {
        System.out.println("ASd");
        JSONObject response_data = json.getJSONObject("responseData");
        JSONArray results = response_data.getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = (JSONObject) results.get(i);
            System.out.println(result);
            //now you can grab data from the result using:
            //result.getInt(key);
            //result.getString(key);
            //etc
        }
    }

    public static void saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }
}
