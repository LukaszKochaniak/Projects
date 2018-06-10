package i18n.authors;

import java.util.ListResourceBundle;

public class MenuBundle_pl_PL extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return contents;
    }

    private Object[][] contents = {
        {"authorTitle", new String("Autor:")},
        {"author", new String("Łukasz Kochaniak")},};

}
