package com.example.nohunger.Object;

public class RestaurantDonate {

    String RestaurantID;
    String RestaurantAddress;
    String RestaurantName;
    Integer RestaurantFoodQuantity;
    String RestaurantFoodName;

    public RestaurantDonate() {
    }

    public RestaurantDonate(String restaurantID, String restaurantAddress, String restaurantName, Integer restaurantFoodQuantity, String restaurantFoodName) {
        RestaurantID = restaurantID;
        RestaurantAddress = restaurantAddress;
        RestaurantName = restaurantName;
        RestaurantFoodQuantity = restaurantFoodQuantity;
        RestaurantFoodName = restaurantFoodName;
    }

    public String getRestaurantID() {
        return RestaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        RestaurantID = restaurantID;
    }

    public String getRestaurantAddress() {
        return RestaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        RestaurantAddress = restaurantAddress;
    }

    public String getRestaurantName() {
        return RestaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        RestaurantName = restaurantName;
    }

    public Integer getRestaurantFoodQuantity() {
        return RestaurantFoodQuantity;
    }

    public void setRestaurantFoodQuantity(Integer restaurantFoodQuantity) {
        RestaurantFoodQuantity = restaurantFoodQuantity;
    }

    public String getRestaurantFoodName() {
        return RestaurantFoodName;
    }

    public void setRestaurantFoodName(String restaurantFoodName) {
        RestaurantFoodName = restaurantFoodName;
    }
}
