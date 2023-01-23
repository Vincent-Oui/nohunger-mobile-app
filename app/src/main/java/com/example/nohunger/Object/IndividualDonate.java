package com.example.nohunger.Object;

public class IndividualDonate {

    String IndividualID;
    String IndividualAddress;
    String IndividualName;
    Integer IndividualFoodQuantity;
    String IndividualFoodName;


    public IndividualDonate() {
    }

    public IndividualDonate(String individualID, String individualAddress, String individualName, Integer individualFoodQuantity, String individualFoodName) {

        this.IndividualID = individualID;
        this.IndividualAddress = individualAddress;
        this.IndividualName = individualName;
        this.IndividualFoodQuantity = individualFoodQuantity;
        this.IndividualFoodName = individualFoodName;
    }

    public String getIndividualID() {
        return IndividualID;
    }

    public void setIndividualID(String individualID) {
        IndividualID = individualID;
    }

    public String getIndividualAddress() {
        return IndividualAddress;
    }

    public void setIndividualAddress(String individualAddress) {
        IndividualAddress = individualAddress;
    }

    public String getIndividualName() {
        return IndividualName;
    }

    public void setIndividualName(String individualName) {
        IndividualName = individualName;
    }

    public Integer getIndividualFoodQuantity() {
        return IndividualFoodQuantity;
    }

    public void setIndividualFoodQuantity(Integer individualFoodQuantity) {
        IndividualFoodQuantity = individualFoodQuantity;
    }

    public String getIndividualFoodName() {
        return IndividualFoodName;
    }

    public void setIndividualFoodName(String individualFoodName) {
        IndividualFoodName = individualFoodName;
    }
}
