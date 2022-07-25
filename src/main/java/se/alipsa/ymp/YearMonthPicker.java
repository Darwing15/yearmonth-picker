package se.alipsa.ymp;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Skin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;

public class YearMonthPicker extends ComboBoxBase<LocalDate> {

    private Locale locale;
    private LocalDate startYearMonth;
    private LocalDate endYearMonth;
    private LocalDate initial;
    private Label inputField, yearLabel;
    private Button yearBackButton, yearForwardButton;
    private String monthPattern;
    private Popup popup;
    private final DateTimeFormatter yearMonthFormatter;
    
    public YearMonthPicker() {
        this(LocalDate.now(), "MM/yyyy");
    }
    
    public YearMonthPicker(String pattern) {
        this(LocalDate.now(), pattern);
    }

    public YearMonthPicker(LocalDate initial, String pattern) {
        this(initial, Locale.getDefault(), pattern);
    }

    public YearMonthPicker(LocalDate initial, Locale locale, String pattern) {
        this(initial.minusYears(6), initial.plusYears(0), initial, locale, "MMMM", pattern);
    }

//    public YearMonthPicker(LocalDate from, LocalDate to, LocalDate initial, Locale locale, String monthPattern, String pattern) {
//        this(from, to, initial, locale, monthPattern, pattern);
//    }

    public YearMonthPicker(LocalDate from, LocalDate to, LocalDate initial, Locale locale, String monthPattern, String yearMonthPattern) {
        getStyleClass().add("year-month-picker");
        setStart(from);
        setEnd(to);
        setInitial(initial);
        setLocale(locale);
        setMonthPattern(monthPattern);
        yearMonthFormatter = DateTimeFormatter.ofPattern(yearMonthPattern, locale);
        setValue(initial);
        valueProperty().addListener(observable -> {
        	createLayout();
        });
    }
    
    private void createLayout() {
        HBox pane = new HBox();

        pane.setAlignment(Pos.CENTER_LEFT);
        pane.setFillHeight(true);
        pane.setStyle("-fx-background-color: #fff; -fx-border-color: #ccc; -fx-border-radius: 3px, 3px;");
        this.getChildren().add(pane);

        inputField = new Label(yearMonthFormatter.format(getValue()));
        inputField.setPrefWidth(100);
        inputField.setFocusTraversable(false);
        inputField.setPadding(new Insets(0,5,0,5));

        pane.getChildren().add(inputField);
        Button pickerButton = new Button();
        inputField.setLabelFor(pickerButton);
        pickerButton.setOnAction(this::handlePopup);
        pane.getChildren().add(pickerButton);
        pickerButton.setGraphic(new ImageView(new Image("calendar.png", 20, 20, true, true)));
        pane.autosize();
    }

    private void handlePopup(ActionEvent a) {
        a.consume(); //need to consume the event, otherwise it is triggering onAction for clients
        showHideSelectBox();
    }

    private void showHideSelectBox() {
        if (popup != null) {
            popup.hide();
            popup = null;
            return;
        }

        popup = new Popup();
        BorderPane selectBox = new BorderPane();
        selectBox.setPrefWidth(140);
        selectBox.setPrefHeight(180);
        //selectBox.setStyle("-fx-background-color:white; -fx-border-color: derive(-fx-color,-23%)");
        selectBox.getStyleClass().addAll("list-view", "combo-box-popup");
        popup.getContent().add(selectBox);

        final ObservableList<LocalDate> items = FXCollections.observableArrayList();
        HBox top = new HBox();
        top.setPadding(new Insets(3));
        top.setAlignment(Pos.CENTER);
        selectBox.setTop(top);
        yearLabel = new Label(String.valueOf(getValue().getYear()));
        yearLabel.setPadding(new Insets(0,7,0,7));
        yearBackButton = new Button("<");
        yearBackButton.setVisible(!yearLabel.getText().equals(String.valueOf(2019)));
        yearBackButton.setOnAction(e -> {
        	yearForwardButton.setVisible(true);
            int yearNum = Year.parse(yearLabel.getText()).minusYears(1).getValue();
            if (yearNum < startYearMonth.getYear()) return;
            yearLabel.setText(String.valueOf(yearNum));
            items.clear();
            for (int i = 1; i <= 12; i++) {
            	items.add(LocalDate.of(yearNum, i, 1));
            }
            yearBackButton.setVisible(!yearLabel.getText().equals(String.valueOf(initial.minusYears(6).getYear())));
        });
        yearForwardButton = new Button(">");
        yearForwardButton.setVisible(!yearLabel.getText().equals(String.valueOf(initial.getYear())));
        yearForwardButton.setOnAction(e -> {
        	yearBackButton.setVisible(true);
            int yearNum = Year.parse(yearLabel.getText()).plusYears(1).getValue();
            if (yearNum > endYearMonth.getYear()) return;
            yearLabel.setText(String.valueOf(yearNum));
            items.clear();
            for (int i = 1; i <= 12; i++) {
                items.add(LocalDate.of(yearNum, i, 1));
            }
            yearForwardButton.setVisible(!yearLabel.getText().equals(String.valueOf(initial.getYear())));
        });
        
        top.getChildren().addAll(yearBackButton, yearLabel, yearForwardButton);

        for (int i = 1; i <= 12; i++) {
            items.add(LocalDate.of(Integer.parseInt(yearLabel.getText()), i, 1));
        }
        ListView<LocalDate> listView = new ListView<>(items);
        selectBox.setCenter(listView);
        listView.setEditable(false);
        listView.setCellFactory(yearMonthListView -> new YearMonthCell(locale, monthPattern));
        listView.getSelectionModel().select(getValue());
        listView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldYearMonth, newYearMonth) -> {
            if (newYearMonth == null) return;
            inputField.setText(yearMonthFormatter.format(newYearMonth));
            setValue(newYearMonth);
            this.fireEvent(new ActionEvent());
            popup.hide();
            popup = null;
        });

        Parent parent = getParent();
        Bounds childBounds = getBoundsInParent();
        Bounds parentBounds = parent.localToScene(parent.getBoundsInLocal());
        double layoutX = childBounds.getMinX() + parentBounds.getMinX() + parent.getScene().getX() + parent.getScene().getWindow().getX() - 10;
        double layoutY = childBounds.getMaxY() + parentBounds.getMinY() + parent.getScene().getY() + parent.getScene().getWindow().getY();
        popup.show(this, layoutX, layoutY);
        popup.requestFocus();
        popup.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1 != aBoolean) {
                popup.hide();
                popup = null;
            }
        });
    }
    
    @Override
    protected Skin<?> createDefaultSkin() {
        return new YearMonthPickerSkin(this);
    }

    public Locale getLocale() {
        return locale;
    }

    private void setLocale(Locale locale) {
        this.locale = locale;
    }

    public LocalDate getStart() {
        return startYearMonth;
    }

    private void setStart(LocalDate start) {
        this.startYearMonth = start;
    }

    public LocalDate getEnd() {
        return endYearMonth;
    }

    private void setEnd(LocalDate end) {
        this.endYearMonth = end;
    }

    public LocalDate getInitial() {
        return initial;
    }

    private void setInitial(LocalDate initial) {
        this.initial = initial;
    }

    public String getMonthPattern() {
        return monthPattern;
    }

    private void setMonthPattern(String monthPattern) {
        this.monthPattern = monthPattern;
    }
}
