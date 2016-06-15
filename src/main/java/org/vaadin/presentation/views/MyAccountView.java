/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.presentation.views;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import org.vaadin.cdiviewmenu.ViewMenuItem;

/**
 *
 * @author Java
 */
@CDIView(value = "My Account")
@ViewMenuItem(icon = FontAwesome.GLOBE, order = 5, enabled = false)
public class MyAccountView implements View {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
