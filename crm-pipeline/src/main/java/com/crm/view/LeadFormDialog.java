package com.crm.view;

import com.crm.model.Lead;
import com.crm.service.LeadService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.*;

public class LeadFormDialog extends Stage {

    private final Lead lead;
    private final boolean isEdit;
    private Runnable onSave;

    private TextField nomeField, emailField, telefoneField, empresaField, valorField;
    private ComboBox<String> prioridadeBox, estagioBox;
    private TextArea notasArea;

    public LeadFormDialog(Lead lead, Stage owner) {
        this.isEdit = (lead != null);
        this.lead = isEdit ? lead : new Lead();

        if (owner != null) initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);
        setTitle(isEdit ? "Edit Lead" : "New Lead");
        setResizable(false);

        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: #0E0E1E;");
        root.setPrefWidth(500);

        HBox header = new HBox();
        header.setPadding(new Insets(20, 24, 16, 24));
        header.setStyle("-fx-background-color: #0E0E1E; -fx-border-color: #1A1A30; -fx-border-width: 0 0 1 0;");
        Label titulo = new Label(isEdit ? "Edit Lead" : "New Lead");
        titulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        titulo.setTextFill(Color.WHITE);
        header.getChildren().add(titulo);

        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(14);
        grid.setPadding(new Insets(22, 24, 16, 24));

        nomeField     = criarCampo("Full name");
        emailField    = criarCampo("Email");
        telefoneField = criarCampo("Phone");
        empresaField  = criarCampo("Company");
        valorField    = criarCampo("0.00");
        valorField.setTextFormatter(new TextFormatter<>(c ->
            c.getControlNewText().matches("[0-9.,]*") ? c : null));

        prioridadeBox = new ComboBox<>();
        prioridadeBox.getItems().addAll("HIGH", "MEDIUM", "LOW");
        prioridadeBox.setValue("MEDIUM");
        estilizarCombo(prioridadeBox);

        estagioBox = new ComboBox<>();
        estagioBox.getItems().addAll(LeadService.ESTAGIOS);
        estagioBox.setValue("New Lead");
        estilizarCombo(estagioBox);

        notasArea = new TextArea();
        notasArea.setPromptText("Notes, next steps...");
        notasArea.setPrefRowCount(3);
        notasArea.setWrapText(true);
        notasArea.setStyle(
            "-fx-background-color: #1A1A30; -fx-text-fill: white;" +
            "-fx-prompt-text-fill: #444466; -fx-border-color: #2A2A45;" +
            "-fx-border-radius: 8; -fx-background-radius: 8; -fx-font-size: 13px;");

        if (isEdit) {
            nomeField.setText(lead.getNome() != null ? lead.getNome() : "");
            emailField.setText(lead.getEmail() != null ? lead.getEmail() : "");
            telefoneField.setText(lead.getTelefone() != null ? lead.getTelefone() : "");
            empresaField.setText(lead.getEmpresa() != null ? lead.getEmpresa() : "");
            valorField.setText(lead.getValor() > 0 ? String.valueOf(lead.getValor()) : "");
            prioridadeBox.setValue(lead.getPrioridade() != null ? lead.getPrioridade() : "MEDIUM");
            estagioBox.setValue(lead.getEstagio() != null ? lead.getEstagio() : "New Lead");
            notasArea.setText(lead.getNotas() != null ? lead.getNotas() : "");
        }

        ColumnConstraints c0 = new ColumnConstraints(); c0.setPrefWidth(95);
        ColumnConstraints c1 = new ColumnConstraints(); c1.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(c0, c1);

        int row = 0;
        grid.add(rotulo("Name *"),    0, row); grid.add(nomeField,     1, row++);
        grid.add(rotulo("Email"),     0, row); grid.add(emailField,    1, row++);
        grid.add(rotulo("Phone"),     0, row); grid.add(telefoneField, 1, row++);
        grid.add(rotulo("Company"),   0, row); grid.add(empresaField,  1, row++);
        grid.add(rotulo("Value ($)"), 0, row); grid.add(valorField,    1, row++);
        grid.add(rotulo("Priority"),  0, row); grid.add(prioridadeBox, 1, row++);
        grid.add(rotulo("Stage"),     0, row); grid.add(estagioBox,    1, row++);
        grid.add(rotulo("Notes"),     0, row); grid.add(notasArea,     1, row);

        HBox footer = new HBox(12);
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setPadding(new Insets(16, 24, 22, 24));
        footer.setStyle("-fx-background-color: #0E0E1E; -fx-border-color: #1A1A30; -fx-border-width: 1 0 0 0;");

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle(
            "-fx-background-color: transparent; -fx-text-fill: #666688;" +
            "-fx-border-color: #2A2A45; -fx-border-radius: 8; -fx-background-radius: 8;" +
            "-fx-padding: 9 20; -fx-font-size: 13px; -fx-cursor: hand;");
        cancelBtn.setOnAction(e -> close());

        Button saveBtn = new Button(isEdit ? "  Save  " : "  Add Lead  ");
        String saveStyleOff =
            "-fx-background-color: #7C6AF7; -fx-text-fill: white; -fx-font-weight: bold;" +
            "-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 9 24;" +
            "-fx-font-size: 13px; -fx-cursor: hand;";
        String saveStyleOn =
            "-fx-background-color: #9A8BF7; -fx-text-fill: white; -fx-font-weight: bold;" +
            "-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 9 24;" +
            "-fx-font-size: 13px; -fx-cursor: hand;";
        saveBtn.setStyle(saveStyleOff);
        saveBtn.setOnMouseEntered(e -> saveBtn.setStyle(saveStyleOn));
        saveBtn.setOnMouseExited(e -> saveBtn.setStyle(saveStyleOff));
        saveBtn.setOnAction(e -> salvar());

        root.setOnKeyPressed(e -> {
            if (e.getCode() == javafx.scene.input.KeyCode.ENTER && !notasArea.isFocused()) salvar();
            if (e.getCode() == javafx.scene.input.KeyCode.ESCAPE) close();
        });

        footer.getChildren().addAll(cancelBtn, saveBtn);
        root.getChildren().addAll(header, grid, footer);
        setScene(new Scene(root));
    }

    public void setEstagioInicial(String estagio) {
        if (estagio != null) estagioBox.setValue(estagio);
    }

    public void setOnSave(Runnable callback) { this.onSave = callback; }

    private void salvar() {
        String nome = nomeField.getText().trim();
        if (nome.isEmpty()) { nomeField.requestFocus(); return; }

        String estagioAnterior = lead.getEstagio();
        lead.setNome(nome);
        lead.setEmail(emailField.getText().trim());
        lead.setTelefone(telefoneField.getText().trim());
        lead.setEmpresa(empresaField.getText().trim());
        lead.setNotas(notasArea.getText().trim());
        lead.setPrioridade(prioridadeBox.getValue());

        try {
            String v = valorField.getText().trim().replace(",", ".");
            lead.setValor(v.isEmpty() ? 0 : Double.parseDouble(v));
        } catch (NumberFormatException ex) { lead.setValor(0); }

        String novoEstagio = estagioBox.getValue();
        if (isEdit) {
            if (estagioAnterior != null && !estagioAnterior.equals(novoEstagio))
                LeadService.getInstance().moverLead(lead, novoEstagio);
            else
                LeadService.getInstance().atualizarLead(lead);
        } else {
            lead.setEstagio(novoEstagio);
            LeadService.getInstance().adicionarLead(lead);
        }

        if (onSave != null) onSave.run();
        close();
    }

    private TextField criarCampo(String placeholder) {
        TextField tf = new TextField();
        tf.setPromptText(placeholder);
        tf.setStyle(
            "-fx-background-color: #1A1A30; -fx-text-fill: white;" +
            "-fx-prompt-text-fill: #444466; -fx-border-color: #2A2A45;" +
            "-fx-border-radius: 8; -fx-background-radius: 8;" +
            "-fx-padding: 8 12; -fx-font-size: 13px;");
        return tf;
    }

    private Label rotulo(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.web("#8888AA"));
        l.setFont(Font.font("Segoe UI", 12));
        l.setAlignment(Pos.CENTER_RIGHT);
        l.setMaxWidth(Double.MAX_VALUE);
        return l;
    }

    private void estilizarCombo(ComboBox<?> cb) {
        cb.setMaxWidth(Double.MAX_VALUE);
        cb.setStyle(
            "-fx-background-color: #1A1A30; -fx-border-color: #2A2A45;" +
            "-fx-border-radius: 8; -fx-background-radius: 8; -fx-font-size: 13px;");
    }
}