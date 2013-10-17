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
public class GeometryConverter implements Converter{

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
	    } else {
	    	return new PGgeometry();
	    }
    }

	@Override
    public Object convertDataValueToObjectValue(Object dataValue, Session session) {
		if (dataValue == null) {
			return null;
		} else if (dataValue instanceof PGgeometry) {
			return ((PGgeometry) dataValue).getGeometry();
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

