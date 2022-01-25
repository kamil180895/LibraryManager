package Library;

public class SortPair<T> implements Comparable<SortPair<T>>{
    public T value;
    public int sortValue;

    public SortPair(T value, int sortValue) {
        this.value = value;
        this.sortValue = sortValue;
    }

    @Override
    public int compareTo(SortPair<T> o) {
        if(sortValue > o.sortValue)
            return -1;
        else if(sortValue < o.sortValue)
            return 1;
        else
            return 0;
    }
}
