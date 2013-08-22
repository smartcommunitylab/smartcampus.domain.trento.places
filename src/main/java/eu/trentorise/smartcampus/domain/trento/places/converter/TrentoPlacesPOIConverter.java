package eu.trentorise.smartcampus.domain.trento.places.converter;

import it.sayservice.platform.core.domain.actions.DataConverter;
import it.sayservice.platform.core.domain.ext.Tuple;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smartcampus.service.trento.places.data.message.Trentoplaces.Place;

import com.google.protobuf.ByteString;

import eu.trentorise.smartcampus.domain.discovertrento.GenericPOI;
import eu.trentorise.smartcampus.domain.discovertrento.POIData;

public class TrentoPlacesPOIConverter  implements DataConverter {

	public Serializable toMessage(Map<String, Object> parameters) {
		if (parameters == null)
			return null;
		return new HashMap<String, Object>(parameters);
	}
	
	public Object fromMessage(Serializable object) {
		List<ByteString> data = (List<ByteString>) object;
		Tuple res = new Tuple();
		List<GenericPOI> list = new ArrayList<GenericPOI>();
		for (ByteString bs : data) {
			try {
				Place place = Place.parseFrom(bs);
				GenericPOI gp = extractGenericPOI(place);
				list.add(gp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		res.put("data", list.toArray(new GenericPOI[list.size()]));
		return res;
	}

	private GenericPOI extractGenericPOI(Place place) throws ParseException {
		GenericPOI gp = new GenericPOI();
		
		POIData pd = new POIData();
		pd.setCity(place.getTown());
		pd.setCountry("ITA");
		pd.setDatasetId("smart");
		pd.setLatitude(place.getLatitude());
		pd.setLongitude(place.getLongitude());
		pd.setPoiId(place.getName() + "@smartcampus.service.trento.places");
		pd.setPostalCode("38100");
		pd.setRegion(place.getProvince());
		pd.setState("Italy");
		pd.setStreet(place.getStreet() + ", " + place.getNumber());

		
		gp.setPoiData(pd);
		gp.setType(place.getCategoriesList().get(0));
		
		gp.setSource(place.getSource());
		
		gp.setTitle(place.getName());
		
		gp.setDescription(place.getName());
		
		gp.setId(encode(pd.getPoiId()));
		
		return gp;
	}

	private static String encode(String s) {
		return new BigInteger(s.getBytes()).toString(16);
	}
}

