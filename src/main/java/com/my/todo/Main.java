package com.my.todo;

import com.my.todo.controller.main.MainController;
import com.my.todo.controller.task.EditTaskController;
import com.my.todo.controller.task.NewTaskController;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.datafx.controller.flow.Flow;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.getIcons().add(new Image("/view/ok-icon.png"));
        Flow flow = new Flow(MainController.class)
                .withLink(MainController.class, "taskCreationFormLink", NewTaskController.class)
                .withLink(MainController.class, "editTask", EditTaskController.class)
                .withGlobalBackAction("back");
        flow.startInStage(primaryStage);
        //primaryStage.getScene().getStylesheets().add("/view/style.css");
        Application.setUserAgentStylesheet(STYLESHEET_CASPIAN);
        primaryStage.getScene().getStylesheets().add("/view/style.css");
        //primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/view/ok-icon.png")));
//        DefaultFlowContainer container = new DefaultFlowContainer();
//        flow.createHandler().start(container);
//
//        Scene scene = new Scene(container.getView());
//        primaryStage.setScene(scene);
//        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
