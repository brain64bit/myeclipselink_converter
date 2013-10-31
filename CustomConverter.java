import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;
import org.postgis.Geometry;
import org.postgis.GeometryCollection;
import org.postgis.LineString;
import org.postgis.MultiLineString;
import org.postgis.MultiPoint;
import org.postgis.MultiPolygon;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgis.Polygon;
import org.postgresql.util.PGobject;
import org.springframework.util.StringUtils;

public class CustomConverter implements Converter{

	/**
	 * 
	 */
    private static final long serialVersionUID = 3951879146828520656L;

	@Override
    public Object convertObjectValueToDataValue(Object objectValue, Session session) {
		if (objectValue == null) {
			return null;
		} else if (objectValue instanceof Geometry) {
	    	return new PGgeometry((Geometry)objectValue);
	    } else if (objectValue instanceof Point) {
	    	return new PGgeometry((Point)objectValue);
	    } else if (objectValue instanceof MultiPoint) {
	    	return new PGgeometry((MultiPoint)objectValue);
	    } else if (objectValue instanceof LineString) {
	    	return new PGgeometry((LineString)objectValue);
	    } else if (objectValue instanceof MultiLineString) {
	    	return new PGgeometry((MultiLineString)objectValue);
	    } else if (objectValue instanceof Polygon) {
	    	return new PGgeometry((Polygon)objectValue);
	    } else if (objectValue instanceof MultiPolygon) {
	    	return new PGgeometry((MultiPolygon)objectValue);
	    } else if (objectValue instanceof GeometryCollection) {
	    	return new PGgeometry((GeometryCollection)objectValue);
	    } else if (objectValue instanceof Collection<?> ) {
	    	Collection<?> coll = (Collection<?>) objectValue;
	    	String data = "{"+StringUtils.collectionToCommaDelimitedString(coll)+"}";
	    	if(!coll.isEmpty()){
	    		Object firstElement = coll.toArray()[0];
	    		PGobject pgObject =  new PGobject();
	    		if(firstElement instanceof Long){
	    			pgObject.setType("_int8");
	    			try {
	                    pgObject.setValue(data);
                    } catch (SQLException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
                    }
	    		}else if(firstElement instanceof String){
	    			pgObject.setType("_varchar");
	    			try {
	                    pgObject.setValue(data);
                    } catch (SQLException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
                    }
	    		}
	    		return pgObject;
	    	}
	    	return new PGobject();
	    } else {
	    	return new PGgeometry();
	    }
    }

	@SuppressWarnings("null")
    @Override
    public Object convertDataValueToObjectValue(Object dataValue, Session session) {
		if (dataValue == null) {
			return null;
		} else if (dataValue instanceof PGgeometry) {
			return ((PGgeometry) dataValue).getGeometry();
		} else if (dataValue instanceof PGobject) {
			String data = ((PGobject) dataValue).getValue().replace("[{}]", "");
			Set<String> dataSet = StringUtils.commaDelimitedListToSet(data);
			if(dataSet !=null || !dataSet.isEmpty()){
				Object firstElement = dataSet.toArray()[0];
				try{
					Long.parseLong(firstElement.toString());
					Set<Long> coll = new HashSet<Long>();
					for (String val : dataSet) {
	                    coll.add(Long.parseLong(val));
                    }
					return coll;
				}catch(NumberFormatException nfe){
					Set<String> coll = new HashSet<String>();
					for (String val : dataSet) {
	                    coll.add(val);
                    }
					return coll;
				}
			}
			return null;
		} else {
			return null;
		}
    }

	@Override
    public boolean isMutable() {
	    // TODO Auto-generated method stub
	    return false;
    }

	@Override
    public void initialize(DatabaseMapping mapping, Session session) {
	   mapping.getField().setSqlType(java.sql.Types.OTHER);
	    
    }

}
