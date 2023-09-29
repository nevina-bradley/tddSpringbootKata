package com.codedifferently.phonebook.widgets.controllers;

import com.codedifferently.phonebook.widgets.exceptions.WidgetException;
import com.codedifferently.phonebook.widgets.models.Widget;
import com.codedifferently.phonebook.widgets.services.WidgetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Define the controller for widgets
@RestController
@RequestMapping("/widgets")
public class WidgetController {

    // Logger for WidgetController
    private final Logger logger = LoggerFactory.getLogger(WidgetController.class);
    // Service that contains business logic for Widgets
    private WidgetService widgetService;

    // Autowired constructor for dependency injection of WidgetService
    @Autowired
    public WidgetController(WidgetService widgetService){
        this.widgetService = widgetService;
    }

    // Endpoint for creating a new Widget
    @PostMapping("")
    public ResponseEntity<Widget> createWidgetRequest(@RequestBody Widget widget){
        Widget savedWidget = widgetService.create(widget);
        ResponseEntity response = new ResponseEntity(savedWidget, HttpStatus.CREATED);
        return response;
    }

    // Endpoint for getting all Widgets
    @GetMapping("")
    public ResponseEntity<List<Widget>> getAllWidgets(){
        List<Widget> widgets = widgetService.getAllWidgets();
        ResponseEntity<List<Widget>> response = new ResponseEntity<>(widgets, HttpStatus.OK);
        return response;
    }

    // Endpoint for getting a Widget by its ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProfileById(@PathVariable Integer id){
        try {
            Widget widget = widgetService.getWidgetById(id);
            ResponseEntity<?> response = new ResponseEntity<>(widget, HttpStatus.OK);
            return response;
        } catch (WidgetException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    // Endpoint for updating a Widget by its ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Integer id, @RequestBody Widget widget){
        try{
            Widget updatedWidget = widgetService.updateWidget(id, widget);
            ResponseEntity response = new ResponseEntity(updatedWidget,HttpStatus.OK);
            return response;
        } catch (WidgetException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    // Endpoint for deleting a Widget by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable Integer id){
        try{
            widgetService.deleteWidget(id);
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (WidgetException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }
}

