

/* First created by JCasGen Thu Mar 14 12:53:49 CET 2019 */
package de.sebastiangombert.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Mar 14 12:53:49 CET 2019
 * XML source: D:/laura-projekt-workspace/QuickAnno/src/main/resources/desc/text.xml
 * @generated */
public class Text extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Text.class);
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
  protected Text() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Text(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Text(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Text(JCas jcas, int begin, int end) {
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
  //* Feature: date

  /** getter for date - gets 
   * @generated
   * @return value of the feature 
   */
  public String getDate() {
    if (Text_Type.featOkTst && ((Text_Type)jcasType).casFeat_date == null)
      jcasType.jcas.throwFeatMissing("date", "de.sebastiangombert.types.Text");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Text_Type)jcasType).casFeatCode_date);}
    
  /** setter for date - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setDate(String v) {
    if (Text_Type.featOkTst && ((Text_Type)jcasType).casFeat_date == null)
      jcasType.jcas.throwFeatMissing("date", "de.sebastiangombert.types.Text");
    jcasType.ll_cas.ll_setStringValue(addr, ((Text_Type)jcasType).casFeatCode_date, v);}    
   
    
  //*--------------*
  //* Feature: id

  /** getter for id - gets 
   * @generated
   * @return value of the feature 
   */
  public String getId() {
    if (Text_Type.featOkTst && ((Text_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "de.sebastiangombert.types.Text");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Text_Type)jcasType).casFeatCode_id);}
    
  /** setter for id - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setId(String v) {
    if (Text_Type.featOkTst && ((Text_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "de.sebastiangombert.types.Text");
    jcasType.ll_cas.ll_setStringValue(addr, ((Text_Type)jcasType).casFeatCode_id, v);}    
   
    
  //*--------------*
  //* Feature: legislature

  /** getter for legislature - gets 
   * @generated
   * @return value of the feature 
   */
  public String getLegislature() {
    if (Text_Type.featOkTst && ((Text_Type)jcasType).casFeat_legislature == null)
      jcasType.jcas.throwFeatMissing("legislature", "de.sebastiangombert.types.Text");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Text_Type)jcasType).casFeatCode_legislature);}
    
  /** setter for legislature - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setLegislature(String v) {
    if (Text_Type.featOkTst && ((Text_Type)jcasType).casFeat_legislature == null)
      jcasType.jcas.throwFeatMissing("legislature", "de.sebastiangombert.types.Text");
    jcasType.ll_cas.ll_setStringValue(addr, ((Text_Type)jcasType).casFeatCode_legislature, v);}    
   
    
  //*--------------*
  //* Feature: vz

  /** getter for vz - gets 
   * @generated
   * @return value of the feature 
   */
  public String getVz() {
    if (Text_Type.featOkTst && ((Text_Type)jcasType).casFeat_vz == null)
      jcasType.jcas.throwFeatMissing("vz", "de.sebastiangombert.types.Text");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Text_Type)jcasType).casFeatCode_vz);}
    
  /** setter for vz - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setVz(String v) {
    if (Text_Type.featOkTst && ((Text_Type)jcasType).casFeat_vz == null)
      jcasType.jcas.throwFeatMissing("vz", "de.sebastiangombert.types.Text");
    jcasType.ll_cas.ll_setStringValue(addr, ((Text_Type)jcasType).casFeatCode_vz, v);}    
   
    
  //*--------------*
  //* Feature: legislaturePeriod

  /** getter for legislaturePeriod - gets 
   * @generated
   * @return value of the feature 
   */
  public String getLegislaturePeriod() {
    if (Text_Type.featOkTst && ((Text_Type)jcasType).casFeat_legislaturePeriod == null)
      jcasType.jcas.throwFeatMissing("legislaturePeriod", "de.sebastiangombert.types.Text");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Text_Type)jcasType).casFeatCode_legislaturePeriod);}
    
  /** setter for legislaturePeriod - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setLegislaturePeriod(String v) {
    if (Text_Type.featOkTst && ((Text_Type)jcasType).casFeat_legislaturePeriod == null)
      jcasType.jcas.throwFeatMissing("legislaturePeriod", "de.sebastiangombert.types.Text");
    jcasType.ll_cas.ll_setStringValue(addr, ((Text_Type)jcasType).casFeatCode_legislaturePeriod, v);}    
   
    
  //*--------------*
  //* Feature: party

  /** getter for party - gets 
   * @generated
   * @return value of the feature 
   */
  public String getParty() {
    if (Text_Type.featOkTst && ((Text_Type)jcasType).casFeat_party == null)
      jcasType.jcas.throwFeatMissing("party", "de.sebastiangombert.types.Text");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Text_Type)jcasType).casFeatCode_party);}
    
  /** setter for party - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setParty(String v) {
    if (Text_Type.featOkTst && ((Text_Type)jcasType).casFeat_party == null)
      jcasType.jcas.throwFeatMissing("party", "de.sebastiangombert.types.Text");
    jcasType.ll_cas.ll_setStringValue(addr, ((Text_Type)jcasType).casFeatCode_party, v);}    
   
    
  //*--------------*
  //* Feature: speaker

  /** getter for speaker - gets 
   * @generated
   * @return value of the feature 
   */
  public String getSpeaker() {
    if (Text_Type.featOkTst && ((Text_Type)jcasType).casFeat_speaker == null)
      jcasType.jcas.throwFeatMissing("speaker", "de.sebastiangombert.types.Text");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Text_Type)jcasType).casFeatCode_speaker);}
    
  /** setter for speaker - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSpeaker(String v) {
    if (Text_Type.featOkTst && ((Text_Type)jcasType).casFeat_speaker == null)
      jcasType.jcas.throwFeatMissing("speaker", "de.sebastiangombert.types.Text");
    jcasType.ll_cas.ll_setStringValue(addr, ((Text_Type)jcasType).casFeatCode_speaker, v);}    
   
    
  //*--------------*
  //* Feature: year

  /** getter for year - gets 
   * @generated
   * @return value of the feature 
   */
  public String getYear() {
    if (Text_Type.featOkTst && ((Text_Type)jcasType).casFeat_year == null)
      jcasType.jcas.throwFeatMissing("year", "de.sebastiangombert.types.Text");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Text_Type)jcasType).casFeatCode_year);}
    
  /** setter for year - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setYear(String v) {
    if (Text_Type.featOkTst && ((Text_Type)jcasType).casFeat_year == null)
      jcasType.jcas.throwFeatMissing("year", "de.sebastiangombert.types.Text");
    jcasType.ll_cas.ll_setStringValue(addr, ((Text_Type)jcasType).casFeatCode_year, v);}    
   
    
  //*--------------*
  //* Feature: yearMonth

  /** getter for yearMonth - gets 
   * @generated
   * @return value of the feature 
   */
  public String getYearMonth() {
    if (Text_Type.featOkTst && ((Text_Type)jcasType).casFeat_yearMonth == null)
      jcasType.jcas.throwFeatMissing("yearMonth", "de.sebastiangombert.types.Text");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Text_Type)jcasType).casFeatCode_yearMonth);}
    
  /** setter for yearMonth - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setYearMonth(String v) {
    if (Text_Type.featOkTst && ((Text_Type)jcasType).casFeat_yearMonth == null)
      jcasType.jcas.throwFeatMissing("yearMonth", "de.sebastiangombert.types.Text");
    jcasType.ll_cas.ll_setStringValue(addr, ((Text_Type)jcasType).casFeatCode_yearMonth, v);}    
  }

    