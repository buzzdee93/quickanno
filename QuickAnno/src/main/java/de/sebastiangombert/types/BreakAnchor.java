

/* First created by JCasGen Wed Mar 13 15:24:45 CET 2019 */
package de.sebastiangombert.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Wed Mar 13 15:24:45 CET 2019
 * XML source: D:/laura-projekt-workspace/QuickAnno/src/main/resources/desc/break_anchor.xml
 * @generated */
public class BreakAnchor extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(BreakAnchor.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected BreakAnchor() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public BreakAnchor(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public BreakAnchor(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public BreakAnchor(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: sentenceIndex

  /** getter for sentenceIndex - gets 
   * @generated
   * @return value of the feature 
   */
  public int getSentenceIndex() {
    if (BreakAnchor_Type.featOkTst && ((BreakAnchor_Type)jcasType).casFeat_sentenceIndex == null)
      jcasType.jcas.throwFeatMissing("sentenceIndex", "de.sebastiangombert.types.BreakAnchor");
    return jcasType.ll_cas.ll_getIntValue(addr, ((BreakAnchor_Type)jcasType).casFeatCode_sentenceIndex);}
    
  /** setter for sentenceIndex - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSentenceIndex(int v) {
    if (BreakAnchor_Type.featOkTst && ((BreakAnchor_Type)jcasType).casFeat_sentenceIndex == null)
      jcasType.jcas.throwFeatMissing("sentenceIndex", "de.sebastiangombert.types.BreakAnchor");
    jcasType.ll_cas.ll_setIntValue(addr, ((BreakAnchor_Type)jcasType).casFeatCode_sentenceIndex, v);}    
  }

    