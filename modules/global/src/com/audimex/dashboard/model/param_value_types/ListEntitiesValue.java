/*
 * Copyright (c) 2016-2018 Haulmont. All rights reserved.
 */

package com.audimex.dashboard.model.param_value_types;

import java.util.ArrayList;
import java.util.List;

public class ListEntitiesValue implements Value {
    protected List<EntityValue> entityValues;

    public ListEntitiesValue() {
        entityValues = new ArrayList<>();
    }

    public ListEntitiesValue(List<EntityValue> entityValues) {
        this.entityValues = entityValues;
    }

    public List<EntityValue> getEntityValues() {
        return entityValues;
    }

    public void setEntityValues(List<EntityValue> entityValues) {
        this.entityValues = entityValues;
    }
}
