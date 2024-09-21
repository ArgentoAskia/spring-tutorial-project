package cn.argento.askia.bean;

public class Good {

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(super.toString() + " = {");
        sb.append('}');
        return sb.toString();
    }
}
