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

package com.haulmont.addon.dashboard.web.dashboard.layouts;

import com.haulmont.addon.dashboard.model.visualmodel.DashboardLayout;
import com.haulmont.addon.dnd.components.DDLayout;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ComponentContainer;
import com.haulmont.cuba.gui.components.HBoxLayout;
import com.haulmont.cuba.gui.components.LayoutClickNotifier;

import java.util.UUID;

public interface CanvasLayout extends Component, ComponentContainer, LayoutClickNotifier, HasWeight {

    ComponentContainer getDelegate();

    void addComponent(Component component);

    HBoxLayout getButtonsPanel();

    void setButtonsPanel(HBoxLayout buttonsPanel);

    UUID getUuid();

    void setUuid(UUID uuid);

    DashboardLayout getModel();

}
