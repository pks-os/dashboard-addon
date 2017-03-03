/*
 * Copyright (c) 2016-2016 Haulmont. All rights reserved.
 */

package com.audimex.dashboard.web.widgets;

import com.audimex.dashboard.web.drophandlers.TreeDropHandler;
import com.audimex.dashboard.web.layouts.HasGridSpan;
import com.audimex.dashboard.web.layouts.HasMainLayout;
import com.audimex.dashboard.web.layouts.HasWeight;
import com.audimex.dashboard.web.settings.DashboardSettings;
import com.audimex.dashboard.web.utils.DashboardUtils;
import com.audimex.dashboard.web.utils.LayoutUtils;
import com.audimex.dashboard.web.utils.TreeUtils;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.web.App;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;

import java.util.HashMap;
import java.util.Map;

public class FramePanel extends CssLayout implements HasWeight, HasGridSpan, HasMainLayout {
    protected VerticalLayout contentLayout = new VerticalLayout();
    protected Tree tree;

    protected int weight = 1;
    protected int colSpan = 1;
    protected int rowSpan = 1;

    protected DashboardSettings dashboardSettings;
    private String frameId;

    public FramePanel(Tree tree, String frameId, Frame parentFrame) {
        this.tree = tree;
        this.frameId = frameId;
        dashboardSettings = AppBeans.get(DashboardSettings.class);

        HorizontalLayout buttonsPanel = new HorizontalLayout();
        Button configButton = new Button(FontAwesome.GEARS);
        configButton.addClickListener((Button.ClickListener) (event) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("widget", this);
            params.put("tree", tree);
            parentFrame.openWindow("widgetConfigWindow", WindowManager.OpenType.DIALOG, params);
        });
        Button removeButton = new Button(FontAwesome.TRASH);
        removeButton.addClickListener((Button.ClickListener) event -> {
            TreeUtils.removeComponent(tree, tree.getValue());
            ((TreeDropHandler) tree.getDropHandler()).getTreeChangeListener().accept(tree);
        });
        buttonsPanel.addComponent(configButton);
        buttonsPanel.addComponent(removeButton);
        buttonsPanel.addStyleName(DashboardUtils.AMXD_LAYOUT_CONTROLS);

        contentLayout.setSizeFull();
        contentLayout.setStyleName("amxd-widget-content");
        contentLayout.setMargin(true);

        addComponent(buttonsPanel);
        addComponent(contentLayout);

        setSizeFull();
        addStyleName(DashboardUtils.AMXD_BORDERING);

        WindowManager windowManager = App.getInstance().getWindowManager();
        WindowConfig windowConfig = AppBeans.get(WindowConfig.class);
        WindowInfo windowInfo = windowConfig.getWindowInfo(frameId);

        Frame frame = windowManager.openFrame(parentFrame, null, windowInfo);
        frame.setParent(parentFrame);
        setContent(frame.unwrapComposition(Layout.class));
    }

    public void setContent(Component c) {
        contentLayout.removeAllComponents();

        if (c != null) {
            c.setSizeFull();
            contentLayout.addComponent(c);
        }
    }

    public String getFrameId() {
        return frameId;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public void setWeight(int weight) {
        this.weight = weight;

        if (getParent() instanceof AbstractOrderedLayout) {
            ((AbstractOrderedLayout) getParent()).setExpandRatio(this, weight);
        }
    }

    @Override
    public int getColSpan() {
        return colSpan;
    }

    @Override
    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
        GridCell gridCell = LayoutUtils.getWidgetCell(tree, this);

        if (gridCell != null) {
            gridCell.setColspan(colSpan);
        }

        if (getParent() instanceof GridLayout) {
            GridLayout parent = (GridLayout) getParent();
            parent.removeComponent(this);

            DashboardUtils.removeEmptyLabelsForSpan(parent, gridCell);
            parent.addComponent(this, gridCell.getColumn(), gridCell.getRow(),
                    gridCell.getColumn() + gridCell.getColspan() - 1,
                    gridCell.getRow() + gridCell.getRowspan() - 1);
            DashboardUtils.addEmptyLabelsToLayout(parent, tree);
            DashboardUtils.lockGridCells(parent, tree);
        }
    }

    @Override
    public int getRowSpan() {
        return rowSpan;
    }

    @Override
    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
        GridCell gridCell = LayoutUtils.getWidgetCell(tree, this);

        if (gridCell != null) {
            gridCell.setRowspan(rowSpan);
        }

        if (getParent() instanceof GridLayout) {
            GridLayout parent = (GridLayout) getParent();

            parent.removeComponent(this);

            DashboardUtils.removeEmptyLabelsForSpan(parent, gridCell);
            parent.addComponent(this, gridCell.getColumn(), gridCell.getRow(),
                    gridCell.getColumn() + gridCell.getColspan() - 1,
                    gridCell.getRow() + gridCell.getRowspan() - 1);
            DashboardUtils.addEmptyLabelsToLayout(parent, tree);
            DashboardUtils.lockGridCells(parent, tree);
        }
    }

    @Override
    public AbstractOrderedLayout getMainLayout() {
        return contentLayout;
    }

    @Override
    public void setMargin(boolean margin) {
        contentLayout.setMargin(margin);
    }
}