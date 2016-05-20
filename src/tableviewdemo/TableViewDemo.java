/**
 *
 * @author Carlos F. Meneses
 * carlosfmeneses@gmail.com
 * 05/13/2016
 * 
 * R&D TableView, TableColumn and TableCell classes.
 */ 

package tableviewdemo;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
 
public class TableViewDemo extends Application {
 
    private final TableView<Person> table = new TableView<>();
    private final ObservableList<Person> data =
            FXCollections.observableArrayList(
            new Person("Apple", "(800)692–7753", "apple.com"),
            new Person("Google", "(800)877-2981", "google.com"),
            new Person("Facebook", "(888)275-2174 ", "facebook.com"));
    final HBox hb = new HBox();
 
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Demo");
        stage.setWidth(450);
        stage.setHeight(550);
 
        final Label label = new Label("Customers");
        label.setFont(new Font("Arial", 20));
 
        table.setEditable(true);
        Callback<TableColumn, TableCell> cellFactory;
        cellFactory = (TableColumn p) -> new EditingCell();
 
        TableColumn companyCol = new TableColumn("Company");
        companyCol.setMinWidth(100);
        companyCol.setCellValueFactory(
            new PropertyValueFactory<>("companyName"));
        companyCol.setCellFactory(cellFactory);
        companyCol.setOnEditCommit(
            new EventHandler<CellEditEvent<Person, String>>() {
                @Override
                public void handle(CellEditEvent<Person, String> t) {
                    ((Person) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setCompanyName(t.getNewValue());
                }
             }
        );
 
 
        TableColumn phoneCol = new TableColumn("Phone");
        phoneCol.setMinWidth(150);
        phoneCol.setCellValueFactory(
            new PropertyValueFactory<>("phoneNumber"));
        phoneCol.setCellFactory(cellFactory);
        phoneCol.setOnEditCommit(
            new EventHandler<CellEditEvent<Person, String>>() {
                @Override
                public void handle(CellEditEvent<Person, String> t) {
                    ((Person) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setPhoneNumber(t.getNewValue());
                }
            }
        );
 
        TableColumn websiteCol = new TableColumn("Website");
        websiteCol.setMinWidth(150);
        websiteCol.setCellValueFactory(
            new PropertyValueFactory<>("website"));
        websiteCol.setCellFactory(cellFactory);
        websiteCol.setOnEditCommit(
            new EventHandler<CellEditEvent<Person, String>>() {
                @Override
                public void handle(CellEditEvent<Person, String> t) {
                    ((Person) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setWebsite(t.getNewValue());
                }
            }
        );
 
        table.setItems(data);
        table.getColumns().addAll(companyCol, phoneCol, websiteCol);
 
        final TextField addCompany = new TextField();
        addCompany.setPromptText("Company");
        addCompany.setMaxWidth(companyCol.getPrefWidth());
        final TextField addPhone = new TextField();
        addPhone.setMaxWidth(phoneCol.getPrefWidth());
        addPhone.setPromptText("Phone");
        final TextField addWebsite = new TextField();
        addWebsite.setMaxWidth(websiteCol.getPrefWidth());
        addWebsite.setPromptText("Website");
 
        final Button addButton = new Button("Add");
        addButton.setOnAction((ActionEvent e) -> {
            data.add(new Person(
                    addCompany.getText(),
                    addPhone.getText(),
                    addWebsite.getText()));
            addCompany.clear();
            addPhone.clear();
            addWebsite.clear();
        });
 
        hb.getChildren().addAll(addCompany, addPhone, addWebsite, addButton);
        hb.setSpacing(3);
 
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hb);
 
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
 
        stage.setScene(scene);
        stage.show();
    }
 
    public static class Person {
 
        private final SimpleStringProperty companyName;
        private final SimpleStringProperty phoneNumber;
        private final SimpleStringProperty website;
 
        private Person(String cName, String pNumber, String wSite) {
            this.companyName = new SimpleStringProperty(cName);
            this.phoneNumber = new SimpleStringProperty(pNumber);
            this.website = new SimpleStringProperty(wSite);
        }
 
        public String getCompanyName() {
            return companyName.get();
        }
 
        public void setCompanyName(String cName) {
            companyName.set(cName);
        }
 
        public String getPhoneNumber() {
            return phoneNumber.get();
        }
 
        public void setPhoneNumber(String pNumber) {
            phoneNumber.set(pNumber);
        }
 
        public String getWebsite() {
            return website.get();
        }
 
        public void setWebsite(String wSite) {
            website.set(wSite);
        }
    }
 
    class EditingCell extends TableCell<Person, String> {
 
        private TextField textField;
 
        public EditingCell() {
        }
 
        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }
 
        @Override
        public void cancelEdit() {
            super.cancelEdit();
 
            setText((String) getItem());
            setGraphic(null);
        }
 
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
 
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }
 
        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
            textField.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) -> {
                if (!arg2) {
                    commitEdit(textField.getText());
                }
            });
        }
 
        private String getString() {
            return getItem() == null ? "" : getItem();
        }
    }
}