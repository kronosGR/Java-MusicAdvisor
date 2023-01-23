// do not remove imports

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Function;

class ArrayUtils {
    // define hasNull method here
    public static <T> boolean hasNull(T[] t) {
        for (T tt : t) {
            if (Objects.isNull(tt)) return true;
        }
        return false;
    }
}