/* 
 * This file is part of the QuickAnno application (https://github.com/buzzdee93/quickanno).
 * Copyright (c) 2019 Sebastian Gombert.
 * 
 * This program is free software: you can redistribute it and/or modify  
 * it under the terms of the GNU General Public License as published by  
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package de.sebastiangombert.gui;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;

import de.sebastiangombert.annotators.LegacyToNewDeclarativeConverter;
import de.sebastiangombert.annotators.MLToCorpusClassificationAnnotator;
import de.sebastiangombert.ml.MLSet;
import de.sebastiangombert.model.Corpus;
import de.sebastiangombert.model.TextContainer;
import de.sebastiangombert.reader.CorpusReader;
import de.sebastiangombert.reader.NewVRTReader;
import de.sebastiangombert.reader.LegacyVRTReader;
import de.sebastiangombert.writer.CorpusWriter;
import de.sebastiangombert.writer.MLSetConsumer;
import de.sebastiangombert.writer.VRTWriter;


import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpNamedEntityRecognizer;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpSegmenter;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import weka.classifiers.trees.LMT;
import weka.core.converters.*;
import javafx.stage.Stage;

public class GuiController {
	
	public int contextNumSentences = 15;
	private static final String LB = System.lineSeparator();
	
	//List<Sentence> sentences = new ArrayList<>();
	int index = 0;
	
	Corpus corpus = new Corpus();
	boolean loadedCorpus = false;
	
	Stage primary;
	
	@FXML
	TextArea leftContextArea;
	
	@FXML
	TextArea rightContextArea;
	
	@FXML
	TextArea sentenceArea;
	
	@FXML
	Slider contextSizeSlider;
	
	@FXML
	TextField contextSize;
	
	@FXML
	HTMLEditor guidelineEditor;
	
	@FXML
	WebView guidelines;
	
	@FXML
	Label progressLabel;
	
	@FXML
	Label textLabel;
	
	@FXML
	Tab annotationTab;
	
	@FXML
	Tab guidelinesTab;
	
	@FXML
	Tab interAnnotatorTab;
	
	@FXML
	TabPane tabs;
	
	@FXML
	TableView<String[]> textParamsTable;
	
	@FXML
	TableColumn<String[], String> textParams;
	
	@FXML
	TableColumn<String[], String> textParamsValues;
	
	@FXML
	TableView<String[]> interAnnotatorTable;
	
	@FXML
	RadioButton allSentences;
	
	@FXML
	RadioButton matchingSentences;
	
	@FXML
	RadioButton differingSentences;
	
	@FXML
	Button proposeAnnotationsButton;
	
	@FXML
	WebView mlGuidelines;
	
	private List<String[]> iaaLines = new ArrayList<>();
	
	private void solveBlurriness(TextArea area) {
		area.setCache(false);
		ScrollPane sp = (ScrollPane)area.getChildrenUnmodifiable().get(0);
		sp.setCache(false);
		for (Node n : sp.getChildrenUnmodifiable()) {
		    n.setCache(false);
		}
	}
	
	public void init(Stage primary) {
		this.primary = primary;
		
		this.guidelines.getEngine().loadContent(guidelineEditor.getHtmlText());
		this.mlGuidelines.getEngine().loadContent(guidelineEditor.getHtmlText());
		
		guidelineEditor.addEventHandler(InputEvent.ANY, e -> {
			guidelines.getEngine().loadContent(guidelineEditor.getHtmlText());
			mlGuidelines.getEngine().loadContent(this.guidelineEditor.getHtmlText());
		});
		
		contextSizeSlider.valueProperty().addListener((o1, o2, o3) -> {
			contextNumSentences = (int)contextSizeSlider.getValue();
			updateDisplay();
		});
		
		textParams.setCellValueFactory(p -> new SimpleStringProperty(p.getValue()[0]));
		textParamsValues.setCellValueFactory(p -> new SimpleStringProperty(p.getValue()[1]));
		
		Platform.runLater(() -> {
			solveBlurriness(leftContextArea);
			solveBlurriness(sentenceArea);
			solveBlurriness(rightContextArea);
			
			primary.getScene().setOnKeyPressed(ke -> {
				if (annotationTab.isSelected()) {
					if (ke.getCode() == KeyCode.Q)					
						klickedYes(ke);
					else if (ke.getCode() == KeyCode.W)
						klickedMaybe(ke);
					else if (ke.getCode() == KeyCode.E)
						klickedNo(ke);
					else if (ke.getCode() == KeyCode.R)
						klickedBack(ke);
				}
			});
		});
	}
	
	@FXML
	public void switchToGuidelinesEditor(Event e) {
		this.tabs.getSelectionModel().select(guidelinesTab);
	}

	@FXML
	public void loadUnprocessed(Event e) {
		this.tabs.getSelectionModel().select(annotationTab);
		 FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Open File");
		 fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("VRT File", "*.vrt", "*.VRT"));
		 List<File> files = fileChooser.showOpenMultipleDialog(primary);
		 if (files.size() > 0) {
		        try {
		        	this.corpus = new Corpus();
		        	
					CollectionReader vrtReader = createReader(NewVRTReader.class, NewVRTReader.PARAM_LIST_FILES,
					        files, NewVRTReader.IGNORE_SENTENCES, false, NewVRTReader.IGNORE_POS_TAGS, false);
					
					AnalysisEngine sentenceSplitter = createEngine(OpenNlpSegmenter.class, 
							OpenNlpSegmenter.PARAM_LANGUAGE, "de",
							OpenNlpSegmenter.PARAM_WRITE_TOKEN, false);
					AnalysisEngine corpusWriter = createEngine(CorpusWriter.class,
							CorpusWriter.PARAM_CORPUS, this.corpus.getId());
					
					SimplePipeline.runPipeline(vrtReader,sentenceSplitter, corpusWriter);
					this.index = 0;
					loadedCorpus = true;
				} catch (ResourceInitializationException e1) {
					e1.printStackTrace();
					Alert alert = new Alert(AlertType.CONFIRMATION, "Error. Could not open file(s)", ButtonType.CANCEL);
					alert.showAndWait();

					if (alert.getResult() == ButtonType.CANCEL) {
						Platform.exit();
					}
				} catch (UIMAException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		 }
		 displaySentenceAndContext(e);
	}
	
	
	
	@FXML
	public void loadProcessed(Event e) {
		this.tabs.getSelectionModel().select(annotationTab);
		 FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Open File");
		 fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("VRT File", "*.vrt", "*.VRT"));
		 List<File> files = fileChooser.showOpenMultipleDialog(primary);
		 if (files.size() > 0) {
		        try {
					this.corpus = new Corpus();
		        	
					CollectionReader vrtReader = createReader(NewVRTReader.class, NewVRTReader.PARAM_LIST_FILES,
					        files, NewVRTReader.IGNORE_SENTENCES, false, NewVRTReader.IGNORE_POS_TAGS, false);
					
					AnalysisEngine corpusWriter = createEngine(CorpusWriter.class,
							CorpusWriter.PARAM_CORPUS, this.corpus.getId());
					
					SimplePipeline.runPipeline(vrtReader, corpusWriter);

					loadedCorpus = true;
				} catch (ResourceInitializationException e1) {
					e1.printStackTrace();
					Alert alert = new Alert(AlertType.CONFIRMATION, "Error. Could not open file(s)", ButtonType.CANCEL);
					alert.showAndWait();

					if (alert.getResult() == ButtonType.CANCEL) {
						Platform.exit();
					}
				} catch (UIMAException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		 }
		 setIndexToBreakAnchorPoint();
		 displaySentenceAndContext(e);		
	}
	
	@FXML
	public void loadLegacy(Event e) {
		this.tabs.getSelectionModel().select(annotationTab);
		 FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Open File");
		 fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("VRT File", "*.vrt", "*.VRT"));
		 File file = fileChooser.showOpenDialog(primary);
		 if (file != null) {
		        try {
		        	this.corpus = new Corpus();
		        	
					CollectionReader vrtReader = createReader(LegacyVRTReader.class, LegacyVRTReader.PARAM_LIST_FILE,
					        file);
					
					AnalysisEngine sentenceSplitter = createEngine(OpenNlpSegmenter.class, 
							OpenNlpSegmenter.PARAM_LANGUAGE, "de",
							OpenNlpSegmenter.PARAM_WRITE_TOKEN, false);
					
					AnalysisEngine legacyToNew = createEngine(LegacyToNewDeclarativeConverter.class);
					
					AnalysisEngine corpusWriter = createEngine(CorpusWriter.class,
							CorpusWriter.PARAM_CORPUS, this.corpus.getId());
					
					SimplePipeline.runPipeline(vrtReader, sentenceSplitter, legacyToNew, corpusWriter);
					this.index = 0;
					loadedCorpus = true;
				} catch (ResourceInitializationException e1) {
					e1.printStackTrace();
					Alert alert = new Alert(AlertType.CONFIRMATION, "Error. Could not open file(s)", ButtonType.CANCEL);
					alert.showAndWait();

					if (alert.getResult() == ButtonType.CANCEL) {
						Platform.exit();
					}
				} catch (UIMAException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		 }
		 displaySentenceAndContext(e);			
	}
	
	@FXML
	public void klickedYes(Event e) {
		if (this.corpus.getSentenceIndices().size() > 0) {
			if (this.corpus.getMaybeIndices().contains(this.corpus.getSentenceIndices().get(index)))
				this.corpus.getMaybeIndices().remove(this.corpus.getSentenceIndices().get(index));
			
			if (!this.corpus.getDeclIndices().contains(this.corpus.getSentenceIndices().get(index)))
				this.corpus.getDeclIndices().add(this.corpus.getSentenceIndices().get(index));
			if (index < this.corpus.getSentenceIndices().size()) index++;
			
			displaySentenceAndContext(e);
		}
	}

	
	@FXML
	public void klickedNo(Event e) {
		if (this.corpus.getSentenceIndices().size() > 0) {
			
			if (this.corpus.getDeclIndices().contains(this.corpus.getSentenceIndices().get(index)))
				this.corpus.getDeclIndices().remove(this.corpus.getSentenceIndices().get(index));
			
			if (this.corpus.getMaybeIndices().contains(this.corpus.getSentenceIndices().get(index)))
				this.corpus.getMaybeIndices().remove(this.corpus.getSentenceIndices().get(index));
			
			if (index < this.corpus.getSentenceIndices().size()) index++;
			displaySentenceAndContext(e);
		}		
	}
	
	@FXML
	public void klickedMaybe(Event e) {
		if (this.corpus.getSentenceIndices().size() > 0) {
			if (this.corpus.getDeclIndices().contains(this.corpus.getSentenceIndices().get(index)))
				this.corpus.getDeclIndices().remove(this.corpus.getSentenceIndices().get(index));
			
			if (!this.corpus.getMaybeIndices().contains(this.corpus.getSentenceIndices().get(index)))
				this.corpus.getMaybeIndices().add(this.corpus.getSentenceIndices().get(index));
			if (index < this.corpus.getSentenceIndices().size()) index++;
			displaySentenceAndContext(e);
		}
	}
	
	@FXML
	public void klickedBack(Event e) {	
		if (index > 0) {
			index--;
			displaySentenceAndContext(e);
		}
	}
	
	private void setIndexToBreakAnchorPoint() {
		if (this.corpus.getBreakIndices() != null) {
			for (int[] sentenceIndices : this.corpus.getSentenceIndices()) {
				if (sentenceIndices[0] == this.corpus.getBreakIndices()[0] && sentenceIndices[1] == this.corpus.getBreakIndices()[1])
					break;
				else
					this.index++;
			}
		}
	}
	
	@FXML
	public boolean klickedSave(Event e) {
		boolean ret = true;
		if (this.corpus.getSentenceIndices().size() > 0) {
			 FileChooser fileChooser = new FileChooser();
			 fileChooser.setTitle("Save File");
			 fileChooser.getExtensionFilters().addAll(
			         new ExtensionFilter("VRT File", "*.vrt", "*.VRT"),
			         new ExtensionFilter("All Files", "*.*"));
			 File fl = fileChooser.showSaveDialog(primary);
			 if (fl != null) {
				 
				 if (this.index < this.corpus.getSentenceIndices().size() -1)
					 this.corpus.setBreakIndices(this.corpus.getSentenceIndices().get(index)[0], this.corpus.getSentenceIndices().get(index)[1]);
				 
				 try {
					CollectionReader corpusReader = createReader(CorpusReader.class, 
							 CorpusReader.PARAM_CORPUS, this.corpus.getId());
					
					AnalysisEngine vrtWriter = createEngine(VRTWriter.class,
							VRTWriter.PARAM_FILE, fl);
					
					SimplePipeline.runPipeline(corpusReader, vrtWriter); 
					
					ret = true;
				} catch (ResourceInitializationException e1) {

					e1.printStackTrace();
				} catch (UIMAException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			 } else {
				 ret = false;
				 displaySentenceAndContext(e);
			 }
			 
			 return ret;
		} return ret;
	}
	
	@FXML
	public void changeContextSizes(ScrollEvent e) {
		
	}
	
	@FXML
	public void displaySentenceAndContext(Event e) {
		
		if (this.corpus.getSentenceIndices().size() > 0) {
			
			if (index < this.corpus.getSentenceIndices().size()) {		
				updateDisplay();
			} else {
				if (klickedSave(e)) {
					this.index = 0;
					this.corpus = new Corpus();
					leftContextArea.setText("");
					sentenceArea.setText("");
					rightContextArea.setText("");
				}
				
			}
		}
	}
	
	private int getProgress() {
		return this.corpus.getSentenceIndices().size() > 0 ? (index * 100) / this.corpus.getSentenceIndices().size() : 0; 
	}
	
	private String[] getTextColumn(String key, String value) {
		String[] ret = new String[2];
		ret[0] = key;
		ret[1] = value;
		return ret;
	}
	
	private void updateTextData() {
		this.textParamsTable.getItems().clear();
		if (this.corpus.getTextList().size() > 0) {
			int[] sentenceIndices = this.corpus.getSentenceIndices().get(index);
			int i = 1;
			for (TextContainer text : this.corpus.getTextList()) {
				if (sentenceIndices[0] >= text.getBegin() && sentenceIndices[1] <= text.getEnd()) {
					this.textParamsTable.getItems().add(getTextColumn("date", text.getTextDate()));
					this.textParamsTable.getItems().add(getTextColumn("id", text.getTextId()));
					this.textParamsTable.getItems().add(getTextColumn("inhaltsverzeichnis", text.getTextVz()));
					this.textParamsTable.getItems().add(getTextColumn("legislaturperiode", text.getTextLegislature()));
					this.textParamsTable.getItems().add(getTextColumn("party", text.getTextParty()));
					this.textParamsTable.getItems().add(getTextColumn("specher", text.getTextSpeaker()));
					this.textParamsTable.getItems().add(getTextColumn("year", text.getTextYear()));
					this.textParamsTable.getItems().add(getTextColumn("yearmonth", text.getTextYearMonth()));
					this.textLabel.setText("Text: " + i + "/" + this.corpus.getTextList().size());
					break;
				}
				i++;
			}
		} else {
			this.textLabel.setText("0/0");
		}
	}
	
	private String getSentencePrefix(int i) {
		for (int[] declIndices : this.corpus.getDeclIndices())
			if (declIndices[0] == this.corpus.getSentenceIndices().get(i)[0]
					&& declIndices[1] == this.corpus.getSentenceIndices().get(i)[1])
				return "[Deklarativ] ";
		
		for (int[] maybeIndices : this.corpus.getMaybeIndices())
			if (maybeIndices[0] == this.corpus.getSentenceIndices().get(i)[0]
					&& maybeIndices[1] == this.corpus.getSentenceIndices().get(i)[1])
				return "[Maybe] ";		
		
		return "[Nicht annotiert] ";
	}
	
	private void updateDisplay() {
		contextSize.setText(""+this.contextNumSentences);
		
		progressLabel.setText("Fortschritt: " + getProgress() + "%");
		
		if (this.corpus.getSentenceIndices().size() > 0)
			sentenceArea.setText(getSentencePrefix(index) + this.corpus.getCorpusText().substring(this.corpus.getSentenceIndices().get(index)[0], 
					this.corpus.getSentenceIndices().get(index)[1]));
		
		StringBuilder leftContextBuilder = new StringBuilder();
		for (int i = index - this.contextNumSentences; i < index; i++) {
			if (i >= 0) {
				leftContextBuilder.append(getSentencePrefix(i) + this.corpus.getCorpusText().substring(this.corpus.getSentenceIndices().get(i)[0], 
						this.corpus.getSentenceIndices().get(i)[1]));
				leftContextBuilder.append(LB);
				leftContextBuilder.append(LB);
			}
		}
		
		StringBuilder rightContextBuilder = new StringBuilder();
		for (int i = index + 1; i < index + this.contextNumSentences + 1 && i < this.corpus.getSentenceIndices().size(); i++) {
			rightContextBuilder.append(getSentencePrefix(i) + this.corpus.getCorpusText().substring(this.corpus.getSentenceIndices().get(i)[0], 
					this.corpus.getSentenceIndices().get(i)[1]));
			rightContextBuilder.append(LB);
			rightContextBuilder.append(LB);
		}
		
		this.leftContextArea.setText(leftContextBuilder.toString());
		this.rightContextArea.setText(rightContextBuilder.toString());
		this.leftContextArea.setScrollTop(Double.MAX_VALUE);
		
		this.updateTextData();
	}

	@FXML
	public void openGuidelines(Event e) {
		 FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Guidelines öffnen");
		 fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("HTML", "*.html", "*.htm", "*.HTML", "*.HTM"),
		         new ExtensionFilter("TXT", "*.txt", ".TXT"));
		 File file = fileChooser.showOpenDialog(primary);
		 
		 if (file != null) {
			 try {
				BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf8"));
				
				StringBuilder build = new StringBuilder();
				String ln;
				while ((ln = read.readLine()) != null)
					build.append(ln);
				read.close();
				
				this.guidelineEditor.setHtmlText(build.toString());
				this.guidelines.getEngine().loadContent(this.guidelineEditor.getHtmlText());
				this.mlGuidelines.getEngine().loadContent(this.guidelineEditor.getHtmlText());
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 }
	}
	
	@FXML
	public void saveGuidelines(Event e) {
		 FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Guidelines öffnen");
		 fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("HTML", "*.html", "*.htm", "*.HTML", "*.HTM"),
		         new ExtensionFilter("TXT", "*.txt", ".TXT"));
		 File file = fileChooser.showSaveDialog(primary);
		 
		 if (file != null) {
			 try {
				BufferedWriter write = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf8"));
				
				write.write(this.guidelineEditor.getHtmlText());
				write.close();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 }
	}
	
	@FXML
	public void openIAACorpora() {
		this.tabs.getSelectionModel().select(this.interAnnotatorTab);
		
		 FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Save File");
		 fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("VRT File", "*.vrt", "*.VRT"),
		         new ExtensionFilter("All Files", "*.*"));
		 List<File> files = fileChooser.showOpenMultipleDialog(primary);
		 
		 List<Corpus> iaaCorpora = new ArrayList<>();
		 List<String> corporaNames = new ArrayList<>();
		 for (File file : files) {
		        try {
					Corpus corpus = new Corpus();
					
					List<File> singleFileList = new ArrayList<>();
					singleFileList.add(file);
		        	
					CollectionReader vrtReader = createReader(NewVRTReader.class, NewVRTReader.PARAM_LIST_FILES,
					        singleFileList, NewVRTReader.IGNORE_SENTENCES, false, NewVRTReader.IGNORE_POS_TAGS, false);
					
					AnalysisEngine corpusWriter = createEngine(CorpusWriter.class,
							CorpusWriter.PARAM_CORPUS, corpus.getId());
					
					SimplePipeline.runPipeline(vrtReader, corpusWriter);
					
					iaaCorpora.add(corpus);
					corporaNames.add(file.getName());
					System.out.println("Corpus: " + corpus.getId());
				} catch (ResourceInitializationException e1) {
					e1.printStackTrace();
					Alert alert = new Alert(AlertType.ERROR, "Error. Could not open file(s)", ButtonType.CANCEL);
					alert.showAndWait();

					if (alert.getResult() == ButtonType.CANCEL) {
						Platform.exit();
					}
				} catch (UIMAException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}			 
		 }
		 
		 if (iaaCorpora.size() > 0) {
			 interAnnotatorTable.getColumns().clear();
			 interAnnotatorTable.getItems().clear();
			 this.iaaLines.clear();
			 
			 interAnnotatorTable.getColumns().add(new TableColumn<String[], String>("Satz"));
			 
			 TableColumn<String[], String> sentenceCol = 
					 (TableColumn<String[], String>) interAnnotatorTable.getColumns().get(0);
			 
			 sentenceCol.setCellFactory(new Callback<TableColumn<String[], String>, TableCell<String[], String>>() {
			        public TableCell<String[], String> call(TableColumn<String[], String> param) {
			            return new TableCell<String[], String>() {
			                @Override
			                public void updateItem(String item, boolean empty) {
			                    super.updateItem(item, empty);
			                    this.setTextFill(Color.web("#c8d5d5"));
			                    this.setText(item);
			                }
			            };
			        }
			    });
			 
			 sentenceCol.setCellValueFactory(p -> {
				 return new SimpleStringProperty(p.getValue()[0]);
			 });
			 
			 sentenceCol.setPrefWidth(400);
			 
			 for (int i = 0; i < iaaCorpora.size(); i++) {
				 interAnnotatorTable.getColumns().add(new TableColumn<String[], String>(corporaNames.get(i)));
				 
				 TableColumn<String[], String> col = 
						 (TableColumn<String[], String>) interAnnotatorTable.getColumns().get(interAnnotatorTable.getColumns().size() -1);
				 
				 final int idx = i +1;
				 
				 col.setCellFactory(new Callback<TableColumn<String[], String>, TableCell<String[], String>>() {
				        public TableCell<String[], String> call(TableColumn<String[], String> param) {
				            return new TableCell<String[], String>() {
				                @Override
				                public void updateItem(String item, boolean empty) {
				                    super.updateItem(item, empty);
				                    if (!isEmpty() && item.equals("Declarative")) {
				                        this.setTextFill(Color.YELLOW);
				                    } else {
				                    	this.setTextFill(Color.web("#c8d5d5"));
				                    }
				                    this.setText(item);
				                }
				            };
				        }
				    });
				 
				 col.setCellValueFactory(p -> {
					 return new SimpleStringProperty(p.getValue()[idx]);
				 });
			 }
			 
			 Corpus first = iaaCorpora.get(0);
			 
			 for (int i = 0; i < first.getSentenceIndices().size(); i++) {
				 String[] line = new String[iaaCorpora.size() +1];
				 line[0] = first.getCorpusText().substring(first.getSentenceIndices().get(i)[0], 
						 first.getSentenceIndices().get(i)[1]);
				 for (int j = 0; j < iaaCorpora.size(); j++) {
					 Corpus c = iaaCorpora.get(j);
					 int[] sentenceIndices = c.getSentenceIndices().get(i);
					 if (hasAnnotation(sentenceIndices[0], sentenceIndices[1], c.getDeclIndices()))
						 line[j + 1] = "Declarative";
					 else if (hasAnnotation(sentenceIndices[0], sentenceIndices[1], c.getMaybeIndices()))
						 line[j + 1] = "Maybe";
					 else
						 line[j + 1] = "None";
					 
				 }
				 
				 this.iaaLines.add(line);
			 }
			 
			 updateIAAList();
		 } 
	}
	
	@FXML
	public void updateIAAList() {
		interAnnotatorTable.getItems().clear();
		if (this.allSentences.isSelected()) {
			interAnnotatorTable.getItems().addAll(iaaLines);
		} else if (this.matchingSentences.isSelected()) {
			for (String[] iaaLine : this.iaaLines) {
				String first = iaaLine[1];
				boolean matching = true;
				for (int i = 2; i < iaaLine.length; i++) {
					if (!iaaLine[i].equals(first)) {
						 matching = false;
						 break;
					}
				}
				if (matching)
					interAnnotatorTable.getItems().add(iaaLine);
			}
		} else if (this.differingSentences.isSelected()) {
			for (String[] iaaLine : this.iaaLines) {
				String first = iaaLine[1];
				boolean matching = true;
				for (int i = 2; i < iaaLine.length; i++) {
					if (!iaaLine[i].equals(first)) {
						 matching = false;
						 break;
					}
				}
				if (!matching)
					interAnnotatorTable.getItems().add(iaaLine);
			}			
		}
	}
	
	@FXML
	public void exportIAAList(Event e) {
		 FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Save File");
		 fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("CSV File", "*.csv", "*.CSV"),
		         new ExtensionFilter("All Files", "*.*"));
		 File fl = fileChooser.showSaveDialog(primary);
		 
		 if (fl != null) {
			 try {
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fl), "utf8"));
				StringBuilder csvBuild = new StringBuilder();
				csvBuild.append("sep=,");
				csvBuild.append(LB);
				for (String[] iaaLine : this.interAnnotatorTable.getItems()) {
					for (String str : iaaLine) {
						csvBuild.append(StringEscapeUtils.escapeCsv(str));
						csvBuild.append(',');
					}
					csvBuild.deleteCharAt(csvBuild.length() -1);
					csvBuild.append(LB);
				}
				writer.write(csvBuild.toString());
				writer.close();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 }
	}
	
	private boolean hasAnnotation(int sentenceBegin, int sentenceEnd, List<int[]> declIndices) {
		for (int[] declIndex : declIndices)
			if (declIndex[0] == sentenceBegin && declIndex[1] == sentenceEnd)
				return true;
		return false;
	}
	
	@FXML
	public void generateMLTemp(Event e) {
		 FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Open File");
		 fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("VRT File", "*.vrt", "*.VRT"));
		 List<File> files = fileChooser.showOpenMultipleDialog(primary);
		 if (files.size() > 0) {
			 Platform.runLater(() -> {
			        try {
			        	MLSet mlset = new MLSet();

						CollectionReader vrtReader = createReader(NewVRTReader.class, NewVRTReader.PARAM_LIST_FILES,
						        files, NewVRTReader.IGNORE_SENTENCES, false, NewVRTReader.IGNORE_POS_TAGS, false);
						
						AnalysisEngine ner = createEngine(OpenNlpNamedEntityRecognizer.class, OpenNlpNamedEntityRecognizer.PARAM_VARIANT, "nemgp");
						
						AnalysisEngine mlConsumer = createEngine(MLSetConsumer.class,
								MLSetConsumer.PARAM_ML_SET, mlset.getId(),
								MLSetConsumer.PARAM_CREATE_TRAINING_SET, true,
								MLSetConsumer.PARAM_PERCENT_TOTAL, (float)0.01,
								MLSetConsumer.PARAM_PERCENT_DECLARATIVE, (float)0.007,
								MLSetConsumer.PARAM_NUM_WORD_FEATURES, 30);
						
						SimplePipeline.runPipeline(vrtReader, ner, mlConsumer);


					} catch (ResourceInitializationException e1) {
						e1.printStackTrace();
						Alert alert = new Alert(AlertType.CONFIRMATION, "Error. Could not open file(s)", ButtonType.CANCEL);
						alert.showAndWait();

						if (alert.getResult() == ButtonType.CANCEL) {
							Platform.exit();
						}
					} catch (UIMAException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			 });
		 }
		 

	}
	
	private MLSet mlModelSet = null;
	
	 @FXML
	 Label trainingCorpusLabel;
	 
	 @FXML
	 Label accuracyLabel;
	 
	 @FXML
	 Label precisionDeclarativeLabel;
	 
	 @FXML
	 Label recallDeclarativeLabel;
	 
	 @FXML
	 Label f1DeclarativeLabel;
	 
	 @FXML
	 Label precisionNoneLabel;
	 
	 @FXML
	 Label recallNoneLabel;
	 
	 @FXML
	 Label f1NoneLabel;
	
	 @FXML
	 public void openTrainingCorpus(Event e) {
		 FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Open File");
		 fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("VRT File", "*.vrt", "*.VRT"));
		 List<File> files = fileChooser.showOpenMultipleDialog(primary);
		 
		 if (files.size() > 0) {
		        try {
		        	this.mlModelSet = new MLSet();
		        	this.mlModelSet.setFileName(RandomStringUtils.random(25));

					CollectionReader vrtReader = createReader(NewVRTReader.class, NewVRTReader.PARAM_LIST_FILES,
					        files, NewVRTReader.IGNORE_SENTENCES, false, NewVRTReader.IGNORE_POS_TAGS, false);
					
					AnalysisEngine ner = createEngine(OpenNlpNamedEntityRecognizer.class, OpenNlpNamedEntityRecognizer.PARAM_VARIANT, "nemgp");
					
					AnalysisEngine mlConsumer = createEngine(MLSetConsumer.class,
							MLSetConsumer.PARAM_ML_SET, this.mlModelSet.getId(),
							MLSetConsumer.PARAM_CREATE_TRAINING_SET, true,
							MLSetConsumer.PARAM_PERCENT_TOTAL, (float)0.01,
							MLSetConsumer.PARAM_PERCENT_DECLARATIVE, (float)0.007,
							MLSetConsumer.PARAM_NUM_WORD_FEATURES, 30);
					
					SimplePipeline.runPipeline(vrtReader, ner, mlConsumer);
					
					this.mlModelSet.saveArffTemp(true);
					this.mlModelSet.buildClassificationModel();
					
					StringBuilder trainingCorpusNameBuilder = new StringBuilder();
					for (File fl : files) {
						trainingCorpusNameBuilder.append("\"");
						trainingCorpusNameBuilder.append(fl.getName());
						trainingCorpusNameBuilder.append("\",");
					}
					trainingCorpusNameBuilder.deleteCharAt(trainingCorpusNameBuilder.length() -1);
					
					double truePositives = this.mlModelSet.getEval().correct();
					double all = this.mlModelSet.getTrainingInstances().size();
					double accuracy = truePositives / all;
					
					double precisionDecl = this.mlModelSet.getEval().precision(1);
					double recallDecl = this.mlModelSet.getEval().recall(1);
					double f1Decl = this.mlModelSet.getEval().fMeasure(1);
					
					double precisionNone = this.mlModelSet.getEval().precision(0);
					double recallNone = this.mlModelSet.getEval().recall(0);
					double f1None = this.mlModelSet.getEval().fMeasure(0);
					
					this.trainingCorpusLabel.setText(trainingCorpusNameBuilder.toString());
					
					this.accuracyLabel.setText("Accuracy: " + accuracy);
					
					this.precisionDeclarativeLabel.setText("Precision Declarative: " + precisionDecl);
					this.recallDeclarativeLabel.setText("Recall Declarative: " + recallDecl);
					this.f1DeclarativeLabel.setText("F1 Declarative: " + f1Decl);
					
					this.precisionNoneLabel.setText("Precision None: " + precisionNone);
					this.recallNoneLabel.setText("Recall None: " + recallNone);
					this.f1NoneLabel.setText("F1 None: " + f1None);
					
					this.proposeAnnotationsButton.setDisable(false);
				} catch (ResourceInitializationException e1) {
					e1.printStackTrace();
					Alert alert = new Alert(AlertType.CONFIRMATION, "Error. Could not open file(s)", ButtonType.CANCEL);
					alert.showAndWait();

					if (alert.getResult() == ButtonType.CANCEL) {
						Platform.exit();
					}
				} catch (UIMAException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	 
		 }
	 }
	 
	 private Corpus classificationCorpus = null;
	 
	 @FXML
	 TableView<String[]> mlTable;
	 
	 @FXML
	 TableColumn<String[], String> mlSentenceColumn;
	 
	 @FXML
	 TableColumn<String[], String> mlAnnotationColumn;
	 
	 @FXML
	 public void proposeAnnotations(Event e) {
		 FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Open File");
		 fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("VRT File", "*.vrt", "*.VRT"));
		 List<File> files = fileChooser.showOpenMultipleDialog(primary);
		 
		 if (files.size() > 0) {
		        try {
		        	this.classificationCorpus = new Corpus();
		        	
					CollectionReader vrtReader = createReader(NewVRTReader.class, NewVRTReader.PARAM_LIST_FILES,
					        files, NewVRTReader.IGNORE_SENTENCES, false, NewVRTReader.IGNORE_POS_TAGS, false);
					
					AnalysisEngine sentenceSplitter = createEngine(OpenNlpSegmenter.class, 
							OpenNlpSegmenter.PARAM_LANGUAGE, "de",
							OpenNlpSegmenter.PARAM_WRITE_TOKEN, false);
					
					AnalysisEngine ner = createEngine(OpenNlpNamedEntityRecognizer.class, OpenNlpNamedEntityRecognizer.PARAM_VARIANT, "nemgp");
					
					AnalysisEngine mlConsumer = createEngine(MLSetConsumer.class,
							MLSetConsumer.PARAM_ML_SET, this.mlModelSet.getId(),
							MLSetConsumer.PARAM_CREATE_TRAINING_SET, false,
							MLSetConsumer.PARAM_PERCENT_TOTAL, (float)0.01,
							MLSetConsumer.PARAM_PERCENT_DECLARATIVE, (float)0.007,
							MLSetConsumer.PARAM_NUM_WORD_FEATURES, 30);
					
					AnalysisEngine corpusWriter = createEngine(CorpusWriter.class, CorpusWriter.PARAM_CORPUS, this.classificationCorpus.getId());
					
					SimplePipeline.runPipeline(vrtReader, sentenceSplitter, ner, mlConsumer, corpusWriter);
					
					this.mlModelSet.saveArffTemp(false);
					this.mlModelSet.classify();
					
					CollectionReader corpusReader = createReader(CorpusReader.class, 
							 CorpusReader.PARAM_CORPUS, this.classificationCorpus.getId());
					
					AnalysisEngine declConverter = createEngine(MLToCorpusClassificationAnnotator.class, 
							MLToCorpusClassificationAnnotator.PARAM_ML_SET, this.mlModelSet.getId());
					
					SimplePipeline.runPipeline(corpusReader, declConverter, corpusWriter);
					
					fillMlTable();
				} catch (ResourceInitializationException e1) {
					e1.printStackTrace();
					Alert alert = new Alert(AlertType.CONFIRMATION, "Error. Could not open file(s)", ButtonType.CANCEL);
					alert.showAndWait();

					if (alert.getResult() == ButtonType.CANCEL) {
						Platform.exit();
					}
				} catch (UIMAException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				 
		 }
	 }
	 
	 private void fillMlTable() {
		 this.mlTable.getItems().clear();
		 

		 
		 this.mlSentenceColumn.setCellFactory(new Callback<TableColumn<String[], String>, TableCell<String[], String>>() {
		        public TableCell<String[], String> call(TableColumn<String[], String> param) {
		            return new TableCell<String[], String>() {
		                @Override
		                public void updateItem(String item, boolean empty) {
		                    super.updateItem(item, empty);
		                    this.setTextFill(Color.web("#c8d5d5"));
		                    this.setText(item);
		                }
		            };
		        }
		    });
		 
		 this.mlSentenceColumn.setCellValueFactory(p -> {
			 return new SimpleStringProperty(p.getValue()[0]);
		 });		
		 
		 this.mlAnnotationColumn.setCellFactory(new Callback<TableColumn<String[], String>, TableCell<String[], String>>() {
		        public TableCell<String[], String> call(TableColumn<String[], String> param) {
		        	
		            TableCell<String[], String> cell = new TableCell<String[], String>() {
		                @Override
		                public void updateItem(String item, boolean empty) {
		                    super.updateItem(item, empty);
		                    if (!empty && item.equals("Declarative"))
		                    	this.setTextFill(Color.YELLOW);
		                    else 
		                    	this.setTextFill(Color.web("#c8d5d5"));
		                    this.setText(item);
		                }
		                  
		            };
		            
		            cell.setOnMousePressed(e -> {
                    	if (((String[])cell.getTableRow().getItem())[1].equals("Declarative")) {
                    		cell.setTextFill(Color.web("#c8d5d5"));
                    		((String[])cell.getTableRow().getItem())[1] = "None";
                    		cell.setText("None");
                    	} else {
                    		cell.setTextFill(Color.YELLOW);
                    		((String[])cell.getTableRow().getItem())[1] = "Declarative";
                    		cell.setText("Declarative");
                    	}
                    	
                    });
		            
		            return cell;
		        }
		    });
		 
		 mlAnnotationColumn.setCellValueFactory(p -> {
			 return new SimpleStringProperty(p.getValue()[1]);
		 });
		 
		 for (int[] sentenceIndices : this.classificationCorpus.getSentenceIndices()) {
			 String[] elem = new String[2];
			 
			 elem[0] = this.classificationCorpus.getCorpusText().substring(sentenceIndices[0], sentenceIndices[1]);

			 boolean isDecl = false;
			 for (int[] declIndex : this.classificationCorpus.getDeclIndices()) {
				 if (declIndex[0] == sentenceIndices[0] && declIndex[1] == sentenceIndices[1]) {
					 isDecl = true;
					 break;
				 }
			 }
			 
			 elem[1] = isDecl ? "Declarative" : "None";
			 
			 this.mlTable.getItems().add(elem);
		 }
		 
	 }
	 
	 @FXML
	 public void exportMLAnnotation(Event e) {
		 if (this.classificationCorpus != null) {
			 FileChooser fileChooser = new FileChooser();
			 fileChooser.setTitle("Save File");
			 fileChooser.getExtensionFilters().addAll(
			         new ExtensionFilter("VRT File", "*.vrt", "*.VRT"),
			         new ExtensionFilter("All Files", "*.*"));
			 File fl = fileChooser.showSaveDialog(primary);
			 
			 if (fl != null) {
				 this.classificationCorpus.getDeclIndices().clear();
				 
				 for (int i = 0; i < this.classificationCorpus.getSentenceIndices().size(); i++) {
					 
				 }
				 
				 for (int i = 0; i < this.mlTable.getItems().size(); i++)
					 if (this.mlTable.getItems().get(i)[1].equals("Declarative"))
						 this.classificationCorpus.getDeclIndices().add(this.classificationCorpus.getSentenceIndices().get(i));

				 try {
					CollectionReader corpusReader = createReader(CorpusReader.class, 
							 CorpusReader.PARAM_CORPUS, this.classificationCorpus.getId());
					
					AnalysisEngine vrtWriter = createEngine(VRTWriter.class,
							VRTWriter.PARAM_FILE, fl);
					
					SimplePipeline.runPipeline(corpusReader, vrtWriter); 
				 } catch (Exception ex) {
					 ex.printStackTrace();
				 }
				 
			 }
			 
			 
		 }
	 }
	
}
