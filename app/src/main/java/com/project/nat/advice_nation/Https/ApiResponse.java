package com.project.nat.advice_nation.Https;

import org.json.JSONObject;

/**
 * Created by Chari on 7/18/2017.
 */

public interface ApiResponse {

    void OnSucess(JSONObject response ,int id);

    void OnFailed(int error);


}
