package com.my.todo.controller.category;

import com.my.todo.service.category.CategoryService;
import com.my.todo.service.category.CategoryServiceImpl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.ActionMethod;
import org.datafx.controller.flow.action.ActionTrigger;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by marcin on 04.01.16.
 */

@FXMLController(title = "New category creator", value = "/view/category/createCategory.fxml")
public class NewCategoryController implements Initializable {

    private CategoryService categoryService;

    @FXML
    private TextField name;

    @FXML
    private TextField categoryName;

    @FXML
    @ActionTrigger("newCategoryCreate")
    private Button create;

    @FXML
    @ActionTrigger("back")
    private Button cancel;

    @FXML
    private Label errors;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errors.setVisible(false);
    }

    @PostConstruct
    public void init() {
        categoryService = new CategoryServiceImpl();
    }

    @ActionMethod("newCategoryCreate")
    public void newCategoryCreate() {
       if(name.getText().isEmpty()) {
           errors.setVisible(true);
           errors.setText("Name should not be empty.");
           return;
       }
        if (categoryService.findByName(name.getText()) != null) {
            errors.setVisible(true);
            errors.setText("Name must be unique.");
            return;
        }

    }


}
