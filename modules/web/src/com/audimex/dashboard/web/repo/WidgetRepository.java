/*
 * Copyright (c) 2016-2017 Haulmont. All rights reserved.
 */

package com.audimex.dashboard.web.repo;

import java.io.Serializable;
import java.util.List;

public interface WidgetRepository {
    String NAME = "amxd_WidgetRepository";

    List<Widget> getWidgets();

    class Widget implements Serializable {
        protected String id;
        protected String caption;
        protected String icon;
        protected String description;
        protected String frameId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getFrameId() {
            return frameId;
        }

        public void setFrameId(String frameId) {
            this.frameId = frameId;
        }
    }
}