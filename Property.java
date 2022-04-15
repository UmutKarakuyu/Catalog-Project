public class Property {

    /* It was created as a prototype.
     The code blocks will be filled in soon.

     */

    private String label;
    private String content;

    public Property(String label, String content) {
        this.label = label;
        this.content = content;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Property{" +
                "label='" + label + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
