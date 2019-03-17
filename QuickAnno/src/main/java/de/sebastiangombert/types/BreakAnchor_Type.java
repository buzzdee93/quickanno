
/* First created by JCasGen Wed Mar 13 15:24:45 CET 2019 */
package de.sebastiangombert.types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Wed Mar 13 15:24:45 CET 2019
 * @generated */
public class BreakAnchor_Type extends Annotation_Type {
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = BreakAnchor.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.sebastiangombert.types.BreakAnchor");
 
  /** @generated */
  final Feature casFeat_sentenceIndex;
  /** @generated */
  final int     casFeatCode_sentenceIndex;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getSentenceIndex(int addr) {
        if (featOkTst && casFeat_sentenceIndex == null)
      jcas.throwFeatMissing("sentenceIndex", "de.sebastiangombert.types.BreakAnchor");
    return ll_cas.ll_getIntValue(addr, casFeatCode_sentenceIndex);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setSentenceIndex(int addr, int v) {
        if (featOkTst && casFeat_sentenceIndex == null)
      jcas.throwFeatMissing("sentenceIndex", "de.sebastiangombert.types.BreakAnchor");
    ll_cas.ll_setIntValue(addr, casFeatCode_sentenceIndex, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public BreakAnchor_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_sentenceIndex = jcas.getRequiredFeatureDE(casType, "sentenceIndex", "uima.cas.Integer", featOkTst);
    casFeatCode_sentenceIndex  = (null == casFeat_sentenceIndex) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_sentenceIndex).getCode();

  }
}



    