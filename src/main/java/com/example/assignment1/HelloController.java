package com.example.assignment1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class HelloController {

    @FXML
    private Button closeButton;

    // handle action when close button is clicked
    @FXML
    private void setCloseButton(ActionEvent event) {
        // Get the current stage and close it
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private Hyperlink surveyLink;

    //handle action when survey link is clicked
    @FXML
    private void openSurveyLink(ActionEvent event) {
        // Get the source hyperlink and open the specified URL in a web browser
        Hyperlink hyperlink = (Hyperlink) event.getSource();
        String url = "https://survey.stackoverflow.co/2023"; // Specify the URL here
        openWebpage(url);
    }

    //open a webpage in the default web browser
    private void openWebpage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private TableView<ProgrammingLanguage> languagesTable;

    @FXML
    private TableColumn<ProgrammingLanguage, Integer> colPosition;

    @FXML
    private TableColumn<ProgrammingLanguage, String> colLanguage;

    @FXML
    private TableColumn<ProgrammingLanguage, Double> colPercentage;

    @FXML
    private TableColumn<ProgrammingLanguage, Integer> colCreation;

    @FXML
    private TableColumn<ProgrammingLanguage, String> colParadigm;

    @FXML
    private PieChart pieChart;

    DatabaseConnector dbConnector = new DatabaseConnector();
    private ObservableList<ProgrammingLanguage> languageList;

    //initialize the controller
    @FXML
    public void initialize() {
        //load data into the table and populate the pie chart
        loadData();
        languagesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        populatePieChart();
    }

    // load data from the database into the table
    private void loadData() {
        try (Connection connection = dbConnector.connect()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ProgrammingLanguages");

            languageList = FXCollections.observableArrayList();
            //iterate through result set and add ProgrammingLanguage objects to the list
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String language = resultSet.getString("Language");
                double percentage = resultSet.getDouble("Percentage");
                String creationYear = resultSet.getInt("Year_of_Creation") != 0 ? resultSet.getString("Year_of_Creation") : "";
                String paradigm = resultSet.getString("Paradigm");

                languageList.add(new ProgrammingLanguage(id, language, percentage, creationYear, paradigm));
            }

            //set cell value factories for table columns
            colPosition.setCellValueFactory(new PropertyValueFactory<>("id"));
            colLanguage.setCellValueFactory(new PropertyValueFactory<>("language"));
            colPercentage.setCellValueFactory(new PropertyValueFactory<>("percentage"));
            colCreation.setCellValueFactory(new PropertyValueFactory<>("creationYear"));
            colParadigm.setCellValueFactory(new PropertyValueFactory<>("paradigm"));

            //set the items in the table
            languagesTable.setItems(languageList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //populate the pie chart with data from the table
    private void populatePieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        //iterate through the language list and add data to the pie chart
        for (ProgrammingLanguage language : languageList) {
            pieChartData.add(new PieChart.Data(language.getLanguage(), language.getPercentage()));
        }
        //set data to the pie chart
        pieChart.setData(pieChartData);

        //adjust label line length and visibility
        pieChart.setLabelLineLength(20);
        pieChart.setLabelsVisible(true);
    }

    @FXML
    private Button button_view;

    private boolean viewChanged = false;

    //handle action when button is clicked to switch views
    @FXML
    void buttonClicked(ActionEvent event) {
        //toggle between table view and pie chart view
        if (viewChanged) {
            //change button graphic, hide pie chart, and show table
            ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/data-management.png"))));
            imageView.setFitHeight(24.0);
            imageView.setFitWidth(24.0);
            button_view.setGraphic(imageView);
            pieChart.setVisible(false);
            languagesTable.setVisible(true);
            viewChanged = false;
        } else {
            //change button graphic, show pie chart, and hide table
            ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/cells.png"))));
            imageView.setFitHeight(24.0);
            imageView.setFitWidth(24.0);
            button_view.setGraphic(imageView);
            pieChart.setVisible(true);
            languagesTable.setVisible(false);
            viewChanged = true;
        }
    }
}