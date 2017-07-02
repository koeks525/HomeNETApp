package Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/06/27.
 */

public class SearchViewModel {

    @SerializedName("searchParameters")
    private String searchParameters;

    public SearchViewModel(String searchParameter) {
        this.searchParameters = searchParameter;
    }

    public String getSearchParameter() {
        return searchParameters;
    }

    public void setSearchParameter(String searchParameter) {
        this.searchParameters = searchParameter;
    }
}
