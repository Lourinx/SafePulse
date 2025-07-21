package model;

public class LabTest {
    private String testName, result, normalRange, date;

    public LabTest(String testName, String result, String normalRange, String date) {
        this.testName = testName;
        this.result = result;
        this.normalRange = normalRange;
        this.date = date;
    }

    public String getTestName() {
        return testName;
    }

    public String getResult() {
        return result;
    }

    public String getNormalRange() {
        return normalRange;
    }

    public String getDate() {
        return date;
    }
}
