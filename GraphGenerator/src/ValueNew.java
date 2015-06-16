/**
 * Created by seanbaergen on 15-05-28.
 */
/*
//http://stackoverflow.com/questions/15610183/if-else-statements-in-antlr-using-listeners 28/05/2015
public class ValueNew {

    public static ValueNew VOID = new ValueNew(new Object());

    final Object value;

    public ValueNew(Object value) {
        this.value = value;
    }

    public Boolean asBoolean() {
        return (Boolean)value;
    }

    public Double asDouble() {
        return (Double)value;
    }

    public String asString() {
        return String.valueOf(value);
    }

    public boolean isDouble() {
        return value instanceof Double;
    }

    @Override
    public int hashCode() {

        if(value == null) {
            return 0;
        }

        return this.value.hashCode();
    }

    @Override
    public boolean equals(Object o) {

        if(value == o) {
            return true;
        }

        if(value == null || o == null || o.getClass() != value.getClass()) {
            return false;
        }

        ValueNew that = (ValueNew)o;

        return this.value.equals(that.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}*/


