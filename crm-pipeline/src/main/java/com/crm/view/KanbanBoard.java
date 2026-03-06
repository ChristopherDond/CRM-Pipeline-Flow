package com.crm.view;

import com.crm.model.Lead;
import com.crm.service.LeadService;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class KanbanBoard extends BorderPane {

    private final LeadService service = LeadService.getInstance();
    private final Stage ownerStage;
    private Lead leadArrastado;
    private String filtro = "";

    private VBox topArea;
    private HBox columnsBox;

    public KanbanBoard(Stage ownerStage) {
        this.ownerStage = ownerStage;
        setStyle("-fx-background-color: #0A0A18;");

        topArea = new VBox(0, criarToolbar(), criarStatsBar());
        setTop(topArea);

        columnsBox = new HBox(14);
        columnsBox.setPadding(new Insets(20));
        columnsBox.setStyle("-fx-background-color: #0A0A18;");

        ScrollPane sp = new ScrollPane(columnsBox);
        sp.setFitToHeight(true);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setStyle("-fx-background: #0A0A18; -fx-background-color: #0A0A18;");
        setCenter(sp);

        construirColunas();
    }

    private HBox criarToolbar() {
        Label logo = new Label("CRM Pipeline");
        logo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        logo.setTextFill(Color.web("#7C6AF7"));

        TextField searchBox = new TextField();
        searchBox.setPromptText("Search by name, company or email...");
        searchBox.setPrefWidth(300);
        searchBox.setStyle(
            "-fx-background-color: #14142A; -fx-text-fill: white;" +
            "-fx-prompt-text-fill: #444466; -fx-border-color: #222240;" +
            "-fx-border-radius: 20; -fx-background-radius: 20;" +
            "-fx-padding: 8 16; -fx-font-size: 13px;");
        searchBox.textProperty().addListener((obs, old, val) -> {
            filtro = val.trim().toLowerCase();
            construirColunas();
        });

        Button novoLeadBtn = criarBotaoPrimario("+ New Lead");
        novoLeadBtn.setOnAction(e -> abrirFormulario(null));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox toolbar = new HBox(16, logo, spacer, searchBox, novoLeadBtn);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(14, 20, 14, 20));
        toolbar.setStyle(
            "-fx-background-color: #0D0D1F;" +
            "-fx-border-color: #161630; -fx-border-width: 0 0 1 0;");
        return toolbar;
    }

    private HBox criarStatsBar() {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        HBox bar = new HBox(28);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(12, 24, 12, 24));
        bar.setStyle(
            "-fx-background-color: #0D0D1F;" +
            "-fx-border-color: #161630; -fx-border-width: 0 0 1 0;");
        bar.getChildren().addAll(
            statCard("TOTAL LEADS",       String.valueOf(service.getTotalLeads()),              "#7C6AF7"),
            vSep(),
            statCard("PIPELINE VALUE",    nf.format(service.getValorTotal()),                  "#4DA8FF"),
            vSep(),
            statCard("CLOSED VALUE",      nf.format(service.getValorFechado()),                "#52D9A0"),
            vSep(),
            statCard("CONVERSION RATE",   String.format("%.0f%%", service.getTaxaConversao()), "#FFB347"),
            vSep(),
            statCard("DEALS CLOSED",      service.getLeads("Closed").size() + " deals",        "#FF6B6B")
        );
        return bar;
    }

    private VBox statCard(String label, String value, String cor) {
        Label lbl = new Label(label);
        lbl.setFont(Font.font("Segoe UI", 9));
        lbl.setTextFill(Color.web("#444466"));
        Label val = new Label(value);
        val.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        val.setTextFill(Color.web(cor));
        return new VBox(2, lbl, val);
    }

    private Separator vSep() {
        Separator s = new Separator(Orientation.VERTICAL);
        s.setStyle("-fx-background-color: #1A1A35;");
        return s;
    }

    private void construirColunas() {
        columnsBox.getChildren().clear();
        String[] estagios = LeadService.ESTAGIOS;
        String[] cores    = LeadService.CORES;
        for (int i = 0; i < estagios.length; i++) {
            columnsBox.getChildren().add(criarColuna(estagios[i], cores[i]));
        }
    }

    private VBox criarColuna(String estagio, String cor) {
        List<Lead> leads = service.getLeads(estagio).stream()
            .filter(l -> filtro.isEmpty()
                || (l.getNome()    != null && l.getNome().toLowerCase().contains(filtro))
                || (l.getEmpresa() != null && l.getEmpresa().toLowerCase().contains(filtro))
                || (l.getEmail()   != null && l.getEmail().toLowerCase().contains(filtro)))
            .collect(Collectors.toList());

        double totalValor = leads.stream().mapToDouble(Lead::getValor).sum();
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);

        VBox coluna = new VBox(0);
        coluna.setPrefWidth(245);
        coluna.setMinWidth(220);
        coluna.setMaxWidth(280);
        coluna.setStyle(colunaStyle());

        HBox accentBar = new HBox();
        accentBar.setMinHeight(4);
        accentBar.setStyle("-fx-background-color: " + cor + "; -fx-background-radius: 12 12 0 0;");

        Label nomeLbl = new Label(estagio);
        nomeLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        nomeLbl.setTextFill(Color.WHITE);

        Label countBadge = new Label(String.valueOf(leads.size()));
        countBadge.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        countBadge.setTextFill(Color.web(cor));
        countBadge.setStyle(
            "-fx-background-color: " + cor + "28;" +
            "-fx-padding: 2 8; -fx-background-radius: 10;");

        HBox titleRow = new HBox(8, nomeLbl, countBadge);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label valorLbl = new Label(totalValor > 0 ? nf.format(totalValor) : "No value registered");
        valorLbl.setFont(Font.font("Segoe UI", 11));
        valorLbl.setTextFill(Color.web(totalValor > 0 ? "#6666AA" : "#333355"));

        VBox headerBox = new VBox(5, titleRow, valorLbl);
        headerBox.setPadding(new Insets(12, 14, 10, 14));

        VBox cardsBox = new VBox(8);
        cardsBox.setPadding(new Insets(10));

        for (Lead lead : leads) cardsBox.getChildren().add(criarCard(lead, cor));

        if (leads.isEmpty()) {
            Label empty = new Label("No leads here\nyet.");
            empty.setFont(Font.font("Segoe UI", 12));
            empty.setTextFill(Color.web("#2A2A48"));
            empty.setAlignment(Pos.CENTER);
            empty.setMaxWidth(Double.MAX_VALUE);
            empty.setPadding(new Insets(24, 0, 24, 0));
            cardsBox.getChildren().add(empty);
        }

        ScrollPane scroll = new ScrollPane(cardsBox);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setPrefHeight(460);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        Button addBtn = new Button("+ Add Lead");
        addBtn.setMaxWidth(Double.MAX_VALUE);
        String addOff = "-fx-background-color: transparent; -fx-text-fill: " + cor + "66;" +
                        "-fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 9 0; -fx-border-width: 0;";
        String addOn  = "-fx-background-color: " + cor + "12; -fx-text-fill: " + cor + ";" +
                        "-fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 9 0; -fx-border-width: 0;";
        addBtn.setStyle(addOff);
        addBtn.setOnMouseEntered(e -> addBtn.setStyle(addOn));
        addBtn.setOnMouseExited(e -> addBtn.setStyle(addOff));
        addBtn.setOnAction(e -> abrirFormulario(estagio));

        VBox footer = new VBox(addBtn);
        footer.setPadding(new Insets(2, 10, 8, 10));
        footer.setStyle("-fx-border-color: #181830; -fx-border-width: 1 0 0 0;");

        coluna.setOnDragOver(e -> {
            if (e.getDragboard().hasString()) {
                e.acceptTransferModes(TransferMode.MOVE);
                coluna.setStyle(colunaStyleHover(cor));
            }
            e.consume();
        });
        coluna.setOnDragExited(e -> coluna.setStyle(colunaStyle()));
        coluna.setOnDragDropped(e -> {
            String id = e.getDragboard().getString();
            if (leadArrastado != null && leadArrastado.getId() != null
                    && leadArrastado.getId().equals(id)
                    && !leadArrastado.getEstagio().equals(estagio)) {
                service.moverLead(leadArrastado, estagio);
                leadArrastado = null;
                refresh();
            }
            e.setDropCompleted(true);
            e.consume();
            coluna.setStyle(colunaStyle());
        });

        coluna.getChildren().addAll(accentBar, headerBox, new Separator(), scroll, footer);
        return coluna;
    }

    private VBox criarCard(Lead lead, String cor) {
        VBox card = new VBox(7);
        card.setPadding(new Insets(12));
        card.setStyle(cardStyle(false, cor));

        String prioCor = switch (lead.getPrioridade() != null ? lead.getPrioridade() : "MEDIUM") {
            case "HIGH" -> "#FF6B6B";
            case "LOW"  -> "#52D9A0";
            default     -> "#FFB347";
        };
        String prioLabel = switch (lead.getPrioridade() != null ? lead.getPrioridade() : "MEDIUM") {
            case "HIGH" -> "^ HIGH";
            case "LOW"  -> "v LOW";
            default     -> "- MEDIUM";
        };
        Label prioBadge = new Label(prioLabel);
        prioBadge.setFont(Font.font("Segoe UI", FontWeight.BOLD, 9));
        prioBadge.setTextFill(Color.web(prioCor));
        prioBadge.setStyle(
            "-fx-background-color: " + prioCor + "22;" +
            "-fx-padding: 2 7; -fx-background-radius: 4;");

        Label nomeLbl = new Label(lead.getNome() != null ? lead.getNome() : "");
        nomeLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        nomeLbl.setTextFill(Color.WHITE);
        nomeLbl.setWrapText(true);

        card.getChildren().addAll(prioBadge, nomeLbl);

        if (lead.getEmpresa() != null && !lead.getEmpresa().isBlank()) {
            Label emp = new Label(lead.getEmpresa());
            emp.setFont(Font.font("Segoe UI", 11));
            emp.setTextFill(Color.web("#7777AA"));
            card.getChildren().add(emp);
        }

        if (lead.getEmail() != null && !lead.getEmail().isBlank()) {
            Label email = new Label(lead.getEmail());
            email.setFont(Font.font("Segoe UI", 10));
            email.setTextFill(Color.web("#555577"));
            card.getChildren().add(email);
        }

        HBox bottomRow = new HBox();
        bottomRow.setAlignment(Pos.CENTER_LEFT);
        if (lead.getValor() > 0) {
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            Label valLbl = new Label(nf.format(lead.getValor()));
            valLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
            valLbl.setTextFill(Color.web("#52D9A0"));
            bottomRow.getChildren().add(valLbl);
        }
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        if (lead.getDataCriacao() != null && !lead.getDataCriacao().isBlank()) {
            Label dataLbl = new Label(lead.getDataCriacao());
            dataLbl.setFont(Font.font("Segoe UI", 9));
            dataLbl.setTextFill(Color.web("#333355"));
            bottomRow.getChildren().addAll(spacer, dataLbl);
        }
        if (!bottomRow.getChildren().isEmpty()) {
            Separator sep = new Separator();
            sep.setStyle("-fx-background-color: #1E1E38;");
            card.getChildren().addAll(sep, bottomRow);
        }

        card.setOnMouseEntered(e -> card.setStyle(cardStyle(true, cor)));
        card.setOnMouseExited(e -> card.setStyle(cardStyle(false, cor)));
        card.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && e.getButton() == MouseButton.PRIMARY)
                abrirEdicao(lead);
        });

        ContextMenu menu = new ContextMenu();
        MenuItem editItem = new MenuItem("Edit Lead");
        editItem.setOnAction(e -> abrirEdicao(lead));
        Menu moverMenu = new Menu("Move to...");
        for (String est : LeadService.ESTAGIOS) {
            if (!est.equals(lead.getEstagio())) {
                MenuItem mi = new MenuItem(est);
                mi.setOnAction(e -> { service.moverLead(lead, est); refresh(); });
                moverMenu.getItems().add(mi);
            }
        }
        MenuItem deleteItem = new MenuItem("Delete Lead");
        deleteItem.setOnAction(e -> confirmarExclusao(lead));
        menu.getItems().addAll(editItem, moverMenu, new SeparatorMenuItem(), deleteItem);
        card.setOnContextMenuRequested(e -> menu.show(card, e.getScreenX(), e.getScreenY()));

        card.setOnDragDetected(e -> {
            leadArrastado = lead;
            Dragboard db = card.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent cc = new ClipboardContent();
            cc.putString(lead.getId() != null ? lead.getId() : "");
            db.setContent(cc);
            card.setOpacity(0.45);
            e.consume();
        });
        card.setOnDragDone(e -> card.setOpacity(1.0));

        return card;
    }

    public void abrirFormulario(String estagioInicial) {
        LeadFormDialog dialog = new LeadFormDialog(null, ownerStage);
        if (estagioInicial != null) dialog.setEstagioInicial(estagioInicial);
        dialog.setOnSave(this::refresh);
        dialog.show();
    }

    private void abrirEdicao(Lead lead) {
        LeadFormDialog dialog = new LeadFormDialog(lead, ownerStage);
        dialog.setOnSave(this::refresh);
        dialog.show();
    }

    private void confirmarExclusao(Lead lead) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete \"" + lead.getNome() + "\"?");
        alert.setContentText("This action cannot be undone.");
        if (ownerStage != null) alert.initOwner(ownerStage);
        alert.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) { service.removerLead(lead); refresh(); }
        });
    }

    public void refresh() {
        HBox newStats = criarStatsBar();
        if (topArea.getChildren().size() > 1) {
            topArea.getChildren().set(1, newStats);
        }
        construirColunas();
    }

    private Button criarBotaoPrimario(String texto) {
        Button btn = new Button(texto);
        String off = "-fx-background-color: #7C6AF7; -fx-text-fill: white; -fx-font-weight: bold;" +
                     "-fx-background-radius: 20; -fx-padding: 8 20; -fx-font-size: 13px; -fx-cursor: hand;";
        String on  = "-fx-background-color: #9A8BF7; -fx-text-fill: white; -fx-font-weight: bold;" +
                     "-fx-background-radius: 20; -fx-padding: 8 20; -fx-font-size: 13px; -fx-cursor: hand;";
        btn.setStyle(off);
        btn.setOnMouseEntered(e -> btn.setStyle(on));
        btn.setOnMouseExited(e -> btn.setStyle(off));
        return btn;
    }

    private String colunaStyle() {
        return "-fx-background-color: #111125; -fx-background-radius: 14;" +
               "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.55), 18, 0, 0, 6);";
    }

    private String colunaStyleHover(String cor) {
        return "-fx-background-color: #161632; -fx-background-radius: 14;" +
               "-fx-border-color: " + cor + "; -fx-border-width: 2; -fx-border-radius: 14;" +
               "-fx-effect: dropshadow(gaussian, " + cor + "55, 22, 0, 0, 0);";
    }

    private String cardStyle(boolean hover, String cor) {
        if (hover) return
            "-fx-background-color: #1E1E38; -fx-background-radius: 10;" +
            "-fx-border-color: " + cor + "55; -fx-border-width: 1; -fx-border-radius: 10;" +
            "-fx-cursor: hand; -fx-effect: dropshadow(gaussian, " + cor + "33, 10, 0, 0, 3);";
        return "-fx-background-color: #181832; -fx-background-radius: 10;" +
               "-fx-border-color: #1E1E38; -fx-border-width: 1; -fx-border-radius: 10; -fx-cursor: hand;";
    }
}