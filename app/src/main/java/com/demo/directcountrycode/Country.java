package com.demo.directcountrycode;

/**
 * Created by Nishi on 4/27/2018.
 */

public class Country {

    public String CountryDial_code = "";
    public String CountryCode= "";
    public String CountryName = "";

    public Country(String COuntryDial_code, String countryCode, String countryName) {
        this.CountryDial_code = COuntryDial_code;
        CountryCode = countryCode;
        CountryName = countryName;
    }

    public Country() {
    }

    public String getCountryDial_code() {
        return CountryDial_code;
    }

    public void setCountryDial_code(String countryDial_code) {
        CountryDial_code = countryDial_code;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }


}
