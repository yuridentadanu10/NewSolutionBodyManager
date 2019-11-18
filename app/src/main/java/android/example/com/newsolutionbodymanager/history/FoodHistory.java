package android.example.com.newsolutionbodymanager.history;

public class FoodHistory {
    private String name;
    private int calorie;
    private String imageUrl;
    private String waktuMakan;

    public FoodHistory() {
        //empty constructor needed
    }

    public FoodHistory(String name, int calorie, String imageUrl,String waktuMakan) {
        this.name = name;
        this.calorie = calorie;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getWaktuMakan() {
        return waktuMakan;
    }

    public void setWaktuMakan(String waktuMakan) {
        this.waktuMakan = waktuMakan;
    }
}
