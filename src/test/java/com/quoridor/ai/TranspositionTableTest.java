package com.quoridor.ai;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TranspositionTableTest {

    @Test
    void testPutAndGet() {
        TranspositionTable table = new TranspositionTable();
        table.put(12345L, 42, 3, TranspositionTable.NodeType.EXACT);
        
        TranspositionTable.Entry entry = table.get(12345L);
        assertNotNull(entry);
        assertEquals(42, entry.value);
        assertEquals(3, entry.depth);
        assertEquals(TranspositionTable.NodeType.EXACT, entry.type);
    }
}
