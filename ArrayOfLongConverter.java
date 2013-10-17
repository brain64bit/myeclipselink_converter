
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;
import org.postgresql.util.PGobject;
import org.springframework.util.StringUtils;


public class ArrayOfLongConverter implements Converter{

	/**
	 * 
	 */
    private static final long serialVersionUID = -1523729613495510199L;

	@Override
    public Object convertDataValueToObjectValue(Object obj, Session session) {
	    PGobject arrayObject = new PGobject();
	    arrayObject.setType("_int8");
	    
	    if(obj == null){
	    	return null;
	    }else{
	    	Set<Long> collection = new HashSet<Long>();
	    	
	    	if(obj instanceof PGobject){
	    		PGobject p = (PGobject) obj;
		    	String data = p.getValue().replaceAll("[{}]", ""); 
		        for (String l : StringUtils.commaDelimitedListToSet(data)) {
		            collection.add(Long.parseLong(l));
	            }
	    	}else if(obj instanceof Long[]){
	    		// automatically PgObject same Long[]
	    		for (Long id : (Long[])obj) {
	    			collection.add(id);
                }
	    	}	    	
	    	return collection;
	    }
    }

	@Override
    public Object convertObjectValueToDataValue(Object obj, Session session) {
	    // TODO Auto-generated method stub
	    if(obj == null){
	    	return null;
	    }else if(obj instanceof Set<?>){
	    	PGobject arrayObject = new PGobject();
	    	arrayObject.setType("_int8");
	    	String data = "{"+StringUtils.collectionToCommaDelimitedString((Set<?>) obj)+"}";
	    	try {
	            arrayObject.setValue(data);
            } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
            }
	    	return arrayObject;
	    }
	    return null;
    }

	@Override
    public void initialize(DatabaseMapping arg0, Session arg1) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public boolean isMutable() {
	    // TODO Auto-generated method stub
	    return true;
    }

}

