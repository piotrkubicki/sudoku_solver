package sudoku_solver;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GUI extends Application {

	private static Solver solver;
	private TableView table = new TableView();
	private Scene scene = new Scene(new VBox());
	private String[] columnNames = {
			"field0", "field1", "field2", "field3", "field4", "field5",
			"field6", "field7", "field8"
	};
	
	private final ObservableList<SudokuRow> rows = FXCollections.observableArrayList(
			new SudokuRow("", "", "", "", "", "", "", "", ""),
			new SudokuRow("", "", "", "", "", "", "", "", ""),
			new SudokuRow("", "", "", "", "", "", "", "", ""),
			new SudokuRow("", "", "", "", "", "", "", "", ""),
			new SudokuRow("", "", "", "", "", "", "", "", ""),
			new SudokuRow("", "", "", "", "", "", "", "", ""),
			new SudokuRow("", "", "", "", "", "", "", "", ""),
			new SudokuRow("", "", "", "", "", "", "", "", ""),
			new SudokuRow("", "", "", "", "", "", "", "", "")
	);
	
	public static void main(String[] args) {
		launch(args);
    }
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Sudoku Solver");
		primaryStage.setWidth(550);
		primaryStage.setHeight(670);
		
		createMenu();
		
		table.setEditable(true);
		table.setMaxWidth(526);
		table.setMinHeight(543);
		
		for (int i = 0; i < sudoku_solver.Parameters.BOARD_SIZE; i++) {
			TableColumn column = new TableColumn();
			column.setCellValueFactory(new PropertyValueFactory<SudokuRow, String>(columnNames[i]));
			
			if ((i + 1) % 3 == 0 && i != 0 && i != 8)
				column.getStyleClass().add("bold-line-vertical");
			
			int col = i;
			
			column.setCellFactory(TextFieldTableCell.forTableColumn());
			column.setOnEditCommit(new EventHandler<CellEditEvent<SudokuRow, String>>() {
				
				@Override
				public void handle(CellEditEvent<SudokuRow, String> t) {
					((SudokuRow) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setField(col, t.getNewValue());
				}
			});
			
			table.getColumns().add(column);
		}
		
		table.setItems(rows);
		
		final Button solveButton = new Button("Solve");
		solveButton.setId("solve-btn");
		
        solveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	
            	for (int i = 0; i < sudoku_solver.Parameters.BOARD_SIZE; i++) {
            		for (int j = 0; j < sudoku_solver.Parameters.BOARD_SIZE; j++) {
            			String value = rows.get(i).getField(j);
            			solver.sudoku.gameboard[i][j].value = Integer.parseInt(value == "" ? "0" : value);
            		}
            	}
            	
            	solver.solve();
            }
        });
		
		final VBox vbox = new VBox();
		vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
		vbox.getChildren().addAll(table, solveButton);

		((VBox) scene.getRoot()).getChildren().addAll(vbox);
		
		scene.getStylesheets().add("styles/styles.css");
		
		primaryStage.setScene(scene);
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				solver.shutdown();
			}
		});

		primaryStage.show();
		
		solver = new Solver();
        solver.addListener(new SudokuSolverListener() {
			@Override
			public void onGameboardChange() {
				update();
			}
        });
        
        int i = 0;
        for (Node n: table.lookupAll("TableRow")) {
			
			if (n instanceof TableRow) {
				i++;
				TableRow row = (TableRow) n;
				
				if (i % 3 == 0 && i != 1 && i != 9)
					row.getStyleClass().add("bold-line-horizontal");;
			}
		}
	}
	
	private void createMenu() {
		MenuBar menuBar = new MenuBar();
		
		Menu menuFile = new Menu("File");
		MenuItem read = new MenuItem("Read");
		read.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				solver.prepareData();
			}
			
		});
		
		MenuItem clear = new MenuItem("Clear");
		clear.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				clear();
			}
			
		});
		
		menuFile.getItems().addAll(read, clear);
		
		menuBar.getMenus().addAll(menuFile);
		
		((VBox) scene.getRoot()).getChildren().addAll(menuBar);
	}
	
	public void clear() {
		ObservableList<SudokuRow> tempRows = FXCollections.observableArrayList(
				new SudokuRow("", "", "", "", "", "", "", "", ""),
				new SudokuRow("", "", "", "", "", "", "", "", ""),
				new SudokuRow("", "", "", "", "", "", "", "", ""),
				new SudokuRow("", "", "", "", "", "", "", "", ""),
				new SudokuRow("", "", "", "", "", "", "", "", ""),
				new SudokuRow("", "", "", "", "", "", "", "", ""),
				new SudokuRow("", "", "", "", "", "", "", "", ""),
				new SudokuRow("", "", "", "", "", "", "", "", ""),
				new SudokuRow("", "", "", "", "", "", "", "", "")
		);
		
		rows.removeAll(rows);
		rows.addAll(tempRows);
	}
	
	public void update() {
		System.out.println("Update");
		ObservableList<SudokuRow> tempRows = FXCollections.observableArrayList(
				new SudokuRow("", "", "", "", "", "", "", "", ""),
				new SudokuRow("", "", "", "", "", "", "", "", ""),
				new SudokuRow("", "", "", "", "", "", "", "", ""),
				new SudokuRow("", "", "", "", "", "", "", "", ""),
				new SudokuRow("", "", "", "", "", "", "", "", ""),
				new SudokuRow("", "", "", "", "", "", "", "", ""),
				new SudokuRow("", "", "", "", "", "", "", "", ""),
				new SudokuRow("", "", "", "", "", "", "", "", ""),
				new SudokuRow("", "", "", "", "", "", "", "", "")
		);
	
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				
				for (int i = 0; i < sudoku_solver.Parameters.BOARD_SIZE; i++) {
					for (int j = 0; j < sudoku_solver.Parameters.BOARD_SIZE; j++) {
						tempRows.get(i).fields[j].set(((Integer)solver.sudoku.gameboard[i][j].value).toString());
					}
				}
				
				rows.removeAll(rows);
				rows.addAll(tempRows);
			}
			
		});
	}

	public class SudokuRow {
		private SimpleStringProperty[] fields;
		private SimpleStringProperty field0;
		private SimpleStringProperty field1;
		private SimpleStringProperty field2;
		private SimpleStringProperty field3;
		private SimpleStringProperty field4;
		private SimpleStringProperty field5;
		private SimpleStringProperty field6;
		private SimpleStringProperty field7;
		private SimpleStringProperty field8;
		
		public SudokuRow(String field0, String field1, String field2,
				String field3, String field4, String field5,
				String field6, String field7, String field8) {
			super();
			this.field0 = new SimpleStringProperty(field0);
			this.field1 = new SimpleStringProperty(field1);
			this.field2 = new SimpleStringProperty(field2);
			this.field3 = new SimpleStringProperty(field3);
			this.field4 = new SimpleStringProperty(field4);
			this.field5 = new SimpleStringProperty(field5);
			this.field6 = new SimpleStringProperty(field6);
			this.field7 = new SimpleStringProperty(field7);
			this.field8 = new SimpleStringProperty(field8);
			
			fields = new SimpleStringProperty[] {
					this.field0, this.field1, this.field2, this.field3, this.field4, this.field5, this.field6,
					this.field7, this.field8
			};
		}
		
		public boolean setField(int col, String newValue) {
			fields[col].set(newValue);
			
			return false;
		}
		
		public String getField(int col) {
			return fields[col].get();
		}

		public String getField0() {
			return field0.get();
		}

		public void setField0(String field0) {
			this.field0.set(field0);
		}

		public String getField1() {
			return field1.get();
		}

		public void setField1(String field1) {
			this.field0.set(field1);
		}

		public String getField2() {
			return field2.get();
		}

		public void setField2(String field2) {
			this.field2.set(field2);
		}

		public String getField3() {
			return field3.get();
		}

		public void setField3(String field3) {
			this.field3.set(field3);
		}

		public String getField4() {
			return field4.get();
		}

		public void setField4(String field4) {
			this.field4.set(field4);
		}

		public String getField5() {
			return field5.get();
		}

		public void setField5(String field5) {
			this.field5.set(field5);
		}

		public String getField6() {
			return field6.get();
		}

		public void setField6(String field6) {
			this.field6.set(field6);
		}

		public String getField7() {
			return field7.get();
		}

		public void setField7(String field7) {
			this.field7.set(field7);
		}

		public String getField8() {
			return field8.get();
		}

		public void setField8(String field8) {
			this.field8.set(field8);
		}
		
	}
}

