package android.example.com.newsolutionbodymanager.recyclerView;

public class Food {
    private String name;
    private int calorie;
    private String imageUrl;

    public Food() {
        //empty constructor needed
    }

    public Food(String name, int calorie, String imageUrl) {
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
}
