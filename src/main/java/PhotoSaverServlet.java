
import java.io.IOException;
import java.util.Collection;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.backend.TaggedUserService;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import java.util.ArrayList;
import java.util.Arrays;
import org.vaadin.backend.domain.Tag;
import org.vaadin.backend.domain.TagUser;

/**
 * Servlet implementation class PhotoSaverServlet
 */
@WebServlet("/tagMe")
@MultipartConfig
public class PhotoSaverServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Inject
    private TaggedUserService service;
    GeometryFactory factory = new GeometryFactory();

    private Logger log = LoggerFactory.getLogger(PhotoSaverServlet.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PhotoSaverServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        Collection<Part> parts = request.getParts();

        System.out.println("TEST");
        String operation = request.getParameter("operation");
        if (operation.equals("create")) {
            Part image = request.getPart("imagefile");
            System.out.println(image.getSize());
            String lat = request.getParameter("lat");
            String lon = request.getParameter("lon");
            String name = request.getParameter("name");
            String tags = request.getParameter("tags");
            System.out.println("Name is " + name + " in location " + lat + " " + lon);

            TagUser user = new TagUser();
            user.setLocation(factory.createPoint(new Coordinate(Double.valueOf(lon), Double.valueOf(lat))));
            user.setName(name);
            user.setPhoto(IOUtils.toByteArray(image.getInputStream()));
            user.setTags(new ArrayList<Tag>());
            service.save(user, Arrays.asList(tags.split(";")));
            System.out.println("YES");
            response.getWriter().append("Served at: ").append(request.getContextPath()).append(lat).append(lon).append(name);
        } else {
            for(Tag user : service.getAllTags()) {
                service.deleteEntity(user);
            }
            for(TagUser user : service.findAll()) {
                service.deleteEntity(user);
            }
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
