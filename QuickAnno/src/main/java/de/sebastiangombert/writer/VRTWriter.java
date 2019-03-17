package de.sebastiangombert.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.sebastiangombert.types.BreakAnchor;
import de.sebastiangombert.types.Declarative;
import de.sebastiangombert.types.Maybe;
import de.sebastiangombert.types.Text;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class VRTWriter extends JCasConsumer_ImplBase {
	public static final String PARAM_FILE = "File";
	@ConfigurationParameter(name = PARAM_FILE,
			description = "Final VRT file.",
			mandatory = true)
	private File file;
	
	private void writeSentences(BufferedWriter buffWrite, JCas aJCas, Collection<Sentence> sentences) throws IOException {
		for (Sentence sent : sentences) {
			
			if (!JCasUtil.selectCovered(aJCas, BreakAnchor.class, sent).isEmpty()) {
				buffWrite.write("!##break");
				buffWrite.newLine();
			}
			
			if (!JCasUtil.selectCovered(aJCas, Declarative.class, sent).isEmpty()) {
				buffWrite.write("<s type=\"declarative\">");
				buffWrite.newLine();
			} else if (!JCasUtil.selectCovered(aJCas, Maybe.class, sent).isEmpty()) {
				buffWrite.write("<s type=\"maybe\">");
				buffWrite.newLine();
			} else {
				buffWrite.write("<s type=\"unspecified\">");
				buffWrite.newLine();
			}
			
			for (Token tk : JCasUtil.selectCovered(aJCas, Token.class, sent)) {
				StringBuilder strBuild = new StringBuilder();
				strBuild.append(tk.getCoveredText());
				strBuild.append('\t');
				strBuild.append(JCasUtil.selectCovered(aJCas, POS.class, tk).get(0).getPosValue());
				strBuild.append('\t');
				strBuild.append(JCasUtil.selectCovered(aJCas, Lemma.class, tk).get(0).getValue());
				buffWrite.write(strBuild.toString());
				buffWrite.newLine();
			}
			
			buffWrite.write("</s>");
			buffWrite.newLine();
		}		
	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		
		BufferedWriter buffWrite;
		try {
			buffWrite = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.file), "UTF-8"));
			buffWrite.write("<corpus>");
			buffWrite.newLine();
			
			Collection<Text> texts = JCasUtil.select(aJCas, Text.class);
			
			if (texts.size() > 0) {
				for (Text text : texts) {
					System.out.println("text there in corpuswriter");
					
					StringBuilder textBuild = new StringBuilder();
					textBuild.append("<text date=\"");
					textBuild.append(text.getDate());
					textBuild.append("\" id=\"");
					textBuild.append(text.getId());
					textBuild.append("\" inhaltsverzeichnis=\"");
					textBuild.append(text.getVz());
					textBuild.append("\" legislaturperiode=\"");
					textBuild.append(text.getLegislature());
					textBuild.append("\" partei=\"");
					textBuild.append(text.getParty());
					textBuild.append("\" sprecher=\"");
					textBuild.append(text.getSpeaker());
					textBuild.append("\" year=\"");
					textBuild.append(text.getYear());
					textBuild.append("\" yearmonth=\"");
					textBuild.append(text.getYearMonth());
					textBuild.append("\">");
					buffWrite.write(textBuild.toString());
					buffWrite.newLine();
					
					writeSentences(buffWrite, aJCas, JCasUtil.selectCovered(aJCas, Sentence.class, text));
					
					buffWrite.write("</text>");
					buffWrite.newLine();
				}
			} else
				writeSentences(buffWrite, aJCas, JCasUtil.select(aJCas, Sentence.class));
			
			buffWrite.write("</corpus>");
			buffWrite.close();
		} catch (IOException e1) {
			throw new AnalysisEngineProcessException(e1);
		}
	
	}

}
