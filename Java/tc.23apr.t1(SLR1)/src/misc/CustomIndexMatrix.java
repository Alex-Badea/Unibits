package misc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CustomIndexMatrix<I1, I2, E> extends LinkedHashMap<I1, Map<I2, E>> {
    @Deprecated
    @Override
    public Map<I2, E> get(Object key) {
        return super.get(key);
    }

    @Nullable
    public E get(I1 row, I2 col) {
        return super.get(row) != null ? super.get(row).get(col) : null;
    }

    @Deprecated
    @Override
    public Map<I2, E> put(I1 key, Map<I2, E> value) {
        return super.put(key, value);
    }

    public void set(I1 row, I2 col, E value) {
        super.putIfAbsent(row, new LinkedHashMap<>());
        super.get(row).put(col, value);
    }

    public boolean contains(@NotNull E value) {
        return this.values().stream().anyMatch(i2EMap -> i2EMap.values().stream().anyMatch(value::equals));
    }

    @Override
    public String toString() {
        var result = new StringBuilder();
        for (var row : this.entrySet()) {
            var rowName = row.getKey();
            for (var entry : row.getValue().entrySet()) {
                var colName = entry.getKey();
                var val = entry.getValue();
                result.append("[").append(rowName).append("][").append(colName).append("]:\n").append(val).append("\n");
            }
        }
        return result.toString();
    }
}
