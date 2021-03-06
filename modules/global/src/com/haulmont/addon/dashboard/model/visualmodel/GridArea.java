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
import com.haulmont.cuba.core.entity.BaseUuidEntity;

import javax.validation.constraints.NotNull;

@MetaClass(name = "dashboard$GridArea")
public class GridArea extends BaseUuidEntity {
    private static final long serialVersionUID = 5378347946733397250L;

    @NotNull
    @MetaProperty(mandatory = true)
    protected GridCellLayout component;
    @NotNull
    @MetaProperty(mandatory = true)
    protected Integer row;
    @NotNull
    @MetaProperty(mandatory = true)
    protected Integer col;

    @MetaProperty
    protected Integer col2;

    @MetaProperty
    protected Integer row2;

    public void setComponent(GridCellLayout component) {
        this.component = component;
    }

    public GridCellLayout getComponent() {
        return component;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getRow() {
        return row;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public Integer getCol() {
        return col;
    }

    public Integer getCol2() {
        return col2;
    }

    public void setCol2(Integer col2) {
        this.col2 = col2;
    }

    public Integer getRow2() {
        return row2;
    }

    public void setRow2(Integer row2) {
        this.row2 = row2;
    }
}
