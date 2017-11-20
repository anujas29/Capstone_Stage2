package anuja.project.finalproject.rest;

import java.util.List;

/**
 * Created by USER on 16-10-2017.
 */

public class ProductModel {

    public String mUUID;
    public String mUrl;
    public String mSite;
    public String mTitle;
    public String mName;
    public String mDescription;
    public String mLastChange;
    public String mPrice;
    public String mCurrency;
    public String mOffer;
    //public String mImage;
    public List<String> mImage;


    public ProductModel(String uuid, String url, String site, String title, String name, String description, String LastChange, String Price, String Currency, String Offer) {
        mUUID = uuid;
        mUrl = url;
        mSite = site;
        mTitle = title;
        mName = name;
        mDescription = description;
        mLastChange = LastChange;
        mPrice = Price;
        mCurrency = Currency;
        mOffer = Offer;


    }


}
