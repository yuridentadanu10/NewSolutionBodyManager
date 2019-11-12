package android.example.com.newsolutionbodymanager.recyclerView;

public class Food {
    private String name;
    private int calorie;


    public Food() {
        //empty constructor needed
    }

    public Food(String name, int calorie) {
        this.name = name;
        this.calorie = calorie;
    }

    public String getName() {
        return name;
    }

    public int getCalorie() {
        return calorie;
    }

}
