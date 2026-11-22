package utils;

public class Date {
    private int year;
    private int month;
    private int day;

    public Date(int year, int month, int day) {
        setYear(year);
        setMonth(month);
        setDay(day);
    }

    public static int comparer(Date a, Date b) {
        if (a.getYear() != b.getYear()) {
            return a.getYear() < b.getYear() ? -1 : 1;
        }
        if (a.getMonth() != b.getMonth()) {
            return a.getMonth() < b.getMonth() ? -1 : 1;
        }
        if (a.getDay() != b.getDay()) { 
            return a.getDay() < b.getDay() ? -1 : 1;
        }
        return 0;
    }

    public String toString() {
        String toReturn = year + "/" + month + "/" + day;
        return toReturn;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return this.year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getMonth() {
        return this.month;
        
    }
    public void setDay(int day) {
        this.day = day;
    }

    public int getDay() {
        return this.day;
    }

}
