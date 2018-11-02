/*
 * Copyright (c) 2008-2018 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.addon.dashboard.model.visualmodel;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@MetaClass(name = "dashboard$GridLayout")
public class GridLayout extends DashboardLayout implements ContainerLayout {

    @MetaProperty
    protected Integer rows = 0;
    @MetaProperty
    protected Integer columns = 0;
    @MetaProperty
    protected Set<GridArea> areas = new HashSet<>();

    public void setAreas(Set<GridArea> areas) {
        this.areas = areas;
    }

    public Set<GridArea> getAreas() {
        return areas;
    }

    public void addArea(GridArea area) {
        areas.add(area);
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getColumns() {
        return columns;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    @Override
    public List<DashboardLayout> getChildren() {
        return null;
    }

    @Override
    public void setChildren(List<DashboardLayout> children) {

    }

    @Override
    public void addChild(DashboardLayout child) {
        throw new UnsupportedOperationException("Use addArea() method");
    }

    @Override
    public String getCaption() {
        Messages messages = AppBeans.get(Messages.class);
        return messages.getMessage(getClass(), "Layout.grid");
    }
}
