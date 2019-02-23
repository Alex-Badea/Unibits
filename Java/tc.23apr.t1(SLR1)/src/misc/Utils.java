package misc;

import parser.grammar.SequenceOfSymbols;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Utils {
    @BijectiveMap
    public static <K,V> Map<V, K> inverse(@BijectiveMap Map<K, V> map) {
        return map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }
}
