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

package com.haulmont.addon.dashboard.web.repository;

import java.io.Serializable;

/**
 * Contains values of annotation {@link com.haulmont.addon.dashboard.web.annotation.DashboardWidget}
 */
public class WidgetTypeInfo implements Serializable {

    protected String name;
    protected String browseFrameId;
    protected String editFrameId;

    public WidgetTypeInfo() {
    }

    public WidgetTypeInfo(String name, String browseFrameId, String editFrameId) {
        this.name = name;
        this.browseFrameId = browseFrameId;
        this.editFrameId = editFrameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrowseFrameId() {
        return browseFrameId;
    }

    public void setBrowseFrameId(String browseFrameId) {
        this.browseFrameId = browseFrameId;
    }

    public String getEditFrameId() {
        return editFrameId;
    }

    public void setEditFrameId(String editFrameId) {
        this.editFrameId = editFrameId;
    }
}
