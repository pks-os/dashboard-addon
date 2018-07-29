/*
 * Copyright (c) 2016-2018 Haulmont. All rights reserved.
 */

package com.haulmont.addon.dashboard.converter;

import com.haulmont.addon.dashboard.model.Dashboard;
import com.haulmont.addon.dashboard.model.Parameter;
import com.haulmont.addon.dashboard.model.Widget;
import com.haulmont.addon.dashboard.model.param_value_types.ParameterValue;
import com.haulmont.addon.dashboard.model.visual_model.DashboardLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import static com.haulmont.addon.dashboard.converter.JsonConverter.NAME;

/**
 * Supports the conversion to json for non-persistent objects with types: {@link Dashboard},
 * {@link Widget}, {@link Parameter}
 */
@Component(NAME)
public class JsonConverter {
    public static final String NAME = "amxd_JsonConverter";

    protected Gson gson;

    public JsonConverter() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ParameterValue.class, new InheritanceAdapter());
        builder.registerTypeAdapter(Widget.class, new InheritanceAdapter());
        builder.registerTypeAdapter(DashboardLayout.class, new InheritanceAdapter());
        gson = builder.create();
    }

    public String widgetToJson(Widget widget) {
        return gson.toJson(widget, new TypeToken<Widget>() {
        }.getType());
    }

    public String dashboardToJson(Dashboard dashboard) {
        return gson.toJson(dashboard, new TypeToken<Dashboard>() {
        }.getType());
    }

    public Dashboard dashboardFromJson(String json) {
        return gson.fromJson(json, Dashboard.class);
    }

    public Widget widgetFromJson(String json) {
        return gson.fromJson(json, Widget.class);
    }

    public Parameter parameterFromJson(String json) {
        return gson.fromJson(json, Parameter.class);
    }
}