package com.imastudio.newgooglemapsapp.response;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class ResultsItem{

	@SerializedName("user_ratings_total")
	private int userRatingsTotal;

	@SerializedName("icon")
	private String icon;

	@SerializedName("name")
	private String name;

	@SerializedName("opening_hours")
	private OpeningHours openingHours;

	@SerializedName("rating")
	private double rating;

	@SerializedName("geometry")
	private Geometry geometry;

	@SerializedName("vicinity")
	private String vicinity;

	@SerializedName("photos")
	private List<PhotosItem> photos;

	@SerializedName("place_id")
	private String placeId;

	public void setUserRatingsTotal(int userRatingsTotal){
		this.userRatingsTotal = userRatingsTotal;
	}

	public int getUserRatingsTotal(){
		return userRatingsTotal;
	}

	public void setIcon(String icon){
		this.icon = icon;
	}

	public String getIcon(){
		return icon;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setOpeningHours(OpeningHours openingHours){
		this.openingHours = openingHours;
	}

	public OpeningHours getOpeningHours(){
		return openingHours;
	}

	public void setRating(double rating){
		this.rating = rating;
	}

	public double getRating(){
		return rating;
	}

	public void setGeometry(Geometry geometry){
		this.geometry = geometry;
	}

	public Geometry getGeometry(){
		return geometry;
	}

	public void setVicinity(String vicinity){
		this.vicinity = vicinity;
	}

	public String getVicinity(){
		return vicinity;
	}

	public void setPhotos(List<PhotosItem> photos){
		this.photos = photos;
	}

	public List<PhotosItem> getPhotos(){
		return photos;
	}

	public void setPlaceId(String placeId){
		this.placeId = placeId;
	}

	public String getPlaceId(){
		return placeId;
	}

	@Override
 	public String toString(){
		return 
			"ResultsItem{" + 
			"user_ratings_total = '" + userRatingsTotal + '\'' + 
			",icon = '" + icon + '\'' + 
			",name = '" + name + '\'' + 
			",opening_hours = '" + openingHours + '\'' + 
			",rating = '" + rating + '\'' + 
			",geometry = '" + geometry + '\'' + 
			",vicinity = '" + vicinity + '\'' + 
			",photos = '" + photos + '\'' + 
			",place_id = '" + placeId + '\'' + 
			"}";
		}
}