package se.alipsa.ymp;

import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class YearMonthCell extends ListCell<LocalDate> {

  private final DateTimeFormatter dateFormat;

  YearMonthCell(Locale locale, String format) {
    dateFormat = DateTimeFormatter.ofPattern(format, locale);
  }

  @Override
  protected void updateItem(LocalDate item, boolean empty) {
    super.updateItem(item, empty);
    
    if (empty || item == null) {
      setText(null);
    } else {
    	if (item.getMonthValue() >= LocalDate.now().getMonthValue() + 1 && item.getYear() == LocalDate.now().getYear()) {
        	setDisable(true);
        	setTextFill(Color.LIGHTGRAY);
        } else {
        	setDisable(false);
        	setTextFill(Color.BLACK);
        }
    	setText(dateFormat.format(item).toUpperCase());
    }
  }
}
