/*
 * Copyright (c) 2016-2018 Haulmont. All rights reserved.
 */

package com.haulmont.addon.dashboard.web.dashboard.events;

import com.haulmont.addon.dashboard.web.dashboard.layouts.CanvasLayout;

public class WeightChangedEvent extends WidgetCanvasEvent {
    public WeightChangedEvent(CanvasLayout source) {
        super(source);
    }
}
