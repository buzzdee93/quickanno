package de.sebastiangombert.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
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

import de.sebastiangombert.types.Declarative;
import de.sebastiangombert.types.LegacyDeclarative;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class LegacyVRTReader extends JCasCollectionReader_ImplBase {
	public static final String PARAM_LIST_FILE = "File";
	@ConfigurationParameter(name = PARAM_LIST_FILE,
			description = "File to be read",
			mandatory = true)
	private File file;
	
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
	public void getNext(JCas j) throws IOException, CollectionException {
		File f = file;
		
		BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
		
		JCasBuilder casBuild = new JCasBuilder(j);
		
		boolean declOpen = false;
		int declStart = -1;
		
		String ln = "";
		int k = 0;
		while ((ln = read.readLine()) != null) {
			if (ln.contains("<") && !ln.contains("<decl>") && !ln.contains("</decl>")) continue;
			
			if (ln.contains("<decl>")) {
				declOpen = true;
				continue;
			}
			
			if (ln.contains("</decl>")) {
				declOpen = false;
				casBuild.add(declStart, LegacyDeclarative.class);
				declStart = -1;
				continue;
			}
			String[] parts = ln.split("\\t");
			if (ln.length() > 0) {
				//System.out.println(parts[0]);
				
				if (k != 0 && !parts[0].equals(".") && !parts[0].equals(",") && !parts[0].equals("!") && !parts[0].equals(":") && !parts[0].equals(";") && !parts[0].equals("?")) {
					casBuild.add(" ");
				}
				casBuild.add(parts[0]);
				casBuild.add(casBuild.getPosition() - parts[0].length(), Token.class);
				POS pos = casBuild.add(casBuild.getPosition() - parts[0].length(), POS.class);
				pos.setPosValue(parts[1]);

				Lemma lemma = casBuild.add(casBuild.getPosition() - parts[0].length(), Lemma.class);
				lemma.setValue(parts[2]);

				if (declOpen && declStart == -1)
					declStart = casBuild.getPosition() - parts[0].length();
				
				k++;
			}
		}
		casBuild.close();
		read.close();
		
		
		i++;
	}
}
