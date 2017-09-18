package tw.com.bean;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;

public class CglibBean {  
  
    public Object object = null;  
      
      
    public BeanMap beanMap = null;  
      
    public CglibBean() {       
          super();       
    }  
      
    public CglibBean(Map<String,?> propertyMap) throws Exception {       
          
          
          this.object = generateBean(propertyMap);       
          this.beanMap = BeanMap.create(this.object);       
        }  
      
      
     public void setValue(String property, Object value) {       
          beanMap.put(property, value);       
        }   
     public Object getValue(String property) {       
          return beanMap.get(property);       
     }  
     public Object getObject() {       
          return this.object;       
        }   
       
       
     private Object generateBean(Map<String,?> propertyMap) throws Exception  {      
          BeanGenerator generator = new BeanGenerator();      
          Set<String> keySet = propertyMap.keySet();       
          for (Iterator<String> i = keySet.iterator(); i.hasNext();) {       
           String key =  i.next();  
           //修改無獲取屬性的class屬性，提高兼容  
           if(propertyMap.get(key)!=null)
           generator.addProperty(key,  propertyMap.get(key).getClass());       
          }     
            
          return generator.create();       
        }     
       
       
      
}  
