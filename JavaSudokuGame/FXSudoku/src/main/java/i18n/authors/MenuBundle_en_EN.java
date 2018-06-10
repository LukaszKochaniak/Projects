package i18n.authors;

import java.util.ListResourceBundle;

public class MenuBundle_en_EN extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return contents;
    }

    private Object[][] contents = {
        {"authorTitle", new String("Author:")},
        {"author", new String("Lukasz Kochaniak")},};

}
