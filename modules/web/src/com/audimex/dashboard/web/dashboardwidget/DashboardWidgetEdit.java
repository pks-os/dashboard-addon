/*
 * Copyright (c) 2016-2017 Haulmont. All rights reserved.
 */
package com.audimex.dashboard.web.dashboardwidget;

import com.audimex.dashboard.entity.DashboardWidget;
import com.audimex.dashboard.entity.WidgetParameter;
import com.audimex.dashboard.entity.WidgetParameterType;
import com.audimex.dashboard.web.tools.DashboardModelTools;
import com.audimex.dashboard.web.tools.DashboardTools;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.ScreensHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.CreateAction;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.reports.entity.ParameterType;
import com.haulmont.reports.entity.Report;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.FileNotFoundException;
import java.util.*;

import static com.audimex.dashboard.web.tools.DashboardTools.*;

public class DashboardWidgetEdit extends AbstractEditor<DashboardWidget> {
    protected WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);

    @Inject
    protected Datasource<DashboardWidget> dashboardWidgetDs;

    @Inject
    protected DashboardModelTools dashboardModelTools;

    @Inject
    protected CollectionDatasource<WidgetParameter, UUID> widgetParametersDs;

    @Inject
    protected ComponentsFactory componentsFactory;
    @Inject
    protected ScreensHelper screensHelper;
    @Inject
    protected DashboardTools dashboardTools;
    @Inject
    protected Metadata metadata;

    @Inject
    protected FieldGroup fieldGroup;
    @Inject
    protected Table<WidgetParameter> parametersTable;
    @Inject
    protected Button btnCreate;
    @Inject
    protected Button btnUp;
    @Inject
    protected Button btnDown;

    @Named("parametersTable.create")
    protected CreateAction parametersTableCreateAction;

    protected List<Field> allFieldNames;
    protected List<Field> commonFieldNames;
    protected List<Field> listFieldNames;
    protected List<Field> chartFieldNames;

    protected LookupField lookupWidgetViewTypeField;
    protected LookupField reportLookupField;
    protected LookupField lookupEntityTypeField;
    protected LookupField lookupFrameIdField;

    protected HashMap<Integer, WidgetParameter> orderNumbers;

    @Override
    protected void initNewItem(DashboardWidget item) {
        super.initNewItem(item);
        item.setIsTemplate(true);
        item.setWidgetViewType(COMMON);
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        initFieldList();
        initCreateButtonActions();
    }

    protected Integer regenerateParametersOrderNumbers(WidgetParameter parameter) {
        Integer orderNumber = 1;
        Integer parameterOrderNumber = orderNumber;

        for (WidgetParameter param : widgetParametersDs.getItems()) {
            param.setOrderNum(orderNumber++);
            if (param.equals(parameter)) {
                parameterOrderNumber = param.getOrderNum();
            }
        }

        fillParametersOrderNumbers();

        return parameterOrderNumber;
    }

    protected void fillParametersOrderNumbers() {
        orderNumbers = new HashMap<>();
        widgetParametersDs.getItems().forEach(wp -> orderNumbers.put(wp.getOrderNum(), wp));
    }

    @Override
    protected void postInit() {
        super.postInit();
        showComponents(getItem().getWidgetViewType());
        initListeners();
        fillParametersOrderNumbers();
        sortParameterTable();
    }

    protected void initListeners() {
        widgetParametersDs.addItemChangeListener(event -> {
            if (event.getItem() != null) {
                Integer orderNumber = event.getItem().getOrderNum();

                if (orderNumber == null) {
                    orderNumber = regenerateParametersOrderNumbers(event.getItem());
                }

                btnUp.setEnabled(true);
                btnDown.setEnabled(true);

                if (orderNumber == 1){
                    btnUp.setEnabled(false);
                } else if (orderNumber == widgetParametersDs.size()) {
                    btnDown.setEnabled(false);
                }
            }
        });

        reportLookupField.addValueChangeListener(e -> {
            clearParameterDs();
            Report report = (Report) e.getValue();
            createReportParameters(report);
        });
    }

    protected void initFieldList() {
        lookupWidgetViewTypeField = initWidgetViewTypeField();
        lookupFrameIdField = initFrameIdField();
        reportLookupField = initReportField();
        lookupEntityTypeField = initEntityTypeField();

        allFieldNames = new ArrayList<Field>(){
            {add(lookupFrameIdField);}
            {add(reportLookupField);}
            {add(lookupEntityTypeField);}
        };

        commonFieldNames = new ArrayList<Field>(){
            {add(lookupFrameIdField);}
            {add(lookupEntityTypeField);}
        };

        listFieldNames = new ArrayList<Field>(){
            {add(lookupFrameIdField);}
            {add(lookupEntityTypeField);}
        };

        chartFieldNames = new ArrayList<Field>(){
            {add(reportLookupField);}
            {add(lookupEntityTypeField);}
        };
    }

    protected LookupField initWidgetViewTypeField() {
        FieldGroup.FieldConfig entityTypeFieldConfig = fieldGroup.getField("widgetViewType");
        LookupField widgetViewTypeField = componentsFactory.createComponent(LookupField.class);
        widgetViewTypeField.setDatasource(dashboardWidgetDs, entityTypeFieldConfig.getProperty());
        entityTypeFieldConfig.setComponent(widgetViewTypeField);

        widgetViewTypeField.setOptionsMap(dashboardTools.getWidgetViewTypes());

        widgetViewTypeField.addValueChangeListener(event -> {
            if (event.getPrevValue() == null) return;

            if (CHART.equals(dashboardWidgetDs.getItem().getWidgetViewType()) &&
                    dashboardWidgetDs.getItem().getReport() != null) {
                createReportParameters(dashboardWidgetDs.getItem().getReport());
            } else {
                clearParameterDs();
            }

            showComponents((String) event.getValue());
        });

        return widgetViewTypeField;
    }

    protected LookupField initFrameIdField() {
        FieldGroup.FieldConfig frameIdFieldConfig = fieldGroup.getField("frameId");
        LookupField lookupField = componentsFactory.createComponent(LookupField.class);
        lookupField.setDatasource(dashboardWidgetDs, frameIdFieldConfig.getProperty());
        frameIdFieldConfig.setComponent(lookupField);
        setDefaultApplicableScreens(lookupField);
        return lookupField;
    }

    protected void setDefaultApplicableScreens(LookupField lookupEntityTypeField) {
        Map<String, String> availableFrames = new HashMap<>();

        try {
            for (WindowInfo windowInfo : windowConfig.getWindows()) {
                if (isApplicableScreen(windowInfo)) {
                    String screenCaption = screensHelper.getScreenCaption(windowInfo);
                    String screenName;
                    if (StringUtils.isNotBlank(screenCaption)) {
                        screenName = screenCaption +
                                " (" +
                                windowInfo.getId() +
                                ")";
                    } else {
                        screenName = windowInfo.getId();
                    }
                    availableFrames.put(screenName,
                            windowInfo.getId());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        lookupEntityTypeField.setOptionsMap(availableFrames);
    }


    protected LookupField initReportField() {
        FieldGroup.FieldConfig reportFieldConfig = fieldGroup.getField("report");
        LookupField reportLookupField = componentsFactory.createComponent(LookupField.class);
        reportLookupField.setDatasource(dashboardWidgetDs, reportFieldConfig.getProperty());
        reportFieldConfig.setComponent(reportLookupField);

        CollectionDatasource ds = new DsBuilder(getDsContext())
                .setJavaClass(Report.class)
                .setAllowCommit(false)
                .setViewName("report.edit")
                .setId("reportDs")
                .buildCollectionDatasource();

        ds.refresh();

        reportLookupField.setOptionsDatasource(ds);

        return reportLookupField;
    }

    protected void createReportParameters(Report report) {
        if (report != null && report.getInputParameters() != null) {
            report.getInputParameters().forEach(param -> {
                String name = param.getAlias();
                String alias = param.getAlias();
                String metaClass = param.getEntityMetaClass();
                WidgetParameterType type = WidgetParameterType.UNDEFINED;
                ParameterType parameterType = param.getType();
                switch (parameterType) {
                    case DATE: case TIME: case DATETIME:
                        type = WidgetParameterType.DATE;
                        break;
                    case TEXT:
                        type = WidgetParameterType.STRING;
                        break;
                    case BOOLEAN:
                        type = WidgetParameterType.BOOLEAN;
                        break;
                    case NUMERIC:
                        type = WidgetParameterType.DECIMAL;
                        break;
                       /*case ENUMERATION:
                            break;*/
                    case ENTITY:
                        type = WidgetParameterType.ENTITY;
                        break;
                    case ENTITY_LIST:
                        type = WidgetParameterType.LIST_ENTITY;
                        break;
                    default:
                }
                WidgetParameter wp = createWidgetParameter(name, alias, metaClass, type);
                widgetParametersDs.addItem(wp);
            });
        }
    }

    protected WidgetParameter createWidgetParameter(String name, String alias, String metaClass,
                                                    WidgetParameterType parameterType) {
        WidgetParameter wp = metadata.create(WidgetParameter.class);
        wp.setName(name);
        wp.setAlias(alias);
        wp.setParameterType(parameterType);
        wp.setDashboardWidget(dashboardWidgetDs.getItem());
        wp.getReferenceToEntity().setMetaClassName(metaClass);
        wp.getReferenceToEntity().setViewName(View.LOCAL);
        return wp;
    }

    protected LookupField initEntityTypeField() {
        FieldGroup.FieldConfig entityTypeFieldConfig = fieldGroup.getField("entityTypeField");
        LookupField lookupField = componentsFactory.createComponent(LookupField.class);
        lookupField.setDatasource(dashboardWidgetDs, entityTypeFieldConfig.getProperty());
        entityTypeFieldConfig.setComponent(lookupField);

        Map<String, Object> metaClasses = new LinkedHashMap<>();
        metadata.getTools().getAllPersistentMetaClasses()
                .forEach(metaClass ->
                        metaClasses.put(metaClass.getName(), metaClass.getName())
                );
        lookupField.setOptionsMap(metaClasses);

        lookupField.addValueChangeListener(event -> {
            String metaClass = (String) event.getValue();
            if (metaClass != null) {
                Class mClass = metadata.getSession().getClassNN(metaClass).getJavaClass();
                Map<String, Object> applicableScreens = screensHelper.getAvailableScreens(mClass);
                lookupFrameIdField.setOptionsMap(applicableScreens);
            } else {
                setDefaultApplicableScreens(lookupFrameIdField);
            }
        });

        return lookupField;
    }


    protected void clearParameterDs() {
        Collection<WidgetParameter> parameters = new ArrayList<>(widgetParametersDs.getItems());
        parameters.forEach(widgetParametersDs::removeItem);
    }

    protected void initCreateButtonActions() {
        parametersTableCreateAction.setBeforeActionPerformedHandler(() -> {
            if (PersistenceHelper.isNew(getItem())) {
                showNotification(getMessage("notification.warning.pleaseSaveDashboard"),
                        NotificationType.TRAY);
                return false;
            }

            return true;
        });
    }

    protected void showComponents(String type) {
        if (type == null)
            return;
        lookupEntityTypeField.setRequired(false);
        switch (type) {
            case COMMON:
                showComponents(commonFieldNames);
                break;
            case LIST:
                showComponents(listFieldNames);
                lookupEntityTypeField.setRequired(true);
                break;
            case CHART:
                showComponents(chartFieldNames);
                break;
            default:
        }
    }

    protected void showComponents(List<Field> componentNames) {
        if (componentNames == null)
            return;
        allFieldNames.forEach(field -> {
            if (componentNames.contains(field)) {
                showComponent(field, true);
            } else {
                showComponent(field, false);
            }
        });
    }

    protected void showComponent(Field field, boolean isShowing) {
        field.setVisible(isShowing);
    }

    protected boolean isApplicableScreen(WindowInfo windowInfo) {
        if (windowInfo.getId().contains(".edit")) {
            return false;
        }

        if (windowInfo.getId().contains("filterSelect")) {
            return false;
        }

        if (windowInfo.getId().contains(".changePassword")) {
            return false;
        }

        if (windowInfo.getId().contains("filterEditor")) {
            return false;
        }

        if (windowInfo.getId().contains("loginWindow")) {
            return false;
        }

        if (windowInfo.getId().contains("mainWindow")) {
            return false;
        }

        return true;
    }

    @Override
    protected boolean preCommit() {
        if (CHART.equals(getItem().getWidgetViewType())) {
            getItem().setFrameId("amxd$Empty.frame");
        }
        return super.preCommit();
    }

    public void parameterUp() {
        WidgetParameter parameter = parametersTable.getSingleSelected();
        if (parameter != null) {
            Integer orderNumber = parameter.getOrderNum();
            if (orderNumber != 1) {
                Integer previewOrderNumber = orderNumber - 1;
                WidgetParameter previewParameter = orderNumbers.get(previewOrderNumber);
                previewParameter.setOrderNum(orderNumber);
                parameter.setOrderNum(previewOrderNumber);
                sortParameterTable();
            }
        }
    }

    public void parameterDown() {
        WidgetParameter parameter = parametersTable.getSingleSelected();
        if (parameter != null) {
            Integer orderNumber = parameter.getOrderNum();
            if (orderNumber != widgetParametersDs.size()) {
                Integer nextOrderNumber = orderNumber + 1;
                WidgetParameter nextParameter = orderNumbers.get(nextOrderNumber);
                if (nextParameter != null) {
                    nextParameter.setOrderNum(orderNumber);
                }
                parameter.setOrderNum(nextOrderNumber);
                sortParameterTable();
            }
        }
    }

    protected void sortParameterTable() {
        parametersTable.sortBy(
                widgetParametersDs.getMetaClass().getPropertyPath("orderNum"),
                true
        );
    }

    public void propagateWidgetChanges() {
        dashboardModelTools.propagateWidgetChanges(getItem());
        showNotification(getMessage("message.theChangesHaveBeenPropagateSuccessful"));
    }
}