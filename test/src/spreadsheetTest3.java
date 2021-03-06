import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class spreadsheetTest3 {

    /**
     * Excel的等式应该正确地获取其他列的值
     */
    @Test
    public void testThatCellReferenceWorks() {
        spreadsheet sheet = new spreadsheet();
        sheet.put("A", "8");
        sheet.put("B", "=A");
        assertEquals("cell lookup", "8", sheet.get("B"));

        sheet.put("A", "9");
        assertEquals("cell change propagation", "9", sheet.get("B"));
    }


    /**
     * Excel的等式应该正确地计算包含其他列的值的等式
     */
    @Test
    public void testThatFormulasKnowCellsAndRecalculate() {
        spreadsheet sheet = new spreadsheet();
        sheet.put("A", "8");
        sheet.put("B", "3");
        sheet.put("E", "=A*(A-B)+B/3");
        assertEquals("calculation with cells", "41", sheet.get("E"));

        sheet.put("B", "6");
        assertEquals("re-calculation", "18", sheet.get("E"));
    }

    /**
     * Excel的等式应该正确地获取其他列的值
     */
    @Test
    public void testThatDeepPropagationWorks() {
        spreadsheet sheet = new spreadsheet();
        sheet.put("A", "8");
        sheet.put("B", "=A");
        sheet.put("C", "=B");
        sheet.put("D", "=C");
        assertEquals("deep propagation", "8", sheet.get("D"));

        sheet.put("B", "6");
        assertEquals("deep re-calculation", "6", sheet.get("D"));
    }

    /**
     * Excel的等式应该正确地处理复杂的计算。
     */
    @Test
    public void testThatFormulaWorksWithManyCells() {
        spreadsheet sheet = new spreadsheet();
        sheet.put("A", "10");
        sheet.put("B", "=A+E");
        sheet.put("C", "=B+F");
        sheet.put("D", "=C");
        sheet.put("E", "7");
        sheet.put("F", "=B");
        sheet.put("G", "=C-B");
        sheet.put("H", "=D+G");

        assertEquals("multiple expressions - D", "34", sheet.get("D"));
        assertEquals("multiple expressions - H", "51", sheet.get("H"));
    }

    /**
     * 如果出现了循环引用，Excel应该提示。
     */
    @Test
    public void testThatCircularReferencesAdmitIt() {
        spreadsheet sheet = new spreadsheet();
        sheet.put("A", "=A");
        assertEquals("Detect circularity", "#Circular", sheet.get("A"));
    }
}
