
/* First created by JCasGen Thu Mar 14 12:53:49 CET 2019 */
package de.sebastiangombert.types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Thu Mar 14 12:53:49 CET 2019
 * @generated */
public class Text_Type extends Annotation_Type {
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Text.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.sebastiangombert.types.Text");
 
  /** @generated */
  final Feature casFeat_date;
  /** @generated */
  final int     casFeatCode_date;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getDate(int addr) {
        if (featOkTst && casFeat_date == null)
      jcas.throwFeatMissing("date", "de.sebastiangombert.types.Text");
    return ll_cas.ll_getStringValue(addr, casFeatCode_date);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setDate(int addr, String v) {
        if (featOkTst && casFeat_date == null)
      jcas.throwFeatMissing("date", "de.sebastiangombert.types.Text");
    ll_cas.ll_setStringValue(addr, casFeatCode_date, v);}
    
  
 
  /** @generated */
  final Feature casFeat_id;
  /** @generated */
  final int     casFeatCode_id;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getId(int addr) {
        if (featOkTst && casFeat_id == null)
      jcas.throwFeatMissing("id", "de.sebastiangombert.types.Text");
    return ll_cas.ll_getStringValue(addr, casFeatCode_id);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setId(int addr, String v) {
        if (featOkTst && casFeat_id == null)
      jcas.throwFeatMissing("id", "de.sebastiangombert.types.Text");
    ll_cas.ll_setStringValue(addr, casFeatCode_id, v);}
    
  
 
  /** @generated */
  final Feature casFeat_legislature;
  /** @generated */
  final int     casFeatCode_legislature;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getLegislature(int addr) {
        if (featOkTst && casFeat_legislature == null)
      jcas.throwFeatMissing("legislature", "de.sebastiangombert.types.Text");
    return ll_cas.ll_getStringValue(addr, casFeatCode_legislature);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setLegislature(int addr, String v) {
        if (featOkTst && casFeat_legislature == null)
      jcas.throwFeatMissing("legislature", "de.sebastiangombert.types.Text");
    ll_cas.ll_setStringValue(addr, casFeatCode_legislature, v);}
    
  
 
  /** @generated */
  final Feature casFeat_vz;
  /** @generated */
  final int     casFeatCode_vz;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getVz(int addr) {
        if (featOkTst && casFeat_vz == null)
      jcas.throwFeatMissing("vz", "de.sebastiangombert.types.Text");
    return ll_cas.ll_getStringValue(addr, casFeatCode_vz);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setVz(int addr, String v) {
        if (featOkTst && casFeat_vz == null)
      jcas.throwFeatMissing("vz", "de.sebastiangombert.types.Text");
    ll_cas.ll_setStringValue(addr, casFeatCode_vz, v);}
    
  
 
  /** @generated */
  final Feature casFeat_legislaturePeriod;
  /** @generated */
  final int     casFeatCode_legislaturePeriod;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getLegislaturePeriod(int addr) {
        if (featOkTst && casFeat_legislaturePeriod == null)
      jcas.throwFeatMissing("legislaturePeriod", "de.sebastiangombert.types.Text");
    return ll_cas.ll_getStringValue(addr, casFeatCode_legislaturePeriod);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setLegislaturePeriod(int addr, String v) {
        if (featOkTst && casFeat_legislaturePeriod == null)
      jcas.throwFeatMissing("legislaturePeriod", "de.sebastiangombert.types.Text");
    ll_cas.ll_setStringValue(addr, casFeatCode_legislaturePeriod, v);}
    
  
 
  /** @generated */
  final Feature casFeat_party;
  /** @generated */
  final int     casFeatCode_party;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getParty(int addr) {
        if (featOkTst && casFeat_party == null)
      jcas.throwFeatMissing("party", "de.sebastiangombert.types.Text");
    return ll_cas.ll_getStringValue(addr, casFeatCode_party);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setParty(int addr, String v) {
        if (featOkTst && casFeat_party == null)
      jcas.throwFeatMissing("party", "de.sebastiangombert.types.Text");
    ll_cas.ll_setStringValue(addr, casFeatCode_party, v);}
    
  
 
  /** @generated */
  final Feature casFeat_speaker;
  /** @generated */
  final int     casFeatCode_speaker;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getSpeaker(int addr) {
        if (featOkTst && casFeat_speaker == null)
      jcas.throwFeatMissing("speaker", "de.sebastiangombert.types.Text");
    return ll_cas.ll_getStringValue(addr, casFeatCode_speaker);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setSpeaker(int addr, String v) {
        if (featOkTst && casFeat_speaker == null)
      jcas.throwFeatMissing("speaker", "de.sebastiangombert.types.Text");
    ll_cas.ll_setStringValue(addr, casFeatCode_speaker, v);}
    
  
 
  /** @generated */
  final Feature casFeat_year;
  /** @generated */
  final int     casFeatCode_year;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getYear(int addr) {
        if (featOkTst && casFeat_year == null)
      jcas.throwFeatMissing("year", "de.sebastiangombert.types.Text");
    return ll_cas.ll_getStringValue(addr, casFeatCode_year);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setYear(int addr, String v) {
        if (featOkTst && casFeat_year == null)
      jcas.throwFeatMissing("year", "de.sebastiangombert.types.Text");
    ll_cas.ll_setStringValue(addr, casFeatCode_year, v);}
    
  
 
  /** @generated */
  final Feature casFeat_yearMonth;
  /** @generated */
  final int     casFeatCode_yearMonth;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getYearMonth(int addr) {
        if (featOkTst && casFeat_yearMonth == null)
      jcas.throwFeatMissing("yearMonth", "de.sebastiangombert.types.Text");
    return ll_cas.ll_getStringValue(addr, casFeatCode_yearMonth);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setYearMonth(int addr, String v) {
        if (featOkTst && casFeat_yearMonth == null)
      jcas.throwFeatMissing("yearMonth", "de.sebastiangombert.types.Text");
    ll_cas.ll_setStringValue(addr, casFeatCode_yearMonth, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public Text_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_date = jcas.getRequiredFeatureDE(casType, "date", "uima.cas.String", featOkTst);
    casFeatCode_date  = (null == casFeat_date) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_date).getCode();

 
    casFeat_id = jcas.getRequiredFeatureDE(casType, "id", "uima.cas.String", featOkTst);
    casFeatCode_id  = (null == casFeat_id) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_id).getCode();

 
    casFeat_legislature = jcas.getRequiredFeatureDE(casType, "legislature", "uima.cas.String", featOkTst);
    casFeatCode_legislature  = (null == casFeat_legislature) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_legislature).getCode();

 
    casFeat_vz = jcas.getRequiredFeatureDE(casType, "vz", "uima.cas.String", featOkTst);
    casFeatCode_vz  = (null == casFeat_vz) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_vz).getCode();

 
    casFeat_legislaturePeriod = jcas.getRequiredFeatureDE(casType, "legislaturePeriod", "uima.cas.String", featOkTst);
    casFeatCode_legislaturePeriod  = (null == casFeat_legislaturePeriod) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_legislaturePeriod).getCode();

 
    casFeat_party = jcas.getRequiredFeatureDE(casType, "party", "uima.cas.String", featOkTst);
    casFeatCode_party  = (null == casFeat_party) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_party).getCode();

 
    casFeat_speaker = jcas.getRequiredFeatureDE(casType, "speaker", "uima.cas.String", featOkTst);
    casFeatCode_speaker  = (null == casFeat_speaker) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_speaker).getCode();

 
    casFeat_year = jcas.getRequiredFeatureDE(casType, "year", "uima.cas.String", featOkTst);
    casFeatCode_year  = (null == casFeat_year) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_year).getCode();

 
    casFeat_yearMonth = jcas.getRequiredFeatureDE(casType, "yearMonth", "uima.cas.String", featOkTst);
    casFeatCode_yearMonth  = (null == casFeat_yearMonth) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_yearMonth).getCode();

  }
}



    