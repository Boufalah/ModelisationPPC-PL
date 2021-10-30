package nqueen;

public interface TryYourStuff {
    default BaseQueenModel.Stats ferre() {
        BaseQueenModel.Stats stats = new BaseQueenModel.Stats();
        stats.resolutionTime = -1;
        stats.numOfNodes = -1;
        return stats;
    };
    default void pizzoli(String[] args) {};
    default void remi() { }
    default void meryem() { }
    default void alessandro() { }
    default void general() { }
}
