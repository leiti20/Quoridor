package com.quoridor.ai;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * Mémorise les états déjà calculés par l'IA pour optimiser la recherche.
 */
@Component
public class TranspositionTable {

    public enum NodeType {
        EXACT, LOWER, UPPER
    }

    public static class Entry {
        public int value;
        public int depth;
        public NodeType type;

        public Entry(int value, int depth, NodeType type) {
            this.value = value;
            this.depth = depth;
            this.type = type;
        }
    }

    private Map<Long, Entry> table = new HashMap<>();

    public void put(long hash, int value, int depth, NodeType type) {
        Entry e = table.get(hash);
        if (e == null || e.depth <= depth) {
            table.put(hash, new Entry(value, depth, type));
        }
    }

    public Entry get(long hash) {
        return table.get(hash);
    }

    public void clear() {
        table.clear();
    }
}
