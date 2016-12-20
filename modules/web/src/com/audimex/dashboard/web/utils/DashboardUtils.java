/*
 * Copyright (c) 2016-2016 Haulmont. All rights reserved.
 */

package com.audimex.dashboard.web.utils;

import com.audimex.dashboard.entity.ComponentType;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import fi.jasoft.dragdroplayouts.DDGridLayout;
import fi.jasoft.dragdroplayouts.DDHorizontalLayout;
import fi.jasoft.dragdroplayouts.DDVerticalLayout;

public class DashboardUtils {
    private DashboardUtils() {
    }

    public static ComponentType getTypeByComponent(Component component) {
        ComponentType componentType;
        if (component instanceof DDVerticalLayout) {
            componentType = ComponentType.VERTICAL_LAYOUT;
        } else if (component instanceof DDHorizontalLayout) {
            componentType = ComponentType.HORIZONTAL_LAYOUT;
        } else if (component instanceof DDGridLayout) {
            componentType = ComponentType.GRID_LAYOUT;
        } else {
            componentType = ComponentType.WIDGET;
        }
        
        return componentType;
    }

    public static GridLayout removeEmptyLabels(GridLayout gridLayout) {
        for (int i=0; i<gridLayout.getRows(); i++) {
            for (int j=0; j<gridLayout.getColumns(); j++) {
                Component innerComponent = gridLayout.getComponent(j, i);
                if (innerComponent != null && innerComponent instanceof Label) {
                    gridLayout.removeComponent(j, i);
                }
            }
        }

        return gridLayout;
    }

    public static GridLayout addEmptyLabels(GridLayout gridLayout) {
        for (int i=0; i<gridLayout.getRows(); i++) {
            for (int j=0; j<gridLayout.getColumns(); j++) {
                Component innerComponent = gridLayout.getComponent(j, i);
                if (innerComponent == null) {
                    Label label = new Label("");
                    label.addStyleName("dd-grid-slot");
                    label.setSizeFull();
                    gridLayout.addComponent(label, j, i);
                    gridLayout.setComponentAlignment(label, com.vaadin.ui.Alignment.MIDDLE_CENTER);
                }
            }
        }

        return gridLayout;
    }
}