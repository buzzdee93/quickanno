package de.sebastiangombert.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.JCasBuilder;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

import de.sebastiangombert.types.BreakAnchor;
import de.sebastiangombert.types.Declarative;
import de.sebastiangombert.types.Maybe;
import de.sebastiangombert.types.Text;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class NewVRTReader extends JCasCollectionReader_ImplBase {
	public static final String PARAM_LIST_FILES = "File";
	@ConfigurationParameter(name = PARAM_LIST_FILES,
			description = "File to be read.",
			mandatory = true)
	private List<File> files;
	
	public static final String IGNORE_SENTENCES = "Ignore Sentences";
	@ConfigurationParameter(name = IGNORE_SENTENCES,
			description = "File to be read.",
			mandatory = true)
	private boolean ignoreSentences;
	
	public static final String IGNORE_POS_TAGS = "Ignore POS tags";
	@ConfigurationParameter(name = IGNORE_SENTENCES,
			description = "File to be read.",
			mandatory = false)
	private boolean ignorePosTags;
	
	private int i = 0;

	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);

	}

	@Override
	public boolean hasNext() throws IOException, CollectionException {
		return i < 1;
	}

	@Override
	public Progress[] getProgress() {
		return new Progress[] { new ProgressImpl(i, 1,
				Progress.ENTITIES) };
	}

	@Override
	public void getNext(JCas jCas) throws IOException, CollectionException {
		JCasBuilder casBuild = new JCasBuilder(jCas);
		jCas.setDocumentLanguage("de");
		
		for (File file : files) {
			BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			
			String textDate = "";
			String textId = "";
			String textVz = "";
			String textLegislature = "";
			String textParty = "";
			String textSpeaker = "";
			String textYear = "";
			String textYearMonth = "";
			int textStart = 0;
			
			int k = 0;
			
			boolean sentenceDecl = false;
			boolean sentenceMaybe = false;
			boolean sentenceBreak = false;
			int sentenceStart = 0;
			
			String ln;
			while ((ln = read.readLine()) != null) {
				if (ln.contains("<corpus") || ln.contains("</corpus") || ln.contains("<stage") || ln.contains("</stage"))
					continue;
				
				if (ln.contains("<text ")) {
					textStart = casBuild.getPosition();
					String values = ln.substring(ln.indexOf(' ') +1, ln.length() -1);
					String[] pairStrings = values.split(" ");
					for (String pair : pairStrings) {
						String key = pair.substring(0, pair.indexOf('='));
						String value = pair.substring(pair.indexOf('=') +2, pair.length() -1);
						
						switch (key) {
						case "date": textDate = value; break;
						case "id": textId = value; break;
						case "inhaltsverzeichnis": textVz = value; break;
						case "legislaturperiode": textLegislature = value; break;
						case "partei": textParty = value; break;
						case "sprecher": textSpeaker = value; break;
						case "year": textYear = value; break;
						case "yearmonth": textYearMonth = value; break;
						default: break;
						}
					}
					continue;
				}
				
				if (ln.contains("</text")) {
					Text text = casBuild.add(textStart, Text.class);
					text.setDate(textDate);
					text.setId(textId);
					text.setVz(textVz);
					text.setLegislature(textLegislature);
					text.setParty(textParty);
					text.setSpeaker(textSpeaker);
					text.setYear(textYear);
					text.setYearMonth(textYearMonth);
					continue;
				}
				
				if (ln.contains("<s")) {
					if (!ignoreSentences) {
						sentenceStart = casBuild.getPosition();
						if (ln.contains("type=\"declarative\""))
							sentenceDecl = true;
						else if (ln.contains("type=\"maybe\""))
							sentenceMaybe = true;
					}
					continue;
				}
				
				if (ln.contains("!##break")) {
					sentenceBreak = true;
					continue;
				}
				
				if (ln.contains("</s")) {
					if (!ignoreSentences) {
						casBuild.add(sentenceStart, Sentence.class);
						if (sentenceDecl)
							casBuild.add(sentenceStart, Declarative.class);
						if (sentenceMaybe)
							casBuild.add(sentenceStart, Maybe.class);
						if (sentenceBreak)
							casBuild.add(sentenceStart, BreakAnchor.class);
						
						sentenceDecl = false;
						sentenceMaybe = false;
						sentenceBreak = false;
					}
					
					continue;
				}
				
				if (ln.length() > 0) {
					String[] parts = ln.split("\\t");

					if (k != 0 && !parts[0].equals(".") && !parts[0].equals(",") && !parts[0].equals("!") 
							&& !parts[0].equals(":") && !parts[0].equals(";") 
							&& !parts[0].equals("?") && !parts[0].equals("(")) {
						casBuild.add(" ");
					}
					casBuild.add(parts[0]);
					casBuild.add(casBuild.getPosition() - parts[0].length(), Token.class);
					
					if (!ignorePosTags) {
						POS pos = casBuild.add(casBuild.getPosition() - parts[0].length(), POS.class);
						pos.setPosValue(parts[1]);
					}

					Lemma lemma = casBuild.add(casBuild.getPosition() - parts[0].length(), Lemma.class);
					lemma.setValue(parts[2]);
					
					k++;
				}
			}
			read.close();
		}
		
		i++;
		casBuild.close();
	}

}
