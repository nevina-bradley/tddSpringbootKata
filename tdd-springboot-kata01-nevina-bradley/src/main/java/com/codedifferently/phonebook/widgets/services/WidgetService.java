package com.codedifferently.phonebook.widgets.services;

import com.codedifferently.phonebook.widgets.exceptions.WidgetException;
import com.codedifferently.phonebook.widgets.models.Widget;

import java.util.List;

public interface WidgetService {
    Widget create(Widget widget);
    Widget getWidgetById(Integer id) throws WidgetException;
    List<Widget> getAllWidgets();
    Widget updateWidget(Integer id, Widget widget) throws WidgetException;
    Boolean deleteWidget(Integer id) throws WidgetException;
}
