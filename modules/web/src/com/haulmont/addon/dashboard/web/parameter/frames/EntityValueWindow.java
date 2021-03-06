package com.haulmont.addon.dashboard.web.parameter.frames;

import com.haulmont.addon.dashboard.model.paramtypes.EntityParameterValue;
import com.haulmont.addon.dashboard.model.paramtypes.ParameterValue;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Window;

import javax.inject.Inject;
import java.util.Map;

public class EntityValueWindow extends AbstractWindow implements ValueFrame, Window.Committable {

    @Inject
    protected EntityValueFrame entityValueFrame;

    protected EntityParameterValue prevValue;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        entityValueFrame.init(params);
        prevValue = (EntityParameterValue) entityValueFrame.getValue();
    }

    @Override
    public ParameterValue getValue() {
        return entityValueFrame.getValue();
    }

    public void commitAndClose() {
        close(Window.COMMIT_ACTION_ID, true);
    }

    public void cancelAndClose() {
        close(Window.CLOSE_ACTION_ID);
    }

    @Override
    public boolean isModified() {
        EntityParameterValue currentVal = (EntityParameterValue) entityValueFrame.getValue();
        if ((prevValue == null && currentVal != null) || (prevValue != null && currentVal == null)) {
            return true;
        }
        if (prevValue != null) {
            return !prevValue.equals(currentVal);
        }
        return false;
    }
}
