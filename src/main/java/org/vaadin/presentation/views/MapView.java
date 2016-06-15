package org.vaadin.presentation.views;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletClickListener;
import org.vaadin.addon.leaflet.control.LZoom;
import org.vaadin.addon.leaflet.shared.ControlPosition;
import org.vaadin.backend.TaggedUserService;
import org.vaadin.cdiviewmenu.ViewMenuItem;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.cdi.CDIView;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Notification;
import java.util.ArrayList;
import java.util.List;
import org.vaadin.backend.domain.Tag;
import org.vaadin.backend.domain.TagUser;
import org.vaadin.tokenfield.TokenField;

@CDIView(value = "My city map")
@ViewMenuItem(icon = FontAwesome.GLOBE, order = 2)
public class MapView extends MVerticalLayout implements View {

    //@Inject
    //CustomerService service;
    @Inject
    TaggedUserService userService;

    LMap worldMap = new LMap();
    private TokenField f;

    @PostConstruct
    void init() {

        add(new Header("My city map").setHeaderLevel(2));

        BeanItemContainer<String> beans
                = new BeanItemContainer<>(String.class);
        for (Tag t : userService.getAllTags()) {
            beans.addBean(t.getName());
        }
        f = new TokenField("Add tags to filter") {

            @Override
            protected void onTokenInput(Object tokenId) {
                super.onTokenInput(tokenId); //To change body of generated methods, choose Tools | Templates.
                selected.add((String) tokenId);
            }

            @Override
            protected void onTokenDelete(Object tokenId) {
                super.onTokenDelete(tokenId); //To change body of generated methods, choose Tools | Templates.
                selected.remove((String) tokenId);
            }

        };
        f.setNewTokensAllowed(false);
        f.setFilteringMode(FilteringMode.CONTAINS);
        f.setContainerDataSource(beans);
        f.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                System.out.println("YEAH");
                Object tags;
                System.out.println(tags = f.getValue());

                System.out.println(f.getInputPrompt());
                for (LMarker marker : markers) {
                    worldMap.removeComponent(marker);
                }
                markers.clear();
                initMarkers(tags == null ? "" : tags.toString());
            }
        });
        addComponent(f);

        expand(worldMap);
        setMargin(new MarginInfo(false, true, true, true));

        LZoom zoom = new LZoom();
        zoom.setPosition(ControlPosition.topright);
        worldMap.addControl(zoom);
    }

    private List<LMarker> markers = new ArrayList<>();
    private List<String> selected = new ArrayList<>();

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

        worldMap.removeAllComponents();
        LOpenStreetMapLayer osm = new LOpenStreetMapLayer();
        osm.setDetectRetina(true);
        worldMap.addComponent(osm);
        System.out.println("Get users");
        initMarkers("");
        worldMap.zoomToContent();
    }

    private void initMarkers(String tags) {
        for (final TagUser customer : userService.findAll()) {
            System.out.println("customer " + customer);
            System.out.println("Field " + f);
            System.out.println("Field tags " + f.getTokenIds());
//            String tags = f.getInputPrompt();
            System.out.println("SELECTED " + tags);

            if (!(tags == null) && !tags.isEmpty() && !tags.equals("[]")) {
                System.out.println("Tags inserted " + tags);
                System.out.println("Photo tag " + customer.getTags());
                boolean contains = false;
                for (Tag t : customer.getTags()) {
                    if (tags.contains(t.getName())) {
                        contains = true;
                        break;
                    }
                }
                if (!contains) {
                    System.out.println("User " + customer + " ne ponhodit");
                    continue;
                }
            }
            System.out.println("User " + customer.getName());
            if (customer.getLocation() != null) {
                LMarker marker = new LMarker(customer.getLocation());
                markers.add(marker);
                marker.addClickListener(new LeafletClickListener() {
                    @Override
                    public void onClick(LeafletClickEvent event) {
                        List<Tag> tags = customer.getTags();
                        StringBuilder sb = new StringBuilder();
                        for (Tag a : tags) {
                            sb.append(a.getName()).append(" ");
                        }

                        Notification not = new Notification(sb.toString());
                        StreamResource.StreamSource ss = new StreamResource.StreamSource() {

                            /**
                             *
                             */
                            private static final long serialVersionUID = 1L;

                            @Override
                            public InputStream getStream() {
                                return new ByteArrayInputStream(customer.getPhoto());
                            }
                        };

                        Resource res = new StreamResource(ss, "image" + UUID.randomUUID().toString());
                        NewsView.showNotify(res, sb.toString());
                    }
                });
                worldMap.addComponent(marker);
            }
        }
    }
}
