/*
 * Copyright (c) 2016-2017 Haulmont. All rights reserved.
 */
package com.audimex.dashboard.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import java.util.List;

@Table(name = "AMXD_DASHBOARD")
@Entity(name = "amxd$Dashboard")
public class Dashboard extends StandardEntity {
    private static final long serialVersionUID = 8876942042181481797L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    protected User user;

    @Column(name = "TITLE")
    protected String title;

    @Lob
    @Column(name = "MODEL")
    protected String model;

    @OneToMany(mappedBy = "dashboard")
    protected List<DashboardWidgetLink> widgetLinks;

    @Column(name = "ENTITY_TYPE")
    protected String entityType;

    public void setWidgetLinks(List<DashboardWidgetLink> widgetLinks) {
        this.widgetLinks = widgetLinks;
    }

    public List<DashboardWidgetLink> getWidgetLinks() {
        return widgetLinks;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityType() {
        return entityType;
    }


    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }


}