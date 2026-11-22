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

    public boolean equals(Object o) {
        if (!(o instanceof Date)) {
            return false;
        }
        Date dateo = (Date) o;
        if (comparer(this, dateo) != 0) {
            return false;
        }
        return true;
    }

    public static Date parse(String texte) {
        String[] morceaux = texte.split("[/-]");
        if (morceaux.length != 3) {
            throw new IllegalArgumentException("Date invalide (format aaaa/mm/jj) : " + texte);
        }
        int year;
        int month;
        int day;
        try {
            year = Integer.parseInt(morceaux[0]);
            month = Integer.parseInt(morceaux[1]);
            day = Integer.parseInt(morceaux[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Date invalide (format aaaa/mm/jj) : " + texte);
        }
        if (month < 1 || month > 12 || day < 1 || day > 31) {
            throw new IllegalArgumentException("Date invalide (mois 1-12, jour 1-31) : " + texte);
        }
        return new Date(year, month, day);
    }

}
