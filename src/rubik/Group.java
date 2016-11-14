package rubik;

/**
 * Interface of a group
 */
public interface Group {
    /**
     * inverse
     * @return inversion of the element
     */
    public Group inv();
    /**
     * multiplication
     * @param g group on the right hand side
     * @return product of two elements
     */
    public Group times(Group g);
}