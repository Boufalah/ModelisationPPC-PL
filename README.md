# UE ModelisationPPC-PL

## Groupe 🎓

- **M**eryem Boufalah
- **A**lessandro Pepegna
- **D**avide Ferre'
- **D**avide Pizzoli
- **R**émi Janin

## Collaboration :pencil2:

Hi guys, I did a little bit of code refactoring to avoid Git merge/push conflits, so that everyone can run its tests without unintentionally affecting the others. I've created a single class per model and they all implement the following interface:
```java
public interface nqueen.TryYourStuff {
    default void ferre() { }
    default void pizzoli() { }
    default void remi() { }
    default void meryem() { }
    default void alessandro() { }
    default void general() { }
}
```

If you want to try something for a specific model, simply override the method that has your name and put your code there, then call your function from the main(). An example is given below:

```java
public class nqueen.RowColumnModel implements nqueen.TryYourStuff {

    @Override
    public void ferre(){
        // I put my stuff here
    }
    
    @Override
    public void meryem(){
        // Meryem puts her stuff here
    }

    public static void main(String[] args) {
        nqueen.RowColumnModel m = new nqueen.RowColumnModel();
        m.ferre();
        m.meryem();
    }
}
```

I hope you find this helpful.
