package main;

public class GNode {
    private static int autoIndex = 0;
    // Has only one attribute an ID
    private int mID;
    // Basic constructor the ID must be set at declaration
    public GNode() {
        this.mID = autoIndex++;
    }
    // Getter for the ID
    public int getId() {
        return mID;
    }
}
