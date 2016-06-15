/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.presentation.views;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.vaadin.cdi.CDIView;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.MouseEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.backend.TaggedUserService;
import org.vaadin.backend.domain.Tag;
import org.vaadin.backend.domain.TagUser;
import org.vaadin.cdiviewmenu.ViewMenuItem;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 *
 * @author Java
 */
@CDIView(value = "News")
@ViewMenuItem(icon = FontAwesome.GLOBE, order = 1)
public class NewsView extends MVerticalLayout implements View {

    @Inject
    TaggedUserService service;

    @PostConstruct
    void init() {
        add(new Header("News").setHeaderLevel(2));

        int records = service.findAll().size();
        add(new Label("There are " + records + " new photos."));

        setMargin(new MarginInfo(false, true, true, true));
        setStyleName(ValoTheme.LAYOUT_CARD);
//        BeanItemContainer<Person> people
//                = new BeanItemContainer<>(Person.class);
//
//        people.addBean(new Person("Nicolaus Copernicus", 1473));
//        people.addBean(new Person("Galileo Galilei", 1564));
//        people.addBean(new Person("Johannes Kepler", 1571));
//
//// Generate button caption column
//        GeneratedPropertyContainer gpc
//                = new GeneratedPropertyContainer(people);
//        gpc.addGeneratedProperty("delete",
//                new PropertyValueGenerator<String>() {
//
//                    @Override
//                    public String getValue(Item item, Object itemId,
//                            Object propertyId) {
//                        return "Delete"; // The caption
//                    }
//
//                    @Override
//                    public Class<String> getType() {
//                        return String.class;
//                    }
//                });
//
//// Create a grid
//        Grid grid = new Grid(gpc);
//// Render a button that deletes the data row (item)
//        grid.getColumn("delete")
//                .setRenderer(new ButtonRenderer(e -> // Java 8
//                                grid.getContainerDataSource()
//                                .removeItem(e.getItemId())));
//        add(grid);

//        Grid grid = new Grid();
//        grid.setHeaderVisible(false);
//        grid.addColumn("picture1", Resource.class)
//                .setRenderer(new ImageRenderer());
//        grid.addColumn("picture2", Resource.class)
//                .setRenderer(new ImageRenderer());
//        grid.addColumn("picture3", Resource.class)
//                .setRenderer(new ImageRenderer());
//
//        grid.addRow(new ExternalResource("http://stanlemmens.nl/wp/wp-content/uploads/2014/07/bill-gates-wealthiest-person.jpg"),
//                new ExternalResource("http://stanlemmens.nl/wp/wp-content/uploads/2014/07/bill-gates-wealthiest-person.jpg"),
//                new ExternalResource("http://stanlemmens.nl/wp/wp-content/uploads/2014/07/bill-gates-wealthiest-person.jpg"));
//        grid.addRow(new ExternalResource("http://stanlemmens.nl/wp/wp-content/uploads/2014/07/bill-gates-wealthiest-person.jpg"),
//                new ExternalResource("http://stanlemmens.nl/wp/wp-content/uploads/2014/07/bill-gates-wealthiest-person.jpg"),
//                new ExternalResource("http://stanlemmens.nl/wp/wp-content/uploads/2014/07/bill-gates-wealthiest-person.jpg"));
//        grid.addRow(new ExternalResource("http://stanlemmens.nl/wp/wp-content/uploads/2014/07/bill-gates-wealthiest-person.jpg"),
//                new ExternalResource("http://stanlemmens.nl/wp/wp-content/uploads/2014/07/bill-gates-wealthiest-person.jpg"),
//                new ExternalResource("http://stanlemmens.nl/wp/wp-content/uploads/2014/07/bill-gates-wealthiest-person.jpg"));
////        grid.setStyleName("gridwithpics128px");
////        grid.getColumn("picture1").setWidth(120);
//        grid.setCellStyleGenerator(cell
//                -> cell.getPropertyId().toString().contains("picture")
//                        ? "imagecol" : null);
//        add(grid);
        try {
            IndexedContainer container = createContainer();
            Table table = createTable(container);

            VerticalLayout mainLayout = new VerticalLayout();
            mainLayout.addComponent(table);
            mainLayout.setSizeFull();

            add(mainLayout);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /* Build a model for the table with the data : Lots of ways of doing this;
     Just a quick-and-dirty example */
    private IndexedContainer createContainer() {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("title", String.class, null);
        container.addContainerProperty("url1", Resource.class, null);
        container.addContainerProperty("url2", Resource.class, null);
        container.addContainerProperty("url3", Resource.class, null);
        container.addContainerProperty("tags1", String.class, null);
        container.addContainerProperty("tags2", String.class, null);
        container.addContainerProperty("tags3", String.class, null);
        container.addContainerProperty("height", Integer.class, null);
        container.addContainerProperty("width", Integer.class, null);
        List<TagUser> users = new ArrayList<>(service.findAll());
        Collections.reverse(users);
        for (List<TagUser> partition : Lists.partition(users, 3)) {
            // do something with partition
            TagUser first = partition.get(0), second = null, third = null;
            Resource firstR = getRes(first), secondR = null, thirdR = null;
            if (partition.size() > 1) {
                second = partition.get(1);
                secondR = getRes(second);
            }
            if (partition.size() > 2) {
                third = partition.get(2);
                thirdR = getRes(third);
            }
            System.out.println(firstR + " " + secondR + " " + thirdR);
            addItem(container, "Sami Viitanen",
                    firstR, secondR, thirdR, 121, 134,
                    getLabel(first), 
                    getLabel(second),
                    getLabel(third));

        }
        return container;
    }

    private String getLabel(TagUser user) {
        if (user == null) {
            return "";
        }
        List<Tag> tags = user.getTags();
        StringBuilder sb = new StringBuilder();
        for (Tag a : tags) {
            sb.append(a.getName()).append(" ");
        }
        return sb.toString();
    }

    private Resource getRes(TagUser customer) {
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
        return res;
    }

    private void addItem(IndexedContainer container, String title, Resource url1, Resource url2, Resource url3,
            int height, int width, String title1, String title2, String title3) {
        Object itemId = container.addItem();
        container.getItem(itemId).getItemProperty("title").setValue(title);
        container.getItem(itemId).getItemProperty("url1").setValue(url1);
        container.getItem(itemId).getItemProperty("url2").setValue(url2);
        container.getItem(itemId).getItemProperty("url3").setValue(url3);
        container.getItem(itemId).getItemProperty("tags1").setValue(title1);
        container.getItem(itemId).getItemProperty("tags2").setValue(title2);
        container.getItem(itemId).getItemProperty("tags3").setValue(title3);
        container.getItem(itemId).getItemProperty("height").setValue(height);
        container.getItem(itemId).getItemProperty("width").setValue(width);
    }

    private Table createTable(IndexedContainer container) {
        Table table = new Table("Image Table", container);
        table.setSizeFull();
        table.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);

        table.addGeneratedColumn("image1", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Item item = source.getItem(itemId);
                Resource url = (Resource) item.getItemProperty("url1").getValue();
                 String tags = (String) item.getItemProperty("tags1").getValue();
                int height = (Integer) item.getItemProperty("height").getValue();
                int width = (Integer) item.getItemProperty("width").getValue();
                Image enb = new Image("", url);
                enb.setWidth("320px");
//                enb.setWidth(width, Unit.PIXELS);
//                enb.setHeight(height, Unit.PIXELS);
                enb.addClickListener(new MouseEvents.ClickListener() {

                    @Override
                    public void click(MouseEvents.ClickEvent event) {
                        showNotify(url, tags);
                    }
                });
                return enb;

            }

        });
        table.addGeneratedColumn("image2", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Item item = source.getItem(itemId);
                Resource url = (Resource) item.getItemProperty("url2").getValue();
                 String tags = (String) item.getItemProperty("tags2").getValue();
                int height = (Integer) item.getItemProperty("height").getValue();
                int width = (Integer) item.getItemProperty("width").getValue();
                Image enb = new Image("", url);
                enb.setWidth("320px");
//                enb.setHeight(height, Unit.PIXELS);
                enb.addClickListener(new MouseEvents.ClickListener() {

                    @Override
                    public void click(MouseEvents.ClickEvent event) {
                        showNotify(url, tags);
                    }
                });
                return enb;

            }

        });
        table.addGeneratedColumn("image3", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Item item = source.getItem(itemId);
                Resource url = (Resource) item.getItemProperty("url3").getValue();
                 String tags = (String) item.getItemProperty("tags3").getValue();
                int height = (Integer) item.getItemProperty("height").getValue();
                int width = (Integer) item.getItemProperty("width").getValue();
                Image enb = new Image("", url);
                enb.setWidth("320px");
//                enb.setWidth(width, Unit.PIXELS);
//                enb.setHeight(height, Unit.PIXELS);
                enb.addClickListener(new MouseEvents.ClickListener() {

                    @Override
                    public void click(MouseEvents.ClickEvent event) {
                        showNotify(url, tags);
                    }
                });
                return enb;

            }

        });
        table.setColumnAlignment("image1", Table.Align.CENTER);
        table.setColumnAlignment("image2", Table.Align.CENTER);
        table.setColumnAlignment("image3", Table.Align.CENTER);

        table.setVisibleColumns(new Object[]{"image1", "image2", "image3"});
//        table.setColumnExpandRatio("title", 1f);
        return table;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }

    public static void showNotify(Resource url) {
        showNotify(url, "");
    }

    public static void showNotify(Resource url, String tags) {
//        Notification not = new Notification("#test #tags #hello #people");
//        not.setIcon(new ExternalResource(url));
//        not.setDelayMsec(-1);
//        not.show(Page.getCurrent());
        MySub sub = new MySub(url, tags);

        // Add it to the root component
        UI.getCurrent().addWindow(sub);
    }

    public static class MySub extends Window {

        public MySub(Resource url, String tags) {
            super("Photo"); // Set window caption
            center();

            // Some basic content for the window
            VerticalLayout content = new VerticalLayout();
            content.addComponent(new Label("Just say it's OK!"));
            content.setMargin(true);
            HorizontalLayout context2 = new HorizontalLayout();
//            Resource res = new ExternalResource(url);
            setWidth("640");
// Display the image without caption
            Image image = new Image(null, url);
            image.setWidth("400");
            context2.setSpacing(true);
            context2.setMargin(new MarginInfo(true, true, true, true));
            context2.addComponent(image);
            Label l = new Label(tags);
            l.setWidth("200");
            l.setStyleName("wrapLine");
            context2.addComponent(l);
            setContent(context2);

            // Disable the close button
            setClosable(true);

            // Trivial logic for closing the sub-window
        }
    }

}
