// do not remove imports
import java.util.*;
import java.util.function.Function;

class ArrayUtils {
    // define invert method here
    public static <T> T[] invert(T[] arr){
        Collections.reverse(Arrays.asList(arr));
        return arr;
    }
}