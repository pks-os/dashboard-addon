package com.haulmont.addon.dashboard.web.parameter.frames;

import com.haulmont.addon.dashboard.model.paramtypes.ParameterValue;
import com.haulmont.cuba.gui.components.AbstractWindow;

import javax.inject.Inject;
import java.util.Map;

public class EntityValueWindow extends AbstractWindow implements ValueFrame {

    @Inject
    protected EntityValueFrame entityValueFrame;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        entityValueFrame.init(params);
    }

    @Override
    public ParameterValue getValue() {
        return entityValueFrame.getValue();
    }

    public void commitAndClose() {
        close("commit");
    }

    public void cancelAndClose() {
        close("cancel");
    }
}
