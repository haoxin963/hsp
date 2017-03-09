package net.hsp.web.util;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.beanutils.Converter;
 
 
 
 
 
public class CustomerDateConverter implements Converter {
 
    private final static SimpleDateFormat DATE_FORMATE_SHOW = new SimpleDateFormat("yyyy-MM-dd");
 
    public Object convert(Class type, Object value){
 
       // TODO Auto-generated method stub
 
       if (type.equals(java.util.Date.class) ) {
 
              try {
 
                  return DATE_FORMATE_SHOW.parse(value.toString());
 
              } catch (ParseException e) {
 
                  // TODO Auto-generated catch block
 
                  e.printStackTrace();
 
              }
 
       } 
       return null;
 
    }
 
}