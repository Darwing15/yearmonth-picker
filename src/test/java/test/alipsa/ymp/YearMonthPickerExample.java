package test.alipsa.ymp;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Locale;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import se.alipsa.ymp.YearMonthPicker;
import se.alipsa.ymp.YearMonthPickerCombo;

public class YearMonthPickerExample extends Application {

    @Override
    public void start(Stage stage) {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));

        YearMonthPicker ymp = new YearMonthPicker("yyyy/MM");
        ymp.setOnAction(a -> System.out.println("Default YearMonthPicker, Value picked was " + ymp.getValue()));
        HBox hbox = new HBox(new Label("YearMonthPicker: "), ymp);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(10));
        vBox.getChildren().add(hbox);

        YearMonthPicker picker = new YearMonthPicker(LocalDate.of(2019, 1, 1), LocalDate.of(2020, 8, 1),
        		LocalDate.of(2019,12,1), Locale.SIMPLIFIED_CHINESE, "MMMM", "yyyy MMMM");
        picker.setOnAction(a -> System.out.println("Chinese YearMonthPicker, Value picked was " + picker.getValue()));
        HBox hbox2 = new HBox(new Label("YearMonthPicker: "), picker);
        hbox2.setAlignment(Pos.CENTER_LEFT);
        hbox2.setPadding(new Insets(10));
        vBox.getChildren().add(hbox2);

        YearMonthPickerCombo ympc = new YearMonthPickerCombo();
        ympc.setOnAction(a -> System.out.println("YearMonthPickerCombo: value picked was " + ympc.getValue()));
        HBox cboBox = new HBox(new Label("YearMonthPickerCombo: "), ympc);
        cboBox.setAlignment(Pos.CENTER_LEFT);
        cboBox.setPadding(new Insets(10));
        vBox.getChildren().add(cboBox);

        YearMonthPickerCombo ympc2 = new YearMonthPickerCombo(YearMonth.now().minusYears(2),
                YearMonth.now().plusYears(2), YearMonth.now(), Locale.forLanguageTag("sv-SE"), "MMMM yy");
        ympc2.setOnAction(a -> System.out.println("YearMonthPickerCombo: value picked was " + ympc2.getValue()));
        HBox cboBox2 = new HBox(new Label("YearMonthPickerCombo: "), ympc2);
        cboBox2.setAlignment(Pos.CENTER_LEFT);
        cboBox2.setPadding(new Insets(10));
        vBox.getChildren().add(cboBox2);

        Scene scene = new Scene(vBox, 350, 220);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
