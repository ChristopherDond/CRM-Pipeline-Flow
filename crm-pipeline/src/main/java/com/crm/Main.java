package com.crm;

import com.crm.view.KanbanBoard;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        KanbanBoard kanban = new KanbanBoard(stage);
        Scene scene = new Scene(kanban, 1380, 720);
        stage.setTitle("CRM - Pipeline de Vendas");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}