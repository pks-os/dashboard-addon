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

package com.haulmont.addon.dashboard.web.dashboard.tools.drophandler;


import com.haulmont.addon.dnd.components.DropHandler;
import com.haulmont.addon.dnd.components.acceptcriterion.AcceptCriterion;
import com.haulmont.addon.dnd.components.dragevent.DragAndDropEvent;
import com.haulmont.addon.dnd.web.gui.components.AcceptCriterionWrapper;
import com.vaadin.event.dd.acceptcriteria.ServerSideCriterion;

public class NotDropHandler implements DropHandler {

    @Override
    public void drop(DragAndDropEvent event) {
        //do nothing
    }

    @Override
    public AcceptCriterion getCriterion() {
        return (AcceptCriterionWrapper) () -> new ServerSideCriterion() {
            @Override
            public boolean accept(com.vaadin.event.dd.DragAndDropEvent dragEvent) {
                return false;
            }
        };
    }
}
