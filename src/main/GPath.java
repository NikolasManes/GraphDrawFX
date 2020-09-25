package main;

public class GPath {
    // The path connects 2 nodes - has direction and weight
    private GNode mStart;
    private GNode mEnd;
    private int mWeight;
    // Basic constructor
    public GPath(GNode start, GNode end, int weight) {
        this.mStart = start;
        this.mEnd = end;
        this.mWeight = weight;
    }

    public GNode getStart() {
        return mStart;
    }

    public GNode getEnd() {
        return mEnd;
    }

    public int getWeight() {
        return mWeight;
    }

    public void printPath(){
        System.out.println("| START: " + this.getStart().getId() + " | END: " + this.getEnd().getId() + " | WEIGHT: " + this.getWeight() + " |");
    }
}
